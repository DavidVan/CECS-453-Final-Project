package edu.csulb.suitup;

/**
 * A Wardrobe Combination class
 * consisting of combinations of shirts, pants, and shoes
 */

public class WardrobeCombination {
    private int shirt_id;
    private int pants_id;
    private int shoes_id;

    public WardrobeCombination(int shirt, int pants, int shoes){
        shirt_id = shirt;
        pants_id = pants;
        shoes_id = shoes;
    }

    public int getShirt(){
        return shirt_id;
    }

    public int getPants(){
        return pants_id;
    }

    public int getShoes(){
        return shoes_id;
    }
}
