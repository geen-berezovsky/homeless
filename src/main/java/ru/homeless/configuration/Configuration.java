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
    public static String timestampFile;
    public static String reportEngineUrl;
    public static String profilesDir;

    @Autowired
    public Configuration(@Value("${photos}") String photos, @Value("${timestampFile}") String timestampFile, @Value("${reportEngineUrl}") String reportEngineUrl, @Value("${profilesDir}") String profilesDir) {
        this.photos = photos;
        this.timestampFile = timestampFile;
        this.reportEngineUrl = reportEngineUrl;
        this.profilesDir = profilesDir;
    }


}
