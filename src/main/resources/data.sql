INSERT INTO lookup_type (code, name)
VALUES ('TOOL_STATUS', 'Eszköz státusz'),
       ('TOOL_CATEGORY', 'Eszköz kategória'),
       ('RESERVATION_STATUS', 'Foglalás státusz') ON CONFLICT (code) DO NOTHING;

INSERT INTO lookup (code, name, lookup_type_id)
VALUES ('ACTIVE', 'Aktív', (SELECT id FROM lookup_type WHERE code = 'TOOL_STATUS')),
       ('INACTIVE', 'Inaktív',
        (SELECT id FROM lookup_type WHERE code = 'TOOL_STATUS')) ON CONFLICT (lookup_type_id, code) DO NOTHING;

INSERT INTO lookup (code, name, lookup_type_id)
VALUES ('DRILL', 'Fúrógép', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('SAW', 'Fűrész', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('GRINDER', 'Csiszoló', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('HAMMER', 'Kalapács', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('SCREWDRIVER_SET', 'Csavarhúzó készlet', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('WRENCH_SET', 'Villáskulcs készlet', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('SANDER', 'Csiszológép', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('LADDER', 'Létra', (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')),
       ('PRESSURE_WASHER', 'Magasnyomású mosó',
        (SELECT id FROM lookup_type WHERE code = 'TOOL_CATEGORY')) ON CONFLICT (lookup_type_id, code) DO NOTHING;

INSERT INTO lookup (code, name, lookup_type_id)
VALUES ('ACTIVE', 'Aktív', (SELECT id FROM lookup_type WHERE code = 'RESERVATION_STATUS')),
       ('FINISHED', 'Befejezett',
        (SELECT id FROM lookup_type WHERE code = 'RESERVATION_STATUS')) ON CONFLICT (lookup_type_id, code) DO NOTHING;
