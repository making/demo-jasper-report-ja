package com.example.demojasperreportja;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    private final JasperReport report;

    private HelloController(@Value("classpath:jasperreports/Blank_A4.jrxml") Resource jrxml) throws Exception {
        try (InputStream inputStream = jrxml.getInputStream()) {
            this.report = JasperCompileManager.compileReport(inputStream);
        }
    }

    @GetMapping(path = "/")
    public void hello(@RequestParam(name = "name", defaultValue = "山田") String name, HttpServletResponse response) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        JRDataSource dataSource = new JREmptyDataSource();
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
