package studyRoom.seat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import db.DbExecute;
/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-17, 2022-11-01(수정)
 * 내용 : 새로운 좌석을 입력한 수 만큼 추가
 */

public class AddSeats {
	
	public static void addSeat(Connection conn) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		String sql = null;
		int n=0;
		
		System.out.print("추가할 좌석의 수>> ");
		n=Integer.parseInt(br.readLine());
		
		sql="INSERT INTO seat VALUES();";
		String[] sqlArr= {sql};
		
		for(int i=0;i<n;i++) {
			DbExecute.insert(conn, sqlArr);
		}
		
		System.out.println("[ 좌석을 성공적으로 추가했습니다 ]\n");
	}
}
