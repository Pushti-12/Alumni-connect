package com.example.alumniconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String dbUrl,
            @Value("${spring.datasource.username}") String dbUser,
            @Value("${spring.datasource.password}") String dbPass,
            @Value("${spring.datasource.driver-class-name}") String driverClassName
    ) {
        String effectiveUrl = dbUrl;
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");

        if (effectiveUrl.startsWith("postgres://") || effectiveUrl.startsWith("postgresql://")) {
            try {
                URI dbUri = URI.create(effectiveUrl);
                String userInfo = dbUri.getUserInfo();
                if (userInfo != null && (username == null || username.isBlank())) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts.length > 1 ? parts[1] : password;
                }
                String host = dbUri.getHost();
                int port = dbUri.getPort();
                String path = dbUri.getPath();
                String query = dbUri.getQuery();
                effectiveUrl = "jdbc:postgresql://" + host + (port > 0 ? ":" + port : "") + path + (query != null ? "?" + query : "");
            } catch (Exception ignored) {
                if (effectiveUrl.startsWith("postgres://")) {
                    effectiveUrl = "jdbc:postgresql://" + effectiveUrl.substring("postgres://".length());
                } else if (effectiveUrl.startsWith("postgresql://")) {
                    effectiveUrl = "jdbc:" + effectiveUrl;
                }
            }
        }

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(effectiveUrl);
        if (username != null && !username.isBlank()) {
            ds.setUsername(username);
        } else {
            ds.setUsername(dbUser);
        }
        if (password != null && !password.isBlank()) {
            ds.setPassword(password);
        } else {
            ds.setPassword(dbPass);
        }
        ds.setDriverClassName(driverClassName);
        return ds;
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
}
