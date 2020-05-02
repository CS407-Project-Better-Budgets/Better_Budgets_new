package com.example.better_budgets;

public class Spending {
    private String id;
    private String date;
    private String source;
    private double amount;
    private String seller;
    public Spending(String id, String date, String source, double amount, String seller){
        this.id = id;
        this.date = date;
        this.source = source;
        this.amount = amount;
        this.seller = seller;
    }
    public String getDate(){return date;}
    public String getId(){return id;}
    public String getSource(){return source;}
    public double getAmount(){return amount;}
    public String getSeller(){return seller;}
}
