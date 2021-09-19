package com.black.driver_service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "driver_uploads")
public class DriverUpload {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "driver_id")
	private long driverId;
	
	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "created")
    private String created;
    
	@Column(name = "modified")
    private String modified;
	
	
	public DriverUpload() {
	}

	public DriverUpload(long driverId, String name, String type, String created, String modified) {
		super();
		this.driverId = driverId;
		this.name = name;
		this.type = type;
		this.created = created;
		this.modified = modified;
	}

	public long getId() {
		return id;
	}

	public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

}