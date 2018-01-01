package fxcapitalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Button capitalizeButton;
    @FXML
    private RadioButton stylizeRadioButton, firstLetterRadioButton,
	    allLowerRadioButton, allUpperRadioButton;
    @FXML
    private TextField inputTextField, outputTextField;

    private List<String> wordList;
    private final String fileName = "wordsEN.txt";

    @FXML
    private void handleButtonAction(ActionEvent event) {
	if (stylizeRadioButton.isSelected()) {
	    stylize();
	} else if (firstLetterRadioButton.isSelected()) {
	    firstLetter();
	} else if (allLowerRadioButton.isSelected()) {
	    outputTextField.setText(inputTextField.getText().toLowerCase());
	} else {
	    outputTextField.setText(inputTextField.getText().toUpperCase());
	}
    }
    
    private void stylize() {
	String text = inputTextField.getText();
	String[] items = text.split(" ");
	for (int i = 0, n = items.length; i < n; i++) {
	    if (!items[i].equals("")) {
		String wordLower = items[i].toLowerCase();
		if (wordLower.contains("-")) {
		    String[] subItems = wordLower.split("-");
		    for (int j = 0; j < subItems.length; j++) {
			if ((wordList.indexOf(subItems[j]) == - 1
				|| (i == 0 && j == 0))
				|| (i != 0 && j == 0 && items[i - 1].endsWith(":"))) {
			    subItems[j] = subItems[j].substring(0, 1).toUpperCase()
				    + subItems[j].substring(1);
			}
			items[i] = String.join("-", subItems);
		    }
		} else if ((wordList.indexOf(wordLower) == - 1 || i == 0)
			|| (i != 0 && items[i - 1].endsWith(":"))) {
		    items[i] = wordLower.substring(0, 1).toUpperCase()
			    + wordLower.substring(1);
		}
	    }
	}
	outputTextField.setText(String.join(" ", items));
	outputTextField.requestFocus();
    }

    private void firstLetter() {
	String text = inputTextField.getText().toLowerCase();
	text = text.substring(0, 1).toUpperCase() + text.substring(1);
	outputTextField.setText(text);
    }
    
    private void loadWords() throws IOException {
	String filePath = System.getProperty("user.dir") + File.separator
		+ fileName;
	FileReader fr = new FileReader(filePath);
	BufferedReader br = new BufferedReader(fr);
	wordList = new ArrayList<>();
	String word;
	while ((word = br.readLine()) != null) {
	    wordList.add(word);
	}
	br.close();
	fr.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	try {
	    loadWords();
	} catch (IOException ex) {
	    Logger.getLogger(FXMLDocumentController.class.getName())
		    .log(Level.SEVERE, null, ex);
	}
	ToggleGroup group = new ToggleGroup();
	stylizeRadioButton.setToggleGroup(group);
	firstLetterRadioButton.setToggleGroup(group);
	allLowerRadioButton.setToggleGroup(group);
	allUpperRadioButton.setToggleGroup(group);
	Platform.runLater(inputTextField::requestFocus);
	inputTextField.textProperty().addListener(
		(ObservableValue<? extends String> observableValue,
			String s1, String s2) -> {
		    capitalizeButton.setDisable(s2.trim().length() == 0);
		});
    }

}