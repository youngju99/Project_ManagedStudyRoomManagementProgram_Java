package studyRoom.sms;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-11-05
 * 내용 : 시간표 저장
 */

public class TimeTable {
	private int classNum;
	private String start;
	private String end;
	
	public TimeTable() {}

	public TimeTable(int classNum, String start, String end) {
		super();
		this.classNum = classNum;
		this.start = start;
		this.end = end;
	}

	public int getClassNum() {return classNum;}
	public String getStart() {return start;}
	public String getEnd() {return end;}

	public void setClassNum(int classNum) {this.classNum = classNum;}
	public void setStart(String start) {this.start = start;}
	public void setEnd(String end) {this.end = end;}
	
	@Override
	public String toString() {
		return this.classNum+"교시 : "+this.start+" - "+this.end+"\n";
	}
}
