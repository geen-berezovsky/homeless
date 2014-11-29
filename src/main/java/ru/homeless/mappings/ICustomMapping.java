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
public interface ICustomMapping {

    public XWPFDocument getDocument(Map<String, String> map);
    public XWPFDocument getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context);

}
