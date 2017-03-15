package ru.homeless.mappings;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import ru.homeless.services.IReportService;
import ru.homeless.shared.IDocumentMapping;
import ru.homeless.processors.DocTypeProcessor;
import ru.homeless.report.entities.ResultWorkReportEntity;

/**
 * Created by maxim on 30.11.14.
 */
@Component
public class ResultWorkReportMappingImpl implements ICustomMappingExcelDocument {

    @Autowired
    private IReportService reportService;

    @Override
    public XSSFWorkbook getDocument(Map<String, Date> requestParameters) {

        //HERE WE ASK DATABASE FOR ALL PARAMETERS AND POPULATE THE MAP
        //THEN WE CALL DOCTYPEPROCESSOR WHICH SHOULD LOAD TEMPLATE, ATTACH THE DATA FROM MAP AND RETURN FINISHED DOCUMENT
        Map<Integer, List<String>> sheetData = new HashMap<>();

        List<ResultWorkReportEntity> reportResult = reportService.getResultWorkReport(requestParameters.get("from"), requestParameters.get("till"));
        int i = 0;
        for (ResultWorkReportEntity r : reportResult) {
            List<String> row = new ArrayList<>();
            row.add(r.getWorkerSurname());
            row.add(r.getContractPointsCaption());
            row.add(r.isLivingInShelter() ? "да" : "нет");
            row.add(String.valueOf(r.getTasksPerformed()));
            sheetData.put(i, row);
            i++;
        }
        return new DocTypeProcessor(IDocumentMapping.REPORT_WORK_RESULT_TEMPLATE_PATH).generateReport(sheetData, IDocumentMapping.REPORT_WORK_RESULT);
    }

}
