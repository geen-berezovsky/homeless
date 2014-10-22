package ru.homeless.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by maxim on 09.10.14.
 */
@Component
@Scope("singleton")
public class Configuration {

    public static String photos;

    @Autowired
    public Configuration(@Value("${photos}") String photos) {
        this.photos = photos;
    }

}