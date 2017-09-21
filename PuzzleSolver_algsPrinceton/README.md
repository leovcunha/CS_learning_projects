
## **Word Searching** 
from http://www.cs.princeton.edu/courses/archive/fall04/cos226/assignments/puzzle.html


#### **I. Objective**


Write a program to solve word searching puzzles, where words from a dictionary are to be found in a two-dimensional array of letters. For example, you might be given the following array:
```
                     m v j l i x a p e
                     j h b x e e n p p
                     h k t t h b s w y
                     r w a i n u y z h
                     p p f x r d z k q
                     t p n l q o y j y
                     a n h a p f g b g
                     h x m s h w y l y
                     u j f j h r s o a
```

Your program is to find words from the dictionary that can be spelled out in the puzzle by starting at any character, then moving in a straight line up, down, left, right, or on any of the four diagonals. For example, the above array contains the word algorithm because it can be spelled out by starting the lower right corner and moving up and to the left; and it contains the word syzygy because it can be spelled out by starting at the s on the third row and moving down.

----------

####  **II. Approach**

I thought of three possibilities out of many:

**1.** Extract string of all possibilities and use a substring search algorithm like Knuth-Morris-Pratt.

**Complexity analysis:** 	

N => n² - number of chars in the matrix		
M => number of chars in the word dictionary	 
Preprocessing:  both text and pattern would need to be preprocessed		
O(N+M)	

Search:		
O(8*N) //considering all possible directions

**2.** Create a hashTable linking every letter with its existing position in the char matrix and for each position create a graph of all neighbor characters. Search for first letter in the hashtable for possible starting positions. Second letter would give direction of search.

**Complexity analysis:**	
N => n² - number of chars in the matrix	 	
M => number of chars in the word dictionary  	
Preprocessing: 	
O(N)	
Search:		
case half of the positions are the same first char	
O(8*N/2 + M) = O(4N+M) 

**3.** Create a suffix tree of all horizontal, vertical and diagonal arrays using Ukkonen Algorithm. Search for dictionary words by traversing the tree. 

**Complexity analysis:**		
N => n² - number of chars in the matrix		
M => number of chars in the word dictionary 	
Preprocessing: 	
O(4N) // got only 4directions	
Search:		
O(2M) //reverse string to see if pattern is backwards in the puzzle

----------
#### **III. Design**
My option was for the Suffix Tree, although harder to implement from scratch , it gave ok performance of search for this problem. It also gave me an excellent opportunity to learn about this curious and complex data structure construction using Ukkonen Algorithm. 
##### ** Classes: **

* __UkkonenSuffixTree__
parameters: String text 
methods: buildSuffixTree(), search(String pattern), printTree()

* __GeneralizedSuffixTree extends UkkonenSuffixTree__
  methods: setSuffixEnds()
  for a string containing many words separate each word in a separate branch. I used this to separate each diagonal, horizontal and vertical charArray.
  
* __WordSearcher__
  methods: setSuffixEnds()
Main class that handles input , output and call classes.

----------
#### **IV. References**
 * https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english
 * http://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/

![V. Amazing suffix tree](https://github.com/leovcunha/CS_learning_projects/blob/master/PuzzleSolver_algsPrinceton/suffixtree.png)
