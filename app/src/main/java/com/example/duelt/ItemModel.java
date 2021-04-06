package com.example.duelt;

public class ItemModel {
    private int currency;
    private int food;
    private int toy;



    public ItemModel(int currency, int food, int toy) {
        this.currency = currency;
        this.food = food;
        this.toy = toy;
    }


    //shop getter
    public int getFood(){return food; }

    public int getToy(){return toy; }

    public int getCurrency(){return currency; }

    //shop setter
    public void  setCurrency(int currency) { this.currency = currency; }

    public void setFood(int food) { this.food = food; }

    public void setToy(int toy) {this.toy = toy;}
}
