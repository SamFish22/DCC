package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordEngine {
	//StringTrieMap leftTrie = new StringTrieMap<>();
	//StringTrieMap rightTrie = new StringTrieMap<>();
	ArrayList<String> leftColumn = new ArrayList<String>();
	ArrayList<String> rightColumn = new ArrayList<String>();
	
	public void buildTrie() {
		try {
			InputStream in = WordEngine.class.getResourceAsStream("DoublySpelledWords");
			InputStreamReader scream = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(scream);
			String line = reader.readLine();
			while (line != null) {
				singleLine(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void singleLine(String text) {
		String[] bothWords = text.split(",");
		//leftTrie.put(bothWords[0].trim().toLowerCase());
		//rightTrie.put(bothWords[1].trim().toLowerCase());
		leftColumn.add(bothWords[0]);
		rightColumn.add(bothWords[1]);
	}
	
	public ArrayList<String> getLeftColumn() {
		return leftColumn;
	}
	public ArrayList<String> getRightColumn() {
		return rightColumn;
	}
}
