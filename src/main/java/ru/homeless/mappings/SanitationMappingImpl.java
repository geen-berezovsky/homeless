package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.shared.IDocumentMapping;

/**
 * Created by maxim on 30.11.14.
 */
public class SanitationMappingImpl implements ICustomMapping {

    @Override
    public WordprocessingMLPackage getDocument(Map map) {
        return new DocTypeProcessor(map, IDocumentMapping.DOCUMENT_SANITATION_TEMPLATE_PATH).replaceParametersInDocument(null,0);
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
