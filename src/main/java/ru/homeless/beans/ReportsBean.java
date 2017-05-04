package ru.homeless.beans;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import ru.homeless.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean (name = "reports")
@ViewScoped
public class ReportsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ReportsBean.class);

    private Date startDate;
    private Date endDate;

    private StreamedContent file;

	public ReportsBean() {
//		HttpSession session = Util.getSession();
	}



    public void openDlg() {
        RequestContext rc = RequestContext.getCurrentInstance();
        //rc.update("room_settings");
        rc.execute("PF('reportsFormDlgWv').show();");

        Calendar c1 = GregorianCalendar.getInstance();
        endDate = c1.getTime();
        c1.add(Calendar.MONTH, -1);
        startDate = c1.getTime();

    }

    private StreamedContent getMacroReport(int type, String from, String till, String filename) throws IOException {
        String requestSuffix = "/getMacroReport?requestType="+type+"&from="+ from + "&till=" + till;
        String saveFilePath = "/tmp" + File.separator + filename;
        String docType = "application/vnd.ms-excel.sheet.macroEnabled.12";
        log.info("Getting URL: "+requestSuffix);
        return Util.downloadDocument(requestSuffix, saveFilePath, docType, filename);
    }

    private StreamedContent getReport(int type, String from, String till, String filename) throws IOException {
        String requestSuffix = "/getReport?requestType="+type+"&from="+ from + "&till=" + till;
        String saveFilePath = "/tmp" + File.separator + filename;
        String docType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        log.info("Getting URL: "+requestSuffix);
        return Util.downloadDocument(requestSuffix, saveFilePath, docType, filename);
    }


    public void downloadWorkResultReport() throws IOException {
        String start = Util.formatDate(startDate);
        String end = Util.formatDate(endDate);
        file = getMacroReport(200, start, end,
                String.format("О предоставленных услугах с %s по %s.xlsm", start, end));
    }


    public void downloadStatisticsReport() throws IOException {
        file = getMacroReport(212, Util.formatDate(startDate), Util.formatDate(endDate), "StatisticsReport.xlsm");
    }

    public void downloadInnerReport() throws IOException {
        file = getMacroReport(210, Util.formatDate(startDate), Util.formatDate(endDate), "InnerReport.xlsm");
    }

    public void downloadOuterReport() throws IOException {
        file = getMacroReport(208, Util.formatDate(startDate), Util.formatDate(endDate), "OuterReport.xlsm");
    }

    public void downloadOverVacReport() throws IOException {
        file = getReport(206, Util.formatDate(startDate), Util.formatDate(endDate), "OverVacReport.xlsx");
    }

    public void downloadEvictedReport() throws IOException {
        file = getMacroReport(202, Util.formatDate(startDate), Util.formatDate(endDate), "EvictedReport.xlsm");
    }

    public void downloadOnceOnlyReport() throws IOException {
        file = getMacroReport(204, Util.formatDate(startDate), Util.formatDate(endDate), "OnceOnlyReport.xlsm");
    }

    public void downloadProvidedServicesByClientReport() throws IOException {
        file = getReport(214, Util.formatDate(startDate), Util.formatDate(endDate), "OnceOnlyByClientReport.xlsx");
    }

    public void downloadServiceRecipientReport() throws IOException {
	    file = getReport(215, Util.formatDate(startDate), Util.formatDate(endDate), "ServiceRecipientReport.xlsx");
    }




    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
