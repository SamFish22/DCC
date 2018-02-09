package core;

import java.util.ArrayList;

public class Inconsistency {
	String option1, option2;
	ArrayList<Integer> wordIndex;
	ArrayList<Integer> whereOppositesAre;
		
	public Inconsistency(String option1, String option2) {
		this.option1 = option1;
		this.option2 = option2;
		
		wordIndex = new ArrayList<Integer>();
		whereOppositesAre = new ArrayList<Integer>();
	}
	
	
	public void setWordNum(int y) {
		wordIndex.add(y);
	}
	
	public void setOppositesLocation(ArrayList<Integer> z) {
		whereOppositesAre = z;
	}
	
	public ArrayList<Integer> getWordNum() {return wordIndex;}
	public ArrayList<Integer> getOppositesLocation() {return whereOppositesAre;}
	public String getOption1() {return option1;}
	public String getOption2() {return option2;}



}
