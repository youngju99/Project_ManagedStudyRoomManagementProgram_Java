package studyRoom.pay;

/*
 * @date : 2022/11/04
 * @memo : 신규회원등록 sql
 * 
 * @re : 
 * @date : 2022/11/07
 * 
 * @re : 경고 제거
 * @date : 2022/11/08
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DbExecute;

public class UserPaySql {
	
	
	// select--------------------------------------------------------------------
	// 이름으로 검색한 유저 목록 리스트뽑기
	public List<UserSeatHistory> selectUser(Connection conn, PreparedStatement tmt, String name) {
		List<UserSeatHistory> list = new ArrayList<>();
		
		String sql = "SELECT userID, userName, userSchool, userGrade, userMobile, parentMobile, inputTime, outputTime, sms "
				+ " FROM user WHERE userName = '" + name + "' ;";
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
		int seatNum = 0;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			if (rs.next()) {
				seatNum = rs.getInt(1);
			}
			
			if (seatNum <= 0) {
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
	
	// 등록되어있는 회원인지 확인하기
	public boolean selectUserID(Connection conn, PreparedStatement tmt, String name) {
		String sql = "SELECT userID"
				+ " FROM user WHERE userName = '" + name + "';";
		int userID = 0;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			if (rs.next()) {
				userID = rs.getInt(1);
			}
			
			if (userID <= 0) {
				
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
	

	// insert----------------------------------------------------------------
	// 신규 회원 정보 추가 메서드(usermain)
	public int addUser(Connection conn, PreparedStatement tmt, UserSeatHistory adduser) {
	
		String[] sql = {"INSERT INTO user(userName, userSchool, userGrade, userMobile, parentMobile, sms) "
				+ " VALUES('" + adduser.getUserName() + "', '" + adduser.getUserSchool() + "', '" + adduser.getUserGrade() + "', '"
				+ adduser.getUserMobile() + "', '" + adduser.getParentMobile() + "', '" + adduser.getSms() +"')"};
		DbExecute.insert(conn, sql);
		
		return 1;
	}
	

	// 결제테이블에 정보저장
	public int addPay(Connection conn, PreparedStatement tmt, PayHistory addPay) {
		int insertCount = 0;
		String sql = "INSERT INTO pay(userID, payment, payTime)"
				+ "VALUES('" + addPay.getUserID() + "', '" + addPay.getPayment() + "', '" + addPay.getPayTime() + "')";
		String[] insert = {sql};
		DbExecute.insert(conn, insert);

		return insertCount;
	}
	
	
	// update-----------------------------------------------------
	// 좌석 정보 업데이트 메서드
	public int updateSeat(Connection conn, PreparedStatement tmt, UserSeatHistory role, int userID) {
		int updateCount = 0;
		String sql = "UPDATE seat SET startDate = '" + role.getStartDate() + "', endDate = '" + role.getEndDate()
				+ "' WHERE userID = '" + userID +"';";
		String[] update = {sql};
		DbExecute.update(conn, update);

		return updateCount;
	}
	
	// 좌석 정보 업데이트 메서드
		public int updateNewSeat(Connection conn, PreparedStatement tmt, UserSeatHistory role, int seatNum) {
			int updateCount = 0;
			String sql = "UPDATE seat SET startDate = '" + role.getStartDate() + "', endDate = '" + role.getEndDate()
					+ "' WHERE seatNum = '" + seatNum +"';";
			String[] update = {sql};
			DbExecute.update(conn, update);

			return updateCount;
		}
	
}


