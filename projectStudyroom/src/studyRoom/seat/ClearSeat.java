package studyRoom.seat;

/*
 * 작성자 : 이다영
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import db.DbExecute;

public class ClearSeat {
	
	static Connection conn = null;
	
	// 메뉴(일괄 종료 / 환불)
	public static void showClearSeatMenu(Connection c) throws IOException{
		
		conn = c;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int select = 0;				// 메뉴 선택
		String end = "";			// 일괄 종료 선택
		boolean run = true, check = false;
		
		while(run) {
			System.out.println("\t 0: 일괄 종료 | 1: 환불");
			
			do {
				System.out.print("\t >> ");
				
				try {	// 정수를 입력했을 경우
					select = Integer.parseInt(br.readLine());
					if(select < 0 || select > 1) {
						System.out.println("\t >> 잘못 입력하셨습니다. 다시 입력해주세요.\n");
						check = false;
					}
					else {
						check = true;
					}
				} catch(NumberFormatException e) {	// 문자(열)을 입력했을 경우
					System.out.println("\t >> 잘못 입력하셨습니다. 다시 입력해주세요.\n");
				}
			} while(!check);

			if(select == 0) {
				// 일괄종료
				do {
					System.out.println("\t >> 일괄 종료하시겠습니까? ( Y / N )");
					System.out.print("\t >> ");
					end = br.readLine();
					if(end.equalsIgnoreCase("Y") || end.equalsIgnoreCase("N")) {
						check = true;
					}
					else {
						System.out.println("\t >> 잘못 입력하셨습니다. 다시 입력해주세요.\n");
						check = false;
					}
					
				} while(!check);
				
				if(end.equalsIgnoreCase("Y")) {
					// 일괄종료 하기
					allClearSeat();
					break;
				}
				else {		// "N"일 경우
					continue;
				}
			}
			else {
				// 환불
				String name ="";			// 이름
				String phone = "";			// 핸드폰
				
				
				do {
					// 사용자 정보 입력
					System.out.print("\t >> 이름 : ");
					name = br.readLine();
					System.out.print("\t >> 핸드폰 : ");
					phone = br.readLine();
					
					check = checkUserInfo(name, phone); 		// 사용자 정보 확인
					
				} while(!check);
				refund(name, phone);			// 환불하기
				break;
			}
		}		
	}
	
	// 일괄 종료하기
	public static void allClearSeat() {
		
		// SQL문
		String[] sql = {"UPDATE seat SET userID = NULL, startDate= NULL, endDate = NULL, assignmentStatus = 0 WHERE endDate < DATE(NOW());"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		
		if(row == 0) {
			System.out.println("\n\t >> 이용 종료할 좌석이 없습니다.\n");
		}
		else {
			System.out.println("\n\t >> 좌석 이용이 일괄 종료되었습니다.\n");
		}
		
	}
	
	// 사용자 정보 확인
	public static boolean checkUserInfo(String name, String phone) {
		
		// SQL문
		String sql = "SELECT userID FROM user WHERE userName = '" + name + "' AND userMobile = '" + phone + "';";
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		try {
			if(rs.next()) {
				return true;
			}
			else {
				System.out.println("\n\t >> 일치하는 사용자 정보가 없습니다.\n");
				return false;
			}
			
		} catch(SQLException e) {
			System.out.println("\n\t>> SQL에러가 발생하였습니다.\n");
		} catch(Exception e) {
			System.out.println("\n\t>> 에러가 발생하였습니다.\n");
		}
		return false;
	}
	
	// 환불
	public static void refund(String name, String phone) {
		
		
		String sql = "SELECT U.userID, DATEDIFF(S.endDate, DATE(NOW())) FROM user U "
				+ "INNER JOIN pay P ON U.userID = P.userID "
				+ "INNER JOIN seat S ON U.userID = S.userID AND S.endDate > DATE(NOW()) "
				+ "WHERE U.username = '" + name + "' AND U.userMobile = '" + phone + "';";
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		int userID = 0, date = 0;		// 사용자 id, 남은 이용기간
		
		try {
			if(rs.next()) {
				userID = rs.getInt("U.userID");
				date = rs.getInt("DATEDIFF(S.endDate, DATE(NOW()))");
				doRefund(userID, date);
			}
			else {
				System.out.println("\n\t >> 횐불할 금액이 없습니다.\n");
			}
			
		} catch(SQLException e) {
			System.out.println("\n\t>> SQL에러가 발생하였습니다.\n");
		} catch(Exception e) {
			System.out.println("\n\t>> 에러가 발생하였습니다.\n");
		}
	}
	
	// 금액 표시
	static DecimalFormat df = new DecimalFormat("#,###");
	
	// 환불 진행
	public static void doRefund(int userID, int date) {
		
		// SQL문
		String[] sql = {"UPDATE pay SET refundCheck = 1, refund = 15000 * " + date + " WHERE userID = " + userID + ";"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		
		int refund = 15000 * date;
		
		if(row == 1) {
			System.out.printf("\n\t >> %s원이 환불되었습니다.\n\n", df.format(refund));
			clearSeat(userID);		// 사용자의 좌석 이용 종료
		}
	}
	
	// 좌석 초기화
	public static void clearSeat(int userID) {
		
		// SQL문
		String[] sql = {"UPDATE seat SET userID = NULL, startDate = NULL, endDate = NULL, assignmentStatus = 0 WHERE userID = " + userID + ""};
		
		// 반환값이 있는 update 메서드 호출
		int row =  DbExecute.updateReturnRow(conn, sql);
		
		if(row == 1) {
			System.out.println("\n\t >> 좌석이용이 종료되었습니다.");
		}
		
	}

}