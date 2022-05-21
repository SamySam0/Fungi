package board;

import cards.Card;
import java.util.ArrayList;

public class CardList {

    private ArrayList<Card> cList;

    public CardList() {
        this.cList = new ArrayList<Card>();
    }

    public void add(Card card) {
        this.cList.add(card);
    }

    public int size() {
        return this.cList.size();
    }

    public Card getElementAt(int index) {
        return this.cList.get(index);
    }

    public Card removeCardAt(int index) {
        return this.cList.remove(this.size() - index);
    }

    public void set(int index, Card c) {
        this.cList.set(index, c);
    }

}
