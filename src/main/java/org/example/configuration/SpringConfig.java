package org.example.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.security.JWTFilter;
import org.example.security.JWTUtil;
import org.example.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

/**
 * Основной класс конфигурации Spring для настройки компонентов веб-приложения.
 * <p>
 * Включает в себя конфигурацию объектов, необходимых для работы приложения, таких как
 * маппер JSON, утилиты для работы с JWT и настройки ресурсов для Swagger UI.
 * </p>
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
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

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    public JWTFilter jwtFilter(JWTUtil jwtUtil, PlayerService playerService) {
        return new JWTFilter(jwtUtil, playerService);
    }

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistration(JWTFilter jwtFilter) {
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>(jwtFilter);
        registrationBean.addUrlPatterns("/balances", "/admin");
        return registrationBean;
    }


    /**
     * Настраивает обработчики ресурсов для добавления путей к ресурсам Swagger UI.
     *
     * @param registry реестр обработчиков ресурсов
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/swagger-ui/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
//                .resourceChain(false);
//    }

    /**
     * Настраивает контроллеры представлений для перенаправления на Swagger UI.
     *
     * @param registry реестр контроллеров представлений
     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/swagger-ui/")
//                .setViewName("forward:" + "/swagger-ui/index.html");
//    }

}
