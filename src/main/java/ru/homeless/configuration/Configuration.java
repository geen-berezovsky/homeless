package ru.homeless.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Configuration {

    public static String contractsDir;
    public static String templatesDir;

    @Autowired
    public Configuration(@Value("${contractsDir}") String contractsDir,@Value("${templatesDir}") String templatesDir) {
        this.contractsDir = contractsDir;
        this.templatesDir = templatesDir;
    }


}
