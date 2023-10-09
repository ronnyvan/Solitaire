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
	public void createStock(){
		deck = new ArrayList<Card>();
		String[] suits = new String[]{"h", "d", "c", "s"};
		for(int rank = 1; rank <= 13; rank++){
			for(int suitIndex = 0; suitIndex <= 3; suitIndex++){
				deck.add(new Card(rank, suits[suitIndex]));
			}
		}
		while(deck.size() != 0){
			int randIndex = (int) (Math.random()*deck.size());
			stock.push(deck.remove(randIndex));
		}
	}

	// returns the card on top of the stock,
	// or null if the stock is empty
	public Card getStockCard() {
		if (stock.isEmpty())
			return null;
		return stock.peek();
	}

	// returns the card on top of the waste,
	// or null if the waste is empty
	public Card getWasteCard() {
		if (waste.isEmpty())
			return null;
		return waste.peek();
	}
	public void deal(){
		for(int i = 0; i < piles.length; i++){
			int pileCount = i + 1;
			for(int j = 0; j < pileCount; j++){
				piles[i].push(stock.pop());
			}
			piles[i].peek().turnUp();
		}
	}
	public void dealThreeCards(){
		int count = 0;
		while(!stock.isEmpty() && count < 3){
			Card card = stock.pop();
			card.turnUp();
			waste.push(card);
			count++;
		}
	}
	public void resetStock(){
		for(int i = 0; i < waste.size(); i++){
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
		if(foundations[index] != null) {return null;}
		return foundations[index].peek();
	}

	// precondition: 0 <= index < 7
	// postcondition: returns a reference to the given pile
	public Stack<Card> getPile(int index) {
		return piles[index];
	}

	// called when the stock is clicked
	public void stockClicked() {
		if(!display.isWasteSelected() && !display.isPileSelected()){
			if(stock.size() > 0){
				dealThreeCards();
			} else resetStock();
		}
		System.out.println("stock clicked");
	}

	// called when the waste is clicked
	public void wasteClicked() {
		// IMPLEMENT ME
		if(!display.isPileSelected()){
			if(display.isWasteSelected()) display.unselect();
			else if(!waste.isEmpty()) display.selectWaste();
		}
		System.out.println("waste clicked");
	}

	// precondition: 0 <= index < 4
	// called when given foundation is clicked
	public void foundationClicked(int index) {
		// IMPLEMENT ME
		System.out.println("foundation #" + index + " clicked");
	}

	// precondition: 0 <= index < 7
	// called when given pile is clicked
	public void pileClicked(int index) {
		if(!display.isWasteSelected()){
			if(display.isPileSelected()){
				if(display.selectedPile() == index) display.unselect();
				else display.selectPile(index);
			} else display.selectPile(index);
		} 
		if(waste.isEmpty()) return;
		// IMPLEMENT ME
		Card card = waste.peek();
		if(canAddToPile(card, index)){
			if(display.isWasteSelected()){
				card = waste.pop();
				piles[index].push(card);
				display.unselect();
			}
		}
		System.out.println("pile #" + index + " clicked");
	}
	private boolean canAddToPile(Card card, int index){
		Card topCard = piles[index].peek();
		if(!topCard.isFaceUp && card.suit.equals("k")) return true; 
		if(topCard.rank == 1) return false;
		if(topCard.isRed()){
			return (!card.isRed()) && card.rank == topCard.rank - 1;
		} 
		if(!topCard.isRed()){
			return (card.isRed()) && card.rank == topCard.rank - 1;
		} 
		return true;
	}
}
