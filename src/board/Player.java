package board;

import cards.BirchBolete;
import cards.Card;
import cards.Stick;
import cards.CardType;
import cards.Chanterelle;
import cards.HenOfWoods;
import cards.HoneyFungus;
import cards.LawyersWig;
import cards.Morel;
import cards.Mushroom;
import cards.Pan;
import cards.Porcini;
import cards.Shiitake;
import cards.TreeEar;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {

    private Hand h;
    private Display d;
    private int score;
    private int handlimit;
    private int sticks;

    public Player() {
        this.h = new Hand();
        this.d = new Display();
        this.score = 0;
        this.handlimit = 8;
        this.sticks = 0;
    }

    public int getScore() {
        return this.score;
    }

    public int getHandLimit() {
        return this.handlimit;
    }

    public int getStickNumber() {
        return this.sticks;
    }

    public void addSticks(int amount) {
        this.sticks += amount;
        for (int i = 0; i < amount; i++) {
            this.d.add(new Stick());
        }
    }

    public void removeSticks(int amount) {
        this.sticks -= amount;
        for (int i = 0; i < amount; i++) {
            for (int e = 0; e < this.d.size(); e++) {
                if (this.d.getElementAt(e).getType() == CardType.STICK) {
                    this.d.removeElement(e);
                    break;
                }
            }
        }
    }

    public Hand getHand() {
        return this.h;
    }

    public Display getDisplay() {
        return this.d;
    }

    public void addCardtoHand(Card card) {
        if (card.getType() == CardType.BASKET) {
            this.handlimit += 2;
            this.d.add(card);
        } else {
            this.h.add(card);
        }
    }

    public void addCardtoDisplay(Card card) {
        this.d.add(card);
    }

    public boolean takeCardFromTheForest(int positionOfCard) {

        Card cardToPick = Board.getForest().getElementAt(positionOfCard - 1);

        // If the position is not between 1 and 8, return false
        if (positionOfCard < 1 || positionOfCard > 8) {
            return false;
        }

        if (this.getHandLimit() <= this.getHand().size()) {
            return false;
        }

        // Else if it's not and we have space un hand, check if it is in our FEET or in
        // DEEP FOREST
        else if (this.h.size() < this.handlimit) {
            // If in our FEET, check if it is a basket or not
            if (positionOfCard <= 2) {
                // If it is a basket, add it to Display
                if (cardToPick.getType() == CardType.BASKET) {
                    this.addCardtoDisplay(cardToPick);
                    this.handlimit += 2;
                    return true;
                }
                // Else (if not), add it to Hand
                else {
                    this.h.add(cardToPick);
                    return true;
                }
            }
            // Else (in DEEP FOREST), check if enough sticks
            else {
                int sticksNeeded = positionOfCard - 2;
                // If not enough sticks, return false
                if (sticksNeeded > this.getStickNumber()) {
                    return false;
                }
                // Else (if enough) and if it is a basket, add card to display, +2 to hand
                // limit,
                // use the sticks and return true
                else if (cardToPick.getType() == CardType.BASKET) {
                    this.addCardtoDisplay(cardToPick);
                    this.handlimit += 2;
                    this.removeSticks(sticksNeeded);
                    return true;
                }
                // Else (if enough and not basket), add card to hand, use the sticks and return
                // true
                else {
                    this.h.add(cardToPick);
                    this.removeSticks(sticksNeeded);
                    return true;
                }
            }
        }
        // Else (no place in hand), return false
        else {
            return false;
        }
    }

    public boolean takeFromDecay() {

        int notBasketCards = 0;
        int basketCards = 0;

        // Check for basket in hands, and if there is, move them to display and increase
        // limit :
        // for (int index = 1; index <= this.h.size(); index++) {
        // if (this.h.getElementAt(index).getType() == CardType.BASKET) {
        // this.d.add(this.h.getElementAt(index));
        // this.h.removeElement(this.h.size() - index);
        // this.handlimit += 2;
        // }
        // }

        // Check for baskets and non-baskets in decay pile :
        for (Card card : Board.getDecayPile()) {
            if (card.getType() == CardType.BASKET) {
                basketCards++;
            } else {
                notBasketCards++;
            }
        }

        if (this.h.size() + notBasketCards <= this.handlimit + (2 * basketCards)) {
            this.handlimit += 2 * basketCards;
            for (Card card : Board.getDecayPile()) {
                if (card.getType() != CardType.BASKET) {
                    this.h.add(card);
                } else {
                    this.d.add(card);
                }
            }
            Board.getDecayPile().clear();
            return true;
        } else {
            return false;
        }
    }

    public boolean cookMushrooms(ArrayList<Card> listOfCards) {

        // Check if there is a PAN on DISPLAY :
        boolean isPanOnDisplay = false;
        for (int index = 0; index < this.d.size(); index++) {
            if (this.d.getElementAt(index).getType() == CardType.PAN) {
                isPanOnDisplay = true;
            }
        }

        // Check if there is a PAN in ArrayList :
        boolean isPanOnList = false;
        for (Card card : listOfCards) {
            if (card.getType() == CardType.PAN) {
                isPanOnList = true;
            }
        }

        // Check if mushroom are identical and get the total mushroom count :
        boolean mushroomsAreIdentical = true;
        Card initialMushroom = null;
        int mushroomsCount = 0;
        for (Card card : listOfCards) {
            if (card.getType() == CardType.DAYMUSHROOM
                    || card.getType() == CardType.NIGHTMUSHROOM) {

                // Check if identical
                if (initialMushroom == null) {
                    initialMushroom = card;
                } else {
                    if (initialMushroom.getName() != card.getName()) {
                        mushroomsAreIdentical = false;
                    }
                }

                // Get total count
                if (card.getType() == CardType.DAYMUSHROOM) {
                    mushroomsCount += 1;
                } else {
                    mushroomsCount += 2;
                }

            }
        }

        // Check if Cider or Butter in listOfCards :
        boolean isCiderInCards = false;
        boolean isButterInCards = false;
        for (Card card : listOfCards) {
            if (card.getType() == CardType.BUTTER) {
                isButterInCards = true;
            }
            if (card.getType() == CardType.CIDER) {
                isCiderInCards = true;
            }
            // Check if there is no unappropriate card in the list of cards :
            if (card.getType() == CardType.BASKET || card.getType() == CardType.STICK) {
                return false;
            }
        }

        // Create score dictionnary :
        HashMap<String, Mushroom> scores = new HashMap<String, Mushroom>();
        scores.put("birchbolete", new BirchBolete(CardType.DAYMUSHROOM));
        scores.put("chanterelle", new Chanterelle(CardType.DAYMUSHROOM));
        scores.put("henofwoods", new HenOfWoods(CardType.DAYMUSHROOM));
        scores.put("honeyfungus", new HoneyFungus(CardType.DAYMUSHROOM));
        scores.put("lawyerswig", new LawyersWig(CardType.DAYMUSHROOM));
        scores.put("morel", new Morel(CardType.DAYMUSHROOM));
        scores.put("porcini", new Porcini(CardType.DAYMUSHROOM));
        scores.put("shiitake", new Shiitake(CardType.DAYMUSHROOM));
        scores.put("treeear", new TreeEar(CardType.DAYMUSHROOM));

        // Check if we can cook, and update the score :
        boolean isButterUsed = false;
        boolean isCiderUsed = false;
        if (!isPanOnDisplay && !isPanOnList) {
            return false;
        } else if (mushroomsCount < 3 || !mushroomsAreIdentical) {
            return false;
        } else if (mushroomsCount >= 9 && isCiderInCards && isButterInCards) {
            this.score += 3; // Score added by the Butter
            this.score += 5; // Score added by the Cider
            this.score += mushroomsCount * scores.get(initialMushroom.getName()).getFlavourPoints();
            isCiderUsed = true;
            isButterUsed = true;
        } else if (mushroomsCount >= 5 && isCiderInCards && !isButterInCards) {
            this.score += 5; // Score added by the Cider
            this.score += mushroomsCount * scores.get(initialMushroom.getName()).getFlavourPoints();
            isCiderUsed = true;
        } else if (mushroomsCount >= 4 && isButterInCards && !isCiderInCards) {
            this.score += 3; // Score added by the Butter
            this.score += mushroomsCount * scores.get(initialMushroom.getName()).getFlavourPoints();
            isButterUsed = true;
        } else if (mushroomsCount >= 3 && !isButterInCards && !isCiderInCards) {
            this.score += mushroomsCount * scores.get(initialMushroom.getName()).getFlavourPoints();
        } else {
            return false;
        }

        // Remove the cider or butter if used :
        for (int index = 0; index < this.h.size(); index++) {
            if (this.h.getElementAt(index).getType() == CardType.CIDER && isCiderUsed) {
                this.h.removeElement(this.h.size() - index);
                break;
            }
            if (this.h.getElementAt(index).getType() == CardType.BUTTER && isButterUsed) {
                this.h.removeElement(this.h.size() - index);
                break;
            }
        }

        // Remove used mushrooms :
        int nightMushroomCount = 0;
        int dayMushroomCount = 0;
        // Count number of day and night mushrooms
        for (Card card : listOfCards) {
            if (card.getType() == CardType.NIGHTMUSHROOM) {
                nightMushroomCount++;
            } else if (card.getType() == CardType.DAYMUSHROOM) {
                dayMushroomCount++;
            }
        }
        ArrayList<Integer> indexElementsToRemove = new ArrayList<Integer>();
        for (int index = 0; index < this.h.size(); index++) {
            if (this.h.getElementAt(index).getType() == CardType.DAYMUSHROOM && dayMushroomCount > 0
                    && this.h.getElementAt(index).getName() == initialMushroom.getName()) {
                indexElementsToRemove.add(index);
                dayMushroomCount--;
            } else if (this.h.getElementAt(index).getType() == CardType.NIGHTMUSHROOM
                    && nightMushroomCount > 0
                    && this.h.getElementAt(index).getName() == initialMushroom.getName()) {
                indexElementsToRemove.add(index);
                nightMushroomCount--;
            }
        }
        int count = 0;
        for (int index : indexElementsToRemove) {
            index = index - count;
            this.h.removeElement(index);
            count++;
        }

        if (isPanOnList) {
            for (int i = 0; i < this.getHand().size(); i++) {
                if (this.getHand().getElementAt(i).getType() == CardType.PAN) {
                    this.getHand().removeElement(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < this.getDisplay().size(); i++) {
                if (this.getDisplay().getElementAt(i).getType() == CardType.PAN) {
                    this.getDisplay().removeElement(i);
                    break;
                }
            }
        }

        // Return true :
        return true;

    }

    public boolean sellMushrooms(String string, int integer) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        string = string.toLowerCase();

        // Make the string input valid :
        String result = "";
        for (char c : string.toCharArray()) {
            if (new String(alphabet).contains(Character.toString(c))) {
                result += Character.toString(c);
            }
        }
        string = result;

        // Check if selling mushrooms of the same type and have at least 2 :
        int mushroomsSellingCount = 0;
        for (int index = 0; index < this.h.size(); index++) {
            if (this.h.getElementAt(index).getName().equals(string)) {
                if (this.h.getElementAt(index).getType() == CardType.DAYMUSHROOM) {
                    mushroomsSellingCount++;
                } else if (this.h.getElementAt(index).getType() == CardType.NIGHTMUSHROOM) {
                    mushroomsSellingCount += 2;
                }
            }
        }

        // Create sticks dictionnary :
        HashMap<String, Mushroom> scores = new HashMap<String, Mushroom>();
        scores.put("birchbolete", new BirchBolete(CardType.DAYMUSHROOM));
        scores.put("chanterelle", new Chanterelle(CardType.DAYMUSHROOM));
        scores.put("henofwoods", new HenOfWoods(CardType.DAYMUSHROOM));
        scores.put("honeyfungus", new HoneyFungus(CardType.DAYMUSHROOM));
        scores.put("lawyerswig", new LawyersWig(CardType.DAYMUSHROOM));
        scores.put("morel", new Morel(CardType.DAYMUSHROOM));
        scores.put("porcini", new Porcini(CardType.DAYMUSHROOM));
        scores.put("shiitake", new Shiitake(CardType.DAYMUSHROOM));
        scores.put("treeear", new TreeEar(CardType.DAYMUSHROOM));

        // Check if can't sell, and if we can then add sticks :
        if (mushroomsSellingCount >= integer && integer >= 2) {
            this.addSticks(integer * scores.get(string).getSticksPerMushroom());
        } else {
            return false;
        }

        // Remove mushrooms from hand once mushrooms are sold :
        ArrayList<Integer> indexElementsToRemove = new ArrayList<Integer>();
        for (int index = 0; index < this.h.size(); index++) {
            if (this.h.getElementAt(index).getName().equals(string)
                    && this.h.getElementAt(index).getType() == CardType.DAYMUSHROOM
                    && integer > 0) {
                indexElementsToRemove.add(index);
                integer--;
            } else if (this.h.getElementAt(index).getName().equals(string)
                    && this.h.getElementAt(index).getType() == CardType.NIGHTMUSHROOM
                    && integer > 0) {
                indexElementsToRemove.add(index);
                integer -= 2;
            }
        }

        // Remove mushrooms from hand :
        int count = 0;
        for (int index : indexElementsToRemove) {
            index = index - count;
            this.h.removeElement(index);
            count++;
        }

        // Return true :
        return true;
    }

    public boolean putPanDown() {
        // If the player's hand is empty, then return false :
        if (this.h.size() <= 0) {
            return false;
        }
        // Check if there is a pan in player's hand :
        for (int index = 0; index < this.h.size(); index++) {
            // If there is, delete it from hand, add it to display, and return true
            if (this.h.getElementAt(index).getType() == CardType.PAN) {
                this.h.removeElement(index);
                this.d.add(new Pan());
                return true;
            }
        }

        // Else :
        return false;
    }

}
