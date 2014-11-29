package ru.homeless.mappings;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.shared.IDocumentMapping;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created by maxim on 30.11.14.
 */
public class ZagsQueryMappingImpl implements ICustomMapping {

    @Override
    public XWPFDocument getDocument(Map map) {
        return new DocTypeProcessor(map, IDocumentMapping.DOCUMENT_ZAGS_QUERY_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public XWPFDocument getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
