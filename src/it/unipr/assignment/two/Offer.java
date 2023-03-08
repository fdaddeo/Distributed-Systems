package it.unipr.assignment.two;

import java.io.Serializable;

public class Offer implements Serializable
{
    private int price;
    private int offer;

    private boolean offerAccepted;

    public Offer(int price)
    {
        this.price = price;
        this.offer = 0;
        this.offerAccepted = false;
    }

    public int getPrice()
    {
        return this.price;
    }

    public void setPrice(int newPrice)
    {
        this.price = newPrice;
    }

    public int getOffer()
    {
        return this.offer;
    }

    public void setOffer(int newOffer)
    {
        this.offer = newOffer;
    }

    public void setOfferAccepted(boolean status)
    {
        this.offerAccepted = status;
    }

    public boolean getOfferAccepted()
    {
        return this.offerAccepted;
    }
}
