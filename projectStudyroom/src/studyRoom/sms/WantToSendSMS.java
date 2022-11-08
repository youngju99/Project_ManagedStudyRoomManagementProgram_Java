package studyRoom.sms;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-19 ~ 2022-10-22
 * 내용 : sms전송이 가능한 회원
 */

public class WantToSendSMS {
	private String userName;
	private String userMobile;
	private String parentMobile;
	private String inputTime;
	private String sms;
	private int seatNum;
	private int userID;
	
	public WantToSendSMS() {}

	public WantToSendSMS(String userName, String userMobile, String parentMobile, String inputTime, String sms, int seatNum, int userID) {
		super();
		this.userID=userID;
		this.userName = userName;
		this.userMobile = userMobile;
		this.parentMobile = parentMobile;
		this.inputTime = inputTime;
		this.sms = sms;
		this.seatNum=seatNum;
	}

	public String getUserName() {return userName;}
	public String getParentMobile() {return parentMobile;}
	public String getSms() {return sms;}
	public String getUserMobile() {return userMobile;}
	public String getInputTime() {return inputTime;}
	public int getSeatNum() {return seatNum;}
	public int getUserID() {return userID;}
	
	public void setUserMobile(String userMobile) {this.userMobile = userMobile;}
	public void setUserName(String userName) {this.userName = userName;}
	public void setParentMobile(String parentMobile) {this.parentMobile = parentMobile;}
	public void setSms(String sms) {this.sms = sms;}
	public void setInputTime(String inputTime) {this.inputTime = inputTime;}
	public void setSeatNum(int seatNum) {this.seatNum = seatNum;}
	public void setUserID(int userID) {this.userID = userID;}

	@Override
	public String toString() {
		return String.format("%-5d%-12d%-8s%-20s%-20s%-15s%-5s\n",this.userID,this.seatNum,this.userName,this.userMobile,this.parentMobile,this.inputTime,this.sms);
	}
}
