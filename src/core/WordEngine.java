package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;

public class WordEngine {
	HashMap<String, String> leftWords;
	HashMap<String, String> rightWords;
	
	HashMap<String, Inconsistency> foundFromLeft;
	HashMap<String, Inconsistency> foundFromRight;

	public WordEngine() {
		leftWords = new HashMap<String, String>();
		rightWords = new HashMap<String, String>();
		
		foundFromLeft = new HashMap<String, Inconsistency>();
		foundFromRight = new HashMap<String, Inconsistency>();


		readInWords();
	}
	
	public void readInWords() {
		try {
			File file = new File(WordEngine.class.getResource("DoublySpelledWords").toURI());
			try {
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				String[] temp = s.nextLine().split(",");
				String left = temp[0].trim();
				String right = temp[1].trim();
				leftWords.put(left, right);
				rightWords.put(right, left);
			}
			s.close();
			} catch (FileNotFoundException e){
				System.out.println("It's literally right there how did you miss it: part 2");
			}
		}catch (URISyntaxException e) {
			System.out.println("It's literally right there how did you miss it: part 1");
		}
	}
	
	public void addToLeft(String foundWord, Inconsistency i) {
		foundFromLeft.put(foundWord, i);
	}
	
	public void addToRight(String foundWord, Inconsistency i) {
		foundFromRight.put(foundWord, i);
	}

	public HashMap<String, String> getLeftMap() {
		return leftWords;
	}

	public HashMap<String, String> getRightMap() {
		return rightWords;
	}
	
	public HashMap<String, Inconsistency> getLeftPotentials() {
		return foundFromLeft;
	}
	
	public HashMap<String, Inconsistency> getRightPotentials() {
		return foundFromRight;
	}
	
	public String insertWord(String inserting, String insertingsPair, String insertInto) {
		String substringFront = "";
		String substringBack = "";
		int count = 0;
		char c = insertInto.charAt(count);
		while (!Character.isLetter(c)) {
			substringFront += c;
			count++;
		}
		
		if (insertingsPair.length() != insertInto.length()) { //where insertInto >= insertingsPair
			substringBack = insertInto.substring(count + insertingsPair.length(), insertInto.length());
		}
		return substringFront + inserting + substringBack;
	}
}
