package ru.homeless.mappings;

import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.homeless.entities.Room;
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
		Map<Integer, List<String>> sheetData = new TreeMap<Integer, List<String>>();
        try {

            List<Integer> alreadyAddedPeople = new ArrayList<>();


            Map<Room, List<OverVacReportEntity>> reportResult = reportService.getOverVacReportEntity();
            int i =0; //first row
            for (Map.Entry entry : reportResult.entrySet()) {
                //for each room
                Room room = (Room) entry.getKey();
                List<OverVacReportEntity> peopleInRoom = (List<OverVacReportEntity>) entry.getValue();
                List<String> row = new ArrayList<String>();
                for (int k=0; k<4; k++) {
                    row.add("");
                }
                row.add("Комната " + room.getRoomnumber() + " - " + room.getRoommaxlivers() + " мест");
                sheetData.put(i,row);
                i++;
                int j=1;
                for (OverVacReportEntity mans : peopleInRoom) {

                    /*
                        Customization according HS-1: people could have more than one opened contract, so according current SQL in DAO
                        we can see the same people in one room. Next code will check if this man already added to the report or not.
                     */
                    if (!alreadyAddedPeople.contains(mans.getId())) {
                        List<String> man = new ArrayList<String>();
                        man.add(String.valueOf(j));
                        man.add(mans.getName());
                        man.add(mans.getDateOfBirth());
                        man.add(mans.getInShelter());
                        man.add(mans.getOutShelter());
                        man.add(mans.getWorkerSurname());
                        man.add(mans.getFluoragr());
                        man.add(mans.getHepotitsVac());
                        man.add(mans.getDipthVac());
                        man.add(mans.getTyphVac());
                        man.add(mans.getComments());
                        sheetData.put(i, man);
                        i++;
                        j++;
                        alreadyAddedPeople.add(mans.getId());
                    } else {
                        for (Map.Entry<Integer, List<String>> shRow : sheetData.entrySet()) {
                            List<String> cols = shRow.getValue();
                            System.out.println(cols.get(0) + " " +cols.get(1));
                            if (cols.get(1).equals(mans.getName()) && cols.get(2).equals(mans.getDateOfBirth())) { //if this is the same man then modify it
                                cols.set(5,cols.get(5)+", "+mans.getWorkerSurname());
                            }
                        }
                    }



                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
		return new DocTypeProcessor(IDocumentMapping.REPORT_OVERVAC_PATH).generateReport(sheetData, IDocumentMapping.REPORT_OVERVAC);
	}

}
