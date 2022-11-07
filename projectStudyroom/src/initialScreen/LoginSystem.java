package initialScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbExecute;
import db.MySqlConnect;

class LoginSystem extends Thread{		// 접근 제한과 동시 동작을 위한 Thread 상속
	public void run() {
		try {
			resetPW();
		} catch (IOException e) {
			
		}
	}
	
	// MySQL 연결
	static MySqlConnect mysql = new MySqlConnect();
	static Connection conn = null;
	
	// 로그인
	static boolean login() throws IOException {
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		String id = "", pw = "";		// 입력받을 id, pw
		int loginCont = 0;				// 로그인 횟수
		
		while(++loginCont <= 3) {
			// id, pw 입력받기
			System.out.println("\n\t| [ 로그인 ]");
			System.out.print("\t| ID>> ");
			id = br.readLine();
			System.out.print("\t| PW>> ");
			pw = br.readLine();
			
			conn = mysql.connect();
			
			// SQL문
			String sql = "SELECT * FROM manager WHERE id = '" + id + "' AND pw = '" + pw + "';";
			ResultSet rs = null;
			
			// select 메서드 호출
			rs = DbExecute.select(conn, rs, sql);
			try {
				if(rs.next()) {
					return true;
				}
				else {
					System.out.printf("\t------------아이디 또는 비밀번호가 틀렸습니다. 로그인 %d회 시도 ---------------\n",loginCont);
				}
				
			} catch(SQLException e) {
				System.out.println("\n\t>> SQL에러가 발생하였습니다.\n");
				return false;
			} catch(Exception e) {
				System.out.println("\n\t>> 에러가 발생하였습니다.\n");
				return false;
			}
			
		}
		mysql.disconnect(conn);;
		System.out.println("\n\t로그인을 3회 시도하였습니다. 강제 종료합니다.\n");
		return false;
		
	}
	
	// 비밀번로 재설정
	static void resetPW() throws IOException {
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		String select = "";								// 비밀번호 재설정 여부
		String name = "", phone = "", code ="";			// 입력받을 사용자 정보
		String pw = "", pwCheck = "";					// 새 비밀번호, 비밀번호 확인
		char[] special = {'?', '!', '~', '@', '#', '$', '%', '^', '&', '*'};			// 특수문자 배열
		boolean check = false, upperCheck = false, lowerCheck = false, numCheck = false, specialCheck = false;		// 특수문자 확인 변수
		
		conn = mysql.connect();
		while(true) {
			System.out.println("\n\t >> 비밀번호 재설정하시겠습니까? ( Y / N )");
			System.out.print("\t >> ");
			select = br.readLine();
			if(select.equalsIgnoreCase("Y")) {
				break;
			}
			else if(select.equalsIgnoreCase("N")) {
				System.out.println("\n\t 접근 제한 중 ...\n");
				return;
			}
			else {
				System.out.println("\n\t >> 잘못 입력하셨습니다. 다시 입력해주세요.\n");
			}
			
			
		}
		do {
			// 사용자 정보 입력 받기
			System.out.print("\t >> 이름 : ");
			name = br.readLine();
			System.out.print("\t >> 핸드폰 : ");
			phone = br.readLine();
			System.out.print("\t >> 관리자 인증번호 : ");
			code = br.readLine();
			
			// 사용자 정보 확인
			check = infoCheck(name, phone, code);
		} while (!check);

		do {
			// 새 비밀번호 입력 받기
			System.out.println("\n\t >> 비밀번호 재설정 : 8자리 이상(영어 대소문자, 숫자, 특수문자 포함)");
			System.out.println("\t >> 영어 대소문자, 숫자, 특수문자(?, !, ~, @, #, $, %, ^, &, *) 포함\n");
			System.out.print("\t >> 새 비밀번호 : ");
			pw = br.readLine();
			System.out.print("\t >> 비밀번호 확인 : ");
			pwCheck = br.readLine();
			
			if (pw.equals(pwCheck)) {	// 새 비밀번호와 비밀번호가 일치
				// 8자리 이상이 아니면 다시 입력
				if (pw.length() < 8) {
					System.out.println("\n\t >> 비밀번호는 8자리 이상이여야 합니다. 다시 입력하세요.");
					continue;
				}
				// 입력받은 비밀번호를 문자 배열로 저장 -> 문자 하나, 하나를 비교하기 위해
				char[] pwArray = pw.toCharArray();
				
				// for-each 배열 요소 접근
				for (char p : pwArray) {
					if (Character.isUpperCase(p)) {			// 대문자
						upperCheck = true;
						continue;
					}
					if (Character.isLowerCase(p)) {			// 소문자
						lowerCheck = true;
						continue;
					}
					if (Character.isDigit(p)) {				// 숫자
						numCheck = true;
						continue;
					}
					for (char c : special) {				// 특수문자
						if (p == c) {
							specialCheck = true;
							break;
						}
					}

				}
				if (!upperCheck || !lowerCheck || !numCheck || !specialCheck) {
					System.out.println("\n\t >> 영어 대소문자, 숫자, 특수문자를 반드시 하나씩 포함해야합니다. 다시 입력하세요.");
				}
			}
			else {	// 새 비밀번호와 비밀번호가 불일치
				System.out.println("\n\t >> 비밀번호가 일치하지 않습니다. 다시 입력하세요.");
				continue;
			}
		} while (!upperCheck || !lowerCheck || !numCheck || !specialCheck);
			
		// DB에 UPDATE
		updatePW(pw, name);
		mysql.disconnect(conn);
		System.out.println("\n\t 접근 제한 중 ...\n");
		return;
	}
	
	// 정보 확인
	static boolean infoCheck(String name, String phone, String code) {
		
		// SQL문		
		String sql = "SELECT * FROM manager WHERE managerName = '" + name + "' AND mobile = '" + phone + "' AND managerCode = '" + code + "';";
		ResultSet rs = null;
		
		// select메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		try {
			if(rs.next()) {
				return true;
			}
			else {
				System.out.println("\n\t >> 관리자 정보와 일치하지 않습니다.\n");
				return false;
			}
			
		} catch(SQLException e) {
			System.out.println("\n\t >> SQL에러가 발생하였습니다.\n");
			return false;
		} catch(Exception e) {
			System.out.println("\n\t >> 에러가 발생하였습니다.\n");
			return false;
		}
		
	}
	
	// DB에 UPDATE
	static void updatePW(String pw, String name) {
		
		// SQL문
		String[] sql = {"UPDATE manager SET pw = '" + pw + "' WHERE managerName = '" + name + "';"};
		
		// 반환값이 있는  update메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		if(row == 1) {
			System.out.println("\n\t >> 비밀번호가 재설정되었습니다.\n");
		}
		
	}

}
