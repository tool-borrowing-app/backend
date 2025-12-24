package com.toolborrow.backend.service;

import com.toolborrow.backend.exception.TBAException;
import com.toolborrow.backend.mapping.ReservationMapper;
import com.toolborrow.backend.mapping.ToolMapper;
import com.toolborrow.backend.model.dto.CreateToolDto;
import com.toolborrow.backend.model.dto.ReservationDto;
import com.toolborrow.backend.model.dto.ToolDto;
import com.toolborrow.backend.model.entity.Lookup;
import com.toolborrow.backend.model.entity.Reservation;
import com.toolborrow.backend.model.entity.Tool;
import com.toolborrow.backend.model.entity.User;
import com.toolborrow.backend.model.enums.LookupTypeCode;
import com.toolborrow.backend.repository.LookupRepository;
import com.toolborrow.backend.repository.ReservationRepository;
import com.toolborrow.backend.repository.ToolRepository;
import com.toolborrow.backend.repository.UserRepository;
import com.toolborrow.backend.utils.JwtUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {

    private final @NonNull ToolRepository toolRepository;
    private final @NonNull LookupRepository lookupRepository;
    private final @NonNull ToolMapper toolMapper;
    private final @NonNull ImageStorageService imageStorageService;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public @NonNull List<ToolDto> list() {
        final @NonNull List<Tool> all = toolRepository.findAll();
        return all
            .stream()
            .map(toolMapper::convert)
            .toList();
    }

    @Override
    public @NonNull ToolDto get(final @NonNull Long id) {
        return toolRepository
            .findById(id)
            .map(toolMapper::convert)
            .orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found with id: " + id));
    }

    @Override
    @Transactional
    public @NonNull ToolDto create(final @NonNull CreateToolDto tool) {
        final @NonNull Lookup status = resolveToolStatus("ACTIVE");
        final User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        final @NonNull Tool entity = toolMapper.convert(tool, status, user);
        entity.setStatus(status);

        final List<String> imageUrls = imageStorageService.uploadBase64Images(
                tool.getImages(),
                "tools"    // the name of the folder in the bucket
        );

        if (!imageUrls.isEmpty()) {
            newToolEntity.setImageUrls(imageUrls);
        }


        final @NonNull Tool saved = toolRepository.save(newToolEntity);
        return toolMapper.convert(saved);
    }

    @Override
    @Transactional
    public @NonNull ToolDto update(
        final @NonNull Long id,
        final @NonNull ToolDto tool
    ) {
        final @NonNull Tool current = toolRepository.findById(id)
            .orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found: " + id));
        final User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        if(user == null || !current.getUser().getId().equals(user.getId())) {
            throw new TBAException(BAD_REQUEST, "Updating tool is not allowed for current user!");
        }

        current.setName(tool.getName());
        current.setDescription(tool.getDescription());
        current.setRentalPrice(tool.getRentalPrice());
        current.setDepositPrice(tool.getDepositPrice());

        final @NonNull String statusCode = tool.getLookupStatus().getCode();
        final @NonNull Lookup status = resolveToolStatus(statusCode);
        current.setStatus(status);

        final boolean hasActive = reservationRepository.existsActiveReservationForToolId(id);
        if (hasActive) {
            throw new TBAException(BAD_REQUEST, "Tool has active reservations and cannot be updated: " + id);
        }

        final @NonNull Tool saved = toolRepository.save(current);
        return toolMapper.convert(saved);
    }

    @Transactional
    @Override
    public void delete(final @NonNull Long id) {
        if (!toolRepository.existsById(id)) {
            throw new TBAException(NOT_FOUND, "Tool not found: " + id);
        }

        final boolean hasActive = reservationRepository.existsActiveReservationForToolId(id);
        if (hasActive) {
            throw new TBAException(BAD_REQUEST, "Tool has active reservations and cannot be deleted: " + id);
        }

        toolRepository.deleteById(id);
    }

    @Override
    public @NonNull List<ReservationDto> getToolReservations(final @NonNull Long id) {
        final @NonNull Tool current = toolRepository.findById(id)
            .orElseThrow(() -> new TBAException(NOT_FOUND, "Tool not found: " + id));
        final User user = userRepository.findByEmail(JwtUtils.getCurrentUserEmail());

        if(user == null || !current.getUser().getId().equals(user.getId())) {
            throw new TBAException(BAD_REQUEST, "User doesn't own tool with id: " + id);
        }

        return reservationRepository.findByToolId(id).stream().map(reservationMapper::from).toList();
    }

    // ===============================================================================

    private @NonNull Lookup resolveToolStatus(final @NonNull String statusCode) {
        final @NonNull String lookupTypeCode = LookupTypeCode.TOOL_STATUS.getCode();

        return lookupRepository
            .findByCodeAndLookupTypeCode(statusCode, lookupTypeCode)
            .orElseThrow(() -> new TBAException(
                NOT_FOUND,
                "Tool status lookup not found: code=%s type=%s".formatted(statusCode, lookupTypeCode)
            ));
    }
}
