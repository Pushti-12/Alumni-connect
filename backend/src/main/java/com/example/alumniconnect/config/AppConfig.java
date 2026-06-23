package com.example.alumniconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${app.database.name}")
    private String dbName;

    @Bean
    public DataSource dataSource() {
    HikariDataSource ds = new HikariDataSource();
    String dbUrl = System.getenv().getOrDefault("DATABASE_URL", 
        "jdbc:postgresql://localhost:5432/alumni_connect");
    // Render DATABASE_URL starts with "postgres://" fix it
    if (dbUrl.startsWith("postgres://")) {
        dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://");
    }
    ds.setJdbcUrl(dbUrl);
    ds.setUsername(System.getenv().getOrDefault("PGUSER", "postgres"));
    ds.setPassword(System.getenv().getOrDefault("PGPASSWORD", "postgres"));
    ds.setDriverClassName("org.postgresql.Driver");
    return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
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
