package ru.homeless.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Configuration {

    public static String contractsDir;

    @Autowired
    public Configuration(@Value("${contractsDir}") String contractsDir) {
        this.contractsDir = contractsDir;
    }


}
