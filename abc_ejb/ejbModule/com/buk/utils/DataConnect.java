package com.buk.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnect {

	public static Connection getConnection() {
		try {
			Class.forName("mysql-connector-java-5.1.49-bin.jar");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/bukdb", "root", "");
			return con;
		} catch (Exception ex) {
			System.out.println("Database.getConnection() Error -->"
					+ ex.getMessage());
			return null;
		}
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}