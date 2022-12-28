package studyRoom.pay;

/*
 * @date : 2022/11/04
 * @memo : 신규회원등록 메인창
 * 
 * @re : 
 * @date : 2022/11/07
 * 
 * @re : 경고 제거
 * @date : 2022/11/08
 */

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserMain {
	public static void inputUser(Connection conn) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		PreparedStatement tmt=null;
		
		UserPaySql userinput = new UserPaySql();
		
		
		System.out.println("이름을 입력하세요 >> ");
		String userName = br.readLine();
		System.out.println("학교를 입력하세요 >> ");
		String userSchool = br.readLine();
		System.out.println("학년을 입력하세요 >> ");
		int userGrade = Integer.parseInt(br.readLine());
		System.out.println("회원 핸드폰번호를 입력하세요 >> ");
		String userMobile = br.readLine();
		System.out.println("부모님 핸드폰번호를 입력하세요 >> ");
		String parentMobile = br.readLine();
		System.out.println("SMS 사용여부를 입력하세요(Y/N) >> ");
		String sms = br.readLine();
		
		// usertable에 추가
		UserSeatHistory user_input = new UserSeatHistory(userName, userSchool, userGrade, 
				userMobile, parentMobile, sms);
		userinput.addUser(conn, tmt, user_input);
		
		System.out.println("등록되었습니다.");
	}
}
