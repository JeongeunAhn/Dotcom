package com.nts.sqa.dotcom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//game이 끝났을 때 호출되어 유저이름, 초, 미스카운트 개수 저장
public class RankHelper {
	String id = null;
	long time = 0;
	int misscount = 0;
	int total = 0;
	boolean id_exist = false;
	File file = new File("C:\\Users\\huyu0\\java\\Dotcom-master\\home\\rank.txt");
	ArrayList<RankData> ranklist = new ArrayList<RankData>();
	
	public RankHelper(String username, long time, int misscount) {
		this.id = username;
		this.time = time;
		this.misscount = misscount;
		this.total = (int) (time * misscount);
	}

	// rank 파일에 기록 입력하기
	public void rank() {
		try {// C:\\Users\\User\\eclipse-workspace\\DotCom\\user.txt
				// 파일 객체 생성

			// 입력 스트림 생성
			FileReader filereader = new FileReader(file);
			// 입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(filereader);

			String line = null;
			String[] splitedStr = new String[4];
			

			
			
			while ((line = bufReader.readLine()) != null) { // 한줄씩 읽기
				splitedStr = null;
				splitedStr = line.split("\t"); // 탭을 기준으로 나눠준다
				
				for (int i = 0; i < splitedStr.length; i++) {
					splitedStr[i] = splitedStr[i].trim();
				}
				if (splitedStr[0].equals(id)) { // 아이디 존재
					id_exist = true;
					// 본인 기록 갱신 여부 확인. 총점이 적을 수록 높은 순위
					// 기록 갱신이라면 파일 내용 업데이트
					if (total < Integer.parseInt(splitedStr[3])) {
						// 파일 업데이트
						splitedStr[3] = String.valueOf(total);
						System.out.println("NEW BEST RECORD!");
						System.out.println(id + " : " + total + " 점 입니다");
					}
				}
				RankData data = new RankData(splitedStr[0],splitedStr[1],splitedStr[2],splitedStr[3]);
				ranklist.add(data);
			}
			bufReader.close();
			// 아이디가 없다면 새로 적기
			if (id_exist == false) {
				RankData data = new RankData(splitedStr[0],String.valueOf(time),String.valueOf(misscount),String.valueOf(total));
				ranklist.add(data);
			}
			//ranklist에 파일 추가 끝.
			updateRanking();
			checktop3();
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. 다시 실행해주세요.");
		} catch (IOException e) {
			System.out.println("에러가 발생했습니다. 다시 실행해주세요.");
		}
	}

	// 기록 갱신했을 때랑, 새로 랭킹에 등록될 때 3위 안에 드는지 체크, 3위안에 든다면 top3 함수 호출
	// 기존 기록이 3위 안에 있지만 현재 게임 결과가 3위 안에 들지 않는다면 호출하지 않는다.
	public void checktop3() {
		// 3위 안에 드는 지 확인하는 코드 구현하기. 기록갱신 flag 만들기
		
		//
		System.out.println("NEW BEST RECORD");
		System.out.println("TOP 3");
		System.out.println("1위 :");
		System.out.println("2위 :");
		System.out.println("3위 :");
	}

	// rank 파일 정렬하는 함수
	public void updateRanking() {
		Collections.sort(ranklist);
		try {
			FileWriter fw = new FileWriter(file);
			for(RankData ranktoset : ranklist) {
				fw.write( ranktoset.getId() + "\t" +ranktoset.getTime()+ "\t"+ ranktoset.getMisscount()+ "\t"+ranktoset.getTotal()+"\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
