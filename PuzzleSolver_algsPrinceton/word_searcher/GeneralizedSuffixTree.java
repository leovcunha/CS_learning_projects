/**
 * Generalized to trim suffixes where end chars "#" are found.
 */
package word_searcher;

public class GeneralizedSuffixTree extends UkkonenSuffixTree {
  
  public GeneralizedSuffixTree(String text) { 
    super(text); 
    this.buildSuffixTree();
    this.setSuffixesEnd(this.root);
  }
  
  private void setSuffixesEnd(Node n) {
    if (n.outEdges.isEmpty()) return;
    
    for (char c: n.outEdges.keySet()) {
      for (int i = n.outEdges.get(c).start; i < n.outEdges.get(c).end; i++) {
      
        if (text.charAt(i) == '#') {
          n.outEdges.get(c).end = i+1;
        
        }
      }
      setSuffixesEnd(n.outEdges.get(c));
    }
  }
  //tests below
  public static void main(String[] args) { 
    UkkonenSuffixTree GST = new GeneralizedSuffixTree("abc#abx#abcd#defgh#ddefg#gggggg#yyyyy#aaaa#asdasdaa#");
    GST.display(GST.root);
    int[] found = GST.search("daa");
    System.out.println("Found searched word between " + found[0] + " and " + found[1]);
  }
  
  /* ADD YOUR CODE HERE */
  
}
