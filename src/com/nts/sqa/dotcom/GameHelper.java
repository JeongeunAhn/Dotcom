package com.nts.sqa.dotcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameHelper {
	private static final String alphabet = "abcdefghijklm";
	private int gridLength = 7;
	private int gridSize = gridLength * gridLength;

	public int getGridLength() {
		return gridLength;
	}

	public void setGridLength(int gridLength) {
		this.gridLength = gridLength;
	}

	private int[] grid = new int[gridSize];
	private int comCount = 0;

	public String getUserInput(String prompt) throws InterruptedException, ExecutionException {
		String result= null;
		System.out.print(prompt + "  ");

		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			@Override
			public String call() throws Exception {
				String inputLine = null;
				try {
					BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
					inputLine = is.readLine();
					if (inputLine.length() == 0)
						return null;
				} catch (IOException e) {
					System.out.println("IOException: " + e);
				}
				return inputLine;
			}
		});
		threadPool.execute(task);
		try {
			result = task.get(60, TimeUnit.SECONDS);
			
		} catch (TimeoutException e) {
			System.out.println("입력 시간이 초과되었습니다.");
			return "gameover";
		}
		
		return result.toLowerCase();
	}

	public ArrayList<String> placeDotCom(int comSize) { 
		ArrayList<String> alphaCells = new ArrayList<String>();
		//String[] alphacoords = new String[comSize]; // 'f6'과 같은 좌표가 들어감
		String temp = null; // 나중에 연결하기 위한 임시 String 배열
		int[] coords = new int[comSize]; // 현재후보좌표
		int attempts = 0; // 시도 횟수를 세기 위한 카운터
		boolean success = false; // 적당한 위치를 찾았는지 표시하기 위한 플래그
		int location = 0; // 현재시작위치

		comCount++; // n번째 닷컴
		int incr = 1; // 수평방향으로 증가시킬 값 설정
		if ((comCount % 2) == 1) { // if odd dot com (place vertically)
			incr = gridLength; // 수직방향으로 증가시킬 값 설정
		}

		while (!success & attempts++ < 200) { // 주 검색 순환문 (32)
			location = (int) (Math.random() * gridSize); // get random starting point
			// System.out.print(" try " + location);
			int x = 0; // 위치시킬 닷컴의 n번째 위치
			success = true; // 성공할 것으로 가정
			while (success && x < comSize) { // 닷컴이 들어갈 자리가 비었는지 확인
				if (grid[location] == 0) { // if not already used
					coords[x++] = location; // save location
					location += incr; // try 'next' adjacent
					if (location >= gridSize) { // out of bounds - 'bottom'
						success = false; // failure
					}
					if (x > 0 & (location % gridLength == 0)) { // out of bounds - right edge
						success = false; // failure
					}
				} else { // found already used location
					// System.out.print(" used " + location);
					success = false; // failure
				}
			}
		} // end while

		int x = 0; // 위치를 알파벳좌표로 바꿈
		int row = 0;
		int column = 0;
		// System.out.println("\n");
		while (x < comSize) {
			grid[coords[x]] = 1; // 기본그리드 좌표를 '사용중'으로 표시
			row = coords[x] / gridLength; // 행값을 구함
			column = coords[x] % gridLength; // 열 값(숫자)를 구함
			temp = String.valueOf(alphabet.charAt(column)); // 열을 알파벳으로 변환

			alphaCells.add(temp.concat(Integer.toString(row)));
			x++;

			 System.out.print(" coord "+x+" = " + alphaCells.get(x-1));

		}
		// System.out.println("\n");

		return alphaCells;
	}
	
	public ArrayList<String> placeBomb(int numOfDotcom) {
		int location = 0;
		int row = 0;
		int column = 0;
		boolean flag = true;
		ArrayList<String> alphaCells = new ArrayList<String>();
		String temp = null;
		for (int i = 0; i < numOfDotcom; i++) { // 닷컴의 개수만큼 만들거야
			while (flag) { //적당한 위치를 찾을 때까지 반복
				location = (int) (Math.random() * gridSize); //임의의 위치
				if (location >= gridSize || grid[location] == 1) { //그리드 사이즈를 벗어나거나, 이미 사용중인 경우
					flag = true; //다시 찾기
				} else { // 적당한 위치면
					flag = false; //루프 빠져나가기
				}
			}
			grid[location] = 1; // 기본그리드 좌표를 '사용중'으로 표시
			row = location / gridLength; // 행값을 구함
			column = location % gridLength; // 열 값(숫자)를 구함
			temp = String.valueOf(alphabet.charAt(column)); // 열을 알파벳으로 변환
			alphaCells.add(temp.concat(Integer.toString(row))); //array에 위치 저장하기
			flag = true; //flag 초기화
		}
		return alphaCells;
	}
	
	
}
