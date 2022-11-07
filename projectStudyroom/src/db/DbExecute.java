package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbExecute extends MySqlConnect {
	private static int row;

// delete
	public static void delete(Connection conn, String[] sql) {
		execute(conn, sql);
	}

// update
	public static void update(Connection conn, String[] sql) {
		row=execute(conn, sql);
	}
	public static int updateReturnRow(Connection conn, String[] sql) {
		row=execute(conn, sql);
		
		return row;
	}

// insert
	public static void insert(Connection conn, String[] sql) {
		execute(conn, sql);
	}

// select
	public static ResultSet select(Connection conn, ResultSet rs, String sql) {

		try {
			rs = conn.prepareStatement(sql).executeQuery();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		if (rs != null) {
			return rs;
		} else {
			return null;
		}
	}

// setBack(뒤로 가기)
	public static void setBack(Connection conn, String[] deleteSql, String[] insertSql) {
		
		delete(conn, deleteSql);
		insert(conn, insertSql);
	}

// sql 실행
	public static int execute(Connection conn, String[] sql) {
		try {
			for (String s : sql) {
				row = conn.prepareStatement(s).executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		
		return row;
	}
}