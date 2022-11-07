package studyRoom.seat;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-17, 2022-11-01(수정)
 * 내용 : 새로운 좌석을 입력한 수 만큼 추가
 */

import java.sql.Connection;
import db.DbExecute;

public class AddSeats {
	
	public static void addSeat(Connection conn, String sql, int n) {
		String[] sqlArr= {sql};
		
		DbExecute.insert(conn, sqlArr);
		
		System.out.println("[ 좌석을 성공적으로 추가했습니다 ]\n");
	}
}