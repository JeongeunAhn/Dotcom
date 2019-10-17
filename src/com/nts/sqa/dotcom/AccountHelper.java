package com.nts.sqa.dotcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountHelper {
	boolean validaccount = false;
	String id = null, password = null;
	String[] splitedStr = null;
	boolean id_exist = false;

	public void accountcheck(int option) {
		// 계정이 있는 경우
		if (option == 1) {
			login();
		}
		// 계정 만들기
		else if (option == 2) {
			makeaccount();
		}
	}

	public void login() {
		System.out.println("[로그인]");
		int flag = 0;
		Scanner sc = new Scanner(System.in);
		loginloop: while (validaccount == false) { // ID와 패스워드가 일치할 때까지 입력받는다.
			System.out.println("ID를 입력하세요.");
			System.out.print("ID : ");
			id = sc.next();
			System.out.println("Password를 입력하세요");
			System.out.print("PW : ");
			password = sc.next();
			try {
				//File file = new File(" C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt");
					// 파일 객체 생성
				File file = new File("C:\\Users\\huyu0\\java\\Dotcom-master\\home\\user.txt");
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
						} else {// 아이디는 있는데 패스워드는 틀린경우
							System.out.println("비밀번호가 틀립니다. 다시 입력해주세요");
							break;
						}
					}
				}
				bufReader.close();

				if (id_exist == false) {
					System.out.println("입력하신 ID가 존재하지 않습니다.");
					System.out.println("다시 로그인 하시려면 1번, 계정을 만들고 싶으시면 2번을 눌러주세요.");
					int option = 0;
					while (true) {

						try {
							option = sc.nextInt();

						} catch (InputMismatchException e) {
							System.out.println("1 또는 2를 입력해주세요.");
						}
						if (option == 1) {
							break;
						} else if (option == 2) {
							flag = 1;
							break loginloop;
						} else {
							System.out.println("1또는 2를 입력해주세요.");
						}
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("파일을 찾을 수 없습니다. 다시 실행해주세요.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("에러가 발생했습니다. 다시 실행해주세요.");
				System.exit(0);
			}
		}
		// 중간에서 계정만들기를 희망한 경우
		if (flag == 1)
			makeaccount();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void makeaccount() {
		id_exist = true;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("ID는 영문,숫자,_가 포함될 수 있으며, 3~16자 사이로 입력해야 합니다.");
		while (id_exist == true) { // 파일탐색해서 존재하는 ID가 없을 때 까지 입력 받기
			int flag = 0;
			System.out.println("ID: ");
			id = sc.next();
			// 조건에 맞는 ID인지 확인하기
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]{3,16}$"); // 정규 표현식 사용
			Matcher m = p.matcher(id); // id가 조건에 맞는지 검사해서 m에 저장

			if (m.find()) { // 조건에 맞는 ID를 입력했다면,
				// 이미 있는 ID인지 확인하기
				try {
					// 파일 객체 생성
					//File file = new File("C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt"); //회사
					File file = new File("C:\\Users\\huyu0\\java\\Dotcom-master\\home\\user.txt");
					FileReader filereader = new FileReader(file);
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
							System.out.println("이미 등록된 ID가 있습니다. 다른 ID를 입력해주세요");
							id_exist = true;
							flag = 1;
							break;
						}
					} // 파일읽기 끝
					if (flag == 0) {
						id_exist = false;
					}
				} catch (Exception e) {
					System.out.println("잘못된 파일 경로입니다. 다시 실행해주세요");
					System.exit(0);
				}

			} else { // 입력한 id가 조건에 맞지 않는다면
				System.out.println("입력한 ID가 조건에 맞지 않습니다. 다시 입력해주세요.");
			}

		} // ID 확인 끝
			// password 체크
		System.out.println("비밀번호는 영문,숫자가 포함될 수 있으며, 3~16자 사이로 입력해야 합니다.");
		
		while (true) {
			System.out.println("PW :");
			password = sc.next();
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]{3,16}$"); // 일단 ID랑 동일 조건. 정규표현식을 사용해서 원하는 조건으로 변경가능
			Matcher m = p.matcher(password); // password가 조건에 맞는지 검사해서 m에 저장
			boolean password_check = false;
			password_check = password.matches(".*" + id.substring(0, 3) + ".*"); // ID 앞 4글자와 일치하는지 검사
			if (m.find()) { // password가 입력조건에 맞는다면
				if (! password_check) { // ID앞 4글차 체크도 통과하면
					// ID 와 password를 user.txt에 추가한다.
					//File file = new File("C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt");
					File file = new File("C:\\Users\\huyu0\\java\\Dotcom-master\\home\\user.txt");
					try {
						FileWriter fw = new FileWriter(file, true);
						fw.write("\r\n" + id + "\t" + password);
						fw.close();
						break;
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					System.out.println("ID앞 4글자를 포함하고 있습니다. ID앞 4글자가 포함되지 않도록 다시 입력해주세요.");
				}
			} else {
				System.out.println("3~16자 사이로 입력해야 합니다.");
			}
		}
		System.out.println("계정이 생성되었습니다.");
		login();
	}

}
