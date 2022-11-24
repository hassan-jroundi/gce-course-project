package ma.sorec.gcecourse.service;

import com.itextpdf.text.DocumentException;
import ma.sorec.gcecourse.data.DataJson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface ReportService {

	public void generateReportFromJson(OutputStream out, DataJson data) throws Exception;

	public ByteArrayInputStream creerFacture(Long idFacture, boolean avecMail) throws DocumentException, IOException;

}
