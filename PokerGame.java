class PokerGame {
	
	public Player[] playerList;
	public CardDeck deck;
	
	public PokerGame(int numOfPlayers) {
		playerList = new Player[numOfPlayers];
		deck = new CardDeck();
	}
	
	public void determineBestHand() {
		
	}
	
	public static void main(String[] args) {
		
		PokerGame game = new PokerGame(2);
		
	}
}

class Player {
	
	public Card[] playerHand;
	
	public Player() {
		playerHand = new Card[5];
	}
}