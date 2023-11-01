package org.example.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Основной класс конфигурации Spring для настройки компонентов веб-приложения.
 * <p>
 * Включает в себя конфигурацию объектов, необходимых для работы приложения, таких как
 * маппер JSON, утилиты для работы с JWT и настройки ресурсов для Swagger UI.
 * </p>
 */
@Configuration
@ComponentScan("org.example")
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableOpenApi
public class SpringConfig implements WebMvcConfigurer {

    /**
     * Bean для ObjectMapper, используемого для сериализации и десериализации JSON.
     *
     * @return экземпляр {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * Bean для утилиты JWT, облегчающей работу с JWT (JSON Web Tokens).
     *
     * @return экземпляр {@link JWTUtil}
     */
    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    /**
     * Настраивает обработчики ресурсов для добавления путей к ресурсам Swagger UI.
     *
     * @param registry реестр обработчиков ресурсов
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    /**
     * Настраивает контроллеры представлений для перенаправления на Swagger UI.
     *
     * @param registry реестр контроллеров представлений
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
    }

}
