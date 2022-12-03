package com.example.Security;

import java.io.File;

public class Config {

	public static final String MAIN_FILE_PATH = File.separator+"Identify";
	public static final String COMPANY_PATH = MAIN_FILE_PATH+File.separator+"Company";
	public static final String ADMIN_PATH = MAIN_FILE_PATH+File.separator+"Admin";
	public static final String SELLER_PATH = MAIN_FILE_PATH+File.separator+"Seller";
	public static final String END_USER_PATH = MAIN_FILE_PATH+File.separator+"End User";
	public static StringBuilder emailer = null;
}
