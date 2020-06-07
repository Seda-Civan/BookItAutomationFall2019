package com.bookit.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigurationReader {
    private static final Properties properties;

    static {
        try {
            //if you create the object in try block it close automatically at the end
            //try with resources
            try (FileInputStream fileInputStream = new FileInputStream("configuration.properties")) {
                properties = new Properties();
                properties.load(fileInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file!");
        }
    }

    //public - visible outside of class Static -visible by classname
    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}

