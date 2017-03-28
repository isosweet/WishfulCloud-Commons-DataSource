package com.wishfulcloud.commons.dataSource.entity;
/**   
 * @Title: 
 * @Description: 
 * @author wangxuezheng
 * @date 2017年3月21日 下午4:14:41
 * @version V1.0   
 *
 */
public class User {
	
	/** id 主键. */
	private String id;

	/** 序号. */
	private String serialNumber;

	/** 租户编码. */
	private String tenantCode;

	/** 用户名. */
	private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
}
