package ru.homeless.mappings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.OverVacReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class OverVacReportMappingImpl implements ICustomMappingExcelDocument {
	
	@Autowired
    private IReportService reportService;

	@Override
	public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {
		Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
		List<OverVacReportEntity> reportResult = reportService.getOverVacReportEntity();
		int i=0;
		for(OverVacReportEntity r : reportResult) {
			List<String> row = new ArrayList<String>();
			row.add(r.getClientId());
			row.add(r.getName());
			row.add(r.getDateOfBirth());
			row.add(r.getRoomId());
			row.add(r.getInShelter());
			row.add(r.getOutShelter());
			row.add(r.getWorkerSurname());
			row.add(r.getFluoragr());
			row.add(r.getTyphVac());
			row.add(r.getDipthVac());
			row.add(r.getHepotitsVac());
			sheetData.put(i, row);
			i++;
		}
		return new DocTypeProcessor(IDocumentMapping.REPORT_OVERVAC_PATH).generateReport(sheetData, IDocumentMapping.REPORT_OVERVAC);
	}

}
