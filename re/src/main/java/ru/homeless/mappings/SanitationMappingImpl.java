package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
public class SanitationMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(SanitationMappingImpl.class);

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) {
        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_SANITATION_TEMPLATE_PATH).replaceParametersInDocument(map, Util.attachPhoto(client, log), ICustomMappingWordDocument.AVATAR_LOCATION_BOTTOM_CENTER);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
