package it.unipr.assignment.one;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PriceWriter extends Remote
{
	void setPrice(final int newPrice) throws RemoteException;
	void setPurchaseOffer(final int offer) throws RemoteException;
	void addPurchaseDone() throws RemoteException;
	int getPrice() throws RemoteException;
	int getPurchaseOffer() throws RemoteException;
	int getPurchaseDone() throws RemoteException;
}
