package com.maintainapps.modules.util;

public class HostVO {
	
	private String host;
	private long numfound;
	
	public HostVO(String host, long numfound)
	{
		this.host = host;
		this.numfound = numfound;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public long getNumfound() {
		return numfound;
	}
	public void setNumfound(long numfound) {
		this.numfound = numfound;
	}
	
	

}
