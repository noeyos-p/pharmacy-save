package com.my.pharmacy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * application.yml 파일의
     * app:
     *   allowed-origins: ${ALLOWED_ORIGINS:http://localhost:3000}*/
    @Value("${app.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(","))
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
                // axios를 요청
                .allowedHeaders("*")
                // header 허용
                .allowCredentials(true);
    }
}

/** Client 하고  API 서버하고 통신을 하려면 반드시 필요함 */