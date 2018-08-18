// WordValues.java
// Jeff Heskett
// CIS314 Advanced Java
// Assignment 4
// April 15 2018
//
// This class' purpose is to total the points a given word would give if it was
// placed on a Scrabble board without bonuses. Each letter has its own value
// defined in values.txt.
//
// When the program starts it should call the load() method to find and load
// values.txt into a HashMap of key,value pairs where each letter is the key
// and the value of the letter is the pair's value.
//
// If values.txt can't be found, a JFileChooser dialog will ask the user to
// find it.
//
// To load the letter values:    WordValues.load();
// To get the points for a word: int total = WordValues.getWordScore(word);

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;

public class WordValues {
   
   /* Properties */

   // each letter and their assigned value is stored in a key,value pair
   private static Map<Character, Integer> valueMap = new HashMap<Character, Integer>();

   /* Methods */
   
   // returns the total of the values from each letter in the given word
   public static int getWordScore(String word)
   {
      int total = 0; // accumulator for word total
      
      // go through each character in the string
      for (int i = 0; i < word.length(); i++)
      {
         Character letter = word.charAt(i);
         // if letter is in the valueMap, then add value to total
         if (valueMap.containsKey(letter))
            total += valueMap.get(letter);
      }
      
      return total; // return total points for the word
   }
   
   // loads values.txt if it can
   public static void load() throws FileNotFoundException
   {
      File file = getValuesFile();
      
      if (file == null) // if no file found/chosen, throw an exception
         throw new FileNotFoundException("Couldn't find values.txt");
      else // if file was found, load its contents into the value map
      {
         FileInputStream input = new FileInputStream(file);
         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         reader.lines().forEach(line -> {
            // each line is formatted as "A\t11": first char is letter, followed
            // by a tab and then an integer value
            Character letter = line.charAt(0); // grab letter to be key
            Integer value = Integer.valueOf(line.substring(2)); // int value
            valueMap.put(letter, value); // and save to map
         });
      }      
   }
   
   // this attempts to get a File handle to the values.txt file. if it's
   // not in the current working directory it will ask the user to find it.
   private static File getValuesFile()
   {
      // start by trying to open the file from current working directory
      File file = new File("values.txt");
      
      // if the file isn't found, have the user find it
      if (!file.exists())
      {
         // start JFileChooser at current working directory
         JFileChooser chooser = new JFileChooser(new File("."));
         chooser.setDialogTitle("Please choose a letter values file");
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
