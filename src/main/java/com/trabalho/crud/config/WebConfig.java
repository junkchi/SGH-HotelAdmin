package com.trabalho.crud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Esta é a configuração do CORS (Cross-Origin Resource Sharing).
     * Basicamente, ela diz ao Spring para aceitar requisições de
     * qualquer origem ("*"). Isso é o que permite o seu index.html
     * conversar com a API.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todos os endpoints (ex: /tipos-quarto)
                .allowedOrigins("*")     // Permite de qualquer origem (ex: file://, localhost:3000, etc)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite estes métodos HTTP
                .allowedHeaders("*");
    }
}