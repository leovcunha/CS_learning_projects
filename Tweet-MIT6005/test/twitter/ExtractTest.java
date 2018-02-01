/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;

import org.junit.Test;

public class ExtractTest {

    /*
     * Testing Strategy:
     * 
     * GetTimeSpan input partitions:
     * list length : 0 tweets, 1 tweet, 2 tweets or more tweets
     * 
     * output: 
     * start > end 
     * start = end
     * start < end 
     * 
     *
     * 
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest @bbitdiddle talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "tchubiru", "haha @bbitdiddle #hype", d3);
    private static final Tweet tweet4 = new Tweet(4, "tcharara", "is it your realname barman@alfabet.com?", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
     @Test
    public void testGetTimespanZeroTweets() {
        Timespan timespan = Extract.getTimespan(new ArrayList<Tweet>());
        assertEquals(timespan.getEnd(), timespan.getStart());   

    }   
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("start=end", timespan.getEnd(), timespan.getStart());
    }     
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    /* 
     * testGetMentionedUsersNoMention() partitions:
     * 0, 1 , more than 1 users
     * 
     * output: valid mentions, two equal user , invalid chracter
     * 
     * 
    */
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2));
        
        assertEquals("one mentioned OK", 1, mentionedUsers.size());
    } 
    @Test
    public void testGetMentionedUsersNoTwoEqualMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet2, tweet3));
        
        assertEquals("No Two Equal Mention OK", 1, mentionedUsers.size());
    }    
    
    @Test
    public void testGetMentionedUsersNoInvalidCharMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertTrue("No invalid char OK", !mentionedUsers.isEmpty());
    }    
      
    
    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
