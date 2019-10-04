package com.nts.sqa.dotcom;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DotComBust {
	private GameHelper helper = new GameHelper();
	private AccountHelper accounthelper = new AccountHelper();
	private ArrayList<DotCom> dotComsList = new ArrayList<DotCom>();
	private int numOfGuesses = 0;

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
		dotComsList.add(four);
		dotComsList.add(five);
		System.out.println("Let's start dotcom Game!");
		System.out.println("Your goal is to sink dot coms.");
		// System.out.println("Pets.com, eToys.com, Go2.com");
		System.out.println("Try to sink them all in the shortest amount of guesses");
		System.out.println("Please login before game start");
		System.out.println("If you have account, please press 1. If not, press 2 to make account.");
		Scanner sc = new Scanner(System.in);
		int login_option = 1;
		int grid_size=7;
		int num_of_dotcom=3;
		try {
			login_option = sc.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("press 1 or 2.");
			login_option = sc.nextInt();
		}
		// 사용자의 입력을 받아 accounthelper 객체로 넘긴다
		accounthelper.accountcheck(login_option);
		// 계정정보가 맞다면, 나머지 게임 세팅을 시작한다.
		if (accounthelper.validaccount == true) {
			//grid 사이즈 받기
			System.out.println("Please, enter grid size");
			grid_size=sc.nextInt();
			//dotcom 개수 받기
			System.out.println("Please, enter the number of dotcoms");
			num_of_dotcom = sc.nextInt();
			//for(int i=0; i<num_of_dotcom;i++)
			// 이부분 책보고 이해한 후 수정하기!
			for (DotCom dotComToSet : dotComsList) {
				ArrayList<String> newLocation = helper.placeDotCom(num_of_dotcom);
				// DotCom dotComToSet = (DotCom) dotComsList.get(i);
				dotComToSet.setLocationCells(newLocation);

			}
		}
	}

	private void startPlaying() {

		while (!dotComsList.isEmpty()) {

			String userGuess = helper.getUserInput("Enter a guess");
			checkUserGuess(userGuess);

		} // close while
		finishGame();
	} // close startPlaying method

	private void checkUserGuess(String userGuess) {
		numOfGuesses++;
		String result = "miss"; // assume a miss until told otherwise

		for (DotCom dotComToTest : dotComsList) {

			// DotCom dotComToTest = (DotCom) dotComsList.get(i);
			result = dotComToTest.checkYourself(userGuess);

			if (result.equals("hit")) {

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

	public static void main(String[] args) {
		DotComBust game = new DotComBust();
		game.setUpGame();
		game.startPlaying();
	}
}
