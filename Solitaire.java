import java.util.*;

public class Solitaire {
	public static void main(String[] args) {
		new Solitaire();

	}
	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;
	private List<Card> deck;
	

	public Solitaire() {
		foundations = new Stack[4];
		piles = new Stack[7];
		stock = new Stack<Card>();
		waste = new Stack<Card>();
		display = new SolitaireDisplay(this);

		for (int i = 0; i < foundations.length; i++) {
			foundations[i] = new Stack<Card>();
		}
		for (int i = 0; i < piles.length; i++) {
			piles[i] = new Stack<Card>();
		}
		createStock();
		deal();
	}

	public void createStock() {
		deck = new ArrayList<Card>();
		String[] suits = new String[] { "h", "d", "c", "s" };
		for (int rank = 1; rank <= 13; rank++) {
			for (String suit : suits) 
				deck.add(new Card(rank, suit));
			
		}
		while (deck.size() != 0) {
			int randIndex = (int) (Math.random() * deck.size());
			stock.push(deck.remove(randIndex));
		}
	}

	// returns the card on top of the stock,
	// or null if the stock is empty
	public Card getStockCard() {
		return stock.isEmpty() ? null : stock.peek();
	}

	// returns the card on top of the waste,
	// or null if the waste is empty
	public Card getWasteCard() {
		return waste.isEmpty() ? null : waste.peek();
	}

	public void deal() {
		for (int i = 0; i < piles.length; i++) {
			int pileCount = i + 1;
			for (int j = 0; j < pileCount; j++) {
				piles[i].push(stock.pop());
			}
			piles[i].peek().turnUp();
		}
	}

	public void dealThreeCards() {
		int count = 0;
		while (!stock.isEmpty() && count++ < 3) {
			Card card = stock.pop();
			card.turnUp();
			waste.push(card);
		}
	}

	public void resetStock() {
		while (!waste.isEmpty()) {
			Card card = waste.pop();
			card.turnDown();
			stock.push(card);

		}
	}

	// precondition: 0 <= index < 4
	// postcondition: returns the card on top of the given
	// foundation, or null if the foundation
	// is empty
	public Card getFoundationCard(int index) {
		return foundations[index].isEmpty() ? null : foundations[index].peek();
	} 

	// precondition: 0 <= index < 7
	// postcondition: returns a reference to the given pile
	public Stack<Card> getPile(int index) {
		return piles[index];
	}

	// called when the stock is clicked
	public void stockClicked() {
		if (!display.isWasteSelected() && !display.isPileSelected()) {
			if (!stock.isEmpty()) 
				dealThreeCards();
			 else
				resetStock();
		}
		System.out.println("stock clicked");
	}

	// called when the waste is clicked
	public void wasteClicked() {

		if (!display.isPileSelected()) {
			if (display.isWasteSelected())
				display.unselect();
			else if (!waste.isEmpty())
				display.selectWaste();
		}
		System.out.println("waste clicked");
	}

	// precondition: 0 <= index < 4
	// called when given foundation is clicked
	public void foundationClicked(int index) {
		System.out.println("foundation #" + index + " clicked");
		if (display.isWasteSelected()) {
			if (canAddToFoundation(getWasteCard(), index)) {
				foundations[index].push(waste.pop());
				display.unselect();
			}
			return;
		}
		if (display.isPileSelected()) {
			Stack<Card> currentPile = getPile(display.selectedPile());
			Card topCard = currentPile.peek();
			if (canAddToFoundation(topCard, index)) {
				foundations[index].push(currentPile.pop());
				display.unselect();
			}
			return;
		}
	}

	// precondition: 0 <= index < 7
	// called when given pile is clicked
	public void pileClicked(int index) {
		// System.out.println("pile #" + index + " clicked");
		if (display.isWasteSelected()) {
			Card card = getWasteCard();
			if (canAddToPile(card, index)) {
				getPile(index).push(waste.pop());
				display.unselect();
			}
			return;
		}
		if (display.isPileSelected()) {
			if (display.selectedPile() == index)
				display.unselect();
			else
				addToPile(removeFaceUpCards(display.selectedPile()), index);
			return;
		}
		if (getPile(index).isEmpty()) 
			return;
		

		Card topCard = getPile(index).peek();
		if (!topCard.isFaceUp) {
			topCard.turnUp();
			return;
		}
		display.selectPile(index);
	}

	private boolean canAddToPile(Card card, int index) {
		Card topCard = (getPile(index).isEmpty()) ? null : getPile(index).peek();
		if (topCard == null || !topCard.isFaceUp)
			return (card.rank == 13);
		if (topCard.rank == 1)
			return false;
		return topCard.isRed() != card.isRed() && card.rank == topCard.rank - 1;
	}

	private Stack<Card> removeFaceUpCards(int index) {
		Stack<Card> pile = getPile(index);
		Stack<Card> dummyStack = new Stack<>();
		while (!pile.isEmpty() && pile.peek().isFaceUp()) {
			dummyStack.push(pile.pop());
		}
		return dummyStack;
	}

	private void addToPile(Stack<Card> cards, int index) {
		int pileIndex = (canAddToPile(cards.peek(), index)) ? index : display.selectedPile();
		int count = 0;
		while (!cards.isEmpty()) {
			getPile(pileIndex).push(cards.pop());
			count++;
		}
		display.unselect();
	}

	private boolean canAddToFoundation(Card card, int index) {
		return foundations[index].isEmpty()
				? (card.rank == 1)
				: (getFoundationCard(index).getSuit().equals(card.getSuit())
						&& card.rank == getFoundationCard(index).rank + 1);
	}
}
