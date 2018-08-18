// WordTree.java
// Jeff Heskett
// CIS314 Advanced Java
// Assignment 4
// April 15 2018
//
// This class defines a WordTree to discover all combination of letters possible
// from a string.
//
// When a WordTree is given a word in its constructor, it will create a new
// WordTree for each letter in the word with that same word except its letter
// removed. ("CAT" makes "AT" "CT" and "CA" WordTrees). Each child will
// recursively repeat the process until all letters are used up. A WordTree
// for "CAT" would look like:
//
//                            (root node)
//                          /      |      \
//                         C       A       T
//                        / \     / \     / \
//                       A   T   C   T   C   A
//                       |   |   |   |   |   |
//                       T   A   T   C   A   C
//
// The end result is a tree of all possible combination of characters from the
// initial word.
//
// Letter combinations are formed by walking through each node and concatenating
// the letters from the node, up parents and eventually to root. CAT would form:
// C, CA, CAT, CT, CTA, A, AC, ACT, AT, ATC, T, TC, TCA, TA, and TAC.
//
// To create a tree:               WordTree tree = new WordTree("cat");
// To get all words in the tree:   List<String> list = tree.getAllWords();
// To print the words in a tree:   System.out.println(tree);
//
// Notes:
// - All words/characters returned are in UPPERCASE (regardless of original case)
// - WordTree is not responsible for knowing if a word is a real word
// - It's also not responsible for any ordering (returned lists are unsorted)
// - Words returned are unique: helLo and heLlo will be in the list just once

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordTree
{
   /* Properties */

   // the letter ('C' 'A' or 'T') of the word stored in this node
   char letter;
   // the parent node is saved so we can walk up to get letter combinations
   WordTree parent;
   // List of child nodes for each letter remaining in the word
   List<WordTree> children = new ArrayList<WordTree>();

   /* Constructors */

   // main constructor recursively breaks down the given word into child nodes
   // and stops when the words can't be broken down any more
   public WordTree(String word)
   {
      word = word.toUpperCase(); // raise word to upper case if not already

      // for each letter in the word
      for (int i = 0; i < word.length(); i++)
      {
         // note the character we're going to drop at index i
         char droppedLetter = word.charAt(i);
         // form a new word with that character dropped/removed
         String remainder = getWordWithIndexDropped(word, i);
         // then recursively create a child WordTree from the remaining word
         children.add(new WordTree(remainder, droppedLetter, this));
      }
   }

   // overloaded constructor also takes the dropped letter to save in the tree
   // (this constructor is private: should only be called from WordTree(word))
   // the extra parameters letter and parent are stored in the node for later use
   private WordTree(String word, char letter, WordTree parent)
   {
      this(word); // call original constructor with reduced word to break apart
      this.letter = letter; // save the dropped letter in this node
      this.parent = parent; // and the parent node (so we can walk upwards)
   }

   // default constructor with no arguments will throw an exception: every created
   // WordTree must be given a word to break apart in the constructor call
   public WordTree()
   {
      throw new IllegalArgumentException("WordTree constructor requires a word: WordTree(\"cat\")");
   }

   /* Methods */

   // given a word, drop the character at index and return the remainder
   // (used by constructor to form diminished words)
   private static String getWordWithIndexDropped(String word, int index)
   {
      String remainder = word.substring(0, index); // get all characters just before index

      if (index < word.length()-1) // if character isn't the last letter of word
         remainder += word.substring(index + 1); // then add the characters after index
      
      return remainder; // return word minus the character at index
   }

   // public getAllWords() returns all letter combinations from this WordTree
   // and its descendants in the form of a List<String>
   public List<String> getAllWords()
   {
      // the recursive getChildWords needs a shared collection to store words.
      // a Set is used instead of List because an 8-letter word has 109,600
      // combinations of letters--but not unique (helLo vs heLlo). pulling out
      // uniques with list.contains() takes about 77 seconds O(N^2) whereas
      // throwing them all into a hash-based Set takes less than a second (O(N))
      Set<String> set = new HashSet<String>();
      // gather words in all children and descendants into the temporary set
      getChildWords(set);

      // now create an ordered List of the words to send back to the caller
      // (the caller is responsible for sorting this; it will be unsorted)
      List<String> list = new ArrayList<String>();
      // add all words from the set (they will be unique because it's a set)
      list.addAll(set);

      return list; // and return list of all gathered unique words to caller
   }

   // this recursive function will add the node's word (formed from the root
   // node down to this nodes) to the given set and repeat for all of its
   // children. it will stop when it reaches a node with no children.
   private void getChildWords(Set<String> set)
   {
      // for each WorldTree in the List of children
      children.forEach(node -> {
         String word = node.getWord(); // get the word at this child node
         set.add(word); // save it (if already in set it's ok; it's a set)
         node.getChildWords(set); // repeat for all children of this node
      });
   }

   // this returns the concatenated letters that lead down to this node.
   // it will start with the letter of the instance's node and keep appending
   // the letter of its parent node (backwards so parent letters appear before
   // child letters) until it's reached the root node.
   private String getWord()
   {
      WordTree node = this; // starting point is this node
      String word = ""; // building word onto this string

      // while the node has a parent (it's not the root node)
      while (node.parent != null)
      {
         word = node.letter + word; // insert node's letter to start of word
         node = node.parent; // and move up to parent node to repeat
      }
      
      return word; // return word formed by walking up tree from this node
   }

   // toString returns a String of all words in the tree separated by commas
   @Override
   public String toString()
   {
      List<String> list = getAllWords(); // gather all words in this WordTree
      return String.join(", ", list); // and return them as a String
   }

}