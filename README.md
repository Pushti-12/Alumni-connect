Prerequisites:
1) Java JDK 17 or higher
2) Apache Maven 3.8 or higher
3) PostgreSQL 14 or higher (Render uses Postgres)
4) Python 3.11 or higher

Render deployment:
1) Ensure `render.yaml` is committed to the repository.
2) On Render, create a new service from this repository.
3) Use the existing service definition in `render.yaml`.
4) Render will provision a managed Postgres database and set `DATABASE_URL`.
5) The backend service uses `backend/Dockerfile` and listens on port `5000`.

Local development:
1) Run a local PostgreSQL instance and create the database `alumni_connect`.
2) Set env vars if needed: `DATABASE_URL`, `PGUSER`, `PGPASSWORD`, `PORT`.
3) Start backend from `/workspaces/Alumni-connect/backend` using:
   `mvn spring-boot:run -DskipTests`
