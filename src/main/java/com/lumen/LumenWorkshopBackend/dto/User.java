package com.lumen.LumenWorkshopBackend.dto;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(keyspace = "lumenkeyspace", value = "users")
public class User {
    
    @PrimaryKey
    private String mobileNo;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
}

