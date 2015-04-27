package ru.homeless.mappings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.CustomStatisticsReportEntity;
import ru.homeless.services.ICustomReportService;
import ru.homeless.shared.IDocumentMapping;

@Component
public class CustomStatisticsReportMappingImpl implements ICustomMappingExcelDocument{

	@Autowired
	public ICustomReportService customReportService;
	
	
	@Override
	public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {
		
		List<CustomStatisticsReportEntity> list = new ArrayList<>();
		Date from = requestParameters.get("from");
		Date till = requestParameters.get("till");
		list.add(customReportService.getReportDataByGender(from, till));
		list.add(customReportService.getReportDataByMartialStatus(from, till));
        list.add(customReportService.getReportDataByEducation(from, till));
        list.add(customReportService.getReportDataByChilds(from, till));
        list.add(customReportService.getReportDataByStudentsOrNot(from, till));
        list.add(customReportService.getReportDataByProfession(from, till));
        list.add(customReportService.getReportDataByLiveInFlat(from, till));

		//1. Add Gender Report Data
		//2. Add ... Data
		//3. Add ... Data
		//N. TBD
		
		
		return new DocTypeProcessor(IDocumentMapping.REPORT_CUSTOM_STATISTICS_PATH).generateReport(list);
	}

}
