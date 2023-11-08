package ylab.ru.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.ylab.common.service.PlayerService;
import ylab.ru.application.security.JWTFilter;
import ylab.ru.application.security.JWTUtil;

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
@ComponentScan({"ru.ylab.common", "org.example.audit"})
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
     * Создает бин для утилиты работы с JWT (JSON Web Token).
     * <p>Этот бин предоставляет методы для генерации и проверки JSON Web Token'ов.</p>
     *
     * @return новый экземпляр {@link JWTUtil}.
     */
    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    /**
     * Создает бин фильтра JWT, который используется для обработки и проверки запросов с JWT.
     * <p>Этот фильтр сверяет JWT в запросах и определяет, имеет ли пользователь право на выполнение операций.</p>
     *
     * @param jwtUtil Утилита для работы с JWT.
     * @param playerService Сервис для работы с данными игроков.
     * @return новый экземпляр {@link JWTFilter}.
     */
    @Bean
    public JWTFilter jwtFilter(JWTUtil jwtUtil, PlayerService playerService) {
        return new JWTFilter(jwtUtil, playerService);
    }

    /**
     * Регистрирует бин фильтра JWT для определенных URL-паттернов.
     * <p>Этот метод настраивает фильтр JWT для обеспечения безопасности определенных эндпоинтов.</p>
     *
     * @param jwtFilter Фильтр JWT, который необходимо зарегистрировать.
     * @return регистрационный бин {@link FilterRegistrationBean} для фильтра JWT.
     */
    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilterRegistration(JWTFilter jwtFilter) {
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>(jwtFilter);
        registrationBean.addUrlPatterns("/balances", "/admin");
        return registrationBean;
    }

}
