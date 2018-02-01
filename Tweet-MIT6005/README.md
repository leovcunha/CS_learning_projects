
#Tweet Tweet

*Solved according to the following spec from http://web.mit.edu/6.005/www/sp16/psets/ps1/*

**Overview**
The theme of this problem set is to build a toolbox of methods that can extract information from a set of tweets downloaded from Twitter.

Since we are doing test-first programming, your workflow for each method should be (in this order).

Study the specification of the method carefully.
Write JUnit tests for the method according to the spec.
Implement the method according to the spec.
Revise your implementation and improve your test cases until your implementation passes all your tests.


##Problem 1: Extracting data from tweets

In this problem, you will test and implement the methods in Extract.java.

You’ll find Extract.java in the src folder, and a JUnit test class ExtractTest.java in the test folder. Separating implementation code from test code is a common practice in development projects. It makes the implementation code easier to understand, uncluttered by tests, and easier to package up for release.

**a.** Devise, document, and implement test cases for getTimespan() and getMentionedUsers(), and put them in ExtractTest.java.

**b.** Implement getTimespan() and getMentionedUsers(), and make sure your tests pass.

If you want to see your code work on a live sample of tweets, you can run Main.java. (Main.java will not be used in grading, and you are free to edit it as you wish.)

Hints:

Note that we use the class Instant to represent the date and time of tweets. You can check this article on Java 8 dates and times to learn how to use Instant.

You may wonder what to do about lowercase and uppercase in the return value of getMentionedUsers(). This spec has an underdetermined postcondition, so read the spec carefully and think about what that means for your implementation and your test cases.

getTimespan() also has an underdetermined postcondition in some circumstances, which gives the implementor (you) more freedom and the client (also you, when you’re writing tests) less certainty about what it will return.

Read the spec for the Timespan class carefully, because it may answer many of the questions you have about getTimespan().

Commit to Git. Once you’re happy with your solution to this problem, commit and push! Committing frequently – whenever you’ve fixed a bug or added a working and tested feature – is a good way to use version control, and will be a good habit to have for your team projects.

##Problem 2: Filtering lists of tweets

In this problem, you will test and implement the methods in Filter.java.

Devise, document, and implement test cases for writtenBy(), inTimespan(), and containing(), and put them in FilterTest.java.

Implement writtenBy(), inTimespan(), and containing(), and make sure your tests pass.

Hints:

For questions about lowercase/uppercase and how to interpret timespans, reread the hints in the previous question.

For all problems on this problem set, you are free to rewrite or replace the provided example tests and their assertions.

Commit to Git. Once you’re happy with your solution to this problem, commit and push!

##Problem 3: Inferring a social network

In this problem, you will test and implement the methods in SocialNetwork.java. The guessFollowsGraph() method creates a social network over the people who are mentioned in a list of tweets. The social network is an approximation to who is following whom on Twitter, based only on the evidence found in the tweets. The influencers() method returns a list of people sorted by their influence (total number of followers).

Devise, document, and implement test cases for guessFollowsGraph() and influencers(), and put them in SocialNetworkTest.java. Be careful that your test cases for guessFollowsGraph() respect its underdetermined postcondition.

Implement guessFollowsGraph() and influencers(), and make sure your tests pass. For now, implement only the minimum required behavior for guessFollowsGraph(), which infers that Ernie follows Bert if Ernie @-mentions Bert.

If you want to see your code work on a live sample of tweets, run Main.java. It will print the top 10 most-followed people according to the social network you generated. You can search for them on Twitter to see if their actual number of followers has a similar order.

##Problem 4: Get smarter

In this problem, you will implement one additional kind of evidence in guessFollowsGraph(). Note that we are taking a broad view of “influence” here, and even Twitter-following is not a ground truth for influence, only an approximation. It’s possible to read Twitter without explicitly following anybody. It’s also possible to be influenced by somebody through other media (email, chat, real life) while producing evidence of the influence on twitter.

Here are some ideas for evidence of following. Feel free to experiment with your own.

Common hashtags. People who use the same hashtags in their tweets (e.g. #mit) may mutually influence each other. People who share a hashtag that isn’t otherwise popular in the dataset, or people who share multiple hashtags, may be even stronger evidence.

Triadic closure. In this context, triadic closure means that if a strong tie (mutual following relationship) exists between a pair A,B and a pair B,C, then some kind of tie probably exists between A and C – either A follows C, or C follows A, or both.

Awareness. If A follows B and B follows C, and B retweets a tweet made by C, then A sees the retweet and is influenced by C.

Keep in mind that whatever additional evidence you implement, your guessFollowsGraph() must still obey the spec. To test your specific implementation, make sure you put test cases in your own MySocialNetworkTest class rather than the SocialNetworkTest class that we will run against staff implementations. Your work on this problem will be judged by the clarity of the code you wrote to implement it and the test cases you wrote to test it.
