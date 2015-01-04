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
import ru.homeless.report.entities.OutOfShelterReportEntity;
import ru.homeless.report.entities.ResultWorkReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class OutOfShelterReportMappingImpl implements ICustomMappingExcelDocument {
	
	@Autowired
    private IReportService reportService;

	@Override
	public SpreadsheetMLPackage getDocument(Map<String, Date> requestParameters) {
		Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
		List<OutOfShelterReportEntity> reportResult = reportService.getOutOfShelterReportEntity(requestParameters.get("from"), requestParameters.get("till"));
		int i=0;
		for (OutOfShelterReportEntity r : reportResult) {
			List<String> row = new ArrayList<String>();
			row.add(r.getClientId());
			row.add(r.getClientSurname());
			row.add(r.getClientDateOfBirth());
			row.add(r.getRoomId());
			row.add(r.getInShelter());
			row.add(r.getContractPointCaption());
			row.add(r.getContractEndDate());
			row.add(r.getOutShelterDate());
			row.add(r.getComments());
			row.add(r.getContacts());
			row.add(r.getWorkerSurname());
			sheetData.put(i, row);
			i++;
		}
		return new DocTypeProcessor(IDocumentMapping.REPORT_OUT_OF_SHELTER_TEMPLATE_PATH).generateReport(sheetData);
	}

}
