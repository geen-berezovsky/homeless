package ru.homeless.mappings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.report.entities.OutOfShelterReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class OneTimeServicesReportMappingImpl implements ICustomMappingExcelDocument {
	
	@Autowired
    private IReportService reportService;

	@Override
	public SpreadsheetMLPackage getDocument(Map<String, Date> requestParameters) {
		Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
		List<OneTimeServicesReportEntity> reportResult = reportService.getOneTimeServicesReportEntity(requestParameters.get("from"), requestParameters.get("till"));
		int i=0;
		for(OneTimeServicesReportEntity r : reportResult) {
			List<String> row = new ArrayList<String>();
			row.add(r.getName());
			row.add(r.getType());
			sheetData.put(i, row);
			i++;
		}
		return new DocTypeProcessor(IDocumentMapping.REPORT_ONE_TIME_SERVICES_PATH).generateReport(sheetData);
	}
	
	
}
