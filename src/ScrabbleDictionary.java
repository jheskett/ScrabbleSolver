// ScrabbleDictionary.java
// Jeff Heskett
// CIS314 Advanced Java
// Assignment 4
// April 15 2018
//
// This class' purpose is to verify that a given word exists in a Scrabble
// dictionary (dictionary.txt).
//
// When the program starts it should call the load() method to find and load
// dictionary.txt into a Set. This file is about 180k words, and it takes up
// ~20MB in memory as a HashSet, but this is a small price for the massive speed
// gains of knowing if a word exists.
//
// If dictionary.txt can't be found, a JFileChooser dialog will ask the user to
// find it.
//
// To load the dictionary: ScrabbleDictionary.load();
// To verify a word:       Boolean valid = ScrabbleDictionary.isWordValid(word);
// To get number of words: int numWords = ScrabbleDictionary.getSize();

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;

public class ScrabbleDictionary {
   
   /* Properties */
   
   // all words from dictionary.txt will be loaded into this Set when the program
   // starts and referenced whenever words need checked.
   private static Set<String> words = new HashSet<String>();
   
   /* Methods */
   
   // returns true if the given word is in the dictionary
   public static boolean isWordValid(String word)
   {
      return words.contains(word);
   }

   // returns the number of words in the dictionary
   public static int getSize()
   {
      return words.size();
   }
   
   // this attempts to load dictionary.txt file into the words set
   public static void load() throws FileNotFoundException
   {
      File file = getDictionaryFile();
      
      if (file == null) // if no file found/chosen, throw an exception
         throw new FileNotFoundException("Couldn't find dictionary.txt");
      else // if file was found, load its contents into the word set
      {
         FileInputStream input = new FileInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         reader.lines().forEach(word -> { words.add(word); });
      }
   }

   // this attempts to get a File handle to the dictionary.txt file. if it's
   // not in the current working directory it will ask the user to find it.
   private static File getDictionaryFile()
   {
      // start by trying to open the file from current working directory
      File file = new File("dictionary.txt");
      
      // if the file isn't found, have the user find it
      if (!file.exists())
      {
         // start JFileChooser at current working directory
         JFileChooser chooser = new JFileChooser(new File("."));
         chooser.setDialogTitle("Please choose a dictionary file");
         chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         // show the dialog
         int success = chooser.showOpenDialog(null);
         
         // APPROVE_OPTION is when a file is chosen from the dialog
         if (success == JFileChooser.APPROVE_OPTION)
         {
            // change chosen file to this user-chosen file
            file = chooser.getSelectedFile();
         }
         else // no file chosen (user cancelled dialog or file can't be opened)
         {
            return null;
         }
      }
      
      return file; // if we reached here, a file was found
   }
   
}
