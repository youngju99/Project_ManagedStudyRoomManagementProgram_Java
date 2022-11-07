package studyRoom.user_editor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/*
 * 작성자 : 정우성
 * 작성일 : 2022-10-24
 * 수정일 : 2022-11-07
 * 내용 : 벌점 추가
 */
public class Editor {
	/*
	 * 	1. 회원정보 가져오기.
	 *     - 조회조건 휴대폰 번호
	 *     
	 *	2. 수정할 정보 입력. 
	 *     - DB 업데이트.
	 *	   - 수정결과 출력.
	 */
	public static void updateUserInfo(Connection conn) {
		PreparedStatement tmt = null;
		try {
			System.out.println("사용자 정보 변경 화면입니다.");
			System.out.println("정보를 변경할 사용자의 전화번호를 입력하세요.");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			UserInfo ui = getUserInfo(conn, br.readLine());
			
			System.out.println("변경할 사용자의 이름을 입력하세요.");
			ui.setUserName(br.readLine());
			System.out.println("변경할 사용자의 학교를 입력하세요");
			ui.setUserSchool(br.readLine());
			System.out.println("변경할 사용자의 학년을 입력하세요");
			ui.setUserGrade(Integer.parseInt(br.readLine()));
			System.out.println("변경할 사용자의 전화번호를 입력하세요");
			ui.setUserMobile(br.readLine());
			System.out.println("변경할 사용자의 부모님 연락처를 입력하세요");
			ui.setParentMobile(br.readLine());
			
			System.out.println("변경할 사용자의 등록날짜를 입력하세요 - 일:2자리/월:2자리/년도:4자리");
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		       // you can change format of date
	        Date input_dt = formatter.parse(br.readLine());
		    Timestamp input_ts = new Timestamp(input_dt.getTime());
			ui.setInputTime(input_ts);
			
			System.out.println("변경할 사용자의 만료일 입력하세요");
			Date output_dt = formatter.parse(br.readLine());
		    Timestamp output_ts = new Timestamp(output_dt.getTime());
			ui.setOutputTime(output_ts);
			
			System.out.println("변경할 사용자의 sms 수신여부 Y / N 을 입력하세요");
			ui.setSms(br.readLine());
			
			String sql = "UPDATE user"
					   + "   SET userName = ?"
					   + "     , userSchool = ?"
					   + "     , userGrade = ?"
					   + "     , userMobile = ?"
					   + "     , parentMobile = ?"
					   + "     , inputTime = ?"
					   + "     , outputTime = ?"
					   + "     , sms = ?"
					   + "     , penalty = ?"
					   + " WHERE userID = ?;";
			
			tmt = conn.prepareStatement(sql);
			tmt.setString(1, ui.getUserName());
			tmt.setString(2, ui.getUserSchool());
			tmt.setInt(3, ui.getUserGrade());
			tmt.setString(4, ui.getUserMobile());
			tmt.setString(5, ui.getParentMobile());
			tmt.setTimestamp(6, ui.getInputTime());
			tmt.setTimestamp(7, ui.getOutputTime());
			tmt.setString(8, ui.getSms());
			tmt.setInt(9, ui.getPenalty());
			tmt.setInt(10, ui.getUserID());
			
			tmt.executeUpdate();
			
			
			tmt.clearParameters();
			tmt.close();
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		
	}
	
	public static UserInfo getUserInfo(Connection conn, String phone) {
		UserInfo ui = new UserInfo();
		try {
			String sql = "SELECT * "
					   + "  FROM user "
					   + " WHERE userMobile = ?;" ;
			
			PreparedStatement tmt = conn.prepareStatement(sql);
			tmt.setString(1, phone);
			
			ResultSet rs = tmt.executeQuery();
			
			while (rs.next()) {
				ui.setUserID(rs.getInt(1));
				ui.setUserName(rs.getString(2));
				ui.setUserSchool(rs.getString(3));
				ui.setUserGrade(rs.getInt(4));
				ui.setUserMobile(rs.getString(5));
				ui.setParentMobile(rs.getString(6));
				ui.setInputTime(rs.getTimestamp(7));
				ui.setOutputTime(rs.getTimestamp(8));
				ui.setSms(rs.getString(9));
				ui.setPenalty(rs.getInt(10));
			}
			System.out.println(ui);
			
			tmt.clearParameters();
			tmt.close();
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
		return ui;
	}
	
	
	
}
