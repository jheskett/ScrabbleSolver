// Assignment4.java
// Jeff Heskett
// CIS314 Advanced Java
// Assignment 4
// April 15 2018
//
// This is the main application for a GUI to find Scrabble words.
//
// When the program launches, it will load a Scrabble dictionary (dictionary.txt)
// and a table of letters and their values (values.txt) into memory. If either
// file is not found it will ask the user to find them with a JFileChooser.
// Once the files are loaded it will display a single dialog where the user
// can enter letters from their Scrabble rack. Hitting Enter or clicking Find
// will list all possible words from those letters and their point values.
//
// Note: For 10 letters there's nearly a million combination of letters, which
// can take some time to calculate (1-2 mins). 9 letters only pauses for a moment
// and 8 and less letters are practically instant. (The UI will warn when you've
// entered 10 or more letters to find words from.)

import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Assignment4 extends Application {
   
   @Override
   public void start(Stage stage) throws Exception {
      
      // before we setup the main dialog, load dictionary.txt and values.txt

      // unlike the previous assignment that made the file optional, i'm choosing
      // to not make these optional in this assignment. if we can't load either
      // dictionary.txt or values.txt, even with the help of a JFileChooser, a
      // popup will inform the user that the file is required and exit.
      
      try // try loading dictionary.txt first
      {
         ScrabbleDictionary.load(); // load dictionary.txt
      }
      catch (FileNotFoundException e)
      {
         JOptionPane.showMessageDialog(null, "The required file dictionary.txt could not be loaded. Exiting, sorry!");
         System.exit(1); // exit with an error
      }
      
      try // try loading values.txt next
      {
         WordValues.load(); // load values.txt
      }
      catch (FileNotFoundException e)
      {
         JOptionPane.showMessageDialog(null, "The required file values.txt could not be loaded. Exiting, sorry!");
         System.exit(1); // exit with an error
      }
      
      // waiting until now to create dialog so the number of words in the
      // dictionary are known in the scene controller's initialization

      Parent root = FXMLLoader.load(getClass().getResource("Assignment4Dialog.fxml"));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setTitle("Scrabble Word Finder");
      stage.setMinWidth(300); // setting the stage min size as opposed to scene
      stage.setMinHeight(250);
      stage.show();
   }

   // entry point of the program
   public static void main(String[] args) {
      launch(args);
   }
   
}
