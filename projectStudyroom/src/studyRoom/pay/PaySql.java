package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 결제 sql
 * 
 * @re : 
 * @date : 2022/11/07
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DbExecute;

public class PaySql {
	public int addPay(Connection conn, PreparedStatement tmt, PayHistory addPay) {
		int insertCount = 0;
		String sql = "INSERT INTO pay(userID, payment, payTime)"
				+ "VALUES('" + addPay.getUserID() + "', '" + addPay.getPayment() + "', '" + addPay.getPayTime() + "')";
		String[] insert = {sql};
		DbExecute.insert(conn, insert);

		return insertCount;
	}
	
	public static void blacklist(Connection conn, PreparedStatement tmt, PayHistory black) {
		
	}
	
}
