# backend

## Set up postgres using docker:

docker run --name my-postgres \
  -e POSTGRES USER=postgres \
  -e POSTGRES PASSWORD=postgres \
  -e POSTGRES DB=tool_db \
  -p 5432:5432 \
  -d postgres:latest

## Set up the spring app:

1. Create a .env file in the project root directory based on .env-example, put your local db credentials in it
2. In IntelliJ, go to your Run Configuration (next to the green run button) -> Edit Configurations -> Add the path of your .env file to the Environment variables

