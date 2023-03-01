package it.unipr.assignment.one;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PriceWriterImpl extends UnicastRemoteObject implements PriceWriter 
{
	private static final long serialVersionUID = 1L;
	
	private int price;
	private int purchaseOffer;
	private int purchaseDone;

	public PriceWriterImpl() throws RemoteException
	{
		price = 0;
		purchaseOffer = 0;
		purchaseDone = 0;
	}
	
	public void setPrice(final int newPrice)
	{
		price = newPrice;
	}
	
	public void setPurchaseOffer(final int offer)
	{
		purchaseOffer = offer;
	}
	
	public void addPurchaseDone()
	{
		purchaseDone++;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public int getPurchaseOffer()
	{
		return purchaseOffer;
	}
	
	public int getPurchaseDone()
	{
		return purchaseDone;
	}
}
