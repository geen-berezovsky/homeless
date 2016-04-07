package ru.homeless.mappings;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.entities.Room;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.OverVacReportEntity;
import ru.homeless.report.entities.ProvidedServicesByClientReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

import java.util.*;

@Component
public class ProvidedServicesByClientReportMappingImpl implements ICustomMappingExcelDocument {
	
	@Autowired
    private IReportService reportService;

	@Override
	public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {
		Map<Integer, List<String>> sheetData = new TreeMap<Integer, List<String>>();
        List<ProvidedServicesByClientReportEntity> result = reportService.getProvidedServicesByClientReport(requestParameters.get("from"), requestParameters.get("till"));

            int i = 0;
            for (ProvidedServicesByClientReportEntity entity : result){
                List<String> row = new ArrayList<String>();
                row.add(entity.getWorker());
                row.add(entity.getClient_id());
                row.add(entity.getClient_fio());
                row.add(entity.getService_type());
                row.add(entity.getDate());
                sheetData.put(i, row);
                i++;
            }

		return new DocTypeProcessor(IDocumentMapping.REPORT_PROVIDED_SERVICES_BY_CLIENT_PATH).generateReport(sheetData, IDocumentMapping.REPORT_PROVIDED_SERVICES_BY_CLIENT);
	}

}
