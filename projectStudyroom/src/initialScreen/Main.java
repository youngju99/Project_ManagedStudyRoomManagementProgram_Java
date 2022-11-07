package initialScreen;

/*
 * 작성자 : 김영주
 * 작성일 : 2022-10-19
 * 내용 : 관리자(로그인 필요. 3회 이상 오류 시 강제종료)|사용자 선택
 * 
 * 수정 : 이다영
 * 내용 : 종료 메뉴 추가
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import studyRoom.student.*;

public class Main {

	public static void main(String[] args) throws IOException, SQLException{
		
		LoginSystem loginThread = new LoginSystem();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		int select=0;
		boolean loginCheck = false, resetCheck = false;		
		
		while(true) {
			System.out.println("\n\t관리형 독서실 [ 2팀 독서실 ]입니다. 접근 권한을 설정해주세요.");
			System.out.println("\t===============================================================");
			System.out.println("\t              0: 관리자 | 1: 사용자 | 2: 종료                  ");
			System.out.println("\t===============================================================");
			
			// select 0: 관리자, 1: 사용자, 2: 종료
			System.out.print("\t접근 권한 선택 >> ");
			
			try {
				select=Integer.parseInt(br.readLine());
				
			} catch(NumberFormatException e) {
				System.out.println("\t >> 잘못된 접근입니다. 다시 입력해주세요.\n");
				continue;
			}
						
			if(select==0) {
				// 로그인
				loginCheck = LoginSystem.login();
				
				if(loginCheck) {
					ManagerMode.managerMode();
				}
				else {
					System.out.println("\n로그인 접근을 15분간 정지합니다.\n");
					try {
						for(int i=120;i>=1;i--) {
							if(!resetCheck) {
								// 비밀번호 재설정
								loginThread.start();
								resetCheck = true;
							}
							Thread.sleep(1000);

							if(i<=6 && i>1) {System.out.println((i-1)+"초 남았습니다.");}
							else if(i==1) {System.out.println("\n접근 제한 해제");}
						}
					} catch (InterruptedException e) {}
				}
				
			} else if(select==1) {
				// 사용자 모드 실행
				System.out.println("\n\t\t\t\t[ 사용자 접근 ]\n");
				
				// 사용자 메서드 호출
				if(!Student.showUserMenu()) {
					continue;					
				}
			}
			else if(select == 2) {
				// 종료
				System.out.println("\n\t >> 시스템을 종료합니다.\n");
				return;
			}
			else {
				System.out.println("\n\t >>잘못된 접근입니다. 다시 입력해주세요.\n");
			}
		}
	}
}