package com.example.alumniconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final JdbcTemplate jdbc;

    public ProfileController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable int userId) {
        List<Map<String, Object>> users =
                jdbc.queryForList("SELECT id, username, account_type, full_name FROM users WHERE id = ?", userId);

        if (users.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        Map<String, Object> user = users.get(0);

        List<Map<String, Object>> profiles =
                jdbc.queryForList("SELECT * FROM profiles WHERE user_id = ?", userId);

        return ResponseEntity.ok(Map.of(
                "user", user,
                "profile", profiles.isEmpty() ? Map.of() : profiles.get(0)
        ));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable int userId, @RequestBody Map<String, Object> data) {
        Set<String> allowed = Set.of("first_name", "last_name", "phone", "batch", "grad_year",
                "department", "course", "current_job", "company", "organization", "experience", "certificates");

        StringBuilder sql = new StringBuilder("UPDATE profiles SET ");
        List<Object> params = new ArrayList<>();
        int count = 0;

        for (String key : allowed) {
            if (data.containsKey(key)) {
                if (count > 0) sql.append(", ");
                sql.append(key).append(" = ?");
                params.add(data.get(key));
                count++;
            }
        }

        sql.append(" WHERE user_id = ?");
        params.add(userId);

        jdbc.update(sql.toString(), params.toArray());

        List<Map<String, Object>> profile =
                jdbc.queryForList("SELECT * FROM profiles WHERE user_id = ?", userId);

        return ResponseEntity.ok(Map.of("message", "Profile updated", "profile", profile.get(0)));
    }
}
