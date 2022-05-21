package board;

import cards.*;
import java.util.ArrayList;

public class Board {
    private static CardPile forestCardsPile;
    private static CardList forest;
    private static ArrayList<Card> decayPile;

    public static void initialisePiles() {
        forestCardsPile = new CardPile();
        forest = new CardList();
        decayPile = new ArrayList<Card>();
    }

    public static void setUpCards() {
        // ADD MUSHROOM CARDS :
        for (int i = 0; i < 10; i++) {
            forestCardsPile.addCard(new HoneyFungus(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new HoneyFungus(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 8; i++) {
            forestCardsPile.addCard(new TreeEar(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new TreeEar(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 6; i++) {
            forestCardsPile.addCard(new LawyersWig(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new LawyersWig(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 5; i++) {
            forestCardsPile.addCard(new Shiitake(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new Shiitake(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 5; i++) {
            forestCardsPile.addCard(new HenOfWoods(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new HenOfWoods(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 4; i++) {
            forestCardsPile.addCard(new BirchBolete(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new BirchBolete(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 4; i++) {
            forestCardsPile.addCard(new Porcini(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new Porcini(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 4; i++) {
            forestCardsPile.addCard(new Chanterelle(CardType.DAYMUSHROOM));
        }
        forestCardsPile.addCard(new Chanterelle(CardType.NIGHTMUSHROOM));
        for (int i = 0; i < 3; i++) {
            forestCardsPile.addCard(new Morel(CardType.DAYMUSHROOM));
        }

        // ADD OTHER CARDS:

        for (int i = 0; i < 3; i++) {
            forestCardsPile.addCard(new Butter());
        }
        for (int i = 0; i < 3; i++) {
            forestCardsPile.addCard(new Cider());
        }
        for (int i = 0; i < 11; i++) {
            forestCardsPile.addCard(new Pan());
        }
        for (int i = 0; i < 5; i++) {
            forestCardsPile.addCard(new Basket());
        }

    }

    public static CardPile getForestCardsPile() {
        return forestCardsPile;
    }

    public static CardList getForest() {
        return forest;
    }

    public static ArrayList<Card> getDecayPile() {
        return decayPile;
    }

    public static void updateDecayPile() {
        if (decayPile.size() >= 4) {
            decayPile.clear();
        }
        decayPile.add(forest.removeCardAt(8));
    }

}
