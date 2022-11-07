package studyRoom.blacklist;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * 작성자 : 정우성
 * 작성일 : 2022-10-26
 * 수정일 : 2022-10-26
 * 내용 : 벌점 추가
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class Punishment {

	Connection conn = null;
	PreparedStatement tmt = null;
	PunishInfo pi = new PunishInfo();
	
	public static void main(String[] args) throws Exception {
		try {
			// todo : 공통 db Conn 사용가능.
			String url = "jdbc:mysql://localhost:3306/studyroom";
			String id = "root";
			String pw = "0194";
			
			Connection conn = DriverManager.getConnection(url, id, pw);
			PreparedStatement tmt = null;
			System.out.println("Test Main Start!!!");
			
			PunishInfo pi = new PunishInfo(1);
			
			while(true) {
				// dummy for test
				int userId = 1;
				
				setPunishInfo(conn, tmt, pi);
				
				pi = getPunishInfo(conn, tmt, pi);
				
				System.out.println("userID : " +pi.getUserID());
				System.out.println("Point : " +pi.getPoint());
			}
		
		} catch(SQLException e) {
			System.out.println(e);
		}
	
		
	}

	/**
	 * 
	 * @param conn
	 * @param tmt
	 * @param pi
	 * @return PunishInfo
	 * 사용자 ID 와 Point 가져오기
	 */
	public static PunishInfo getPunishInfo(Connection conn, PreparedStatement tmt, PunishInfo pi) {
		
		//PunishInfo pi = new PunishInfo(userID);
		
		try {
			String sql = "SELECT userID -- 사용자 ID"
					   + "     , point -- 사용자 벌점"
					   + "  FROM user "
					   + " WHERE userID = ?;" ;
			
			tmt = conn.prepareStatement(sql);
			tmt.setInt(1, pi.getUserID());
			
			ResultSet rs = tmt.executeQuery();
			// userId
//			rs.getInt(1);
			// point			
			pi.setPoint(rs.getInt(2));
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return pi;
	}
	
	/**
	 * 
	 * @param conn
	 * @param tmt
	 * @param pi
	 * @return PunishInfo
	 * todo : 1. 회원 벌점 정보 가져오기
	 *        2. 회원 벌점 업데이트
	 *        3. 블랙리스트 기준 초과시 블랙리스트 인원 추가
	 *        4. 블랙리스트 회원은 비활성화 또는 삭제
	 *        5. 블랙리스트 회원 신규 회원가입 막는 로직 삽입
	 *        
	 *        * 월말에 벌점 초기화 
	 *        * 블랙리스트 정보 트랜잭션
	 */
	public static PunishInfo setPunishInfo(Connection conn, PreparedStatement tmt, PunishInfo pi) {
		PunishInfo pif = getPunishInfo(conn, tmt, pi);
		
		try {
			// 회원의 기존 포인트
			int prePoint = pif.getPoint();
			// Update 할 포인트
			int nextPoint; 
			
			// 벌점 선택
			System.out.println("적용할 벌점을 입력하세요.");
			System.out.println("1.소음");
			System.out.println("2.휴대폰 사용");
			System.out.println("3.무단이용");
			System.out.println("4.다툼");
			System.out.println("5.관리자권한");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			StringTokenizer stk = new StringTokenizer(br.readLine());
			
			String op = stk.nextToken();
			br.close();
			
			switch(op) {
				case "1":
					nextPoint = prePoint -2;
					System.out.println("-2 점이 부과됩니다.");
					break;
				case "2":
					nextPoint = prePoint -3;
					System.out.println("-3 점이 부과됩니다.");
					break;
				case "3":
					nextPoint = prePoint -5;
					System.out.println("-5 점이 부과됩니다.");
					break;
				case "4":
					nextPoint = prePoint -10;
					System.out.println("-10 점이 부과됩니다.");
					break;
				case "5":
					nextPoint = prePoint -15;
					System.out.println("-15 점이 부과됩니다.");
					break;
				default:
					nextPoint = prePoint;
					System.out.println("벌점 항목을 제대로 입력하지 않았습니다.");
					break;
			}
			
			
			String update_sql = "UPDATE user"
					          + "   SET point = ?"
					          + " WHERE userID = ?;";
			
			tmt = conn.prepareStatement(update_sql);
			tmt.setInt(1, nextPoint); 
			tmt.setInt(2, pif.getUserID());
			
			tmt.executeUpdate();
			tmt.clearParameters();
			
			conn.commit();
			tmt.close();
			
			// 회원의 벌점 업데이트
			// 벌점 -15 초과시 블랙리스트 Insert
			if(nextPoint <= -15) {
				String update_black_sql = "INSERT INTO blacklist"
						                + "(userID)"
						                + "VALUES "
						                + "(?);";
				
				tmt.setInt(1, pif.getUserID());
				
				tmt.executeUpdate();
				tmt.clearParameters();
				tmt.close();
				
				conn.commit();
				System.out.println("userID : " +pif.getUserID()+ " 회원이 블랙리스트로 지정되었습니다.");
			}
			
			// 최종 사용자의 벌점 출력
			System.out.println();
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		
		return pi;
	}
}
