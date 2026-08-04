package com.example.alumniconnect.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(resolveJdbcUrl());
        dataSource.setUsername(resolveUsername());
        dataSource.setPassword(resolvePassword());
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMaximumPoolSize(5);
        return dataSource;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

    private String resolveJdbcUrl() {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            return "jdbc:postgresql://localhost:5432/alumni_connect";
        }
        if (dbUrl.startsWith("jdbc:")) {
            return dbUrl;
        }
        if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://")) {
            try {
                URI uri = URI.create(dbUrl);
                StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://");
                if (uri.getHost() != null) {
                    jdbcUrl.append(uri.getHost());
                }
                if (uri.getPort() != -1) {
                    jdbcUrl.append(":").append(uri.getPort());
                }
                if (uri.getPath() != null && !uri.getPath().isBlank()) {
                    jdbcUrl.append(uri.getPath());
                }
                if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                    jdbcUrl.append("?").append(uri.getQuery());
                }
                return jdbcUrl.toString();
            } catch (Exception ignored) {
                return "jdbc:" + dbUrl;
            }
        }
        return "jdbc:" + dbUrl;
    }

    private String resolveUsername() {
        return firstNonBlank(
                System.getenv("SPRING_DATASOURCE_USERNAME"),
                System.getenv("DB_USERNAME"),
                System.getenv("DB_USER"),
                extractFromUrl("user"),
                "postgres"
        );
    }

    private String resolvePassword() {
        return firstNonBlank(
                System.getenv("SPRING_DATASOURCE_PASSWORD"),
                System.getenv("DB_PASSWORD"),
                extractFromUrl("password"),
                "postgres"
        );
    }

    private String extractFromUrl(String key) {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.isBlank()) {
            return null;
        }
        try {
            URI uri = URI.create(dbUrl);
            String userInfo = uri.getUserInfo();
            if (userInfo == null || userInfo.isBlank()) {
                return null;
            }
            String[] parts = userInfo.split(":", 2);
            String username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String password = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : null;
            return "user".equals(key) ? username : password;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}