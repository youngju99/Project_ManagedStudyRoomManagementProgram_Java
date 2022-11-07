package db;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-17
 * 내용: MySQL 연결
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnect {
	private Connection conn;
	
	private String url = "jdbc:mysql://localhost:3306/studyroom";
	private String id = "studyroomManager";
	private String pw = "Jukim!0108";
	
	public MySqlConnect() {}
	
	public Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			System.out.println("                                                      [ 접속 ]                                                          \n");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound Exception: " + e);
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		
		return conn;
	}
	
	public void disconnect(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("                                                      [ 종료 ]                                                          ");
			} catch (SQLException e) {
			}
		}
	}

	public String getUrl() {return url;}
	public String getId() {return id;}
	public String getPw() {return pw;}

	public void setUrl(String url) {this.url = url;}
	public void setId(String id) {this.id = id;}
	public void setPw(String pw) {this.pw = pw;}
}