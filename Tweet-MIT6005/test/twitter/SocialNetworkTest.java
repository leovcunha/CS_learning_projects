/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     *     /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d4 = Instant.parse("2016-03-15T08:00:00Z");
    private static final Instant d5 = Instant.parse("2016-12-15T12:00:00Z");   
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest @alyssa talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "tchubiru", "haha @bbitdiddle #hype", d3);
    private static final Tweet tweet4 = new Tweet(4, "tcharara", "is it your realname barman@alfabet.com?", d1);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "is it you @bbitdiddle ?", d4);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "rivest @tchubiru talk in 30 minutes #hype", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
     /* guessFollowsGraph Test Strategy partitions:
     * empty graph
     * 
     * 1 user: 
     * zero friends, 1 friend , > 1 friend
     * 
     * > 1 user
     * zero friends, 1 friend , > 1 friend
     */
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<Tweet>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    @Test
    public void testGuessFollowsOneUserZeroFriendsGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        assertTrue("expected 1 user graph", followsGraph.size()==1);
        assertTrue("follows zero", followsGraph.get("alyssa").isEmpty());                                                                                                                                                    
    }
    @Test
    public void testGuessFollowsOneUserOneFriendGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
        assertTrue("expected 1 user graph", followsGraph.size()==1);
        assertTrue("follows one", followsGraph.get("bbitdiddle").size()==1);                                                                                                                                                    
    }   
    @Test
    public void testGuessFollowsTwoUserszeroFriendGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet4));
        assertTrue("expected 2 user graph", followsGraph.size()==2);
        assertTrue("follows zero", followsGraph.values().isEmpty());                                                                                 
    }   
    @Test
    public void testGuessFollowsTwoUsersoneFriendGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2, tweet3));
        assertTrue("expected 2 user graph", followsGraph.size()==2);
        assertTrue("follows zero", followsGraph.values().size()==2);                                                                                 
    }
    @Test
    public void testGuessFollowsTwoUsersTwoFriendGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet5, tweet6));
        assertTrue("expected 2 user graph", followsGraph.size()==2);
        assertTrue("expected follow 2" , followsGraph.get("bbitdiddle").size()==2);        
        assertTrue("expected follow 1" , followsGraph.get("alyssa").size()==1);                                                                            
    }
     /* testInfluencers Test Strategy partitions:
      * empty list
      * one user => zero followers
      * two or more users > zero followers, one follower, two or more followers (descending order)  
      *
     */
      
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    @Test
    public void testInfluencersOneUser() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
        List<String> influencers = SocialNetwork.influencers(followsGraph);     
        assertTrue("expected one user", influencers.size() == 1);
        assertTrue("expected zero followers", influencers.isEmpty());                                                                        
    }
    @Test
    public void testInfluencersTwoUsersZeroFollowers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet4));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected one user", influencers.size() == 2);        
    }
    @Test
    public void testInfluencersTwoUsersOneFollowers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2));
        List<String> influencers = SocialNetwork.influencers(followsGraph);                                                                        
        assertTrue("expected one user", influencers.size() == 2);  
        assertTrue("expected zero followers", influencers.get(0).equals("alyssa"));                                                                                
    }
        
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
