package com.nts.sqa.dotcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountHelper {
	boolean validaccount = false;

	public void accountcheck(int option) {
		// 계정이 있는 경우
		Scanner sc = new Scanner(System.in);
		String id, password;
		String[] splitedStr = null;
		boolean id_exist = false;
		if (option == 1) {
			loginloop: while (validaccount == true) { // ID와 패스워드가 일치할 때까지 입력받는다.
				System.out.println("ID를 입력하세요.");
				System.out.print("ID : ");
				id = sc.next();
				System.out.println("Password를 입력하세요");
				password = sc.next();
				try {
					// 파일 객체 생성
					File file = new File("C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt");
					// 입력 스트림 생성
					FileReader filereader = new FileReader(file);
					// 입력 버퍼 생성
					BufferedReader bufReader = new BufferedReader(filereader);

					String line = null;
					splitedStr = null;

					while ((line = bufReader.readLine()) != null) { // 한줄씩 읽기
						splitedStr = null;
						splitedStr = line.split("\t"); // 탭을 기준으로 나눠준다

						for (int i = 0; i < splitedStr.length; i++) {
							splitedStr[i] = splitedStr[i].trim();
						}
						if (splitedStr[0].equals(id)) { // 아이디 존재
							id_exist = true;
							if (splitedStr[1].equals(password)) { // 아이디와 패스워드가 일치하면
								validaccount = true;
								break; // 파일읽기 끝
							}else {// 아이디는 있는데 패스워드는 틀린경우
								System.out.println("비밀번호가 틀립니다. 다시 입력해주세요");
							}
						} 
					}
					bufReader.close();
					
					if (id_exist == false) {
						System.out.println("입력하신 ID가 존재하지 않습니다.");
						System.out.println("다시 로그인 하시려면 1번, 계정을 만들고 싶으시면 2번을 눌러주세요.");
						while (true) {
							try {
								option = sc.nextInt();
								if (option == 1) {
									break;
								} else if (option == 2) {
									break loginloop;
								} else {
									System.out.println("1또는 2를 입력해주세요.");
								}
							} catch (InputMismatchException e) {
								System.out.println("1 또는 2를 입력해주세요.");
							}
						}
					}
				} catch (FileNotFoundException e) {
					System.out.println("파일을 찾을 수 없습니다. 다시 실행해주세요.");
				} catch (IOException e) {
					System.out.println("에러가 발생했습니다. 다시 실행해주세요.");
				}
			}
		}
		// 계정 만들기
		else if (option == 2) {
			System.out.println("계정이 없습니다");
		}

	}
}
