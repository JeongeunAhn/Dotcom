package com.nts.sqa.dotcom;

import java.util.ArrayList;

public class DotCom {
	private ArrayList<String> locationCells;  
	   private String name;

	   public void setLocationCells(ArrayList<String> loc) {
	      locationCells = loc;
	   }

	      public ArrayList<String> getLocationCells() {
		return locationCells;
	}

		public void setName(String n) {
	      name = n;
	  }

	   public String checkYourself(String userInput) { 
	      String result = "miss";
	      int index = locationCells.indexOf(userInput);      
	      if (index >= 0) {                              
	          locationCells.remove(index);
	         
	         if (locationCells.isEmpty()) {
	             result = "kill";
	             System.out.println("오예!" + name +"을 가라앉혔습니다!");
	         } else {
	             result = "hit";
	         }  // close if                       
	      } // close if     
	       return result;

	   } // close method
}
