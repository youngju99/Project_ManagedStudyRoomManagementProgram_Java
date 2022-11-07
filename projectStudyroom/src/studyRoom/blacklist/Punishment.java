package studyRoom.blacklist;
/*
 * 작성자 : 정우성
 * 작성일 : 2022-10-26
 * 수정일 : 2022-10-26
 * 내용 : 벌점 추가
 * 수정 : 이다영
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.StringTokenizer;

import db.DbExecute;
import db.MySqlConnect;

public class Punishment {

	static MySqlConnect mysql = new MySqlConnect();
	static PunishInfo pi = new PunishInfo();
	
	static final int PENALTY = -30;
	
	public static void publish(Connection conn) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int select = 0, penalty = 0;		// 항목 선택, 벌점
		String name = "", phone = "";		// 입력 받을 사용자 정보
		
		while (true) {
			System.out.println("\n\t >> 벌점 부과 항목");
			System.out.println("\t 0: 결석 | 1: 지각 | 2: 휴대폰 미제출 | 3: 전자기기 사용 | 4: 조퇴");
			System.out.print("\t >> ");
			try {
				select = Integer.parseInt(br.readLine());
				if(select < 0 || select > 4) {
					System.out.println("\n\t >> 잘못 입력하셨습니다. 다시 입력해주세요.");
					continue;
				}
				break;
			} catch(NumberFormatException e) {
				System.out.println("\n\t >> 잘못 입력하셨습니다. 숫자를 입력해주세요.");
			}
		}
		
		switch(select) {
			case 0 :	// 결석
				penalty = 10;
				break;
			case 1 :	// 지각
				penalty = 7;
				break;
			case 2 :	// 휴대폰 미제출
				penalty = 6;
				break;
			case 3 :	// 전자기기 사용
				penalty = 4;
				break;
			default :	// 조퇴
				penalty = 2;		
		}
		
		// 사용자 정보 입력
		while(true) {
			System.out.println("\n\t >> 사용자 정보를 입력하세요.");
			System.out.print("\t >> 이름 : ");
			name = br.readLine();
			System.out.print("\t >> 핸드폰 : ");
			phone = br.readLine();
			
			if(checkUserInfo(conn, name, phone)) {
				break;
			}	
		}
		// 벌점 부여하기
		updatePoint(conn, pi, penalty, name);
		return;	
		
	}
	
	// 사용자 정보 확인
	public static boolean checkUserInfo(Connection conn, String name, String phone) {
		
		// SQL문
		String sql = "SELECT userID, penalty FROM user WHERE userName = '" + name + "' AND userMobile = '" + phone + "';";
		ResultSet rs = null;

		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);

		try {
			if (rs.next()) {
				pi.setUserID(rs.getInt("userID"));
				pi.setPoint(rs.getInt("penalty"));
				return true;
			} else {
				System.out.println("\n\t >> 일치하는 사용자 정보가 없습니다.\n");
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("\n\t>> 에러가 발생하였습니다.\n");
		}
		return false;
		
	}
	
	public static void updatePoint(Connection conn, PunishInfo pi, int penalty, String name) {
		
		int point = pi.getPoint() - penalty;	// 벌점 계산
		
		// SQL문
		String[] sql = {"UPDATE user SET penalty = " + point + " WHERE userID = " + pi.getUserID() + ";"};
		
		// 반환값이 있는 update 메서드 호출
		int row =  DbExecute.updateReturnRow(conn, sql);
		
		if(row == 1) {
			System.out.println("\n\t >> 벌점 "+ penalty + "점이 정상적으로 부과되었습니다.\n");
		}
		
		if(point < PENALTY) {
			// 블랙리스트에 추가
			addBlackList(conn, pi);
			
			System.out.println("\n\t >> '" + name + "'님 현재 벌점 '" + Math.abs(point) + "'점으로 벌점 " + Math.abs(PENALTY) + "점을 초과하여 블랙리스트에 추가되었습니다.");
			System.out.println("\t >> 환불을 진행하세요.\n");
		}
		
	}
	
	// 블랙리스트에 추가
	public static void addBlackList(Connection conn, PunishInfo pi) {
		
		// SQL문
		String[] sql = {"INSERT INTO blacklist VALUES(NULL, " + pi.getUserID() + ");"};
		
		// insert 메서드 실행
		DbExecute.insert(conn, sql);	
	}
}
