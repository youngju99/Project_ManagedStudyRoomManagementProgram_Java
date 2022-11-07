package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 신규회원등록 sql
 * 
 * @re : 
 * @date : 2022/11/07
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db.DbExecute;

public class UserSeatSql {
	
	
	// 이름으로 검색한 유저 목록 리스트뽑기
	public List<UserSeatHistory> selectUser(Connection conn, PreparedStatement tmt, String name) {
		List<UserSeatHistory> list = new ArrayList<>();
		
		String sql = "SELECT userID, userName, userSchool, userGrade, userMobile, parentMobile, inputTime, outputTime, sms "
				+ " FROM user WHERE userName = '" + name + "' ;";
		String[] select = {sql};
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			while (rs.next()) {
				int userID = rs.getInt(1);
				String userName = rs.getString(2);
				String userSchool = rs.getString(3);
				int userGrade = rs.getInt(4);
				String userMobile = rs.getString(5);
				String parentMobile = rs.getString(6);
				String sms = rs.getString(9);
				
				UserSeatHistory user = new UserSeatHistory(userID, userName, userSchool, userGrade, userMobile,
						 parentMobile, sms);
				list.add(user);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return list;
	}
	
	// 블랙리스트인지 확인하기
	public static boolean blacklist(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT listNo"
				+ " FROM blacklist WHERE userID = '" + userID + "';";
		String[] select = {sql};
		int listNo = 0;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			if (rs.next()) {
				listNo = rs.getInt(1);
			}
			
			if (listNo <= 0) {
					return true;
			} 
				
			System.out.println("이 회원은 블랙리스트 입니다.");
			return false;

			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
			return false;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			return false;
		} 
		
	}
	
	// 좌석이 있는지 확인하기
	public static boolean selectSeat(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT seatNum"
				+ " FROM seat WHERE userID = '" + userID +"';";
		String[] select = {sql};
		int seatNum = 0;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			if (rs.next()) {
				seatNum = rs.getInt(1);
			}
			
			if (seatNum == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
			return false;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
			return false;
		} 
		
	}
	
	//
	public boolean selectUserID(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT userID"
				+ " FROM user WHERE userID = '" + userID + "';";
		String[] select = {sql};
		boolean result = false;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			while (rs.next()) {
				userID = rs.getInt(1);
				
			}
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return result;
	}

	// 신규 회원 정보 추가 메서드
	public static int addUser(Connection conn, PreparedStatement tmt, UserSeatHistory adduser) {
	
		String[] sql = {"INSERT INTO user(userName, userSchool, userGrade, userMobile, parentMobile, sms) "
				+ " VALUES('" + adduser.getUserName() + "', '" + adduser.getUserSchool() + "', '" + adduser.getUserGrade() + "', '"
				+ adduser.getUserMobile() + "', '" + adduser.getParentMobile() + "', '" + adduser.getSms() +"')"};
		DbExecute.insert(conn, sql);
		
		return 1;
	}
	
	// 좌석 추가 메서드
	public int addSeat(Connection conn, PreparedStatement tmt, UserSeatHistory addseat) {
		int insertCount = 0;
		String[] sql = {"INSERT INTO seat(userID, startDate, endDate)"
				+ " VALUES('" + addseat.getUserID() + "', '" + addseat.getStartDate() + "', '" + addseat.getEndDate() + "')"};
		DbExecute.insert(conn, sql);
		
		return insertCount;
	}

	
	// 좌석 정보 업데이트 메서드
	public int updateSeat(Connection conn, PreparedStatement tmt, UserSeatHistory role, int userID) {
		int updateCount = 0;
		String sql = "UPDATE seat SET startDate = '" + role.getStartDate() + "', endDate = '" + role.getEndDate()
				+ "' WHERE userID = '" + userID +"';";
		String[] update = {sql};
		DbExecute.update(conn, update);

		return updateCount;
	}
	
}

