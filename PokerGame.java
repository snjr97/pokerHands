import java.util.*;
import javax.sql.rowset.*;
import java.util.function.Predicate;

class PokerGame {
	
	public Player[] playerList;
	final static int STRAIGHT_FLUSH = 9, FOUR_OF_A_KIND = 8, FULL_HOUSE = 7, 
			FLUSH = 6, STRAIGHT = 5, THREE_OF_A_KIND = 4, TWO_PAIRS = 3,
			PAIR = 2, HIGH_CARD = 1;
	
	public PokerGame(int numOfPlayers, boolean redirection) {
		playerList = new Player[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++) {
			playerList[i] = new Player();
			if (!redirection)
				playerList[i].setPlayerName(i + 1);
		}
	}
	
	public Player determineBestHand(Player[] playerList) {
		
		for (Player temp : playerList) {
			System.out.printf("%s's hand: \n", temp.getPlayerName() );
			for (int i = 0; i < temp.playerHand.length; i++) {
				temp.playerHand[i].displayCard();
			}
			System.out.println();
		}
		
		Player highestHolder = null;
		
		//Work way through players, find best hand
		for (Player currentPlayer : playerList) {
			
			currentPlayer.sortHand();
			
			int result = findStraightFlush(currentPlayer.playerHand);
						
			if (result != 0) {
				currentPlayer.setHandValue(STRAIGHT_FLUSH);
				currentPlayer.setHighestCardValue(result);
			}
			else {
				
				result = findFourOfAKind(currentPlayer.playerHand);
				if (result != 0) {
					currentPlayer.setHandValue(FOUR_OF_A_KIND);
					currentPlayer.setHighestCardValue(result);
				}
				else {
					
					result = findFullHouse(currentPlayer.playerHand);
					if (result != 0) {
						currentPlayer.setHandValue(FULL_HOUSE);
						currentPlayer.setHighestCardValue(result);
						currentPlayer.setFullHousePair(findPair(currentPlayer.playerHand));
					}
					else {
						
						result = findFlush(currentPlayer.playerHand);
						if (result != 0) {
							currentPlayer.setHandValue(FLUSH);
							currentPlayer.setHighestCardValue(
								findHighestCard(currentPlayer.playerHand));
						}
						else {
							
							result = findStraight(currentPlayer.playerHand);
							if (result != 0) {
								currentPlayer.setHandValue(STRAIGHT);
								currentPlayer.setHighestCardValue(result);
							}
							else {
								
								result = findThreeOfAKind(currentPlayer.playerHand);
								if (result != 0) {
									currentPlayer.setHandValue(THREE_OF_A_KIND);
									currentPlayer.setHighestCardValue(result);
								}
								else {
									result = findTwoPairs(currentPlayer.playerHand);
									if (result != 0){
										currentPlayer.setHandValue(TWO_PAIRS);
										currentPlayer.setHighestCardValue(result);
									}
									else {
										
										result = findPair(currentPlayer.playerHand);
										if (result != 0){
											currentPlayer.setHandValue(PAIR);
											currentPlayer.setHighestCardValue(result);
										}
										else {
											currentPlayer.setHandValue(HIGH_CARD);
											currentPlayer.setHighestCardValue(
												findHighestCard(currentPlayer.playerHand));
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
			playerMap.put(currentPlayer, currentPlayer.getHandValue());
		}
		
		//What's the highest hand value among players?
		int highestHandValue = Collections.max(playerMap.values());
		
		//Create list of player(s) who have that hand value
		for (Map.Entry<Player,Integer> entry : playerMap.entrySet() ){
			if (entry.getValue() == highestHandValue) {
				highestHand_list.add(entry.getKey());
			}
		}
				
		//Determine which player has the winning hand
		int highestCardValue = 0;
		
		for (int i = 0; i < highestHand_list.size(); i++) {
			Player temp = highestHand_list.get(i);

			if (temp.getHighestCardValue() > highestCardValue) {
				highestCardValue = temp.getHighestCardValue();
				highestHolder = temp;
			}
			else if (temp.getHighestCardValue() == highestCardValue) {
				if (i == highestHand_list.size()-1) {
					for (Player temp2 : highestHand_list)
						highestCardValue = temp2.getNextHighestCardValue();
					if (highestCardValue != 0) {
						i = -1;
					}
					else
						highestHolder = null;
						
					highestCardValue = 0;
				}
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

		int value;
		
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
		
		int value = 0;
		
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
		
		int result = 0;
		
		if (numOfMatches == 3) {
			for (Map.Entry<Integer,Integer> entry : hm.entrySet()) {
				if (entry.getValue()==numOfMatches) {
					finalValue = entry.getKey();
				}
			}
			
			List<Card> temp = new ArrayList<Card>(0);
			Collections.addAll(temp, hand);
			
			final int tempValue = finalValue;
			
			Predicate<Card> removePred = card -> (card.getCardValue() == tempValue);
			
			temp.removeIf(removePred);
						
			result = findPair(temp.toArray(new Card[0]));
		}
		return (numOfMatches == 3 && result != 0) ? finalValue : 0;
	}
	
	public int findFlush(Card[] hand) {
		
		Suit matchingSuite = hand[0].getCardSuit();
		
		for (int i = 0; i < hand.length; i++) {
			
			if (hand[i].getCardSuit() != matchingSuite)
				return 0;
		}
				
		return 1;
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
		
		int value;
		
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
		boolean done = false;
		
		for(int i = 0; i < hand.length; i++) {
			
			if (done)
				break;
			
			for (int j = i + 1; j < hand.length; j++) {
				
				if(hand[i].getCardValue() == hand[j].getCardValue() && i != j) {
					numOfPairs++;
					highestValue = (highestValue > hand[i].getCardValue()) ? 
									highestValue : hand[i].getCardValue();
					if (numOfPairs == 2) {
						done = true;
						break;
					}
				}
			}
		}
		
		return (numOfPairs == 2) ? highestValue : 0;
	}
	
	public int findPair(Card[] hand) {
		
		int numOfPairs = 0;
		int highestValue = 0;
		boolean done = false;
		
		for(int i = 0; i < hand.length; i++) {
			if (done)
				break;
			
			for (int j = i + 1; j < hand.length; j++) {
				
				if(hand[i].getCardValue() == hand[j].getCardValue() && i != j) {
					numOfPairs++;
					highestValue = hand[i].getCardValue();
					done = true;
					break;
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
	
	public Card[] getCardValues(String [] input) {
		
		if(input.length > 6) {
			System.out.println("Error: Input format invalid");
			return null;
		}
		
		Card[] array = new Card[5];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = new Card();
		}
		
		for (int i = 1; i <= input.length-1; i++) {
			String temp = input[i];
			
			String cardVal = temp.replaceAll("\\D+", "");
			String suitValue = temp.replaceAll("[^A-Z]", "");
			
			if ((!cardVal.isEmpty()) && suitValue.length() > 1) {
				System.out.println("Error: Card format incorrect");
				return null;
			}
			else if (cardVal.isEmpty() && suitValue.length() > 2) {
				System.out.println("Error: Card format incorrect");
				return null;
			}
			else if (suitValue.isEmpty()) {
				System.out.println("Error: Card format incorrect");
				return null;
			}

			if (!array[i-1].setCardValue((cardVal.isEmpty()) ? 
				String.valueOf(temp.charAt(0)) : cardVal) ) {
					System.out.println("Error: Card format incorrect");
					return null;
				}
			if (!array[i-1].setCardSuit((cardVal.isEmpty()) ? 
				String.valueOf(temp.charAt(1)) : suitValue) ) {
					System.out.println("Error: Card format incorrect");
					return null;
				}
		}
				
		return array;
	}
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		//Setup for redirection
		if (in.hasNext()) {
			while (in.hasNext()) {
				String gameInput = in.nextLine();
				
				if (!gameInput.startsWith("Black")) {
					System.out.println("Error: First Player input needs to start with 'Black:'");
					break;
				}
				
				String[] playerBlack = gameInput.substring(0, gameInput.length()/2).trim().split(" ");
				
				String[] playerWhite = gameInput.substring(gameInput.length()/2).trim().split(" ");
				
				if (!playerWhite[0].startsWith("White")) {
					System.out.println("Error: Second player input needs to start with 'White:'");
					break;
				}
				
				PokerGame game = new PokerGame(2, true);
				game.playerList[0].setPlayerName("Black");
				game.playerList[1].setPlayerName("White");
				
				game.playerList[0].playerHand = game.getCardValues(playerBlack);
				game.playerList[1].playerHand = game.getCardValues(playerWhite);
				
				if (game.playerList[0].playerHand == null || game.playerList[1].playerHand == null)
					break;
				
				
				Player winningPlayer = game.determineBestHand(game.playerList);
				
				if (winningPlayer != null) {
					
					if (winningPlayer.getFullHousePair() != 0) {
						System.out.printf("%s wins. - with %s: %s over %s\n\n", winningPlayer.getPlayerName(), 
							winningPlayer.displayHandValue(), winningPlayer.displayHighestValue(),
							winningPlayer.getFullHousePair());
					}
					else {
						System.out.printf("%s wins. - with %s: %s\n\n", winningPlayer.getPlayerName(), 
							winningPlayer.displayHandValue(), winningPlayer.displayHighestValue());
					}
				}
				else {
					System.out.println("It's a tie\n");
				}
			}
		}
				
	}
}

enum Suit {
	SPADES, DIAMONDS, HEARTS, CLUBS
}

class Card {
	
	//MEMBER VARIABLES
	private Suit cardSuit;
	private int cardValue;
	
	public Card() {
		cardSuit = null;
		cardValue = 0;
	}
	
	//MEMBER FUNCTIONS
	public void setCardSuit(Suit s) { cardSuit = s; }
	
	public boolean setCardSuit(String s) {
		
		switch (s) {
			case "H": cardSuit = Suit.HEARTS; break;
			case "D": cardSuit = Suit.DIAMONDS; break;
			case "S": cardSuit = Suit.SPADES; break;
			case "C": cardSuit = Suit.CLUBS; break;
			default: return false;
		}
		
		return true;
	}
	
	public Suit getCardSuit() { return cardSuit; }
	
	public void setCardValue(int val) { cardValue = val; }
	
	public boolean setCardValue(String val) {
		
		switch(val) {
			case "2": cardValue = 2; break;
			case "3": cardValue = 3; break;
			case "4": cardValue = 4; break;
			case "5": cardValue = 5; break;
			case "6": cardValue = 6; break;
			case "7": cardValue = 7; break;
			case "8": cardValue = 8; break;
			case "9": cardValue = 9; break;
			case "10": cardValue = 10; break;
			case "J": cardValue = 11; break;
			case "Q": cardValue = 12; break;
			case "K": cardValue = 13; break;
			case "A": cardValue = 14; break;
			default: return false;
		}
		
		return true;
		
	}
	
	public int getCardValue() { return cardValue; }
	
	public void displayCard() {
		
		if (cardValue >= 2 && cardValue <= 10) {
			System.out.print(cardValue);
		}
		else {
			switch (cardValue) {
				case 11: System.out.printf("Jack"); break;
				case 12: System.out.printf("Queen"); break;
				case 13: System.out.printf("King"); break;
				case 14: System.out.printf("Ace"); break;
			}
		}
		
		switch (cardSuit) {
			case DIAMONDS: System.out.println(" of Diamonds"); break;
			case HEARTS: System.out.println(" of Hearts"); break;
			case SPADES: System.out.println(" of Spades"); break;
			case CLUBS: System.out.println(" of Clubs"); break;
		}
	}
	
}

class Player {
	
	protected Card[] playerHand;
	private int handValue;
	private int highestCardValue;
	private int fullHousePair;
	private String playerName;
	
	public Player() {
		playerHand = new Card[5];
		handValue = 0;
		highestCardValue = 0;
		fullHousePair = 0;
		playerName = "";
	}
	
	//Set functions
	public void setPlayerName(int index) { playerName = "Player " + index; }
	
	public void setPlayerName(String name) { playerName = name; }
	
	public void setHandValue(int value) { handValue = value; }
	
	public void setHighestCardValue(int value) { highestCardValue = value; }
	
	public void setFullHousePair(int value) { fullHousePair = value; }
	
	//Get functions
	public String getPlayerName() { return playerName; }
	
	public int getHandValue() { return handValue; }
	
	public int getHighestCardValue() { return highestCardValue; }
	
	public int getFullHousePair() { return fullHousePair; }
	
	//Member functions
	public String displayHandValue() {
		
		String str = "";
		
		switch(handValue) {
			case PokerGame.STRAIGHT_FLUSH: str = "Straight Flush"; break;
			case PokerGame.FOUR_OF_A_KIND: str = "Four Of A Kind"; break;
			case PokerGame.FULL_HOUSE: str = "Full House"; break;
			case PokerGame.FLUSH: str = "Flush"; break;
			case PokerGame.STRAIGHT: str = "Straight"; break;
			case PokerGame.THREE_OF_A_KIND: str = "Three Of A Kind"; break;
			case PokerGame.TWO_PAIRS: str = "Two Pairs"; break;
			case PokerGame.PAIR: str = "Pair"; break;
			default: str = "High Card";
		}
		
		return str;
	}
	
	public String displayHighestValue() {
		String str = "";
		
		switch(highestCardValue) {
			case 2: str = "2"; break;
			case 3: str = "3"; break;
			case 4: str = "4"; break;
			case 5: str = "5"; break;
			case 6: str = "6"; break;
			case 7: str = "7"; break;
			case 8: str = "8"; break;
			case 9: str = "9"; break;
			case 10: str = "10"; break;
			case 11: str = "Jack"; break;
			case 12: str = "Queen"; break;
			case 13: str = "King"; break;
			case 14: str = "Ace"; break;
		}
		
		return str;
	}
	
	public int getNextHighestCardValue() {
		
		int tempMax = 0;
		
		for (Card tempCard : playerHand) {
			if (tempCard.getCardValue() > tempMax && 
				tempCard.getCardValue() < highestCardValue) {
					tempMax = tempCard.getCardValue();
			}
		}
		
		highestCardValue = tempMax;
		
		return highestCardValue;
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