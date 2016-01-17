package ru.homeless.mappings;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Client;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.util.Util;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ShelterContractMappingImpl extends ContractMappingImpl implements ICustomMappingWordDocument {

    public static final Logger log = Logger.getLogger(ShelterContractMappingImpl.class);

    @Override
    public WordprocessingMLPackage getDocument(Map map, Client client) {
    	return null;
    }

    @Override
    public WordprocessingMLPackage getDocument(Map<String, String> map, Client client, int contractId, int workerId, ServletContext context) {
    	

    	init(map, client,contractId, workerId, context, IDocumentMapping.DOCUMENT_SHELTER_CONTRACT); //init of generic variables
		if (new File(contractPath).exists()) {          //the contract is already generated, return the document from disk
            log.info("The contract for client ID="+client.getId()+" already exist, taking it from the storage");
            try {
                finalDocumentForSaving = WordprocessingMLPackage.load(new java.io.File(contractPath));
			} catch (Docx4JException e) {
				log.error(e.getMessage(),e);
			}
        } else { //otherwise, generate it
        	preparePlaceholdersMap(client, contractId);

        	//difference
    		placeholdersAndValues.put("[contr:num]", servContract.getDocNum());
    		placeholdersAndValues.put("[contr:date]", contractDate);

    		finalDocumentForSaving = new DocTypeProcessor(IDocumentMapping.DOCUMENT_SHELTER_CONTRACT_TEMPLATE_PATH).replaceParametersInDocument(placeholdersAndValues, Util.attachPhoto(client, log), ICustomMappingWordDocument.AVATAR_LOCATION_TOP_RIGHT);
            try {
    			finalDocumentForSaving.save(new File(contractPath));
    		} catch (Docx4JException e) {
    			log.error(e.getMessage(),e);
    		}
        }

		return finalDocumentForSaving;
    }
}
