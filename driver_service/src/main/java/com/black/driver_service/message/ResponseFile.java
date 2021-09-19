package com.black.driver_service.message;

public class ResponseFile {
	private String driverId;
	private String name;
	private String url;
	private String type;
	private long size;

	public ResponseFile(String driverId, String name, String url, String type, long size) {
		this.driverId = driverId;
		this.name = name;
		this.url = url;
		this.type = type;
		this.size = size;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

}
