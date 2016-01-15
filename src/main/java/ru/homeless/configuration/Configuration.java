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
    public static String profilesDir;
    public static String organizationInfo;
    public static String useSingleSignature;
    public static String orgName;

    @Autowired
    public Configuration(@Value("${contractsDir}") String contractsDir,
                         @Value("${templatesDir}") String templatesDir,
                         @Value("${profilesDir}") String profilesDir,
                         @Value("${organizationInfo}") String organizationInfo,
                         @Value("${useSingleSignature}") String useSingleSignature,
                         @Value("${orgName}") String orgName
                         ) {
        this.contractsDir = contractsDir;
        this.templatesDir = templatesDir;
        this.profilesDir = profilesDir;
        this.organizationInfo = organizationInfo;
        this.useSingleSignature = useSingleSignature;
        this.orgName = orgName;
    }


}
