package ru.homeless.mappings;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.services.IGenericService;
import ru.homeless.shared.IDocumentMapping;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ShelterContractMappingImpl implements ICustomMapping {

    @Autowired
    private IContractService contractService;

    @Override
    public XWPFDocument getDocument(Map map) {
        return new DocTypeProcessor(map, IDocumentMapping.DOCUMENT_SHELTER_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument();
    }

    @Override
    public XWPFDocument getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
