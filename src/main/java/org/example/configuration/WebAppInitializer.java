package org.example.configuration;

import org.example.security.JWTFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Класс инициализатора для веб-приложения, наследуемый от {@link AbstractAnnotationConfigDispatcherServletInitializer}.
 * <p>
 * Этот класс используется для конфигурации контекста приложения Spring MVC, а также для настройки сервлета DispatcherServlet,
 * которые заменяют традиционные web.xml конфигурационные файлы.
 * </p>
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        JWTFilter jwtFilter = new JWTFilter();
        return new Filter[]{ jwtFilter };
    }
}

