
/**
 * definitions:
 * 
 * Ukkonen rules according to : 
 * 
 * Rule 1---
 * After an insertion from root,
 * active_node remains root
 * active_edge is set to the first character of the new suffix we need to insert, i.e. b
 * active_length is reduced by 1
 * 
 *  Rule 2:--
 * If we split an edge and insert a new node, and if that is not the first node created during the current step, 
 * we connect the previously inserted node and the new node through a special pointer, a suffix link. We will 
 * later see why that is useful. Here is what we get, the suffix link is represented as a dotted edge:
 * 
 * Rule 3:
 * After splitting an edge from an active_node that is not the root node, we follow the suffix link going out of 
 * that node, if there is any, and reset theactive_node to the node it points to. If there is no suffix link, 
 * we set theactive_node to the root. active_edgeand active_length remain unchanged.
 */

import java.util.TreeMap;

public class PuzzleSuffixTree {
  
  Node root;  // Root of the SuffixTree
  Node lastNewNode; // for rule 2

  //active point:
  Node activeNode;
  char activeEdge;
  int activeLength;
  
  //remainder suffixes to add
  int remainder;
  int[] currentPosition; 
  //input
  String[][] wordPuzzle;
  int size;
  //
   
  class Node { 
    TreeMap<Character, Node> children;
    Node suffixLink;  
    int[] start, end; // each array guards {x, y} of the puzzle
    
    Node(int[] start, int[] end) {
       this.suffixLink = root;
       this.start = start;
       this.end = end;
       this.children = new TreeMap<Character, Node>();
    }
    
    int edgeLength() {
      return (Math.max(this.end[0] - this.start[0], this.end[1] - this.start[1]) +1);
    }
   
  }
    
  /**
   * Constructor; create a SuffixTree for a Puzzle of Size N
   */
  public PuzzleSuffixTree (int N) {
    this.activeNode = this.root = new Node(new int[] {-1,-1}, new int[] {-1,-1});
    this.size = N;
    this.wordPuzzle = new String[size][size];
    
    
  }
  
  Node walkDown(Node current) {
    if (activeLength >= current.edgeLength()) {
      activeEdge += current.edgeLength()
    }
  
  }
  
  public void processSuffixTree() {
    
    for (i = 0; i < size; i++) {
      for (j = 0; j < size; j++) {
        currentPosition = {i, j};
        remainder++;
        lastNewNode = null;
        
        while (remainder > 0) {
          
          
        }     
      }
    }    
  }
    
    
    
    
 /**
  * search all occurrences of a query string.
  * @return an array of integer indices showing all the places in
  *  the original string where the query string starts; note that
  *  the size of the array matches the number of occurrences
  */
  public int[] search (String query) {
   throw new RuntimeException("not yet implemented"); 
  }
  
 /* Other Methods */
 
 /**
  * Shows all suffixes stored in the tree in sorted (alphabetical) order.
  * If the SuffixTree is built correctly this is easy to do recursively.
  * @return a String representation of the SuffixTree.
  */
 @Override
 public String toString () {
  // Note: "\n" is treated as end-of-line when used within a String.
  //return "SuffixTree for " + string + ":" + toString("\n ",root);
   return "";
 }
 
 
 public static void main (String[] args) {

 }
}