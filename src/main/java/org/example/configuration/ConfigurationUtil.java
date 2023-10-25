package org.example.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилитный класс для загрузки и доступа к свойствам из конфигурационного файла.
 */
public class ConfigurationUtil {

    /**
     * Свойства, загруженные из конфигурационного файла.
     */
    private static final Properties properties = new Properties();

    /**
     * Статический инициализатор для загрузки свойств из файла config.properties.
     */
    static {
        try (InputStream inputStream = ConfigurationUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфигурационный файл", e);
        }
    }

    /**
     * Возвращает значение свойства по указанному ключу.
     *
     * @param key Ключ свойства.
     * @return Значение свойства или null, если свойство с таким ключом не найдено.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
