"""The Game of Hog."""

from dice import four_sided, six_sided, make_test_dice
from ucb import main, trace, log_current_line, interact
from math import pow
GOAL_SCORE = 100 # The goal of Hog is to score 100 points.

######################
# Phase 1: Simulator #
######################

# Taking turns

def roll_dice(num_rolls, dice=six_sided):
    """Roll DICE for NUM_ROLLS times.  Return either the sum of the outcomes,
    or 1 if a 1 is rolled (Pig out). This calls DICE exactly NUM_ROLLS times.

    num_rolls:  The number of dice rolls that will be made; at least 1.
    dice:       A zero-argument function that returns an integer outcome.
    """

    # These assert statements ensure that num_rolls is a positive integer.
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls > 0, 'Must roll at least once.'
    "*** YOUR CODE HERE ***"
    rslt=0
    while(num_rolls > 0):
        t1=dice()
        if t1==1:
            return 0
        else:
            rslt+=t1
        num_rolls-=1
    return rslt

        
def is_prime(number):
    """Verifies if a number is prime"""
    assert type(number) == int, 'number must be integer'
    """CODE"""
    if number == 0 or number == 1:
        return False
    for i in range(2,number):
        if number % i == 0:
            return False
            break
    else:
        return True
        
def next_prime(number):
    assert type(number) == int, 'number must be integer'
    assert number > 1, 'number must be greater than 1'
    for p in range(number+1, 2*number):
        if is_prime(p):
            return p
            break
