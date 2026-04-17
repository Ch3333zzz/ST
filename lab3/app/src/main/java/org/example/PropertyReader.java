package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream is = PropertyReader.class.getClassLoader().getResourceAsStream("conf.properties")) {
            if (is == null) {
                throw new RuntimeException("Cannot find conf.properties");
            }
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Cant read file", e);
        }
    }

    public static String getProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Key '" + key + "' is not found!");
        }
        return value;
    }
}