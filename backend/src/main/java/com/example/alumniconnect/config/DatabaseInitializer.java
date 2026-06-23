package com.example.alumniconnect.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbc;

    public DatabaseInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void init() {

        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(150) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                account_type VARCHAR(50) NOT NULL,
                full_name VARCHAR(255),
                security_answer VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """);

        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS profiles (
                user_id INT PRIMARY KEY,
                first_name VARCHAR(100),
                last_name VARCHAR(100),
                phone VARCHAR(50),
                batch VARCHAR(100),
                grad_year VARCHAR(10),
                department VARCHAR(150),
                course VARCHAR(150),
                current_job VARCHAR(255),
                company VARCHAR(255),
                organization VARCHAR(255),
                experience VARCHAR(50),
                certificates TEXT,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            );
        """);

        jdbc.execute("""
            CREATE TABLE IF NOT EXISTS requests (
                id INT AUTO_INCREMENT PRIMARY KEY,
                sender_user_id INT NULL,
                recipient_user_id INT NULL,
                student_name VARCHAR(255),
                contact VARCHAR(255),
                company_or_college VARCHAR(255),
                batch VARCHAR(50),
                message TEXT,
                status ENUM('pending','accepted','rejected') DEFAULT 'pending',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                FOREIGN KEY (sender_user_id) REFERENCES users(id) ON DELETE SET NULL,
                FOREIGN KEY (recipient_user_id) REFERENCES users(id) ON DELETE SET NULL
            );
        """);
    }
}

