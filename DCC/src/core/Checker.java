package core;

import java.util.ArrayList;
import java.util.HashMap;

public class Checker {
	
	private String[] splitText;
	private WordEngine engine;
	private String text;
	Inconsistency possessive;
	Inconsistency[] justForPossessives = new Inconsistency[2];

	public Checker(String text) {	
		this.text = text;
		
		splitText = text.split(" ");
		engine = new WordEngine();
	}
	
	public ArrayList<Inconsistency> checkText() {
		ArrayList<Inconsistency> confirmed = new ArrayList<Inconsistency>();
		iterate();	
		confirmed = checkPotentials(confirmed);
		checkPossessives();
		return confirmed;
	}

	private void iterate() {
		for (int i = 0; i < splitText.length; i++) {
			String currentWord = splitText[i];
			currentWord = standardizeText(currentWord);
			currentWord = checkForPossesiveS(currentWord, i);
			checkForWords(currentWord, i);
		}
	}
	
	private String checkForPossesiveS(String currentWord, int i) {
		int len = currentWord.length();
		String withoutPossesive = currentWord;
		if (currentWord.length() >= 2) {
			if (currentWord.charAt(len - 2) == 's' & currentWord.charAt(len - 1) == '\''){ //s'
				Inconsistency found = new Inconsistency("s'", "s's");
				found.setWordNum(i);
				handlePossessiveDuplicates(found, 0);
				withoutPossesive = currentWord.substring(0, len - 2);
			} else if (currentWord.charAt(len - 2) == '\'' & currentWord.charAt(len - 1) == 's') {
				withoutPossesive = currentWord.substring(0, len - 2);
			}
		}
		if (currentWord.length() >= 3) {
			if (currentWord.charAt(len - 3) == 's' & currentWord.charAt(len - 2) == '\'' & currentWord.charAt(len - 1) == 's') { //s's
				Inconsistency found = new Inconsistency("s's", "s'");
				found.setWordNum(i);
				handlePossessiveDuplicates(found, 1);
				withoutPossesive = currentWord.substring(0, len - 3);
			} 
		}
		return withoutPossesive;
	}
	
	private void handlePossessiveDuplicates(Inconsistency p, int side) {
		if (justForPossessives[side] != null) {
			justForPossessives[side].setWordNum(p.getWordNum().get(0));
		} else {
			justForPossessives[side] = p;
		}
	}
	
	private void checkPossessives() {
		if (justForPossessives[0] != null & justForPossessives[1] != null) {
			justForPossessives[0].setOppositesLocation(justForPossessives[1].getWordNum());
			possessive = justForPossessives[0];
		}
	}

	private void checkForWords(String currentWord, int index) {
		if (isInLeftMap(currentWord)) {
			Inconsistency found = new Inconsistency(currentWord, engine.getLeftMap().get(currentWord));
			found.setWordNum(index);
			handleLeftDuplicates(currentWord, found, index);
		} else if (isInRightMap(currentWord)) {
			Inconsistency found = new Inconsistency(currentWord, engine.getRightMap().get(currentWord));
			found.setWordNum(index);
			handleRightDuplicates(currentWord, found, index);
		}
	}
	
	private void handleLeftDuplicates(String word, Inconsistency f, int index) {
		if(!engine.foundFromLeft.containsKey(word)) {
			engine.addToLeft(word, f);
		} else {
			engine.foundFromLeft.get(word).setWordNum(index);
		}
	}
	
	private void handleRightDuplicates(String word, Inconsistency f, int index) {
		if(!engine.foundFromRight.containsKey(word)) {
			engine.addToRight(word, f);
		} else {
			engine.foundFromRight.get(word).setWordNum(index);
		}
	}
	
	private boolean isInLeftMap(String string) {
		HashMap<String, String> temp = engine.getLeftMap();
		return temp.containsKey(string);
	}

	private boolean isInRightMap(String string) {
		return engine.getRightMap().containsKey(string);
	}

	private ArrayList<Inconsistency> checkPotentials(ArrayList<Inconsistency> confirmed) {
		HashMap<String, Inconsistency> leftPotentials = engine.getLeftPotentials();
		HashMap<String, Inconsistency> rightPotentials = engine.getRightPotentials();
		for (String each : leftPotentials.keySet()) {
			if (rightPotentials.containsKey(engine.leftWords.get(each))) {
				Inconsistency temp = leftPotentials.get(each);
				temp.setOppositesLocation(rightPotentials.get(temp.option2).wordIndex);
				confirmed.add(leftPotentials.get(each));
			}
		}
		return confirmed;
	}
	
	private String standardizeText(String word) {
		if (word.length() > 0) {
			word = removePunctuation(word);
			word = word.toLowerCase(); 
		}
		return word;
	}
	
	public String removePunctuation(String word) {
		boolean front = frontEndPunctuation(word);
		boolean back = backEndPunctuation(word);
		int len = word.length();
		if (len == 0 | (!front & !back)) {
			return word;
		}
		if (front) {
			word = word.substring(1, len - 1);
		}
		if (back) {
			word = word.substring(0, len - 1);
		}
		return removePunctuation(word);
	}
	
	private boolean frontEndPunctuation(String word) {
		char c = word.charAt(0);
		if (Character.isLetter(c)) {
			return false;
		} return true;
	}
	
	private boolean backEndPunctuation(String word) {
		char c = word.charAt(word.length() - 1);
		if (Character.isLetter(c) | c == '\'') {
			return false;
		} return true;
	}

	public void replaceTextwithList() {
		text = "";
		for (String each : splitText) {
			text += each + " ";
		}
	}
	
	
	public Inconsistency getPossessives() {return possessive;}
	
	public void setAndProcessWordInText(String word, String word2, int where) {
		String newWord = engine.insertWord(word, word2, splitText[where]);
		splitText[where] = newWord;
	}
	
	public String getWordInText(int where) {
		return splitText[where];
	}
	
	public String getText() {return text;}

	public void addToWord(int location) {
		splitText[location] += "s";
	}
	public void takeFromWord(int location) {
		String word = splitText[location];
		word = word.substring(0, word.length() - 1);
		splitText[location] = word;
	}
}
