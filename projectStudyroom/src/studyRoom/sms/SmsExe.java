package studyRoom.sms;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-19 ~ 2022-10-22, 2022-11-01(수정)
 * 내용 : sms 전송 서비스 관리
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.StringTokenizer;

import db.DbExecute;

public class SmsExe extends TotalSmsNum {
	private static ArrayList<WantToSendSMS> wantArr = new ArrayList<WantToSendSMS>();
	private static ArrayList<SmsHistory> historyArr = new ArrayList<SmsHistory>();
	private static ArrayList<TimeTable> ttArr = new ArrayList<TimeTable>();
	private static ResultSet rs;

	public static ArrayList<WantToSendSMS> getWantArr() {
		return wantArr;
	}

// user테이블에서 sms발송 여부가 'Y'이고, 입실을 하지 않은 회원(user 테이블의 inputTime이 null)을 찾아 ResultSet에 저장
	public static void wantToSendSms(Connection conn) {
		rs=null;
		
		String sql = "SELECT u.userID, seatNum, userName, userMobile, parentMobile, inputTime, sms FROM user u JOIN seat s ON u.userID=s.userID WHERE sms='Y' and inputTime is null;";

		rs = DbExecute.select(conn, rs, sql);
	}

// 입실하지 않은 회원 중 SMS 전송을 희망하는 회원을 arrayList의 형태로 저장
	public static void wantSendMsg() {

		try {
			while (rs.next()) {
				WantToSendSMS ws = new WantToSendSMS();

				ws.setUserID(rs.getInt("userID"));
				ws.setSeatNum(rs.getInt("seatNum"));
				ws.setUserName(rs.getString("userName"));
				ws.setUserMobile(rs.getString("userMobile"));
				ws.setParentMobile(rs.getString("parentMobile"));
				ws.setInputTime(rs.getString("inputTime"));
				ws.setSms(rs.getString("sms"));

				wantArr.add(ws);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

// 입실하지 않은 회원 중 SMS 전송을 희망하는 회원: 출력
	public static void show() {
		System.out.println("\n                           SMS를 전송해야 하는 회원 목록                               ");
		System.out.println(
				"----------------------------------------------------------------------------------------------");
		System.out.println("ID | 좌석 번호 | 회원명 | 회원 핸드폰 번호 | 부모님 핸드폰 번호 | 입실 시간 | SMS 서비스 이용");
		System.out.println(
				"----------------------------------------------------------------------------------------------");

		wantSendMsg();

		for (WantToSendSMS w : wantArr) {
			System.out.println(w.toString());
		}
	}

// 문자를 보내야 하는 회원들에게 일괄적으로 문자 전송
	// 1. 요소 삭제
	public static boolean sendMsg(Connection conn, String seatNum, int classNum) {
		StringTokenizer tok = new StringTokenizer(seatNum);
		int rows = 0, seatTemp = 0, cnt = 0;

		// 입실이 확인 된 좌석(입실 처리하지 않고, 착석한 경우)이 있을 경우에만 실행
		if (!seatNum.equals("")) {
			// 관리자가 좌석 번호를 잘못 적은 경우 rollback: fasle 반환하여 메서드 전체 종료 -> 재입력 받기
			for (String s : seatNum.split(" ")) {
				seatTemp = Integer.parseInt(s);
				for (int i = 0; i < wantArr.size(); i++) {
					if (wantArr.get(i).getSeatNum() == seatTemp) {
						cnt++;
					}
				}
				if (cnt == 0) {
					return false;
				} else {
					cnt = 0;
				}
			}

			// 해당하는 좌석 번호를 arrayList에서 삭제
			while (tok.hasMoreTokens()) {
				int tempNum = Integer.parseInt(tok.nextToken());

				for (int i = 0; i < wantArr.size(); i++) {
					if (wantArr.get(i).getSeatNum() == tempNum) {
						wantArr.remove(i);
						break;
					}
				}
			}
		}

		// sms 테이블에 문자 전송 내역 저장하는 함수 호출
		for (int i = 0; i < wantArr.size(); i++) {
			rows += save(conn, wantArr.get(i).getUserID(), wantArr.get(i).getUserName(), wantArr.get(i).getSeatNum(),
					wantArr.get(i).getUserMobile(), wantArr.get(i).getParentMobile(), classNum);
		}

		wantArr.clear();
		wantSendMsg();

		// sms 잔여 건수 - 전송 건수
		check(conn);
		addSmsNum(conn, (-1) * rows);

		return true;
	}

	// 2. sms테이블에 필요한 값 저장
	// 정상적으로 저장된 값 rows 반환
	// id, 발신번호, 메세지, 전송일시, 전송여부(성공/실패)
	public static int save(Connection conn, int userID, String userName, int seatNum, String userMobile, String parentMobile, int classNum) {
		String msg = userName + "학생 " + classNum + "교시 출석하지 않았습니다. -2팀 독서실-";

		// userID, callingNum, msg, sendMsgTime, sendMsg(성공여부)

		String sql_student = "INSERT INTO sms VALUES(" + userID + ", '" + userMobile + "', '" + msg + "', now(), true);";
		String sql_parent = "INSERT INTO sms VALUES(" + userID + ", '" + parentMobile + "', '" + msg + "', now(), true);";

		String[] sql = { sql_student, sql_parent };

		DbExecute.insert(conn, sql);

		return 2;
	}

	// 3. 관리자가 확인할 수 있게 sms 전송 내역 출력(예전 결과까지 모두 출력)
	// 나타내야 할 항목: id, name, 좌석 번호, 전화번호, 메세지, 전송일시, 전송여부(성공/실패)
	public static void sendList(Connection conn) {
		rs = null;
		String sql = "SELECT u.userID, userName, seatNum, s.callingNum, msg, sendMsgTime, sendMsg FROM user u JOIN sms s ON u.userID=s.userID JOIN seat st ON s.userID=st.userID ORDER BY sendMsgTime DESC";

		TotalSmsNum.check(conn);

		rs = DbExecute.select(conn, rs, sql);

		System.out.println(
				"                                                              SMS 전송내역                                                          ");
		System.out.printf(
				"                                                                                                         SMS 발송 [ 잔여건수: %d ]\n",
				TotalSmsNum.getNumOfSmsRemaining());
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println(
				"ID | 좌석 번호 | 회원명 |  발신 번호  |                          메세지                       |        전송 일시       | 전송 여부  ");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------");

		try {
			while (rs.next()) {
				SmsHistory history = new SmsHistory();

				history.setUserID(rs.getInt("userID"));
				history.setUserName(rs.getString("userName"));
				history.setSeatNum(rs.getInt("seatNum"));
				history.setCallingNum(rs.getString("callingNum"));
				history.setMsg(rs.getString("msg"));
				history.setSendMsgTime(rs.getString("sendMsgTime"));
				history.setSendMsg(rs.getBoolean("sendMsg"));

				historyArr.add(history);

				System.out.println(history.toString());
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		historyArr.clear();
		System.out.println();
	}

// 시간표 저장
	public static void timeTable(Connection conn) {
		rs=null;
		
		String sql = "SELECT * FROM timetable;";

		rs = DbExecute.select(conn, rs, sql);
		
		try {
			while (rs.next()) {
				TimeTable tt=new TimeTable();

				tt.setClassNum(rs.getInt("class"));
				tt.setStart(rs.getString("startTime"));
				tt.setEnd(rs.getString("endTime"));

				ttArr.add(tt);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
// 교시 찾기
	public static int classNum(Connection conn) {
		StringTokenizer tok;
		LocalTime now=LocalTime.now();
		
		int classNum=0, hour=0, minute=0;
		int startH=0, startM=0, startS=0, endH=0, endM=0, endS=0;
		String temp=null;
		
		// 현재 시간, 분
		hour=now.getHour();
		minute=now.getMinute();
		
		// 교시 찾기
		for(int i=0;i<ttArr.size();i++) {
			// 교시 저장(임시)
			classNum=ttArr.get(i).getClassNum();
			
			// 시작 시간 가져오기
			temp=ttArr.get(i).getStart();
			tok=new StringTokenizer(temp,":");
			startH=Integer.parseInt(tok.nextToken());
			startM=Integer.parseInt(tok.nextToken());
			startS=Integer.parseInt(tok.nextToken());
			
			// 끝 시간 가져오기
			temp=ttArr.get(i).getEnd();
			tok=new StringTokenizer(temp,":");
			endH=Integer.parseInt(tok.nextToken());
			endM=Integer.parseInt(tok.nextToken());
			endS=Integer.parseInt(tok.nextToken());
			
			// 교시 확인(일치하면 break: 교시 확정)
			if((hour>startH && hour<endH) || ((hour==startH && minute>=startM) || (hour==endH && minute<=endM))) {
				break;
			}
		}
		System.out.println("\n지금은 "+classNum+"교시입니다.");
		
		ttArr.clear(); // ArrayList 비우기
		
		return classNum;
	}

}


