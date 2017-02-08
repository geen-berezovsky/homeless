package ru.homeless.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.homeless.generators.GenericGenerator;

@Controller
public class GetReportsController {

    private static final String CONTENT_TYPE
            = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_MACRO
            = "application/vnd.ms-excel.sheet.macroEnabled.12";
    private static final String CONTENT_DISPOSITION
            = "attachment; filename*=UTF-8''NewReport.xlsx";
    private static final Logger LOG = Logger.getLogger(GetReportsController.class);

    @Autowired
    private GenericGenerator gg;

    @RequestMapping(value = "/getReport", method = RequestMethod.GET)
    @ResponseBody
    public String getReport(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        LOG.info("Called R");
        response.setContentType(CONTENT_TYPE);
        response.addHeader("Content-disposition", CONTENT_DISPOSITION);
        if (gg == null) {
            gg = new GenericGenerator();
        }
        LOG.info(String.format("Called Report: %s\n", request));
        XSSFWorkbook document = gg.generateExcelDocument(request);
        try (OutputStream out = response.getOutputStream()) {
            out.flush();
            document.write(out);
        } catch (IOException e) {
            LOG.error("Failed to write document: ", e);
            e.printStackTrace();
        }
        return "Report sent";
    }

    @ResponseBody
    @RequestMapping(value = "/getMacroReport", method = RequestMethod.GET)
    public String getMacroReport(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        LOG.info("Called MR");
        response.setContentType(CONTENT_TYPE_MACRO);
        response.addHeader("Content-disposition", CONTENT_DISPOSITION);
        if (gg == null) {
            gg = new GenericGenerator();
        }
        LOG.info("Called Macro Report\n" + request);
        XSSFWorkbook document = gg.generateExcelDocument(request);
        LOG.info("Document: " + document);
        try (OutputStream out = response.getOutputStream()) {
            document.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            LOG.error("Failed to write document: ", e);
            e.printStackTrace();
        }
        return "Report sent";
    }
}
