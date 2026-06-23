package com.example.alumniconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;

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
        if (effectiveUrl.startsWith("postgres://")) {
            effectiveUrl = "jdbc:postgresql://" + effectiveUrl.substring("postgres://".length());
        } else if (effectiveUrl.startsWith("postgresql://")) {
            effectiveUrl = "jdbc:" + effectiveUrl;
        }

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(effectiveUrl);
        ds.setUsername(System.getenv().getOrDefault("PGUSER", dbUser));
        ds.setPassword(System.getenv().getOrDefault("PGPASSWORD", dbPass));
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
