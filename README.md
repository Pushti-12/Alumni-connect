# Alumni Connect

Alumni Connect is a full-stack web platform designed to help alumni reconnect, discover mentorship opportunities, join events, and explore career-focused networking possibilities. The project combines a modern, responsive frontend with a Java Spring Boot backend and deployment-ready infrastructure for cloud hosting.

## Highlights
- Built a polished landing experience with clear calls to action and a user-friendly interface.
- Developed full-stack functionality for key pages such as login, signup, profile, events, and networking.
- Implemented a secure backend using Java, Spring Boot, Maven, and PostgreSQL.
- Prepared the application for deployment with Docker and Render.

## Resume-Friendly Project Summary
- Designed and developed Alumni Connect, a full-stack web platform for alumni networking, mentorship, and community engagement.
- Built responsive frontend pages and polished the user experience to improve clarity, accessibility, and visual appeal.
- Implemented backend services and database integration to support user-facing features and deployment-ready architecture.
- Applied modern web development practices, UI/UX improvements, and cloud deployment preparation for a portfolio-worthy project.

## Skills Demonstrated
- Frontend: HTML, CSS, JavaScript
- Backend: Java, Spring Boot, Maven
- Database: PostgreSQL
- DevOps: Docker, Render
- UI/UX: Responsive design, polished layout, user-centered interactions

## Prerequisites
1. Java JDK 17 or higher
2. Apache Maven 3.8 or higher
3. PostgreSQL 14 or higher
4. Python 3.11 or higher

## Render Deployment
1. Ensure `render.yaml` is committed to the repository.
2. Create a new Render service from this repository.
3. Use the existing service definition in `render.yaml`.
4. Render will provision a managed PostgreSQL database and set `DATABASE_URL`.
5. The backend uses `backend/Dockerfile` and listens on port `5000`.

## Local Development
1. Run a local PostgreSQL instance and create the database `alumni_connect`.
2. Set environment variables if needed: `DATABASE_URL`, `PGUSER`, `PGPASSWORD`, `PORT`.
3. Start the backend from `/workspaces/Alumni-connect/backend` using:
   `mvn spring-boot:run -DskipTests`
