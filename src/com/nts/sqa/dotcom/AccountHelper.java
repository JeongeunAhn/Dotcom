package com.nts.sqa.dotcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AccountHelper {
	boolean validaccount = false;
	public void accountcheck(int option) {
		//계정이 있는 경우
		Scanner sc = new Scanner(System.in);
		String id, password;
		if(option == 1) {
			System.out.println("ID를 입력하세요.");
			System.out.print(" ID : ");
			id=sc.next();
			System.out.println("Password를 입력하세요");
			password = sc.next();
			try {
				//파일입출력 한줄씩읽기 예제임 수정요함
				//파일 객체 생성
				File file = new File("C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt");
				//입력 스트림 생성
				FileReader filereader = new FileReader(file);
				//입력 버퍼 생성
				BufferedReader bufReader = new BufferedReader(filereader);
				String line = "";
				while((line= bufReader.readLine()) != null) {
					System.out.println(line);
				}
			}catch(FileNotFoundException e) {
				
			}catch(IOException e) {
				
			}
		}
		//계정이 없는 경우
		else if(option == 2) {
			
		}
		//잘못입력한 경우
		else {
			
		}
	}
}
