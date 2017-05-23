/**
 * Auto Generated Java Class.
 */
public class Test {
  
  
  public static void main(String[] args) { 
    String sentence = "This is a sentence"; 
    StringTokenizer t = new StringTokenizer(sentence);
    String word ="";
    while(t.hasMoreTokens())
    {
      word = t.nextToken();
      System.out.println(word);
    }
  }
  
  /* ADD YOUR CODE HERE */
  
}
