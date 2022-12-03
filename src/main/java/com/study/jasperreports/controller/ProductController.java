package com.study.jasperreports.controller;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RestController("/products")
@RequiredArgsConstructor
public class ProductController {

    private final DataSource dataSource;

    @GetMapping("/report")
    public void printReport(HttpServletResponse response) throws JRException, IOException, SQLException {

        InputStream reportStream = this.getClass().getResourceAsStream("src/main/resources/reports/Product.jasper");
        Map<String, Object> params = new HashMap<>();

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource.getConnection());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=products_son.pdf");

        OutputStream outputStream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }

}
