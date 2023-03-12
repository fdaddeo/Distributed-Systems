package it.unipr.assignment.two;

import java.io.Serializable;

public class Response implements Serializable
{
    private int serverPrice;
    private int clientOffer;
    
    private boolean offerAccepted;
    private boolean closeConnection;

    public Response(int serverPrice, int clientOffer, boolean offerAccepted)
    {
        this.serverPrice = serverPrice;
        this.clientOffer = clientOffer;
        this.offerAccepted = offerAccepted;
        this.closeConnection = false;
    }

    public int getServerPrice()
    {
        return this.serverPrice;
    }

    public int getClientOffer()
    {
        return this.clientOffer;
    }

    public boolean getOfferAccepted()
    {
        return this.offerAccepted;
    }

    public void setOfferAccepted(boolean status)
    {
        this.offerAccepted = status;
    }

    public boolean getCloseConnection()
    {
        return this.closeConnection;
    }

    public void setCloseConnection(boolean closeConnection)
    {
        this.closeConnection = closeConnection;
    }
}
