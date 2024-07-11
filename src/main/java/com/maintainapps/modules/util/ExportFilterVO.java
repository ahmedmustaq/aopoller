package com.maintainapps.modules.util;

public class ExportFilterVO {
	
	private String serverurl;
	
	private long fromdate;
	
	private long todate;
	
	private int noofrecords;
	
	private String category;
	
	private String domain;

	public String getServerurl() {
		return serverurl;
	}

	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}

	public long getFromdate() {
		return fromdate;
	}

	public void setFromdate(long fromdate) {
		this.fromdate = fromdate;
	}

	public long getTodate() {
		return todate;
	}

	public void setTodate(long todate) {
		this.todate = todate;
	}

	public int getNoofrecords() {
		return noofrecords;
	}

	public void setNoofrecords(int noofrecords) {
		this.noofrecords = noofrecords;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
		
	
	

}
