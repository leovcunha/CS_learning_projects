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

import java.io.*;
import java.util.*;

public class UkkonenSuffixTree {
  
  Node root;  // Root of the SuffixTree
  Node lastNewNode; // for rule 2
  PrintWriter out;
  
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
  static int yield = 0;
   
  class Node { 
    TreeMap<Character, Node> outEdges;
    Node suffixLink; //pointer from last new node to newly created node  
    int start, end; // each array guards {x, y} of the puzzle
    int id;
    
    
    Node(int start, int end) {
       this.suffixLink = root;
       this.start = start;
       this.end = end;
       this.outEdges = new TreeMap<Character, Node>();
       this.id = yield++;
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

        System.out.println("iteration" + i+ ":::::::::::");
        remainder++;
        System.out.println("remainder: " + remainder);
        lastNewNode = null;

        while (remainder > 0) {
         
          while (walkDown(next)) {
                activeNode = next;
                activeLength -= next.edgeLength();  
                activeEdge = text.charAt(i);
                next = activeNode.outEdges.get(activeEdge);
                System.out.println("walked down to node: " + activeNode + " active edge: " + activeEdge + " length: " + activeLength);
                
          }
                              
          if (activeLength == 0 && !activeNode.outEdges.containsKey(text.charAt(i))) {
            activeNode.outEdges.put(text.charAt(i), new Node(i, size)); 
            System.out.println("inserted suffix " + activeNode.outEdges.get(text.charAt(i)));
            
            if (lastNewNode != null) { //rule 2 
                lastNewNode.suffixLink = activeNode;
                System.out.println("suffix link from " +  lastNewNode + " to " + lastNewNode.suffixLink);
                lastNewNode = null;
              }
                
              

                                              
          } else {
                       
            if (activeLength == 0) {
              activeEdge = text.charAt(i);
              next = activeNode.outEdges.get(activeEdge); 
              System.out.println("new active edge: " + activeEdge + " length: " + activeLength);             
            } 
            
            if (text.charAt(next.start+activeLength) == text.charAt(i)) {              
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
           
            if (lastNewNode != null) { //rule 2 
              lastNewNode.suffixLink = splitNode;
              System.out.println("suffix link from " +  lastNewNode + " to " + splitNode);
            }
              
              lastNewNode = splitNode; 
              System.out.println("last new node: " + splitNode);
            }
          
          remainder--;
          System.out.println("remainder: "+ remainder);
          
          //rule 1
          if (activeNode.start == -1 && activeLength > 0) {
            activeLength--;
            activeEdge = text.charAt(i - remainder + 1);
            next = activeNode.outEdges.get(activeEdge); 
            System.out.println("new active edge: " + activeEdge + " length: " + activeLength);

          }
          else if (activeNode.start != -1) { //rule 3
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
     System.out.println("out edges of node:" + n +" " + n.outEdges);
     for ( char c: n.outEdges.keySet()) {

       display(n.outEdges.get(c));
     }
   }
   else 
     return;
 } 
  
  void printTree() {
      out.println("digraph {");
      out.println("\trankdir = LR;");
      out.println("\tedge [arrowsize=0.4,fontsize=10]");
      out.println("\tnode0 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
      out.println("//------leaves------");
      printLeaves(root);
      out.println("//------internal nodes------");
      printInternalNodes(root);
      out.println("//------edges------");
      printEdges(root);
      out.println("//------suffix links------");
      printSLinks(root);
      out.println("}");
  }

  void printLeaves(Node x) {
      if (x.outEdges.isEmpty())
          out.println("\tnode"+x.id+ " [label=\"\",shape=point]");
      else {
          for (Node child : x.outEdges.values())
              printLeaves(child);
      }
  }

  void printInternalNodes(Node x) {
      if (x != root && !x.outEdges.isEmpty())
          out.println("\tnode"+x.id+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");

      for (Node child : x.outEdges.values())
          printInternalNodes(child);
  }

  void printEdges(Node x) {
      for (Node child : x.outEdges.values()) {
          out.println("\tnode"+x.id+" -> node"+child.id+" [label=\""+child+"\",weight=3]");
          printEdges(child);
      }
  }

  void printSLinks(Node x) {
      if (x.suffixLink != root && x.suffixLink != null)
          out.println("\tnode"+x.id+" -> node"+x.suffixLink.id+" [label=\"\",weight=1,style=dotted]");
      for (Node child : x.outEdges.values())
          printSLinks(child);
  }
  

  //tests below
 public static void main(String[] args) throws IOException {
   UkkonenSuffixTree st = new UkkonenSuffixTree("abc#abx#abcd#defgh#ddefg#gggggg#yyyyy#aaaa#asdasdaa$");
   st.buildSuffixTree();
   int[] found = st.search("dasd");
   System.out.println("found between " + found[0] + " and " + found[1]);
   st.out = new PrintWriter(new FileWriter("test.dot"));  
   st.printTree();
   st.out.close();
 } 

}