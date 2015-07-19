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
import ru.homeless.report.entities.OuterReportEntity;
import ru.homeless.report.entities.OverVacReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class OuterReportMappingImpl implements ICustomMappingExcelDocument {
	
	@Autowired
    private IReportService reportService;

	@Override
	public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {
		Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
		List<OuterReportEntity> reportResult = reportService.getOuterReportEntity();
		int i=0;
		for(OuterReportEntity r : reportResult) {
			List<String> row = new ArrayList<String>();
			row.add(r.getClientId());
			row.add(r.getName());
			row.add(r.getDateOfBith());
			row.add(r.getStartDate());
			row.add(r.getCaption());
			row.add(r.getEndDate());
			row.add(r.getStopDate());
			row.add(r.getComments());
			row.add(r.getMemo());
			row.add(r.getWorkerSurname());
			sheetData.put(i, row);
			i++;
		}
		return new DocTypeProcessor(IDocumentMapping.REPORT_OUTER_PATH).generateReport(sheetData, IDocumentMapping.REPORT_OUTER);
	}

}
