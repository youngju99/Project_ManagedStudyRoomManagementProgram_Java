package studyRoom.sms;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-20 ~ 2022-10-22
 * 내용 : sms 사용 내역(사용 내역 조회)
 */

public class SmsHistory {
	private int userID;
	private String userName;
	private int seatNum;
	private String callingNum;
	private String msg;
	private String sendMsgTime;
	private boolean sendMsg;
	
	public SmsHistory() {}

	public SmsHistory(int userID, String userName, int seatNum, String callingNum, String msg, String sendMsgTime,
			boolean sendMsg) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.seatNum = seatNum;
		this.callingNum = callingNum;
		this.msg = msg;
		this.sendMsgTime = sendMsgTime;
		this.sendMsg = sendMsg;
	}

	public int getUserID() {return userID;}
	public String getUserName() {return userName;}
	public int getSeatNum() {return seatNum;}
	public String getCallingNum() {return callingNum;}
	public String getMsg() {return msg;}
	public String getSendMsgTime() {return sendMsgTime;}
	public boolean isSendMsg() {return sendMsg;}

	public void setUserID(int userID) {this.userID = userID;}
	public void setUserName(String userName) {this.userName = userName;}
	public void setSeatNum(int seatNum) {this.seatNum = seatNum;}
	public void setCallingNum(String callingNum) {this.callingNum = callingNum;}
	public void setMsg(String msg) {this.msg = msg;}
	public void setSendMsgTime(String sendMsgTime) {this.sendMsgTime = sendMsgTime;}
	public void setSendMsg(boolean sendMsg) {this.sendMsg = sendMsg;}
	
	@Override
	public String toString() {
		return String.format("%-5d%-12s%-6s%-15s%-36s%-26s%-5b\n",this.userID,this.seatNum,this.userName,this.callingNum,this.msg,this.sendMsgTime,this.sendMsg);
	}
}
