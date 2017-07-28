/**
 * Input format. From standard input, read the integer N, then N lines of N characters each (with each characters 
 * separated by one space), followed by the word list (one per line). You may assume that the list of dictionary words 
 * is in lexicographic order. You may also assume that both the puzzle and the dictionary consist of only lowercase letters. 
 * Finally, you may assume that all of the words in the dictionary are at least four letters long.
 * 
 * Output format. Print out every dictionary word (one per line) that is contained somewhere in the puzzle. 
 * If a word appears in the puzzle more than once, print it only once.
 * 
 * Approach. We have covered a number of different algorithms that can be used effectively to solve this problem. 
 * Your task is to study the problem; come up with a strategy for solving it using some of the algorithms and data 
 * structures that you have learned; implement and test your solution; and then explain and defend your design in 
 * the writeup. A good writeup is particularly important for this assignment, because we expect to see a variety 
 * of solution strategies. You need to clearly explain not just what you did, but also why you did it, backed up with 
 * estimates of resource costs. Also, you should clearly explain whatever limitations you need to place on the puzzle 
 * size N and the dictionary size M. As usual, your goal is to find a decent method which works within reasonable bounds. 
 * You need not waste effort squeezing time and space out of a bad algorithm or implementing complex or highly tuned 
 * code. This problem can be solved efficiently with 200 or so lines of code (including comments and basic error 
 * checking).
 *  
 */
import java.util.*;

 /**
 * WordSearcher
 */
public class WordSearcher {

    public WordSearcher () {
        
    }

    public static void main(String[] args) {
        int N = 0;
        Scanner sc = new Scanner(System.in);
        while (N == 0) {
          System.out.println("Enter puzzle Size (square):");
          try {
            N = Integer.parseInt(sc.nextLine());
          }
          catch (NumberFormatException e) {
            System.out.println("you must type a number");
          }
        }
        int n = 0;
        String line = "";
        String[][] puzzle = new String[N][N];
        List<String> searchedWords = new ArrayList<String>();
        
        while (n < N) {      
          System.out.print("Enter line " + (n+1) + ":");
          line = sc.nextLine();
          puzzle[n] = line.split(" ");
          if (puzzle[n].length != N) {
            System.out.println("must have " + N + " columns");
            continue;
          }
          n++;
        }
         
        while (sc.hasNext()) {
          System.out.println("type a word to search (enter when done:");
          line = sc.nextLine();
          if (line.matches("\\n")) break;
          searchedWords.add(line);
        }
        sc.close();
        ;
        for (int i = 0; i < puzzle.length; i++) {
          for (String s: puzzle[i]) {
            System.out.print(s + " ");
          }
          System.out.println("");
        }
        System.out.println(searchedWords);

    }
}