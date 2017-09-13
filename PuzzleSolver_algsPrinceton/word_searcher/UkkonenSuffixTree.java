/**
 * Definitions:
 * 
 * Ukkonen rules according to : 
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * http://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/
 * 
 * Rule 1---
 * After an insertion from root, if active length > 0 
 * active_node�remains root
 * active_edge�is set to the first character of the new suffix we need to insert, (if abx i.e.�b)
 * active_length�is reduced by 1
 * 
 * �Rule 2:--
 * If we split an edge and insert a new node, and if that is�not the first node created during the current step, 
 * we connect the previously inserted node and the new node through a special pointer, a�suffix link. We will 
 * later see why that xxis useful. Here is what we get, the suffix link is represented as a dotted edge:
 * 
 * Rule 3:
 * After splitting an edge from an active_node�that is not the root node, we follow the suffix link going out of 
 * that node, if there is any, and reset theactive_node�to the node it points to. If there is no suffix link, 
 * we set theactive_node�to the root.�active_edgeand�active_length�remain unchanged.
 */
package word_searcher;

import java.util.*;

public class UkkonenSuffixTree {
  
  Node root;  // Root of the SuffixTree
  Node lastNewNode; // for rule 2

  //active point:
  Node activeNode;
  char activeEdge;
  int activeLength;
  
  //remainder suffixes to add
  int remainder;
  //input
  String text;
  int size;
  //
   
  class Node { 
    TreeMap<Character, Node> outEdges;
    Node suffixLink; //pointer from last new node to newly created node  
    int start, end; // each array guards {x, y} of the puzzle
    
    Node(int start, int end) {
       this.suffixLink = root;
       this.start = start;
       this.end = end;
       this.outEdges = new TreeMap<Character, Node>();
    }
   
    int edgeLength() {
      if (start == -1) 
        return 0;
      return (end - start);
    }
    
    @Override
    public String toString() {
      if (start == -1)
        return "root";
      return text.substring(start,end);    
    }
  }
     
  /**
   * Constructor; create a SuffixTree for a Puzzle of Size N
   */
  public UkkonenSuffixTree (String text) {
    this.root = new Node(-1, -1);
    this.activeNode = root;
    this.activeEdge = '\u0000';
    this.activeLength = 0;
    this.size = text.length();
    this.text = text;    
  }
  
  boolean walkDown(Node current) {
    if (current == null) return false;
    if (activeLength >= current.edgeLength())
      return true;
    else 
      return false;
  
  }
  
  public void buildSuffixTree() {
    Node next = null;
    Node splitNode;
    
    for (int i = 0; i < size; i++) {
    	    System.out.println("iteration" + i+ ":");
        remainder++;
        lastNewNode = null;

        while (remainder > 0) {
         
          if (walkDown(next)) {
                activeNode = next;
                activeEdge = text.charAt(i-activeLength);
                activeLength -= next.edgeLength();                                
                next = activeNode.outEdges.get(activeEdge);
                System.out.println("active point: " + activeNode + " active edge: " + activeEdge + " length: " + activeLength);
          }
                              
          if (activeLength == 0 && !activeNode.outEdges.containsKey(text.charAt(i))) {
            activeNode.outEdges.put(text.charAt(i), new Node(i, size)); //direction???
            System.out.println("inserted suffix " + activeNode.outEdges.get(text.charAt(i)));
            
            /*if (lastNewNode != null) {
              lastNewNode.suffixLink = activeNode.outEdges.get(text.charAt(i));
              System.out.println("suffix link from " +  lastNewNode + " to " + lastNewNode.suffixLink);
              lastNewNode = null; 
              
            }*/
                                   
          } else {
                       
            if (activeLength == 0) {
              activeEdge = text.charAt(i);
              next = activeNode.outEdges.get(activeEdge); // what about remainder still > 0 ?
              activeLength++;
              System.out.println("new active edge: " + activeEdge + " length: " + activeLength);
              break;              
            } 

            
            if (text.charAt(next.start+activeLength) == text.charAt(i)) { //direction?               
                activeLength++;
                System.out.println("active Length = " + activeLength);
                break;
            }
              
            int splitEnd = next.start + activeLength;
            
            splitNode = new Node(next.start, splitEnd);
            System.out.print("edge splited at new node:" + splitNode.start + " " );
            System.out.println(splitNode.end);
            splitNode.outEdges.put(text.charAt(i), new Node(i, size));           
            next.start += activeLength;
            splitNode.outEdges.put(text.charAt(next.start), next); 
            activeNode.outEdges.put(activeEdge, splitNode);
            System.out.println("new edge splitted :" + activeNode.outEdges.get(activeEdge).outEdges.get(text.charAt(i)));
            System.out.println("new edge splitted :" + activeNode.outEdges.get(activeEdge).outEdges.get(text.charAt(next.start)));

           
            if (lastNewNode != null) {
              lastNewNode.suffixLink = splitNode;
              System.out.println("suffix link from " +  lastNewNode + " to " + splitNode);
            }
              
              lastNewNode = splitNode;  
              System.out.println("last new node: " + splitNode);
            }
          
          remainder--;
          
          if (activeNode.start == -1 && remainder > 0) {
            activeLength--;
            activeEdge = text.charAt(i - activeLength);
            next = activeNode.outEdges.get(activeEdge); // what about remainder still > 0 ?
            System.out.println("new active edge: " + activeEdge + " length: " + activeLength);
          }
          else if (activeNode.start != -1) {
            activeNode = activeNode.suffixLink;
            next = activeNode.outEdges.get(activeEdge);
            System.out.println("jumped to suffix link " + activeNode);
          }
          
          System.out.println("active point: " + activeNode + " active edge: " + activeEdge + " length: " + activeLength + " remainder: " + remainder);
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
 
 public void display() {
   /* base case: tree end node.end = size;
    */
  
  
 }
  
 public static void main(String[] args) {
   UkkonenSuffixTree st = new UkkonenSuffixTree("abcabxabcd");
   st.buildSuffixTree();
  // st.display();
   System.out.println(st.root.toString());
   System.out.println(st.root.outEdges.keySet());
   System.out.println(st.root.outEdges.get('b').toString());
        
 } 

}