package studyRoom.sms;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-11-01
 * 내용 : sms전송 기능을 이용하기 위한 객체를 생성하는 클래스
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

public class SmsMain {

	public SmsMain() {}

	public void main(Connection conn) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int addSms=0, classNum=0;
		boolean result=false;
		String seatNum=null;
		
		// 시간표 저장
		SmsExe.timeTable(conn);
		// 문자 서비를 원하는 회원 중 입실을 하지 않은 회원 목록 저장 및 출력
		SmsExe.wantToSendSms(conn);
		SmsExe.wantSendMsg();

		while (true) {
			SmsExe.show();

			// sms 잔여 건수가 10건 미만일 경우 sms 잔여 건수 부족을 알리고, 추가할 것인지 물어보기
			TotalSmsNum.check(conn);
			if (TotalSmsNum.getNumOfSmsRemaining() < 10) {
				System.out.println("SMS 잔여건수가 10건 미만입니다.");

				while (true) {
					System.out.println("-----------------");
					System.out.println("0: 추가 | 1: 종료");
					System.out.println("-----------------");
					System.out.print("작업 선택>> ");
					addSms = Integer.parseInt(br.readLine());

					if (addSms == 0) {
						System.out.print("충전 건수>> ");
						TotalSmsNum.addSmsNum(conn, Integer.parseInt(br.readLine()));
						System.out.println("SMS 잔여건수 충전을 완료하였습니다.");
						break;
					} else if (addSms == 1) {
						System.out.println("SMS 잔여건수 추가 작업을 종료합니다.");
						break;
					} else {
						System.out.println("올바른 값으로 다시 입력해주세요.");
					}
				}
			}
			
			do {
				// 문자를 보내야 하는 회원들에게 일괄적으로 문자 전송
				TotalSmsNum.check(conn);
				// 지금이 몇교시인지
				classNum = SmsExe.classNum(conn);
				System.out.printf("\nSMS 발송 [ 잔여건수: %d ]\n", TotalSmsNum.getNumOfSmsRemaining());
				System.out.print("입실이 확인된 좌석 번호(공백으로 구분하여 입력)>> ");
				seatNum = br.readLine();

				// 문자 전송
				result = SmsExe.sendMsg(conn, seatNum, classNum);

				if (result == false) {
					// 잘못된 좌석 번호 입력(문자 보내는 기능 전체 취소)
					System.out.println("\n<< 전송 실패 >>\nSMS 전송을 전체 취소합니다. 좌석 번호 및 전송 내역 확인 후 다시 전송해주세요.\n");
					continue;
				} else {
					// 문자 전송 성공
					System.out.println("\n<< 전송 성공 >>");
					System.out.println("\nSMS 전송 내역을 출력합니다.");

					SmsExe.sendList(conn);
					return;
				}
			} while (true);
			
		}
	}
}
