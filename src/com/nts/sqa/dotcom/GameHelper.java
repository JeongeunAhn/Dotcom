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
	private static final String alphabet = "abcdefg";
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
		String inputLine = "입력안함";
		String result="입력안함2";
		System.out.print(prompt + "  ");

		ExecutorService threadPool = Executors.newCachedThreadPool();
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				try {
					BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
					String inputLine = is.readLine();
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
			result = task.get(5, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			System.out.println("입력 시간이 초과되었습니다.");
		}
		//시간 초과는 동작 확인. 입력값이 제대로 넘어가는지 확인필요
		return result.toLowerCase();
	}

	public ArrayList<String> placeDotCom(int comSize) { // line 19
		ArrayList<String> alphaCells = new ArrayList<String>();
		String[] alphacoords = new String[comSize]; // holds 'f6' type coords
		String temp = null; // temporary String for concat
		int[] coords = new int[comSize]; // current candidate coords
		int attempts = 0; // current attempts counter
		boolean success = false; // flag = found a good location ?
		int location = 0; // current starting location

		comCount++; // nth dot com to place
		int incr = 1; // set horizontal increment
		if ((comCount % 2) == 1) { // if odd dot com (place vertically)
			incr = gridLength; // set vertical increment
		}

		while (!success & attempts++ < 200) { // main search loop (32)
			location = (int) (Math.random() * gridSize); // get random starting point
			// System.out.print(" try " + location);
			int x = 0; // nth position in dotcom to place
			success = true; // assume success
			while (success && x < comSize) { // look for adjacent unused spots
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

		int x = 0; // turn good location into alpha coords
		int row = 0;
		int column = 0;
		// System.out.println("\n");
		while (x < comSize) {
			grid[coords[x]] = 1; // mark master grid pts. as 'used'
			row = (int) (coords[x] / gridLength); // get row value
			column = coords[x] % gridLength; // get numeric column value
			temp = String.valueOf(alphabet.charAt(column)); // convert to alpha

			alphaCells.add(temp.concat(Integer.toString(row)));
			x++;

			// System.out.print(" coord "+x+" = " + alphaCells.get(x-1));

		}
		// System.out.println("\n");

		return alphaCells;
	}
}
