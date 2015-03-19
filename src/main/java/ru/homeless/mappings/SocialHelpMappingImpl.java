package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

import javax.servlet.ServletContext;

import java.util.Map;

/**
 * Created by maxim on 30.11.14.
 */
public class SocialHelpMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(SocialHelpMappingImpl.class);

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) {
        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_SOCIAL_HELP_TEMPLATE_PATH).replaceParametersInDocument(map, Util.attachPhoto(client, log), ICustomMappingWordDocument.AVATAR_LOCATION_BOTTOM_CENTER);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
