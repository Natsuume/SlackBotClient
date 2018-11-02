package com.natsuumeweb.niconico;

public class LoginInfo {
	private final String mail;
	private final String password;
	private String session;
	
	public LoginInfo(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}
	
	public void setSession(String session) {
		this.session = session;
	}
	
	public String getCookie() {
		return session;
	}
	
	public boolean isLogin() {
		return session != null;
	}
}
