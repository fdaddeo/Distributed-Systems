package it.unipr.assignment.two;

import java.io.Serializable;

public class Offer implements Serializable
{
    private int serverPrice;
    private int clientOffer;

    public Offer(int serverPrice, int clientOffer)
    {
        this.serverPrice = serverPrice;
        this.clientOffer = clientOffer;
    }

    public int getServerPrice()
    {
        return this.serverPrice;
    }

    public void setServerPrice(int randomPrice)
    {
        this.serverPrice = randomPrice;
    }

    public int getClientOffer()
    {
        return this.clientOffer;
    }

    public void setClientOffer(int offer)
    {
        this.clientOffer = offer;
    }
}
