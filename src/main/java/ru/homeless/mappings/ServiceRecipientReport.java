package ru.homeless.mappings;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.ServiceRecipientReportEntity;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;

import java.util.*;

/**
 * Отчёт о получателях услуг
 * Created by geen on 29.11.16.
 */
@Component
public class ServiceRecipientReport {

    @Autowired
    private IReportService reportService;

    public XSSFWorkbook makeReport(Date from, Date to) {
        Map<Integer, List<String>> sheetData = new TreeMap<>();

        int i = 0;
        for (ServiceRecipientReportEntity entity : reportService.getServiceRecipientReport(from, to)) {
            List<String> row = new ArrayList<>(4);

            row.add(entity.getWorker());
            row.add(entity.getServiceType());
            row.add(entity.getCountOfUniqueClient().toString());
            row.add(entity.getCountOfService().toString());

            sheetData.put(i, row);
            i++;
        }

        return new DocTypeProcessor(IDocumentMapping.REPORT_SERVICES_RECIPIENT_PATH)
                .generateReport(sheetData, IDocumentMapping.REPORT_SERVICES_RECIPIENT);
    }
}
