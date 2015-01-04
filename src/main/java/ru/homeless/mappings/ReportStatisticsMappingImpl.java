package ru.homeless.mappings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.services.IContractService;
import ru.homeless.shared.IDocumentMapping;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ReportStatisticsMappingImpl implements ICustomMappingExcelDocument {

    @Autowired
    private IContractService contractService;

	@Override
	public SpreadsheetMLPackage getDocument(Map<String, String> requestParameters) {
		
		//HERE WE ASK DATABASE FOR ALL PARAMETERS AND POPULATE THE MAP
		//THEN WE CALL DOCTYPEPROCESSOR WHICH SHOULD LOAD TEMPLATE, ATTACH THE DATA FROM MAP AND RETURN FINISHED DOCUMENT
		
		
		Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
		
		
		return new DocTypeProcessor(IDocumentMapping.REPORT_STATISTICS_TEMPLATE_PATH).generateReport(sheetData);
	}

}
