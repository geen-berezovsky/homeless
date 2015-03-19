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
    public static String organizationInfo;
    public static String useSingleSignature;

    @Autowired
    public Configuration(@Value("${contractsDir}") String contractsDir,
                         @Value("${templatesDir}") String templatesDir,

                         @Value("${organizationInfo}") String organizationInfo,
                         @Value("${useSingleSignature}") String useSingleSignature
                         ) {
        this.contractsDir = contractsDir;
        this.templatesDir = templatesDir;
        this.organizationInfo = organizationInfo;
        this.useSingleSignature = useSingleSignature;
    }


}
