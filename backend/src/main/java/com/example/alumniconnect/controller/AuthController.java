package com.example.alumniconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JdbcTemplate jdbc;

    public AuthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> data) {
        String username = data.getOrDefault("username", "").toString().trim();
        String password = data.getOrDefault("password", "").toString();
        String accountType = data.getOrDefault("accountType", "").toString().trim();
        String fullName = data.getOrDefault("fullName", "").toString().trim();
        String securityAnswer = data.getOrDefault("security_answer",
                                 data.getOrDefault("securityAnswer", null))
                                 == null ? null :
                                 data.getOrDefault("security_answer",
                                 data.get("securityAnswer")).toString();

        if (username.isEmpty() || password.isEmpty() || accountType.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username, password and accountType required"));
        }

        List<Map<String, Object>> check = jdbc.queryForList("SELECT id FROM users WHERE username = ?", username);
        if (!check.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        jdbc.update("INSERT INTO users (username, password_hash, account_type, full_name, security_answer) VALUES (?, ?, ?, ?, ?)",
                username, hashed, accountType, fullName, securityAnswer);

        Integer userId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        jdbc.update(
            "INSERT INTO profiles (user_id, first_name, last_name, phone, batch, grad_year, department, course, current_job, company, organization, experience, certificates) VALUES (?, '', '', '', '', '', '', '', '', '', '', '', '')",
            userId
        );

        return ResponseEntity.ok(Map.of("message", "Account created", "userId", userId));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> data) {
        String username = data.getOrDefault("username", "").toString().trim();
        String password = data.getOrDefault("password", "").toString();
        String accountType = data.getOrDefault("accountType", "").toString().trim();

        if (username.isEmpty() || password.isEmpty() || accountType.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username, password and accountType required"));
        }

        List<Map<String, Object>> users =
                jdbc.queryForList("SELECT * FROM users WHERE username = ? AND account_type = ?", username, accountType);

        if (users.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        Map<String, Object> user = users.get(0);

        if (!BCrypt.checkpw(password, (String) user.get("password_hash"))) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        return ResponseEntity.ok(Map.of("message", "Login successful", "user", Map.of(
                "id", user.get("id"),
                "username", user.get("username"),
                "accountType", user.get("account_type"),
                "fullName", user.get("full_name")
        )));
    }
}
