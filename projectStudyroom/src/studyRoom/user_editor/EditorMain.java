package studyRoom.user_editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import db.DbExecute;

public class EditorMain {

	public static void editorUser(Connection conn) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PreparedStatement tmt=null;
		UserInfo ui = new UserInfo();
		String userMobile = "";
		int userID = 0;
		
		System.out.println();
		System.out.println("사용자 정보 변경 화면입니다.");
		System.out.println("변경할 사용자의 이름을 입력하세요.");
		ui.setUserName(br.readLine());
		System.out.println("변경할 사용자의 학교를 입력하세요");
		ui.setUserSchool(br.readLine());
		System.out.println("변경할 사용자의 학년을 입력하세요");
		ui.setUserGrade(Integer.parseInt(br.readLine()));
		System.out.println("변경할 사용자의 전화번호를 입력하세요");
		userMobile = br.readLine();
		ui.setUserMobile(userMobile);
		System.out.println("변경할 사용자의 부모님 연락처를 입력하세요");
		ui.setParentMobile(br.readLine());
		System.out.println("변경할 사용자의 sms 수신여부 Y / N 을 입력하세요");
		ui.setSms(br.readLine());
		
		System.out.println("변경할 사용자의 등록날짜를 입력하세요");
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	       // you can change format of date
        Date input_dt = formatter.parse(br.readLine());
	    Timestamp input_ts = new Timestamp(input_dt.getTime());
		ui.setInputTime(input_ts);
		
		System.out.println("변경할 사용자의 만료일 입력하세요");
		Date output_dt = formatter.parse(br.readLine());
	    Timestamp output_ts = new Timestamp(output_dt.getTime());
		ui.setOutputTime(output_ts);
		

		userID = selectUserID(conn, tmt, userMobile);
		int updateCount_user = updateUser(conn, tmt, ui, userMobile);
		int updateCount_seat = updateSeat(conn, tmt, ui, userID);
	}
	
	// user 정보 업데이트
	public static int updateUser(Connection conn, PreparedStatement tmt, UserInfo userinfo, String userMobile) {
		int updateCount = 0;
		String sql = "UPDATE user SET username = '" + userinfo.getUserName() + "', userSchool = '" + userinfo.getUserSchool()
			+ "', userGrade = '" + userinfo.getUserGrade() + "', userMobile = '" + userinfo.getUserMobile() 
			+ "', parentMobile = '" + userinfo.getParentMobile() + "', sms = '" + userinfo.getSms() + "'"
			+ "WHERE userMobile = '" + userMobile + "';";
		String[] update = {sql};
		DbExecute.update(conn, update);
		
		return updateCount;
	}
	
	// seat 정보 업데이트
	public static int updateSeat(Connection conn, PreparedStatement tmt, UserInfo userinfo, int userID) {
		int updateCount = 0;
		String sql = "UPDATE seat SET startDate = '" + userinfo.getInputTime() + "', endDate = '" + userinfo.getOutputTime()
				+ "' WHERE userID = '" + userID +"';";
		String[] update = {sql};
		DbExecute.update(conn, update);

		return updateCount;
	}
	
	// 핸드폰번호로 userID검색하기
	public static int selectUserID(Connection conn, PreparedStatement tmt, String userMobile) {
		String sql = "SELECT userID"
				+ " FROM user WHERE userMobile = '" + userMobile + "';";
		String[] select = {sql};
		int userID = 0;
		ResultSet rs = null;
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			
			if (rs.next()) {
				userID = rs.getInt(1);
			}	
			
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return userID;
	}
}
