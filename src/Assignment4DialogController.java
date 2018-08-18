// Assignment4DialogController.java
// Jeff Heskett
// CIS314 Advanced Java
// Assignment 4
// April 15 2018
//
// The skeleton of this class was generated in SceneBuilder. It's the controller
// for Assignment4Dialog.fxml that describes a resizable window with controls
// to enter a series of letters and list all words that can be made from it.
//
// Aside from the UI, the bulk of this scene's work is the findWords() method
// that runs the entered text through a WordTree and forms a stream of strings
// that are filtered, sorted by value, formatted and displayed in the list as
// all possible words from the entered letters.

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Assignment4DialogController {

   // this is the main textfield where user enters letters to find words
   @FXML
   private TextField findTextField;

   // the Find button to the right of the textfield starts the search for words
   @FXML
   private Button findButton;

   // the Reset button to the right of the Find button will clear UI to blank state
   @FXML
   private Button resetButton;

   // this label sits between the textfield and listview to give status/feedback
   @FXML
   private Label resultsLabel;

   // this listview takes up the remainder (bottom) of window and will list all
   // words found from the letters entered in the textfield
   @FXML
   private ListView<String> resultsListView;
   
   // this list is the contents of resultsListView. an ObservableList will
   // update the UI when the list's content changes
   private ObservableList<String> resultsList = FXCollections.observableArrayList();
    
   // when the Find button is clicked, all words for the entered letters are
   // found and siplayed and the textfield prepared for a new set of letters
   @FXML
   void findButtonOnAction(ActionEvent event) {
      findWords();
      findTextField.setText("");
      findTextField.requestFocus();
   }    
   
   // when the Reset button is clicked, everything is cleared and the textfield
   // gains focus
   @FXML
   void resetButtonOnAction(ActionEvent event) {
      findTextField.setText(""); // empty textfield
      resultsList.clear(); // and list
      resultsLabel.setText("Words (and points) will list here:"); // restore label
      findTextField.requestFocus(); // and give textfield focus
   }
    
   // when Enter is hit while in the textField, find words as if Find clicked
   // and prepare for a new set of letters. (but only if Find button enabled)
   @FXML
   void findTextFieldOnAction(ActionEvent event) {
      if (!findButton.isDisabled())
      {
         findWords();
         findTextField.setText("");
      }
   }    
    
   @FXML
   void initialize() {
      assert findTextField != null : "fx:id=\"findTextField\" was not injected: check your FXML file 'ScrabbleWordsDialog.fxml'.";
      assert findButton != null : "fx:id=\"findButton\" was not injected: check your FXML file 'ScrabbleWordsDialog.fxml'.";
      assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'ScrabbleWordsDialog.fxml'.";
      assert resultsLabel != null : "fx:id=\"resultsLabel\" was not injected: check your FXML file 'ScrabbleWordsDialog.fxml'.";
      assert resultsListView != null : "fx:id=\"resultsListView\" was not injected: check your FXML file 'ScrabbleWordsDialog.fxml'.";

      // this is an anonymouse listener for the textfield to warn for long words
      // and to disable the Find button if the textfield is empty
      findTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
         
         // use resultsLabel to warn if 10+ characters are in the textfield
         if (newValue.length()>9) // if more than 9 characters are in the textfield
         {
            resultsLabel.setText("10+ characters can take a long time!");
            resultsLabel.setTextFill(Color.RED); // color label red to stand out
         }
         else if (oldValue.length()>9) // otherwise if old text > 9 chars, need to revert
         {
            resultsLabel.setText("Words (and points) will list here:");
            resultsLabel.setTextFill(Color.BLACK); // return label to black
         }
         
         // disable the Find button if there's no text in the textfield
         findButton.setDisable(newValue.length()==0);
                 
      });
      
      // this binds the observable list (resultsList) to the scrollview (resultsListView)
      resultsListView.setItems(resultsList);
      
      resultsList.add(String.format("%d words loaded from the dictionary", ScrabbleDictionary.getSize()));
      //System.out.printf("%d words loaded from dictionary.txt\n", ScrabbleDictionary.getSize());
   }
   
   // when letters are entered and/or the Find button clicked, this will create
   // a WordTree of the entered text and use a stream to update the ListView
   // with all words (and points) that can be made from those letters
   private void findWords()
   {
      // get the initial letters from the textField
      String text = findTextField.getText().toUpperCase();
      
      // empty list before adding found words
      resultsList.clear();
      
      // create a WordTree from the text
      WordTree tree = new WordTree(text);
      
      // the following stream/lambda expression will create a stream of words
      // from the WordTree, filter out invalid words, sort alphabetically, sort
      // by word values(1), format the words for display, and then add the
      // resulting stream to the resultsList to update the ListView(2)
      tree.getAllWords().stream() // create stream from all words in the WordTree
                        .filter(word -> ScrabbleDictionary.isWordValid(word)) // filter only valid words
                        .sorted() // sort alphabetically first (so words of same value sort alphabetically)
                        .sorted((word1, word2) -> WordValues.getWordScore(word2)-WordValues.getWordScore(word1)) // sort by word values foremost
                        .map(word -> { return String.format("%s (%d points)", word, WordValues.getWordScore(word)); }) // format for display
                        .forEach(resultsList::add); // and add to resultsList
      
      // (1) sorted() appears to be a stable sort for ordered streams; sorting
      // by alpha first and value second will make the primary sort the value
      // of each word and the secondary sort will be alphabetical
      // (2) because resultsList is an obervableList bound to resultsListView's
      // items, adding items to resultsList will also automatically update the
      // ListView's contents, so no further action is needed
      
      // update the label above the list to show how many words were found
      resultsLabel.setText(String.format("Found %d words for %s:", resultsList.size(), text));
   }
   
}
