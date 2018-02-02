package gui;
import java.io.File;

import core.Checker;
import core.Inconsistency;
import core.WordEngine;
import java.io.FileNotFoundException;
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
	}
	
	public boolean acceptFile(File f) {
	   String fileName = new String(f.getName());
	   String stringList[] = fileName.split("\\.");
	   ArrayList<String> acceptedExtention = new ArrayList<String>(Arrays.asList("doc", "docx", "odt", "pdf", "rtf", "tex", "txt", "wks", "wps", "wpd"));
	   if(acceptedExtention.contains(stringList[stringList.length - 1])) {
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
		Checker checkingEngine = new Checker();
		inconsistenciesFound = checkingEngine.checkText(operatingText);
		//checker fills a list with options
		//if list is empty display No Inconsistencies!
		//inconsistencyList.SetText(..)
		inconsistencyLabel.setVisible(true);
		//if list isn't empty
		//option1/2.setText(..)
		changeTo.setVisible(true);
		option1.setVisible(true);
		option2.setVisible(true);
		ignore.setVisible(true);
		check.setVisible(false);
		typeHere.setEditable(false);
	}
	
	public void advanceInconsistencyList() {	//assigned to Ignore button
		//advance Inconsistency list
		//display an All Done when list is at its end
	}
	
	public void pressOption(/*which option*/) {
		//get options somehow for reference
		//go into the array of arrays I've made for the document
		//change each tagged that is the other option
		//advance Inconsistency List
	}
}