def calc_roll0(opponent_score):
    points = max(opponent_score // 10 , opponent_score % 10)+1
    if is_prime(points):
        points = next_prime(points)   
    return points
    
def take_turn(num_rolls, opponent_score, dice=six_sided):
    """Simulate a turn rolling NUM_ROLLS dice, which may be 0 (Free bacon).

    num_rolls:       The number of dice rolls that will be made.
    opponent_score:  The total score of the opponent.
    dice:            A function of no args that returns an integer outcome.
    """
    assert type(num_rolls) == int, 'num_rolls must be an integer.'
    assert num_rolls >= 0, 'Cannot roll a negative number of dice.'
    assert num_rolls <= 10, 'Cannot roll more than 10 dice.'
    assert opponent_score < 100, 'The game should be over.'
    "*** YOUR CODE HERE ***"
    if num_rolls==0:
        points = calc_roll0(opponent_score)
    else:
        points = roll_dice(num_rolls, dice)


    return points
        



    
# Playing a game

def select_dice(score, opponent_score):
    """Select six-sided dice unless the sum of SCORE and OPPONENT_SCORE is a
    multiple of 7, in which case select four-sided dice (Hog wild).

    >>> select_dice(4, 24) == four_sided
    True
    >>> select_dice(16, 64) == six_sided
    True
    >>> select_dice(0, 0) == four_sided
    True
    """
    "*** YOUR CODE HERE ***"
    test = score + opponent_score 
    if (test % 7 == 0):
        return four_sided
    else:
        return six_sided
    
def is_swap(score, opponent_score):
    """checks to see if the last two digits of the players' scores are swapped.
    """
    olast = opponent_score % 10
    ofirst = opponent_score // 10
    last = score % 10
    first = score // 10
    if (olast == first) & (last == ofirst):
        return True
    else:
        return False

def other(who):
    """Return the other player, for a player WHO numbered 0 or 1.

    >>> other(0)
    1
    >>> other(1)
    0
    """
    return 1 - who

def play(strategy0, strategy1, goal=GOAL_SCORE):
    """Simulate a game and return the final scores of both players, with
    Player 0's score first, and Player 1's score second.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    strategy0:  The strategy function for Player 0, who plays first.
    strategy1:  The strategy function for Player 1, who plays second.
    """


    who = 0  # Which player is about to take a turn, 0 (first) or 1 (second)
    score, opponent_score = 0, 0
    score0 = score
    score1 = opponent_score 
          
    "*** YOUR CODE HERE ***"
    while score0 < goal and score1 < goal:
        ddice = select_dice(score, opponent_score)
        if who == 0:
            num_rolls = strategy0(score0,score1)
            points = take_turn(num_rolls, score1, ddice)
            if points == 0:
                score1 += num_rolls #piggy back
            else:
                score0 += points
        elif who == 1:
            num_rolls = strategy1(score1, score0)
            points = take_turn(num_rolls, score1, ddice)
            if points == 0:
                score0 += num_rolls #piggy back
            else:
                score1 += points
        if is_swap(score0,score1):
            score0, score1 = score1, score0
        who=other(who)

    return score0, score1# You may wish to change this line.

#######################
# Phase 2: Strategies #
#######################

# Basic Strategy

BASELINE_NUM_ROLLS = 5
BACON_MARGIN = 8

def always_roll(n):
    """Return a strategy that always rolls N dice.

    A strategy is a function that takes two total scores as arguments
    (the current player's score, and the opponent's score), and returns a
    number of dice that the current player will roll this turn.

    >>> strategy = always_roll(5)
    >>> strategy(0, 0)
    5
    >>> strategy(99, 99)
    5
    """
    def strategy(score, opponent_score):
        return n
    return strategy

# Experiments

def make_averaged(fn, num_samples=5000):
    """Return a function that returns the average_value of FN when called.

    To implement this function, you will have to use *args syntax, a new Python
    feature introduced in this project.  See the project description.

    >>> dice = make_test_dice(3, 1, 5, 6)
    >>> averaged_dice = make_averaged(dice, 1000)
    >>> averaged_dice()
    3.75
    >>> make_averaged(roll_dice, 1000)(2, dice)
    6.0

    In this last example, two different turn scenarios are averaged.
    - In the first, the player rolls a 3 then a 1, receiving a score of 1.
    - In the other, the player rolls a 5 and 6, scoring 11.
    Thus, the average value is 6.0.
    """
    "*** YOUR CODE HERE ***"
    
    def ret(*args):
        sum=0
        for i in range(1,num_samples):
            sum += fn(*args)
        return sum/num_samples               
    return ret
    
def max_scoring_num_rolls(dice=six_sided):
    """Return the number of dice (1 to 10) that gives the highest average turn
    score by calling roll_dice with the provided DICE.  Print all averages as in
    the doctest below.  Assume that dice always returns positive outcomes.

    >>> dice = make_test_dice(3)
    >>> max_scoring_num_rolls(dice)
    1 dice scores 3.0 on average
    2 dice scores 6.0 on average
    3 dice scores 9.0 on average
    4 dice scores 12.0 on average
    5 dice scores 15.0 on average
    6 dice scores 18.0 on average
    7 dice scores 21.0 on average
    8 dice scores 24.0 on average
    9 dice scores 27.0 on average
    10 dice scores 30.0 on average
    10
    """
    "*** YOUR CODE HERE ***"
    for i in range(1,11):
        test = round(make_averaged(roll_dice,1000)(i,dice),2)
#        print(i, "dice scores", test, "on average")
        if i==1:
            max_score=test
            num_dices=i
        elif test > max_score:
            max_score=test
            num_dices=i                
    return num_dices
    
    
def winner(strategy0, strategy1):
    """Return 0 if strategy0 wins against strategy1, and 1 otherwise."""
    score0, score1 = play(strategy0, strategy1)
    if score0 > score1:
        return 0
    else:
        return 1

def average_win_rate(strategy, baseline=always_roll(BASELINE_NUM_ROLLS)):
    """Return the average win rate (0 to 1) of STRATEGY against BASELINE."""
    win_rate_as_player_0 = 1 - make_averaged(winner)(strategy, baseline)
    win_rate_as_player_1 = make_averaged(winner)(baseline, strategy)
    return (win_rate_as_player_0 + win_rate_as_player_1) / 2 # Average results

def run_experiments():
    """Run a series of strategy experiments and report results."""
    if True: # Change to False when done finding max_scoring_num_rolls
        six_sided_max = max_scoring_num_rolls(six_sided)
        print('Max scoring num rolls for six-sided dice:', six_sided_max)
        four_sided_max = max_scoring_num_rolls(four_sided)
        print('Max scoring num rolls for four-sided dice:', four_sided_max)

    if True: # Change to True to test always_roll(8)
        print('always_roll(8) win rate:', average_win_rate(always_roll(8)))
        
    if True: # Change to True to test always_roll(5)
        print('always_roll(5) win rate:', average_win_rate(always_roll(5)))
        
    if True: # Change to True to test bacon_strategy
        print('bacon_strategy win rate:', average_win_rate(bacon_strategy))

    if True: # Change to True to test swap_strategy
        print('swap_strategy win rate:', average_win_rate(swap_strategy))

    if True: # Change to True to test final_strategy
        print('final_strategy win rate:', average_win_rate(final_strategy))

    "*** You may add additional experiments as you wish ***"

# Strategies

def bacon_strategy(score, opponent_score, margin = BACON_MARGIN, num_rolls= BASELINE_NUM_ROLLS):
    """This strategy rolls 0 dice if that gives at least BACON_MARGIN points,
    and rolls BASELINE_NUM_ROLLS otherwise.

    >>> bacon_strategy(0, 0)
    5
    >>> bacon_strategy(70, 50)
    5
    >>> bacon_strategy(50, 70)
    0
    """
    "*** YOUR CODE HERE ***"
    points = calc_roll0(opponent_score)

    if points > margin:
        return 0
    else: 
        return num_rolls # Replace this statement

def swap_strategy(score, opponent_score, num_rolls=BASELINE_NUM_ROLLS):
    """This strategy rolls 0 dice when it would result in a beneficial swap and
    rolls BASELINE_NUM_ROLLS if it would result in a harmful swap. It also rolls
    0 dice if that gives at least BACON_MARGIN points and rolls
    BASELINE_NUM_ROLLS otherwise.

    >>> swap_strategy(23, 60) # 23 + (1 + max(6, 0)) = 30: Beneficial swap
    0
    >>> swap_strategy(27, 18) # 27 + (1 + max(1, 8)) = 36: Harmful swap
    5
    >>> swap_strategy(50, 80) # (1 + max(8, 0)) = 9: Lots of free bacon
    0
    >>> swap_strategy(12, 12) # Baseline
    5
    """
    "*** YOUR CODE HERE ***"
    points = calc_roll0(opponent_score)
    test_score = score + points    
    if is_swap(test_score, opponent_score) and (test_score < opponent_score):
        return 0
    else:        
        return num_rolls # Replace this statement

def wild_strategy(score, opponent_score, num_rolls=5):
    points = max((int(opponent_score / 10), int(opponent_score % 10))) + 1
    tot_score = score + points
    if (tot_score + opponent_score) % 7 == 0:
        return 0    
    else:
        return num_rolls

def desired_pigout(score, opponent_score):
    num_rolls = 1
    for i in range(3,11):
        num_rolls = i
        if is_swap(score, opponent_score + i) == True:
            return num_rolls
    return 1
 
def rate(score, opponent_score):
    if score == 0 or opponent_score == 0:
        return 0
    else:      
        return round(opponent_score/score,2)

    
def find_prob_1(score, opponent_score, num_rolls) :
    if (score + opponent_score) % 7 == 0 :
        return 1-pow(3/4,num_rolls)
    else:
        return 1-pow(5/6,num_rolls)
                   
def final_strategy(score, opponent_score):
    
    """Write a brief description of your final strategy.

    *** YOUR DESCRIPTION HERE ***
    """
    "*** YOUR CODE HERE ***"
    num_rolls = 4
    diff = 100 - score
    bacon_pts = max((int(opponent_score / 10), int(opponent_score % 10))) + 1
    
    if bacon_pts >= diff:
        return 0    
    if swap_strategy(score, opponent_score, 4) == 0:
        return 0
    if desired_pigout(score, opponent_score) > 1:
        if score < opponent_score:    
            return desired_pigout(score, opponent_score)
    if bacon_strategy(score, opponent_score, 6, 4) == 0:
        return 0
    if wild_strategy(score, opponent_score, 4) == 0:
        return 0


    return num_rolls

    """
    num_rolls = 4
    diff = 100 - score
    bacon_pts = max((int(opponent_score / 10), int(opponent_score % 10))) + 1
    
    if bacon_pts >= diff:
        return 0    
    if swap_strategy(score, opponent_score, 4) == 0:
        return 0
    if desired_pigout(score, opponent_score) > 1:
        if score < opponent_score:    
            return desired_pigout(score, opponent_score)
    if bacon_strategy(score, opponent_score, 6, 4) == 0:
        return 0
    if wild_strategy(score, opponent_score, 4) == 0:
        return 0

    if score - 21 > opponent_score:
        return 3
    if score - 11 > opponent_score:
        return 2
    if score + 20 < opponent_score:
        return 5
    if score + 32 < opponent_score:
        return 6
    return num_rolls
    """ 

##########################
# Command Line Interface #
##########################

# Note: Functions in this section do not need to be changed.  They use features
#       of Python not yet covered in the course.

def get_int(prompt, min):
    """Return an integer greater than or equal to MIN, given by the user."""
    choice = input(prompt)
    while not choice.isnumeric() or int(choice) < min:
        print('Please enter an integer greater than or equal to', min)
        choice = input(prompt)
    return int(choice)

def interactive_dice():
    """A dice where the outcomes are provided by the user."""
    return get_int('Result of dice roll: ', 1)

def make_interactive_strategy(player):
    """Return a strategy for which the user provides the number of rolls."""
    prompt = 'Number of rolls for Player {0}: '.format(player)
    def interactive_strategy(score, opp_score):
        if player == 1:
            score, opp_score = opp_score, score
        print(score, 'vs.', opp_score)
        choice = get_int(prompt, 0)
        return choice
    return interactive_strategy

def roll_dice_interactive():
    """Interactively call roll_dice."""
    num_rolls = get_int('Number of rolls: ', 1)
    turn_total = roll_dice(num_rolls, interactive_dice)
    print('Turn total:', turn_total)

def take_turn_interactive():
    """Interactively call take_turn."""
    num_rolls = get_int('Number of rolls: ', 0)
    opp_score = get_int('Opponent score: ', 0)
    turn_total = take_turn(num_rolls, opp_score, interactive_dice)
    print('Turn total:', turn_total)

def play_interactive():
    """Interactively call play."""
    strategy0 = make_interactive_strategy(0)
    strategy1 = make_interactive_strategy(1)
    score0, score1 = play(strategy0, strategy1)
    print('Final scores:', score0, 'to', score1)

@main
def run(*args):
    """Read in the command-line argument and calls corresponding functions.

    This function uses Python syntax/techniques not yet covered in this course.
    """
    import argparse
    parser = argparse.ArgumentParser(description="Play Hog")
    parser.add_argument('--interactive', '-i', type=str,
                        help='Run interactive tests for the specified question')
    parser.add_argument('--run_experiments', '-r', action='store_true',
                        help='Runs strategy experiments')
    args = parser.parse_args()

    if args.interactive:
        test = args.interactive + '_interactive'
        if test not in globals():
            print('To use the -i option, please choose one of these:')
            print('\troll_dice', '\ttake_turn', '\tplay', sep='\n')
            exit(1)
        try:
            globals()[test]()
        except (KeyboardInterrupt, EOFError):
            print('\nQuitting interactive test')
            exit(0)
    elif args.run_experiments:
        run_experiments()
