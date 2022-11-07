package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 결제 sql
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DbExecute;

public class PaySql {
	public int addPay(Connection conn, PreparedStatement tmt, PayHistory addPay) {
		int insertCount = 0;
		String sql = "INSERT INTO pay(userID, payment, payTime)"
				+ "VALUES(?, ?, ?)";
		String[] insert = {sql};
		DbExecute.insert(conn, insert);
		try {
			tmt = conn.prepareStatement(sql);

			tmt.setInt(1, addPay.getUserID());
			tmt.setInt(2, addPay.getPayment());
			tmt.setTimestamp(3, addPay.getPayTime());

			insertCount = tmt.executeUpdate();
			tmt.clearParameters();
			
			tmt.close();

		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		return insertCount;
	}
	
	public static void blacklist(Connection conn, PreparedStatement tmt, PayHistory black) {
		
	}
	
}
