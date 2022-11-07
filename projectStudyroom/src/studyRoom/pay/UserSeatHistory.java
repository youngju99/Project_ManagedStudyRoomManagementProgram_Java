package studyRoom.pay;

/*
 * @author : 조규완
 * @date : 2022/11/04
 * @memo : 신규회원등록 getset
 */

import java.sql.Timestamp;

public class UserSeatHistory {
	private int userID;
	private String userName;
	private String userSchool;
	private Integer userGrade;
	private String userMobile;
	private String parentMobile;
	private String sms;
	
	private Timestamp startDate;
	private Timestamp endDate;
	
	public UserSeatHistory() {}
	
	public UserSeatHistory(int userID) {
		super();
		this.userID = userID;
	}

	public UserSeatHistory(int userID, String userName, String userSchool, Integer userGrade, String userMobile,
			String parentMobile, String sms) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.userSchool = userSchool;
		this.userGrade = userGrade;
		this.userMobile = userMobile;
		this.parentMobile = parentMobile;
		this.sms = sms;
	}
	
	

	public UserSeatHistory(String userName, String userSchool, Integer userGrade, String userMobile,
			String parentMobile, String sms) {
		super();
		this.userName = userName;
		this.userSchool = userSchool;
		this.userGrade = userGrade;
		this.userMobile = userMobile;
		this.parentMobile = parentMobile;
		this.sms = sms;
	}

	public UserSeatHistory(int userID, Timestamp startDate, Timestamp endDate) {
		super();
		this.userID = userID;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSchool() {
		return userSchool;
	}

	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}

	public Integer getUserGrade() {
		return userGrade;
	}

	public void setUserGrade(Integer userGrade) {
		this.userGrade = userGrade;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getParentMobile() {
		return parentMobile;
	}

	public void setParentMobile(String parentMobile) {
		this.parentMobile = parentMobile;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "UserSeatHistory [userID=" + userID + ", userName=" + userName + ", userSchool=" + userSchool
				+ ", userGrade=" + userGrade + ", userMobile=" + userMobile + ", parentMobile=" + parentMobile
				+ ", sms=" + sms + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	
}