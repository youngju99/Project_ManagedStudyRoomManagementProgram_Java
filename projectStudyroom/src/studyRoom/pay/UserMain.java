package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 신규회원등록 메인창
 */

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Scanner;

public class UserMain {
	public static void inputUser(Connection conn) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		Timestamp ts = new Timestamp(System.currentTimeMillis()); // timestamp
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		PreparedStatement tmt=null;
		
		UserSeatSql userinput = new UserSeatSql();
		
		int userID = 0;
		
		System.out.println("이름을 입력하세요 >> ");
		String userName = br.readLine();
		System.out.println("학교를 입력하세요 >> ");
		String userSchool = br.readLine();
		System.out.println("학년을 입력하세요 >> ");
		int userGrade = sc.nextInt();
		System.out.println("회원 핸드폰번호를 입력하세요 >> ");
		String userMobile = br.readLine();
		System.out.println("부모님 핸드폰번호를 입력하세요 >> ");
		String parentMobile = br.readLine();
		System.out.println("SMS 사용여부를 입력하세요(Y/N) >> ");
		String sms = br.readLine();
		
		// usertable에 추가
		UserSeatHistory user_input = new UserSeatHistory(userName, userSchool, userGrade, 
				userMobile, parentMobile, sms);
		int insertCount_user = userinput.addUser(conn, tmt, user_input);
		
//		// userID가져오기
//		userID = userinput.selectUserID(conn, tmt, userMobile);
//		UserSeatHistory seat_input = new UserSeatHistory(userID);
//		
//		// seat에 userID 저장
//		int insertCount_seat = userinput.addSeat(conn, tmt, seat_input);

		System.out.println("등록되었습니다.");
	}
}