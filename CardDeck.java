import java.util.*;

enum Suit {
	SPADES, DIAMONDS, HEARTS, CLUBS
}

class Card {
	
	//MEMBER VARIABLES
	private Suit cardSuit;
	private int cardValue;
	
	//MEMBER FUNCTIONS
	public void setCardSuit(Suit s) { cardSuit = s; }
	
	public Suit getCardSuit() { return cardSuit; }
	
	public void setCardValue(int val) {
		cardValue = val;
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

class CardDeck {
	
	private Card[] cards;
	private int topCard;
	
	public CardDeck() {
		
		topCard = 0;
		cards = new Card[52];
		
		for (int i = 0; i < 52; i++) {
			//Set value between 2 - 14
			cards[i].setCardValue((i % 13) + 2);
			
			switch (i / 13) {
				case 0: cards[i].setCardSuit(Suit.DIAMONDS); break;
				case 1: cards[i].setCardSuit(Suit.HEARTS); break;
				case 2: cards[i].setCardSuit(Suit.SPADES); break;
				case 3: cards[i].setCardSuit(Suit.CLUBS); break;
			}
		}
		
	}
	
	public void shuffleDeck() {
		
		Random rand = new Random();
		
		//Shuffle 4 times
		for (int i = 0; i < 4; i++) {
			
			for (int j = 0; j < 52; j++) {
				int randomNum = rand.nextInt() % 52;
				
				Card temp = cards[j];
				cards[j] = cards[randomNum];
				cards[randomNum] = temp;
				
			}
			
		}
		
		topCard = 0;
		
	}
	
	public void shuffleRemainingDeck() {
		
		Random rand = new Random();
		
		//Shuffle 4 times
		for (int i = 0; i < 4; i++) {
			
			for (int j = topCard; j < 52; j++) {
				int randomNum = rand.nextInt() % (52 - topCard) + topCard;
				
				Card temp = cards[j];
				cards[j] = cards[randomNum];
				cards[randomNum] = temp;
			}
			
		}
	}
	
	public Card dealCard() {
		Card temp;
		
		temp = cards[topCard];
		topCard++;
		return temp;
	}
	
	public int getTopCard() { return topCard; }

}