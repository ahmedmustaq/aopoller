package com.maintainapps.modules.util;

public class UrlVO {
	
	private String[] url;
	
	private String pattern;
	
	private String[] childurls;
	
	private int removequery;
	
	private int removepath;
	
	private int jsmode;
	
	private int waitsec;
	
	private String visibilitySelector;

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String[] getChildurls() {
		return childurls;
	}

	public void setChildurls(String[] childurls) {
		this.childurls = childurls;
	}

	public int getRemovequery() {
		return removequery;
	}

	public void setRemovequery(int removequery) {
		this.removequery = removequery;
	}

	public int getRemovepath() {
		return removepath;
	}

	public void setRemovepath(int removepath) {
		this.removepath = removepath;
	}

	public int getJsmode() {
		return jsmode;
	}

	public void setJsmode(int jsmode) {
		this.jsmode = jsmode;
	}

	public int getWaitsec() {
		return waitsec;
	}

	public void setWaitsec(int waitsec) {
		this.waitsec = waitsec;
	}

	public String getVisibilitySelector() {
		return visibilitySelector;
	}

	public void setVisibilitySelector(String visibilitySelector) {
		this.visibilitySelector = visibilitySelector;
	}

	
	
	
	

}
