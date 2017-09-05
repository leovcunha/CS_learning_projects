
/**
 * Definitions:
 * 
 * Ukkonen rules according to : 
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * http://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/
 * 
 * Rule 1---
 * After an insertion from root, if active length > 0 
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
  char[][] wordPuzzle;
  int size;
  //
   
  class Node { 
    TreeMap<Character, Node> outEdges;
    Node suffixLink; //pointer from last new node to newly created node  
    int[] start, end; // each array guards {x, y} of the puzzle
    
    Node(int[] start, int[] end) {
       this.suffixLink = root;
       this.start = start;
       this.end = end;
       this.outEdges = new TreeMap<Character, Node>();
    }
   
    int edgeLength() {
      if (this.start[0] == -1) 
        return 0;
      return (Math.max(this.end[0] - this.start[0], this.end[1] - this.start[1]) +1);
    } 
  }
    
  /**
   * Constructor; create a SuffixTree for a Puzzle of Size N
   */
  public PuzzleSuffixTree (int N) {
    this.activeNode = this.root = new Node(new int[] {-1,-1}, new int[] {-1,-1});
    this.activeEdge = '\u0000';
    this.activeLength = 0;
    this.size = N;
    this.wordPuzzle = new char[size][size];
    
    
  }
  
  boolean walkDown(Node current) {
    if (activeLength >= current.edgeLength())
      return true;
    else 
      return false;
  
  }
  
  public void extendSuffixTree() {
    
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        currentPosition =  new int[] {i, j};
        remainder++;
        lastNewNode = null;
/*        
        if (activeLength == 0) {
          activeEdge = wordPuzzle[i][j];
        }
*/        
        while (remainder > 0) {
          if (!activeNode.outEdges.containsKey(wordPuzzle[i][j])) {
            activeNode.outEdges.put(wordPuzzle[i][j], new Node(currentPosition, new int[] {i, size})); //direction???
                                    
           /* 
            if (lastNewNode != null) {
              lastNewNode.suffixLink = activeNode;
              lastNewNode = null; 
            }
           */                         
          } else {
             
            if (activeLength == 0) {
              activeEdge = wordPuzzle[i][j];
              Node next = activeNode.outEdges.get(activeEdge);
              activeLength++;
              break;
              
            } else {
             
              if (wordPuzzle[next.start[0]+activeLength[0]][next.start[1]+activeLength[1]] == wordPuzzle[i][j]) { //direction?               
                activeLength++;
                break;
              }
              
              int[] splitEnd = new int[] {next.start[0] + activeLength[0] - 1, next.start[1] + activeLength[1] - 1};
              activeNode.outEdges.put(activeEdge, new Node(next.start, splitEnd));
              next.start + = activeLength;
              activeNode.outEdges.get(activeEdge).put(wordPuzzle[next.start[0]][next.start[1]], next);
              
              
            }
          }
          remainder--;
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