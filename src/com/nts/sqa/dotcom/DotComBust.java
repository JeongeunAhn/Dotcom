package com.nts.sqa.dotcom;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class DotComBust {

	private GameHelper helper = new GameHelper();
	private AccountHelper accounthelper = new AccountHelper();
	private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
	private int numOfGuesses = 0;
	private ArrayList<String> bombLocation = new ArrayList<String>();
	private ArrayList<String> Hint = new ArrayList<String>();
	private int hintcount = 3;
	private long beforetime, aftertime, time;

	private void setUpGame() {
		// first make some dot coms and give them locations
		// 닷컴의 개수는 최대 다섯개이므로 다섯개 세팅
		DotCom one = new DotCom();
		one.setName("Pets.com");
		DotCom two = new DotCom();
		two.setName("eToys.com");
		DotCom three = new DotCom();
		three.setName("Go2.com");
		DotCom four = new DotCom();
		four.setName("naver.com");
		DotCom five = new DotCom();
		five.setName("nts.com");
		dotComsList.add(one);
		dotComsList.add(two);
		dotComsList.add(three);

		System.out.println("닷컴게임을 시작합니다!");
		System.out.println("목표는 닷컴들을 가라앉히는 것입니다.");
		System.out.println("가장 적은 횟수로 모든 닷컴들을 가라앉히세요.");
		System.out.println("점수는 시도횟수와 걸리시간(초)의 곱입니다.");
		System.out.println("로그인 하시려면 1번을, 계정을 만드시려면 2번을 눌러주세요.");

		int login_option = 1;
		int grid_size = 7;
		int num_of_dotcom = 3;
		while (true) {
			try {
				Scanner sc = new Scanner(System.in);
				login_option = sc.nextInt();
				if (login_option == 1 || login_option == 2) {
					break;
				} else {
					System.out.println("1 또는 2를 입력하세요.");
				}
			} catch (InputMismatchException e) {
				System.out.println("1 또는 2를 입력하세요.");
			}
		}
		// 사용자의 입력을 받아 accounthelper 객체로 넘긴다
		accounthelper.accountcheck(login_option);
		// 계정정보가 맞다면, 나머지 게임 세팅을 시작한다.
		if (accounthelper.validaccount == true) {
			// grid 사이즈 받기 7~11
			System.out.println("그리드 사이즈를 입력하세요 : ");
			while (true) {
				try {
					Scanner sc = new Scanner(System.in);
					grid_size = sc.nextInt();
					if (grid_size >= 7 && grid_size <= 11) {
						helper.setGridLength(grid_size);
						break;
					} else {
						System.out.println("7에서 11사이의 숫자를 입력하세요.");
					}
				} catch (InputMismatchException e) {
					System.out.println("숫자를 입력하세요"); //
				}
			}

			// dotcom 개수 받기 3~5. grid size가 7인 경우 dotcom개수는 3개, 나머지는 자유
			if (grid_size == 7) {
				System.out.println("grid size로 7을 선택하셨습니다. dotcom의 개수는 3개입니다.");
			} else {
				System.out.println("dotcom 개수를 입력하세요");
				while (true) {
					try {
						Scanner sc = new Scanner(System.in);
						num_of_dotcom = sc.nextInt();
						if (num_of_dotcom >= 3 && num_of_dotcom <= 5) {
							hintcount = num_of_dotcom;
							if (num_of_dotcom == 4) {
								dotComsList.add(four);
							}
							if (num_of_dotcom == 5) {
								dotComsList.add(four);
								dotComsList.add(five);
							}
							break;
						}

						else {
							System.out.println("3에서 5사이의 숫자를 입력하세요.");
						}
					} catch (InputMismatchException e) {
						System.out.println("숫자를 입력하세요.");
					}
				}
			}
			// 이부분 확인하기
			for (DotCom dotComToSet : dotComsList) { // 목록에 있는 각 닷컴에 대해 반복
				ArrayList<String> newLocation = helper.placeDotCom(3); // 닷컴의 위치를 지정하기 위한 보조 메소드 호출
				dotComToSet.setLocationCells(newLocation);// dot com 객체의 세터 메소드를 호출하여 방금 보조 메소드에서 받아온 위치 지정
				Hint.addAll(dotComToSet.getLocationCells());
			}
			bombLocation = helper.placeBomb(num_of_dotcom);
		}
	}

	private boolean startPlaying() throws InterruptedException, ExecutionException {
		boolean check = false;
		beforetime = System.currentTimeMillis();
		while (!dotComsList.isEmpty()) {
			check = false;
			String userGuess = helper.getUserInput("Enter a guess");
			System.out.println(userGuess);
			if (userGuess.equals("hint")) {
				if (hintcount == 0) {
					System.out.println("힌트가 소진되었습니다.");
				} else {
					System.out.println("힌트 :" + Hint.remove(0));
					hintcount--;
				}
			} else if (userGuess.equals("입력 시간이 초과되었습니다.")) {
				return gameOver();

			} else {
				check = checkUserGuess(userGuess);
				if (check == true)
					return true;
			}
		} // close while

		aftertime = System.currentTimeMillis();
		time = (aftertime - beforetime) / 1000;
		return finishGame();

	} // close startPlaying method

	private boolean checkBomb(String userGuess) {
		for (int i = 0; i < bombLocation.size(); i++) {
			if (bombLocation.get(i).equals(userGuess)) {
				System.out.println("Game Over! 폭탄이 터졌습니다.");
				return gameOver();
			}
		}
		return false;
	}

	private boolean checkUserGuess(String userGuess) {
		numOfGuesses++;
		String result = "miss"; // assume a miss until told otherwise
		// 폭탄을 골랐는지 먼저 체크 후, 없으면 dotcom 검사
		boolean check = false;
		check = checkBomb(userGuess);
		if (check == true) {
			return check;
		}
		for (DotCom dotComToTest : dotComsList) {

			// DotCom dotComToTest = (DotCom) dotComsList.get(i);
			result = dotComToTest.checkYourself(userGuess);

			if (result.equals("hit")) {
				Hint.remove(userGuess); // 입력값이 맞으면 힌트목록에서 제거
				break;
			}
			if (result.equals("kill")) {
				Hint.remove(userGuess);
				dotComsList.remove(dotComToTest); // he's gone
				break;
			}

		} // close for
			// 너무 많이 틀리면 게임오버됨. 30번까지 입력가능하게 설정
		if (numOfGuesses > 29) {
			System.out.println("Game Over! 입력 횟수 초과입니다.");
			return gameOver();
		}
		System.out.println(result);
		return false;
	}

	private boolean finishGame() {
		System.out.println("성공입니다. 모든 닷컴이 가라앉았습니다!");
		RankHelper rankhelper = new RankHelper(accounthelper.getId(), time, numOfGuesses);
		rankhelper.rank();
		return gameOver();
	}

	public boolean gameOver() { // 시간초과, 미스 개수 초과, 폭탄 선택, 게임 정상 종료 시에 호출됨
		boolean check = false;
		numOfGuesses = 0;
		// System.out.println("Game Over!");
		System.out.println("게임을 다시 시작하려면 1번, 프로그램을 종료하려면 1을 제외한 다른키를 아무거나 누르세요.");
		System.out.println("만약 입력 후 프로그램 재시작/종료가 되지 않는다면 한번 더 입력해주세요! :)");
		Scanner sc = new Scanner(System.in);
		String n = sc.next();
		try {
			if (Integer.parseInt(n) == 1) {
				check = true;
				return check;
			} else {
				System.out.println("Dotcom게임을 종료합니다.");
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			System.out.println("Dotcom게임을 종료합니다.");
			System.exit(0);
		}
		return false;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		boolean restart = false; // false로 초기화
		do {
			restart = false;
			DotComBust game = new DotComBust();
			game.setUpGame();
			restart = game.startPlaying();
		} while (restart == true);
	}
}
