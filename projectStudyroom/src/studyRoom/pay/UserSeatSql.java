package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 신규회원등록 sql
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
	private static ResultSet rs;
	
	// 이름으로 검색한 유저 목록 리스트뽑기
	public List<UserSeatHistory> selectUser(Connection conn, PreparedStatement tmt, String name) {
		List<UserSeatHistory> list = new ArrayList<>();
		
		String sql = "SELECT userID, userName, userSchool, userGrade, userMobile, parentMobile, inputTime, outputTime, sms "
				+ "FROM user WHERE userName = ?;";
		String[] select = {sql};
		
		try {
			tmt = conn.prepareStatement(sql);
			tmt.setString(1, name);
//			rs = DbExecute.select(conn, rs, sql);
			ResultSet rs = tmt.executeQuery();
			
			while (rs.next()) {
				int userID = rs.getInt(1);
				String userName = rs.getString(2);
				String userSchool = rs.getString(3);
				int userGrade = rs.getInt(4);
				String userMobile = rs.getString(5);
				String parentMobile = rs.getString(6);
				String sms = rs.getString(9);				
				UserSeatHistory role = new UserSeatHistory(userID, userName, userSchool, userGrade, userMobile,
						 parentMobile, sms);
				list.add(role);
			}
			
			tmt.clearParameters();
			tmt.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return list;
	}
	
	public boolean blacklist(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT listNo"
				+ "FROM blacklist WHERE userID = ?;";
		String[] select = {sql};
		int listNo = 0;
		boolean result = true;
		
		try {
			tmt = conn.prepareStatement(sql);
			tmt.setInt(1, userID);
//			rs = DbExecute.select(conn, rs, sql);
			ResultSet rs = tmt.executeQuery();
			
			while (rs.next()) {
				listNo = rs.getInt(1);
				
				if (listNo == 0) {
					result = true;
				} else {
					result = false;
				}
				
			}
			
			tmt.clearParameters();
			tmt.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return result;
	}
	
	public boolean select_seat(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT seatNum"
				+ "FROM seat WHERE userID = ?;";
		String[] select = {sql};
		boolean result = false;
		
		try {
			tmt = conn.prepareStatement(sql);
			tmt.setInt(1, userID);
			rs = DbExecute.select(conn, rs, sql);
			ResultSet rs = tmt.executeQuery();
			
			while (rs.next()) {
				int seatNum = rs.getInt(1);
				
				if (seatNum == 0) {
					result = false;
				} else {
					result = true;
				}
				
			}
			
			tmt.clearParameters();
			tmt.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return result;
	}
	
	public boolean selectUserID(Connection conn, PreparedStatement tmt, int userID) {
		String sql = "SELECT userID"
				+ "FROM user WHERE userID = ?;";
		String[] select = {sql};
		boolean result = false;
		
		try {
			tmt = conn.prepareStatement(sql);
			tmt.setInt(1, userID);
			rs = DbExecute.select(conn, rs, sql);
			ResultSet rs = tmt.executeQuery();
			
			while (rs.next()) {
				userID = rs.getInt(1);
				
			}
			
			tmt.clearParameters();
			tmt.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return result;
	}

	public int addUser(Connection conn, PreparedStatement tmt, UserSeatHistory adduser) {
		int insertCount = 0;
		String sql = "INSERT INTO user(userName, userSchool, userGrade, userMobile, parentMobile, sms)"
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		String[] insert = {sql};
		DbExecute.insert(conn, insert);

		try {
			tmt = conn.prepareStatement(sql);
			
			tmt.setString(1, adduser.getUserName());
			tmt.setString(2, adduser.getUserSchool());
			tmt.setInt(3, adduser.getUserGrade());
			tmt.setString(4, adduser.getUserMobile());
			tmt.setString(5, adduser.getParentMobile());
			tmt.setString(6, adduser.getSms());

			insertCount = tmt.executeUpdate();
			tmt.clearParameters();
			
			tmt.close();

		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return insertCount;
	}
	
	public int addSeat(Connection conn, PreparedStatement tmt, UserSeatHistory addseat) {
		int insertCount = 0;
		String sql = "INSERT INTO seat(userID, startDate, endDate)"
				+ "VALUES(?, ?, ?)";
		String[] insert = {sql};
		DbExecute.insert(conn, insert);
		
		try {
			tmt = conn.prepareStatement(sql);

			tmt.setInt(1, addseat.getUserID());
			tmt.setTimestamp(2, addseat.getStartDate());
			tmt.setTimestamp(3, addseat.getEndDate());

			insertCount = tmt.executeUpdate();
			tmt.clearParameters();
			
			tmt.close();

		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 

		return insertCount;
	}

	

	public int updateSeat(Connection conn, PreparedStatement tmt, UserSeatHistory role, int userID) {
		int updateCount = 0;
		String sql = "UPDATE seat SET startDate = ?, endDate = ? "
				+ "WHERE userID = ?";
		String[] update = {sql};
		DbExecute.update(conn, update);

		try {
			
			tmt = conn.prepareStatement(sql);

			tmt.setTimestamp(1, role.getStartDate());
			tmt.setTimestamp(2, role.getEndDate());
			tmt.setInt(3, userID);

			updateCount = tmt.executeUpdate();
			
			tmt.clearParameters();
			tmt.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 

		return updateCount;
	}
	
}
