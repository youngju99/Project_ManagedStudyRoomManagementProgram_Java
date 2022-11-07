package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/05
 * @memo : 결제 메인창
 * 
 * @re : 
 * @date : 2022/11/07
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class PayMain {
	
	public static void pay(Connection conn) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		Timestamp ts = new Timestamp(System.currentTimeMillis()); // timestamp
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		PreparedStatement tmt=null;
		
		String name = ""; 	// 결제할 회원의 이름(userName)
		Timestamp startDate;// 시작날짜(결제일)
		int userID = 0;		// 결제할 회원의 ID(userID)
		int date = 0;		// 등록일 수
		int money = 0;		// 결제금액
		UserSeatSql select = new UserSeatSql();
		UserSeatSql userseat = new UserSeatSql();
		PaySql addPay = new PaySql();
		
		
		System.out.println("결제할 회원의 이름을 입력하세요. >> ");
		name = br.readLine();
		System.out.println("========================= 동일 이름의 회원 목록 =========================");
		
		// 회원목록 출력
		List<UserSeatHistory> list = select.selectUser(conn, tmt, name);
		for (UserSeatHistory role_select : list) {
			System.out.println(role_select);
		}
		
		System.out.println();
		System.out.println("결제할 회원의 아이디를 입력하세요 >> ");
		userID = sc.nextInt();
		
		// blacklist인지 판단
		if (userseat.blacklist(conn, tmt, userID)) {
			// 등록일 수 입력
			startDate = ts;
			System.out.println("등록일수를 입력하세요(일단위) >>  ");
			date = sc.nextInt();
			cal.setTime(ts2);
			cal.add(Calendar.DATE, date);
			ts2.setTime(cal.getTime().getTime());
			Timestamp endDate = ts2;
			
			
			// 결제하기
			// 1일 15000원으로 계산해 결제날자랑해서  pay db에 저장
			money = date * 15000;
			PayHistory pay_add = new PayHistory(userID, money, startDate);
			int insertPay = addPay.addPay(conn, tmt, pay_add);
			
			
			UserSeatHistory addSeatDate = new UserSeatHistory(userID, startDate, endDate);
			// 좌석이 있는지 확인(기존 등록되어있던 회원인지 확인)
			if (userseat.selectSeat(conn, tmt, userID)) {
				int updateSeat = userseat.updateSeat(conn, tmt, addSeatDate, userID);
			} else {
				int insertSeat = userseat.addSeat(conn, tmt, addSeatDate);
			}
			
			
			System.out.println("등록이되었습니다.");
		}
	}
}

