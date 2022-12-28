package studyRoom.pay;

/*
 * @date : 2022/11/04
 * @memo : 결제 getset
 */

import java.sql.Timestamp;

public class PayHistory {
	private int payNo;
	private int userID;
	private int payment;
	private Timestamp payTime;
	private boolean refundCheck;
	private int refund;
	
	public PayHistory() {}

	public PayHistory(int userID, int payment, Timestamp payTime) {
		super();
		this.userID = userID;
		this.payment = payment;
		this.payTime = payTime;
	}


	public int getPayNo() {
		return payNo;
	}


	public void setPayNo(int payNo) {
		this.payNo = payNo;
	}


	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public int getPayment() {
		return payment;
	}


	public void setPayment(int payment) {
		this.payment = payment;
	}


	public Timestamp getPayTime() {
		return payTime;
	}


	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}


	public boolean isRefundCheck() {
		return refundCheck;
	}


	public void setRefundCheck(boolean refundCheck) {
		this.refundCheck = refundCheck;
	}


	public int getRefund() {
		return refund;
	}


	public void setRefund(int refund) {
		this.refund = refund;
	}


	@Override
	public String toString() {
		return "PayHistory [payNo=" + payNo + ", userID=" + userID + ", payment=" + payment + ", payTime=" + payTime
				+ ", refundCheck=" + refundCheck + ", refund=" + refund + "]";
	}
	
	
}
