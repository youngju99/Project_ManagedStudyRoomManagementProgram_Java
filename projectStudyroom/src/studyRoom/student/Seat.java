package studyRoom.student;

/*
 * 작성자 : 이다영
 * 사용자 메뉴에 필요한 정보들
 */

import java.sql.Timestamp;

public class Seat {
	
	private int seatNum;				// 좌석 번호
	private String user;				// 사용자 이름
	private Timestamp inputTime;		// 입실 시간
	private Timestamp outputTime;		// 퇴실시간

	// 생성자
	public Seat(int seatNum, String user, Timestamp inputTime, Timestamp outputTime) {
		super();
		this.seatNum = seatNum;
		this.user = user;
		this.inputTime = inputTime;
		this.outputTime = outputTime;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Timestamp getInputTime() {
		return inputTime;
	}

	public void setInputTime(Timestamp inputTime) {
		this.inputTime = inputTime;
	}

	public Timestamp getOutputTime() {
		return outputTime;
	}

	public void setOutputTime(Timestamp outputTime) {
		this.outputTime = outputTime;
	}
	
}