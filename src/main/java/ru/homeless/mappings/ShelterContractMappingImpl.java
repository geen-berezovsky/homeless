package ru.homeless.mappings;

import java.util.Map;

import javax.servlet.ServletContext;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ShelterContractMappingImpl implements ICustomMappingWordDocument {

    @Autowired
    private IContractService contractService;

    @Override
    public WordprocessingMLPackage getDocument(Map map) {
        return new DocTypeProcessor(IDocumentMapping.DOCUMENT_SHELTER_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument(map, null,0); //CHANGE IT WITH REAL PHOTO LOCATION!
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
        return null;
    }
}
