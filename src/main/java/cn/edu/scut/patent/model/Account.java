package cn.edu.scut.patent.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable {

	// 电子邮件
	private String email;
	// 账户类型
	private String accountType;
	// 用户名
	private String username;
	// 单位
	private String department;
	// 密码
	private String password;

	public Account() {
		
	}
	
	public Account(String email, String accountType, String username,
			String department, String password) {
		this.email = email;
		this.accountType = accountType;
		this.username = username;
		this.department = department;
		this.password = password;
	}

	public Account(String email, String accountType, String username,
			String password) {
		this.email = email;
		this.accountType = accountType;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
