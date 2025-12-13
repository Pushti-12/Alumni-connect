package com.example.alumniconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/requests")
public class RequestsController {

    private final JdbcTemplate jdbc;

    public RequestsController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> data) {
        jdbc.update("""
                INSERT INTO requests (sender_user_id, recipient_user_id, student_name, contact, company_or_college, batch, message, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'pending')
                """,
                data.get("sender_user_id"),
                data.get("recipient_user_id"),
                data.getOrDefault("student_name", ""),
                data.getOrDefault("contact", ""),
                data.getOrDefault("company_or_college", ""),
                data.getOrDefault("batch", ""),
                data.getOrDefault("message", "")
        );

        Integer id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        Map<String, Object> req = jdbc.queryForMap("SELECT * FROM requests WHERE id = ?", id);

        return ResponseEntity.ok(Map.of("message", "Request created", "request", req));
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<?> getForRecipient(@PathVariable int recipientId) {
        List<Map<String, Object>> rows =
                jdbc.queryForList("SELECT * FROM requests WHERE recipient_user_id = ?", recipientId);

        return ResponseEntity.ok(Map.of("requests", rows));
    }

    @PostMapping("/{reqId}/respond")
    public ResponseEntity<?> respond(@PathVariable int reqId, @RequestBody Map<String, Object> data) {
        String action = data.get("action").toString().toLowerCase();
        String status = action.startsWith("a") ? "accepted" : "rejected";

        jdbc.update("UPDATE requests SET status = ? WHERE id = ?", status, reqId);

        Map<String, Object> updated =
                jdbc.queryForMap("SELECT * FROM requests WHERE id = ?", reqId);

        return ResponseEntity.ok(Map.of("message", "Request updated", "request", updated));
    }
}
