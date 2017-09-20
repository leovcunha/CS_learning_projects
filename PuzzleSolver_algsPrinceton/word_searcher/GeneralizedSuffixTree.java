/**
 * Generalized to trim suffixes where end chars "#" are found.
 */
package word_searcher;

import java.io.*;

public class GeneralizedSuffixTree extends UkkonenSuffixTree {
  
  public GeneralizedSuffixTree(String text) { 
    super(text); 
    this.buildSuffixTree();
    this.setSuffixesEnd(this.root);
  }
  
  private void setSuffixesEnd(Node n) {
    if (n.outEdges.isEmpty()) return;
    
    for (char c: n.outEdges.keySet()) {  
      
      if (n == root && c == '#') {
        n.outEdges.get(c).outEdges.clear();
        continue;    
      }
      
      for (int i = n.outEdges.get(c).start; i < n.outEdges.get(c).end; i++) {
        
        if (text.charAt(i) == '#') {
          n.outEdges.get(c).end = i+1;
        
        }
      }
      setSuffixesEnd(n.outEdges.get(c));
    }
  }
  //tests below
  public static void main(String[] args) throws IOException { 
    UkkonenSuffixTree GST = new GeneralizedSuffixTree("abc#abx#abcd#defgh#ddefg#gggggg#yyyyy#aaaa#asdasdaa$");
    int[] found = GST.search("daa");
    System.out.println("Found searched word between " + found[0] + " and " + found[1]);
    GST.out = new PrintWriter(new FileWriter("general.dot"));  
    GST.printTree();
    GST.out.close();
  }
  
  /* ADD YOUR CODE HERE */
  
}
