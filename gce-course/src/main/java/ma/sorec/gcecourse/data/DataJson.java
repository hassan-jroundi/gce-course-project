package ma.sorec.gcecourse.data;

import java.util.HashMap;
import java.util.Map;

public class DataJson {

	private static final long serialVersionUID = 1L;
	
	private String reportName;
	private String format;
	private Map<String, Object> parameters = new HashMap<>();
    private String data;
    
	public DataJson() {
		super();
	}

	public DataJson(String reportName, String format, Map<String, Object> parameters, String data) {
		super();
		this.reportName = reportName;
		this.format = format;
		this.parameters = parameters;
		this.data = data;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
