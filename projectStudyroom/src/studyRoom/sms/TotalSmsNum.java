package studyRoom.sms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbExecute;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-22, 2022-11-01(수정)
 * 내용 : SMS 잔여 건수 관리
 */

public class TotalSmsNum {
	private static int numOfSmsRemaining;
	static ResultSet rs=null;
	static String sql=null;
	
// sms 잔여 건수를 확인
	public static int getNumOfSmsRemaining() {return numOfSmsRemaining;}

// sms 잔여 건수 가져오는 메서드
	public static void check(Connection conn) {
		sql="SELECT totalSmsNum FROM totalSms;";
		
		rs=DbExecute.select(conn, rs, sql);
		
		try {
			while(rs.next()) {
				numOfSmsRemaining=rs.getInt("totalSmsNum");
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
// sms 잔여 건수를 추가하는 메서드
	public static void addSmsNum(Connection conn, int addNum) {
			String[] sql={"UPDATE totalSms SET totalSmsNum="+(numOfSmsRemaining+addNum)+";"};
			
			DbExecute.update(conn, sql);
	}
}
