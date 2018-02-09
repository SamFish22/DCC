package gui;
import java.io.File;

import core.Checker;
import core.Inconsistency;
import core.WordEngine;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;

/* Checker will go through the text in the Text Area and put it in an array of arrays, checking each word against the Trie
 * as it goes. If it finds a word that is in the Trie, it logs it's place, which word, and which Trie in a new Tags 
 * data structure. Later, that will make it easier to edit the document*/
public class GUIController {
	@FXML
	Button loadFile, write, check, option1, option2, ignore;

	@FXML
	TextArea typeHere;
	
	@FXML
	Label inconsistencyLabel, changeTo;
	
	@FXML
	MenuButton file;
	
	ArrayList<Inconsistency> inconsistenciesFound;
	
	String currentFilePath = null;
	
	Checker checkingEngine;
	
	boolean possessiveTurn = false;

	
	public void initilize() {
        option1.setVisible(false);
        option2.setVisible(false);
        ignore.setVisible(false);
        typeHere.setVisible(false);
        inconsistencyLabel.setVisible(false);
        changeTo.setVisible(false);
        file.setVisible(false);
		check.setVisible(false);
		typeHere.setWrapText(true);
	}
	
	public void pressLoadFileButton() throws FileNotFoundException {
		//Oracle documentation
		JFileChooser chooser= new JFileChooser();
		int choice = chooser.showOpenDialog(null);
		File chosenFile = chooser.getSelectedFile();
		if (chosenFile == null) {return;}
		if (!acceptFile(chosenFile)) {									
			pressLoadFileButton();
		}
		Scanner s = new Scanner(chosenFile);
		String wholeFile = s.useDelimiter("\\A").next();
		s.close();
		typeHere.setText(wholeFile);
		loadSecondScreen();
		
		currentFilePath = chooser.getSelectedFile().getAbsolutePath();

	}
	
	public boolean acceptFile(File f) {
	   String fileName = new String(f.getName());
	   String stringList[] = fileName.split("\\.");
	   if(stringList[stringList.length - 1].equals("txt")) {
	    	return true;
	    } return false;
	}
	
	public void loadSecondScreen() {
		loadFile.setVisible(false);
		write.setVisible(false);
		file.setVisible(true);
		check.setVisible(true);
        typeHere.setVisible(true);
		inconsistencyLabel.setVisible(false);
		changeTo.setVisible(false);
		option1.setVisible(false);
		option2.setVisible(false);
		ignore.setVisible(false);
	}
	
	public void pressCheckConsistency() {
		String operatingText = typeHere.getText();
		checkingEngine = new Checker(operatingText);
		inconsistenciesFound = checkingEngine.checkText();
		
		putPossessivesFirst(checkingEngine);

		if(inconsistenciesFound.isEmpty()) {
			inconsistencyLabel.setText("No Inconsistencies!");
			inconsistencyLabel.setVisible(true);
		} else {
			inconsistencyLabel.setVisible(true);
			goThroughInconsistencies();
			loadScreenThree();
		}
	}
	
	public void putPossessivesFirst(Checker checking) {
		Inconsistency possessive = checking.getPossessives();
		if (possessive != null) {
			inconsistenciesFound.add(0, possessive);
			possessiveTurn = true;
		}
	}
	
	private void goThroughInconsistencies() {
		if(inconsistenciesFound.isEmpty()) {
			inconsistencyLabel.setText("No More Inconsistencies!");
			checkingEngine.replaceTextwithList();
			typeHere.setText(checkingEngine.getText());
			option1.setVisible(false);
			option2.setVisible(false);
			ignore.setVisible(false);
			changeTo.setVisible(false);
		} else {
			Inconsistency current = inconsistenciesFound.get(0);
			inconsistencyLabel.setText("Inconsistency: " + current.getOption1() + " vs. " + current.getOption2());
			option1.setText(current.getOption1());
			option2.setText(current.getOption2());
		}
	}

	public void loadScreenThree() {
		changeTo.setVisible(true);
		option1.setVisible(true);
		option2.setVisible(true);
		ignore.setVisible(true);
		check.setVisible(false);
		typeHere.setEditable(false);
	}
	
	public void advanceInconsistencyList() {	//assigned to Ignore button
		inconsistenciesFound.remove(0);
		possessiveTurn = false;

		checkingEngine.replaceTextwithList();
		typeHere.setText(checkingEngine.getText());
		goThroughInconsistencies();
	}
	
	public void pressOption1() {
		Inconsistency workingOn = inconsistenciesFound.get(0);
		ArrayList<Integer> replaceLocations = workingOn.getOppositesLocation();
		for (int each : replaceLocations) {	
			if (possessiveTurn) {
				checkingEngine.takeFromWord(each);
			} else {
				checkingEngine.setAndProcessWordInText(workingOn.getOption1(), workingOn.getOption2(), each);
			}
		}
		advanceInconsistencyList();
	}
	
	public void pressOption2() {
		Inconsistency workingOn = inconsistenciesFound.get(0);
		ArrayList<Integer> replaceLocations = workingOn.getWordNum();
		for (int each : replaceLocations) {
			if (possessiveTurn) {
				checkingEngine.addToWord(each);
			} else {
				checkingEngine.setAndProcessWordInText(workingOn.getOption2(), workingOn.getOption1(), each);
			}
		}
		advanceInconsistencyList();
	}
	
	public void saveAs() {
		JFileChooser chooser= new JFileChooser();
		int choice = chooser.showSaveDialog(null);
		if(choice == JFileChooser.APPROVE_OPTION) {
			try(FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt")) {
			    fw.write(typeHere.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		currentFilePath = chooser.getSelectedFile().getAbsolutePath();
	}
	
	public void save() {
		if (currentFilePath != null) {
			try {
				Writer fileWriter = new FileWriter(currentFilePath);
				fileWriter.write(typeHere.getText());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
