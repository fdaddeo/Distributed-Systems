package it.unipr.assignment.two;

import java.io.Serializable;

public class Price implements Serializable
{
    private int serverPrice;

    public Price(int randomPrice)
    {
        this.serverPrice = randomPrice;
    }

    public int getServerPrice()
    {
        return this.serverPrice;
    }

    public void setServerPrice(int randomPrice)
    {
        this.serverPrice = randomPrice;
    }
}
