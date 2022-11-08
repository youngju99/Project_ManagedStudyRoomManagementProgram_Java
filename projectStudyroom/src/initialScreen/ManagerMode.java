// 기본 틀입니다
// 변수들은 코드 병합 및 1차 수정을 담당하시는 규완님께서 필요한 조건에 따라 추가해주시면 감사하겠습니다

// 회원정보 수정부분 추가하였습니다. 2022/11/07

package initialScreen;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-11-01
 * 내용 : manager mode 기본 틀
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import db.MySqlConnect;
import studyRoom.seat.*;
import studyRoom.sms.SmsMain;
import studyRoom.pay.PayMain;
import studyRoom.pay.UserMain;
import studyRoom.user_editor.EditorMain;

public class ManagerMode {

	public static void managerMode()  throws IOException, SQLException, ParseException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		MySqlConnect mySqlConnect=new MySqlConnect();

		Connection conn=null; 
		
		int mode=0;
		String sql=null;
		
		// mysql 연결
		conn=mySqlConnect.connect();
		
		while(true) {
			System.out.println("========================================================================================================================");
			System.out.println(" 0: 좌석 추가 | 1: 결제 | 2: 신규 회원 등록 | 3: 좌석 이용 종료 | 4: 상벌점 관리 | 5: SMS 전송 | 6: 회원정보 변경 | 7: 작업 종료");
			System.out.println("========================================================================================================================");
			System.out.print("실행할 작업>> ");
			mode=Integer.parseInt(br.readLine());
			
			if(mode==0) {
				System.out.print("추가할 좌석의 수>> ");
				sql = "INSERT INTO seat VALUES();";
				AddSeats.addSeat(conn, sql, Integer.parseInt(br.readLine()));
				
			} else if(mode==1) {
				PayMain.pay(conn);
			} else if(mode==2) {
				UserMain.inputUser(conn);
			} else if(mode==3) {
				// 좌석 이용 종료
				ClearSeat.showClearSeatMenu(conn);
			} else if(mode==4) {
				// 벌점 관리
				Punishment.publish(conn);
			} else if(mode==5) {
				SmsMain.main(conn);
			} else if(mode==6) {
				EditorMain.editorUser(conn);
			} else if(mode==7) {
				System.out.println("시스템을 종료합니다.\n");
				mySqlConnect.disconnect(conn);
				break;
			} else {
				
				System.out.println("올바른 값으로 다시 입력해주세요.\n");
			}
		}
		br.close();
	}
}
