import java.util.*;

class PokerGame {
	
	public Player[] playerList;
	public CardDeck deck;
	final int STRAIGHT_FLUSH = 9, FOUR_OF_A_KIND = 8, FULL_HOUSE = 7, 
			FLUSH = 6, STRAIGHT = 5, THREE_OF_A_KIND = 4, TWO_PAIRS = 3,
			PAIR = 2, HIGH_CARD = 1;
	
	public PokerGame(int numOfPlayers) {
		playerList = new Player[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++) {
			playerList[i] = new Player();
		}
		deck = new CardDeck();
	}
	
	public Player determineBestHand(Player[] playerList) {
		
		Player highestHolder = null;
		
		for (Player currentPlayer : playerList) {
			
			currentPlayer.sortHand();
			
			int result = findStraightFlush(currentPlayer.playerHand);
			
			if (result != 0) {
				currentPlayer.handValue = STRAIGHT_FLUSH;
				currentPlayer.highestCardValue = result;
			}
			else {
				
				result = findFourOfAKind(currentPlayer.playerHand);
				if (result != 0) {
					currentPlayer.handValue = FOUR_OF_A_KIND;
					currentPlayer.highestCardValue = result;
				}
				else {
					
					result = findFullHouse(currentPlayer.playerHand);
					if (result != 0) {
						currentPlayer.handValue = FULL_HOUSE;
						currentPlayer.highestCardValue = result;
					}
					else {
						
						result = findFlush(currentPlayer.playerHand);
						if (result != 0) {
							currentPlayer.handValue = FLUSH;
							currentPlayer.highestCardValue = result;
						}
						else {
							
							result = findStraight(currentPlayer.playerHand);
							if (result != 0) {
								currentPlayer.handValue = STRAIGHT;
								currentPlayer.highestCardValue = result;
							}
							else {
								
								result = findThreeOfAKind(currentPlayer.playerHand);
								if (result != 0) {
									currentPlayer.handValue = THREE_OF_A_KIND;
									currentPlayer.highestCardValue = result;
								}
								else {
									result = findTwoPairs(currentPlayer.playerHand);
									if (result != 0){
										currentPlayer.handValue = TWO_PAIRS;
										currentPlayer.highestCardValue = result;
									}
									else {
										
										result = findPair(currentPlayer.playerHand);
										if (result != 0){
											currentPlayer.handValue = PAIR;
											currentPlayer.highestCardValue = result;
										}
										else {
											currentPlayer.handValue = HIGH_CARD;
											currentPlayer.highestCardValue = 
											findHighestCard(currentPlayer.playerHand);
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		
		HashMap<Player, Integer> playerMap = new HashMap<Player, Integer>();
		List<Player> highestHand_list = new ArrayList<Player>(0);
		
		for (Player currentPlayer : playerList) {
			playerMap.put(currentPlayer, currentPlayer.handValue);
		}
		
		int highestValue = Collections.max(playerMap.values());
		
		for (Map.Entry<Player,Integer> entry : playerMap.entrySet() ){
			if (entry.getValue() == highestValue) {
				highestHand_list.add(entry.getKey());
			}
		}
		
		int highestCardValue = 0;
		for (Player temp : highestHand_list) {
			if (temp.highestCardValue > highestCardValue) {
				highestCardValue = temp.highestCardValue;
				highestHolder = temp;
			}
		}
		
		return highestHolder;
	}
	
	public int findStraightFlush(Card[] hand) {
		
		Suit matchingSuite = hand[0].getCardSuit();
		int matchingValue = hand[0].getCardValue();
		int highestValue = 0;
		
		for (int i = 0; i < hand.length; i++) {
			
			if (hand[i].getCardSuit() != matchingSuite || hand[i].getCardValue() != matchingValue + i)
				return 0;
				
			highestValue = matchingValue + i;
		}
		
		return highestValue;
	}
	
	public int findFourOfAKind(Card[] hand) {

		Integer value;
		
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (int i = 0; i < hand.length; i++) {
			int key = hand[i].getCardValue();
			if ( hm.containsKey(key) ) {
				value = hm.get(key);
				hm.put(key, value + 1);
			} else {
				hm.put(key, 1);
			}
		}
		
		int numOfMatches = Collections.max(hm.values());
		
		int finalValue = 0;
		
		if (numOfMatches == 4) {
			for (Map.Entry<Integer,Integer> entry : hm.entrySet()) {
				if (entry.getValue()==numOfMatches) {
					finalValue = entry.getKey();
				}
			}
		}
		
		return (numOfMatches == 4) ? finalValue : 0;
	}
	
	public int findFullHouse(Card[] hand) {
		
		Integer value;
		
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (int i = 0; i < hand.length; i++) {
			int key = hand[i].getCardValue();
			if ( hm.containsKey(key) ) {
				value = hm.get(key);
				hm.put(key, value + 1);
			} else {
				hm.put(key, 1);
			}
		}
		
		int numOfMatches = Collections.max(hm.values());
		
		int finalValue = 0;
		
		if (numOfMatches == 3) {
			for (Map.Entry<Integer,Integer> entry : hm.entrySet()) {
				if (entry.getValue()==numOfMatches) {
					finalValue = entry.getKey();
				}
			}
		}
		
		return (numOfMatches == 3) ? finalValue : 0;
	}
	
	public int findFlush(Card[] hand) {
		
		Suit matchingSuite = hand[0].getCardSuit();
		
		for (int i = 0; i < hand.length; i++) {
			
			if (hand[i].getCardSuit() != matchingSuite)
				return 0;
		}
				
		return findHighestCard(hand);
	}
	
	public int findStraight(Card[] hand) {
		
		int matchingValue = hand[0].getCardValue();
		int highestValue = 0;
		
		for (int i = 0; i < hand.length; i++) {
			
			if (hand[i].getCardValue() != matchingValue + i)
				return 0;
				
			highestValue = matchingValue + i;
		}
				
		return highestValue;
	}
	
	public int findThreeOfAKind(Card[] hand) {
		
		Integer value;
		
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (int i = 0; i < hand.length; i++) {
			int key = hand[i].getCardValue();
			if ( hm.containsKey(key) ) {
				value = hm.get(key);
				hm.put(key, value + 1);
			} else {
				hm.put(key, 1);
			}
		}
		
		int numOfMatches = Collections.max(hm.values());
		
		int finalValue = 0;
		
		if (numOfMatches == 3) {
			for (Map.Entry<Integer,Integer> entry : hm.entrySet()) {
				if (entry.getValue()==numOfMatches) {
					finalValue = entry.getKey();
				}
			}
		}
		
		return (numOfMatches == 3) ? finalValue : 0;
	}
	
	public int findTwoPairs(Card[] hand) {
		
		int numOfPairs = 0;
		int highestValue = 0;
		
		for(int i = 0; i < hand.length; i++) {
			
			for (int j = i + 1; j < hand.length; j++) {
				
				if(hand[i] == hand[j] && i != j) {
					numOfPairs++;
					highestValue = (highestValue > hand[i].getCardValue()) ? 
									highestValue : hand[i].getCardValue();
				}
			}
		}
		
		return (numOfPairs == 2) ? highestValue : 0;
	}
	
	public int findPair(Card[] hand) {
		
		int numOfPairs = 0;
		int highestValue = 0;
		
		for(int i = 0; i < hand.length; i++) {
			
			for (int j = i + 1; j < hand.length; j++) {
				
				if(hand[i] == hand[j] && i != j) {
					numOfPairs++;
					highestValue = hand[i].getCardValue();
				}
			}
		}
		
		return (numOfPairs == 1) ? highestValue : 0;
	}
	
	public int findHighestCard(Card[] hand) {
		
		int highest = 0;
		
		for (int i = 0; i < hand.length; i++) {
			if (hand[i].getCardValue() > highest)
				highest = hand[i].getCardValue();
		}
		
		return highest;
	}
	
	public static void main(String[] args) {
		
		PokerGame game = new PokerGame(2);
		
		for (Player tempPlayer : game.playerList) {
			
			for (int i = 0; i < tempPlayer.playerHand.length; i++) {
				tempPlayer.playerHand[i] = game.deck.dealCard();
			}
		}
		
		Player winningPlayer = game.determineBestHand(game.playerList);
		
		System.out.printf("Winner's hand: %d", winningPlayer.highestCardValue);
		
	}
}

class Player {
	
	public Card[] playerHand;
	int handValue;
	int highestCardValue;
	
	public Player() {
		playerHand = new Card[5];
		handValue = 0;
		highestCardValue = 0;
	}
	
	public void sortHand() {
		
		Card tempCard = null;
		
		for (int i = 0; i < playerHand.length; i++) {
			for (int j = i+1; j < playerHand.length; j++) {
				if(playerHand[i].getCardValue() > playerHand[j].getCardValue()) {
					tempCard = playerHand[i];
					playerHand[i] = playerHand[j];
					playerHand[j] = tempCard;
				}
			}
		}
	}
}