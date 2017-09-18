/**
 * Ukkonen rules according to : 
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * http://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/
 * 
 * General Definitions:
 * If the path from the root labelled S[j..i] ends at leaf edge (i.e. S[i] is last character on leaf edge) then 
 * character S[i+1] is just added to the end of the label on that leaf edge.
 * 
 * If the path from the root labelled S[j..i] ends at non-leaf edge (i.e. there are more characters after S[i] 
 * on path) and next character is not s[i+1], then a new leaf edge with label s{i+1] and number j is created
 * starting from character S[i+1].
 * A new internal node will also be created if s[1..i] ends inside (in-between) a non-leaf edge. 
 * 
 * If the path from the root labelled S[j..i] ends at non-leaf edge (i.e. there are more characters after
 * S[i] on path) and next character is s[i+1] (already in tree), update active point ( active node, edge, length)
 * 
 * Rule 1---
 * After an insertion from root, if active length > 0 
 * active_node� remains root
 * active_edge is set to the first character of the new suffix we need to insert, (if abx i.e.�b)
 * active_length� is reduced by 1
 * 
 * Rule 2:--
 * If we split an edge and insert a new node, and if that is�not the first node created during the current step, 
 * we connect the previously inserted node and the new node through a special pointer, a� suffix link. We will 
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
   * Constructor; create a SuffixTree for a given string
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
  /**
   * Builds Ukkonen Suffix Tree according to specification
   * @return Suffix tree of the text given to the class at object creation
   */
  public void buildSuffixTree() {
    
    Node next = null;
    Node splitNode;
    
    for (int i = 0; i < size; i++) {

        remainder++;
        lastNewNode = null;

        while (remainder > 0) {
         
          if (walkDown(next)) {
                activeNode = next;
                activeEdge = '\u0000';
                activeLength = 0;                                
                
          }
                              
          if (activeLength == 0 && !activeNode.outEdges.containsKey(text.charAt(i))) {
            activeNode.outEdges.put(text.charAt(i), new Node(i, size)); 

                                              
          } else {
                       
            if (activeLength == 0) {
              activeEdge = text.charAt(i);
              next = activeNode.outEdges.get(activeEdge); 
              activeLength++;
              break;              
            } 
            
            if (text.charAt(next.start+activeLength) == text.charAt(i)) {              
                activeLength++;
                break;
            }
              
            int splitEnd = next.start + activeLength;
            
            splitNode = new Node(next.start, splitEnd);
            splitNode.outEdges.put(text.charAt(i), new Node(i, size));           
            next.start += activeLength;
            splitNode.outEdges.put(text.charAt(next.start), next); 
            activeNode.outEdges.put(activeEdge, splitNode);
           
            if (lastNewNode != null) {
              lastNewNode.suffixLink = splitNode;
            }
              
              lastNewNode = splitNode;  
            }
          
          remainder--;
          
          if (activeNode.start == -1 && activeLength > 0) {
            activeLength--;
            activeEdge = text.charAt(i - activeLength);
            next = activeNode.outEdges.get(activeEdge); 

          }
          else if (activeNode.start != -1) {
            activeNode = activeNode.suffixLink;
            next = activeNode.outEdges.get(activeEdge);
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

    activeNode = root;
    activeLength = 0;
    Node next;
    int begin;
    int fin;
    
    if (activeNode.outEdges.containsKey(query.charAt(0))) {
        activeEdge = query.charAt(0);
        next = activeNode.outEdges.get(activeEdge);
        begin = next.start;
        activeLength++;

        
    }
    else return new int[]{-1,-1};
    
    for (int i = 1; i < query.length(); i++) {
       
       if (walkDown(next)) {
           activeNode = next;
           activeEdge = query.charAt(i);
           activeLength = 0;                                
           next = activeNode.outEdges.get(activeEdge);
       }
     
       if (text.charAt(next.start+activeLength) == query.charAt(i)) {      
           activeLength++;
       }
       else
    	   	   return new int[]{-1,-1};
    }
    
    fin = next.start + activeLength;
    begin = fin - query.length();
    
    return new int[] {begin, fin};
  }

  public void display(Node n) {
    
   //base case n.outEdges.isEmpty()
   System.out.println(" [" + n.start + ", "+ n.end + "]");
   
   if (!n.outEdges.isEmpty()) {
     System.out.println("out edges: " + n.outEdges);
     for ( char c: n.outEdges.keySet()) {

       display(n.outEdges.get(c));
     }
   }
   else 
     System.out.println("$");
     return;
 } 
  //tests below
 public static void main(String[] args) {
   UkkonenSuffixTree st = new UkkonenSuffixTree("abc#abx#abcd#defgh#ddefg#gggggg#yyyyy#aaaa#asdasdaa#");
   st.buildSuffixTree();
   st.display(st.root);
   int[] found = st.search("yyyy");
   System.out.println("found between " + found[0] + " and " + found[1]);
        
 } 

}