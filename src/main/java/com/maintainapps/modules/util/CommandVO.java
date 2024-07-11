package com.maintainapps.modules.util;

public class CommandVO {
	
	private String type;
	
	private Object args;
	
	private String crawlId;
	
	private String[] seedUrls;
	
	private String url;
	
	private String name;
	
	private String seedfile;
	
	private String message;
	
	private String configId;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public Object getArgs() {
		return args;
	}

	public void setArgs(Object args) {
		this.args = args;
	}

	public String getCrawlId() {
		return crawlId;
	}

	public void setCrawlId(String crawlId) {
		this.crawlId = crawlId;
	}

	public String[] getSeedUrls() {
		return seedUrls;
	}

	public void setSeedUrls(String[] seedUrls) {
		this.seedUrls = seedUrls;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeedfile() {
		return seedfile;
	}

	public void setSeedfile(String seedfile) {
		this.seedfile = seedfile;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	
	
	

}
