package board;

import java.util.ArrayList;
import cards.Card;
import cards.Pan;

public class Display implements Displayable {

    private ArrayList<Card> displayList;

    public Display() {
        this.displayList = new ArrayList<Card>();
        this.displayList.add(new Pan());
    }

    @Override
    public void add(Card card) {
        this.displayList.add(card);
    }

    @Override
    public int size() {
        return this.displayList.size();
    }

    @Override
    public Card getElementAt(int index) {
        return this.displayList.get(index);
    }

    @Override
    public Card removeElement(int index) {
        return this.displayList.remove(index);
    }

}
