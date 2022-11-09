package studyRoom.student;

/*
 * 작성자 : 이다영
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import db.DbExecute;
import db.MySqlConnect;

public class Student {
	
	// 글자 색
	public static final String RESET = "\u001B[0m";
	public static final String RED = "\u001B[91m";
	public static final String GREEN = "\u001B[92m";
	public static final String GRAY = "\u001B[90m";
	
	// MySQL 연결
	static MySqlConnect mysql = new MySqlConnect();
	static Connection conn = null;
	
	// 1 -> 01로 출력하기 위해
	static DecimalFormat f = new DecimalFormat("00");
	
	// 사용자 메뉴
	public static void showUserMenu() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean run = true;
		
		conn = mysql.connect();
		
		clearTime();

		while (run) {
			showSeat(); // 좌석 보여주기
			int selectSeat = 0; // 선택한 좌석 번호
			boolean check;		// 확인 변수
			String s = "";

			// 좌석 선택 - 올바른 번호와 배정된 좌석을 입력할 때까지
			do {
				System.out.print("\t\t >> 좌석 번호(종료 : X) : ");
				try {	// 정수를 입력했을 경우
					s = br.readLine();
					selectSeat = Integer.parseInt(s);

					check = checkSeatNum(selectSeat); // 유효한 번호인지 확인
					if (!check) {
						continue;
					}
					check = checkSeatStatus(selectSeat); // 빈 좌석인지 배정된 좌석인지 확인

				} catch (NumberFormatException e) {		// 정수를 입력하지 않았을 경우
					if (s.equalsIgnoreCase("X")) {		// 종료 선택
						System.out.println("\t\t >> 사용자 메뉴를 종료합니다.");
						return;
					} else {				// 문자(열)를 입력했을 경우
						System.out.println("\t\t >> 잘못입력하셨습니다. 숫자를 입력해주세요.\n");
						check = false;
					}
				}
			} while (!check);

			System.out.println();
			String name = ""; // 입력 받은 이름을 담을 변수
			String phone = ""; // 입력 받은 전화번호를 담을 변수

			// 학생 로그인 - 배정된 좌석의 학생 정보와 일치할 때까지
			do {
				// 학생 정보 입력 받기
				System.out.print("\t\t >> 이름 : ");
				name = br.readLine();
				System.out.print("\t\t >> 핸드폰 : ");
				phone = br.readLine();

				check = checkInfo(name, phone); // 좌석의 사용자 정보와 일치하는지 확인

			} while (!check);

			// 입실 / 퇴실
			Student.inputOutput(name, phone);
			break;
		}

		mysql.disconnect(conn);;
		return;
	}
	
	// 입실 / 퇴실 시간 초기화
	public static void clearTime() {
		
		// SQL문
		String[] sql = {"UPDATE user SET inputTime = NULL, outputTime = NULL WHERE DAY(NOW()) != DAY(inputTime);"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		if(row != 0) {
			System.out.println("\t\t >> 입실 / 퇴실 시간이 초기화 되었습니다.\n");
		}
		
	}
	

	// 좌석 보여주기
	public static void showSeat() {
			
		ArrayList<Seat> seatList = new ArrayList<>();			// 좌석 정보를 ArrayList로 저장
		
		// SQL문
		String sql = "SELECT S.seatNum, U.userName, U.inputTime, U.outputTime FROM seat S"
				+ " LEFT JOIN user U ON S.userID = U.userID	ORDER BY S.seatNum;";
		ResultSet rs = null;
		
		// select 메서드 실행 
		rs = DbExecute.select(conn, rs, sql);
		try {
			while(rs.next()) {
				int seatNum = rs.getInt("S.seatNum");
				String user = rs.getString("U.userName");
				Timestamp inputTime = rs.getTimestamp("U.inputTime");
				Timestamp outputTime = rs.getTimestamp("U.outputTime");
				
				// 가져온 데이터 저장하기
				Seat seat = new Seat(seatNum, user, inputTime, outputTime);
				seatList.add(seat);
				
			}			
		} catch(SQLException e) {
		} catch(Exception e) {
		}
		
		System.out.println();
		
		int count = 0;
		for(int i=0; i< seatList.size(); i++) {
			
			int seatNum = seatList.get(i).getSeatNum();
			
			if(seatNum % 5 == 1) {
				System.out.print("\t");
			}
			
			System.out.printf("%14s\t\t", f.format(seatNum));				// 좌석 번호 출력
			count++;
			if(seatNum % 5 == 0 || seatNum == seatList.size()) {											// 5개마다 줄 바꿈
				System.out.println("\n");
				System.out.print("\t");
				for(int j=i-(count-1); j<=i; j++) {
					
					String user = seatList.get(j).getUser();
					Timestamp inputTime = seatList.get(j).getInputTime();
					Timestamp outputTime = seatList.get(j).getOutputTime();
					
					if(user != null) {						// 이름이 null이 아니면 배정된 좌석
						if(inputTime != null) {				// 입실한 상태인 경우
							if(outputTime != null) {		// 입실 후 퇴실까지 한 경우
								System.out.printf(RED + "%13s\t" + RESET, user);								
							}
							else {							// 입실만 한 경우
								System.out.printf(GREEN + "%13s\t" + RESET, user);
							}
						}
						else {								// 입실하기 전인 경우
							System.out.printf(GRAY + "%13s\t" + RESET, user);
						}
					}
					else {					// 빈 좌석
						String name = "";
						System.out.printf("%13s\t\t", name);	// 변수 user를 사용하면 'null'로 출력되기 때문
					}
				}
				count = 0;
				System.out.println("\n\n");
			}
		}
	}
	
	// 유효한 번호확인하기
	public static boolean checkSeatNum(int selectSeat) {
		
		// SQL문 - COUNT()를 이용하여 좌석 수 뽑아오기
		String sql = "SELECT COUNT(seatNum) FROM seat;";		
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		int seatCount = 0;		// COUNT(seatNum) 값을 담을 변수
		
		try {
			if(rs.next()) {
				seatCount = rs.getInt("COUNT(seatNum)");		// 좌석 수를 변수에 저장
			}
			
			// 입력한 번호가 범위안에 있는지 확인
			if(1 <= selectSeat && selectSeat <= seatCount) {
				return true;
			}
			// 범위를 벗어난 경우
			System.out.println("\t\t >> 잘못 입력하셨습다. 다시 입력해 주세요.\n");
			return false;
			
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	// 배정된 좌석인지 아닌지 확인하기
	public static boolean checkSeatStatus(int selectSeat) {
		
		// SQL문
		String sql = "SELECT userID FROM seat WHERE seatNum = " + selectSeat + ";";
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		int userID = 0;			// userID 값을 담을 변수
		
		try {
			if(rs.next()) {
				userID = rs.getInt("userID");
			}
			
			if(userID == 0) {		// 빈 좌석
				System.out.println("\t\t >> 빈 좌석입니다. 다시 입력해 주세요.\n");
				return false;
			}
			else {					// 배정된 좌석
				return true;
			}		
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	// 배정된 학생 정보 확인하기
	public static boolean checkInfo(String name, String phone) {
		
		// SQL문
		String sql = "SELECT COUNT(U.userID) FROM user U INNER JOIN seat S ON S.userID = U.userID "
				+ "WHERE U.userName = '" + name + "' AND U.userMobile = '" + phone + "';";
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		int data = 0;
		
		try {
			if(rs.next()) {
				data = rs.getInt("COUNT(U.userID)");
			}
			
			
			if(data == 1) {		// 일치하는 정보가 1개이면 true
				return true;
			}
			else {					// 그렇지않으면 false
				System.out.println("\n\t\t 정보가 일치하지 않습니다.\n");
				return false;
			}
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	// 입실/ 퇴실
	public static void inputOutput(String name, String phone) {
		
		// SQL문
		String sql = "SELECT inputTime, outputTime FROM user WHERE userName = '" + name + "' AND userMobile = '" + phone + "';";
		ResultSet rs = null;
		
		// select 메서드 호출
		rs = DbExecute.select(conn, rs, sql);
		
		Timestamp inputTime = new Timestamp(0);			// inputTime 값 답을 변수
		Timestamp outputTime = new Timestamp(0);		// outputTime 값 담을 변수
		
		try {
			if(rs.next()) {
				inputTime  = rs.getTimestamp("inputTime");
				outputTime  = rs.getTimestamp("outputTime");
			}

			if(inputTime == null) {			// 입실하기 전
				// 입실하기
				input(name, phone);
			}
			else {							// 입실 후 퇴실하기 전
				if(outputTime == null) {
					// 퇴실하기
					output(name, phone);	
				}
				else {
					// 퇴실 후 다시 입실하기
					input(name, phone);	
					setOutputTime(name, phone);
				}
			}
			
		} catch(SQLException e) {
		} catch(Exception e) {
		}
		
		
	}
	// Timestamp는 시간을 초로 나타내기 때문에 년-월-일 시:분:초 형식으로 출력하기 위해
	public static SimpleDateFormat datef = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	
	// 입실하기
	public static void input(String name, String phone) {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());			// 현재시간
		// SQL문
		String[] sql = {"UPDATE user SET inputTime = '" + datef.format(now) + "' WHERE userName = '" + name + "' AND userMobile = '" + phone + "';"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		if(row == 1) {
			System.out.printf("\n\t\t >> %s님 입실하였습니다.\n", name);
		}
		
	}
	// 퇴실하기
	public static void output(String name, String phone) {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());			// 현재시간
		// SQL문
		String[] sql = {"UPDATE user SET outputTime = '" + datef.format(now) + "' WHERE userName = '" + name +"' AND userMobile = '" + phone + "';"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		if(row == 1) {
			System.out.printf("\n\t\t >> %s님 퇴실하였습니다.\n", name);
		}
	}
	
	// 퇴실 시간 초기화
	public static void setOutputTime(String name, String phone) {

		// SQL문							퇴실 후 다시 입실하기 때문에 퇴실시간을 null로 초기화
		String[] sql = {"UPDATE user SET outputTime = " + null + " WHERE userName = '" + name + "' AND userMobile = '" + phone + "';"};
		
		// 반환값이 있는 update 메서드 호출
		int row = DbExecute.updateReturnRow(conn, sql);
		if(row == 1) {
			System.out.println("\t\t >> 퇴실시간이 초기화 되었습니다.\n");
		}	
	}
}
