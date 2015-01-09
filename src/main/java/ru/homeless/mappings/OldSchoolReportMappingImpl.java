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
import ru.homeless.report.entities.OldSchoolReportEntity;
import ru.homeless.report.entities.OneTimeServicesReportEntity;
import ru.homeless.services.ICustomReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class OldSchoolReportMappingImpl implements ICustomMappingExcelDocument{

	@Autowired
	public ICustomReportService customReportService;
	
	
	@Override
	public SpreadsheetMLPackage getDocument(Map<String, Date> requestParameters) {
		
		List<OldSchoolReportEntity> list = new ArrayList<>();
		Date from = requestParameters.get("from");
		Date till = requestParameters.get("till");
		list.add(customReportService.getReportDataByGender(from, till));
		list.add(customReportService.getReportDataByMartialStatus(from, till));
		//1. Add Gender Report Data
		//2. Add ... Data
		//3. Add ... Data
		//N. TBD
		
		
		return new DocTypeProcessor(IDocumentMapping.REPORT_OLD_SCHOOL_PATH).generateReport(list);
	}

}
