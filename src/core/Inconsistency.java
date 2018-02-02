package core;

public class Inconsistency {
	String option1, option2;
	int paragraph, wordNum, index;
	
	public Inconsistency(String option1, String option2) {
		this.option1 = option1;
		this.option2 = option2;
	}
	
	public void setParagraph(int x) {
		paragraph = x;
	}
	
	public void setWordNum(int y) {
		wordNum = y;
	}
	
	public void setIndex(int z) {
		index = z;
	}
	
	public int getParagraph() {return paragraph;}
	public int getWordNum() {return wordNum;}
	public int getIndex() {return index;}

}
