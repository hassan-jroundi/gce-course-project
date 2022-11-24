package ma.sorec.gcecourse.controller;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.DataJson;
import ma.sorec.gcecourse.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(value = "/export")
@Api("Report Controller")
public class ExportReportController {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ReportService reportService;

    // Create the pdf/excel report via jasper framework.
    @PostMapping(value = "/report", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Exporte les données par DataJSON")
    public void exportDataJsonReport(HttpServletResponse response, @RequestBody DataJson data) throws Exception {

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment; filename= " + data.getReportName() + "." + data.getFormat());
        log.info("Preparing the report via jasper.");
        reportService.generateReportFromJson(out, data);
        log.info("File successfully saved at the given path.");

    }

    @GetMapping(value = "/facture/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    @ApiOperation(value = "Génère la facture")
    public ResponseEntity<InputStreamResource> genererFacture(@PathVariable("id") String id) throws IOException, DocumentException {

        ByteArrayInputStream bis = reportService.creerFacture(SharedUtil.getIdLong(id), false);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=facture-numero" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
