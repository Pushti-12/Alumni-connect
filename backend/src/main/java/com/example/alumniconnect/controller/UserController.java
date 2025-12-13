package com.example.alumniconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JdbcTemplate jdbc;

    public UserController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/user/by-username/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        List<Map<String, Object>> rows =
                jdbc.queryForList("SELECT id, username, account_type, full_name FROM users WHERE username = ?", username);

        if (rows.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Not found"));

        return ResponseEntity.ok(Map.of("user", rows.get(0)));
    }

    @GetMapping("/alumni")
    public ResponseEntity<?> list(@RequestParam(required = false) String q) {
        String sql = """
                SELECT u.id, COALESCE(u.full_name, u.username) AS name, u.username,
                       p.company, p.current_job, p.batch, p.department, p.course
                FROM users u
                LEFT JOIN profiles p ON p.user_id = u.id
                WHERE u.account_type = 'alumni'
                """;

        if (q != null && !q.isBlank()) {
            String like = "%" + q + "%";
            sql += " AND (u.full_name LIKE '" + like + "' OR u.username LIKE '" + like +
                    "' OR p.company LIKE '" + like + "' OR p.department LIKE '" + like + "' OR p.batch LIKE '" + like + "')";
        }

        List<Map<String, Object>> list = jdbc.queryForList(sql);

        return ResponseEntity.ok(Map.of("alumni", list));
    }
}
