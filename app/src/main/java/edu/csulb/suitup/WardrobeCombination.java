package edu.csulb.suitup;

import java.util.Objects;

/**
 * A Wardrobe Combination class
 * consisting of combinations of tops, bottom, and shoes
 */

public class WardrobeCombination {
    private int top_id;
    private int bottom_id;
    private int shoes_id;

    public WardrobeCombination(int top, int bottom, int shoes){
        top_id = top;
        bottom_id = bottom;
        shoes_id = shoes;
    }

    public int getTop(){
        return top_id;
    }

    public int getBottom(){
        return bottom_id;
    }

    public int getShoes(){
        return shoes_id;
    }

    @Override
    public boolean equals(Object o){
        WardrobeCombination obj = (WardrobeCombination) o;
        if((obj.getShoes() == this.getShoes()) &&
                (obj.getTop() == this.getTop()) &&
                (obj.getBottom() == this.getBottom())){
            return true;
        }
        return false;
    }
}
