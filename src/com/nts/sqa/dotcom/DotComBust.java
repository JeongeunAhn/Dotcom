package com.nts.sqa.dotcom;

import java.io.IOException;
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
	private int hintcount=3;

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

		System.out.println("닷컴게임을 시작합니다.!");
		System.out.println("목표는 닷컴들을 가라앉히는 것입니다.");
		// System.out.println("Pets.com, eToys.com, Go2.com");
		System.out.println("Try to sink them all in the shortest amount of guesses");
		System.out.println("If you have account, please press 1. If not, press 2 to make account.");
		Scanner sc = new Scanner(System.in);
		int login_option = 1;
		int grid_size = 7;
		int num_of_dotcom = 3;
		while (true) {
			try {
				login_option = sc.nextInt();
				if (login_option == 1 || login_option == 2) {
					break;
				} else {
					System.out.println("1 또는 2를 입력하세요");
				}
			} catch (InputMismatchException e) {
				System.out.println("press 1 or 2.");
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
					grid_size = sc.nextInt();
					if (grid_size >= 7 && grid_size <= 11) {
						helper.setGridLength(grid_size);
						break;
					} else {
						System.out.println("7에서 11사이의 숫자를 입력하세요.");
					}
				} catch (InputMismatchException e) {
					System.out.println("숫자를 입력하세요"); // 무한루프돎 확인필요
				}
			}

			// dotcom 개수 받기 3~5. grid size가 7인 경우 dotcom개수는 3개, 나머지는 자유
			if (grid_size == 7) {
				System.out.println("grid size로 7을 선택하셨습니다. dotcom의 개수는 3개입니다.");
			} else {
				System.out.println("dotcom 개수를 입력하세요");
				while (true) {
					try {
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

			for (DotCom dotComToSet : dotComsList) { // 목록에 있는 각 닷컴에 대해 반복
				ArrayList<String> newLocation = helper.placeDotCom(3); // 닷컴의 위치를 지정하기 위한 보조 메소드 호출
				// DotCom dotComToSet = (DotCom) dotComsList.get(i);
				dotComToSet.setLocationCells(newLocation);// dot com 객체의 세터 메소드를 호출하여 방금 보조 메소드에서 받아온 위치 지정
				Hint.addAll(dotComToSet.getLocationCells());
			}

			bombLocation = helper.placeBomb(num_of_dotcom);
			for (int i = 0; i < bombLocation.size(); i++) {
				System.out.println();
				System.out.print(bombLocation.get(i) + " ");
			}
			// bomb.setLocationCells(bombLocation);
		}
	}

	private void startPlaying() throws InterruptedException, ExecutionException {

		while (!dotComsList.isEmpty()) {

			String userGuess = helper.getUserInput("Enter a guess");
			System.out.println(userGuess);
			if (userGuess.equals("hint")) {
				if (hintcount == 0) {
					System.out.println("힌트가 소진되었습니다.");
				} else {
					System.out.println("힌트 :" + Hint.remove(0));
					hintcount--;
				}
			} else {
				checkUserGuess(userGuess);
			}
		} // close while
		finishGame();
	} // close startPlaying method

	private void checkBomb(String userGuess) {
		for (int i = 0; i < bombLocation.size(); i++) {
			if (bombLocation.get(i).equals(userGuess)) {
				System.out.println("폭탄!");
				gameOver();
			}
		}
	}

	private void checkUserGuess(String userGuess) {
		numOfGuesses++;
		String result = "miss"; // assume a miss until told otherwise
		// 폭탄을 골랐는지 먼저 체크 후, 없으면 dotcom 검사
		checkBomb(userGuess);

		for (DotCom dotComToTest : dotComsList) {

			// DotCom dotComToTest = (DotCom) dotComsList.get(i);
			result = dotComToTest.checkYourself(userGuess);

			if (result.equals("hit")) {
				Hint.remove(userGuess); // 입력값이 맞으면 힌트목록에서 제거
				// System.out.println(Hint);
				break;
			}
			if (result.equals("kill")) {

				dotComsList.remove(dotComToTest); // he's gone
				break;
			}

		} // close for

		System.out.println(result);
	}

	private void finishGame() {
		System.out.println("All Dot Coms are dead! Your stock is now worthless");
		if (numOfGuesses <= 9) {
			System.out.println("It only took you " + numOfGuesses + " guesses.  You get the Enron award!");
		} else {
			System.out.println("Took you long enough. " + numOfGuesses + " guesses.");
			System.out.println("Too bad you didn't get out before your options sank.");
		}
	}

	public void gameOver() { // 시간초과, 미스 개수 초과시에 호출됨
		System.out.println("Game Over!");
		System.out.println("게임을 다시 시작하려면 1번, 프로그램을 종료하려면 1을 제외한 다른키를 아무거나 누르세요.");
		Scanner sc = new Scanner(System.in);
		String n = sc.next();
		try {
			if (Integer.parseInt(n) == 1) {
				restart();
			} else {
				System.out.println("Dotcom게임을 종료합니다.");
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			System.out.println("Dotcom게임을 종료합니다.");
			System.exit(0);
		}
	}

	public void restart() { // restart 구현

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		DotComBust game = new DotComBust();
		game.setUpGame();
		game.startPlaying();
	}
}
