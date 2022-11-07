package studyRoom.blacklist;

public class PunishInfo {
	int userID;
	int point;
	
	public PunishInfo() {
	}
	
	public PunishInfo(int userID) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}