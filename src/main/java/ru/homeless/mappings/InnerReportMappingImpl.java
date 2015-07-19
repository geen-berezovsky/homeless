package ru.homeless.mappings;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.InnerReportEntity;
import ru.homeless.report.entities.OuterReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

import java.util.*;

@Component
public class InnerReportMappingImpl implements ICustomMappingExcelDocument {
    public static final Logger log = Logger.getLogger(InnerReportMappingImpl.class);
	@Autowired
    private IReportService reportService;


	@Override
	public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {
        log.info("Preparing the sheet");
        Map<Integer, List<String>> sheetData = new HashMap<Integer, List<String>>();
        try {
            List<InnerReportEntity> reportResult = reportService.getInnerReportEntity();
            int i = 0;
            for (InnerReportEntity r : reportResult) {
                List<String> row = new ArrayList<String>();
                row.add(r.getClientId());
                row.add(r.getName());
                row.add(r.getDateOfBith());
                row.add(r.getComments());
                row.add(r.getRoom());
                row.add(r.getStartDate());
                row.add(r.getStopDate());
                row.add(r.getPurposes());
                row.add(r.getReleaseDate());
                row.add(r.getReleaseSteps());
                row.add(r.getWorkerSurname());
                sheetData.put(i, row);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DocTypeProcessor(IDocumentMapping.REPORT_INNER_PATH).generateReport(sheetData, IDocumentMapping.REPORT_INNER);
	}

}
