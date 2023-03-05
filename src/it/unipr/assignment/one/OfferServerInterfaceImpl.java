package it.unipr.assignment.one;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.Set;

public class OfferServerInterfaceImpl extends UnicastRemoteObject implements OfferServerInterface
{
    private static final long serialVersionUID = 1L;
	
	private Set<OfferClientInterface> clientReferences;

	private int randomPrice;

	public OfferServerInterfaceImpl(final Set<OfferClientInterface> s) throws RemoteException
	{
		this.clientReferences = s;
		this.randomPrice = 0;
	}
	
	public void subscribe(final OfferClientInterface clientRef) throws RemoteException 
	{
		this.clientReferences.add(clientRef);
	}

	public void unregister(final OfferClientInterface clientRef) throws RemoteException
	{
		this.clientReferences.remove(clientRef);
	}

	public void setServerPrice(final int price) throws RemoteException
	{
		this.randomPrice = price;
	}

	public boolean evaluatePurchaseOffer(final int purchaseOffer) throws RemoteException
	{
		if (purchaseOffer >= this.randomPrice)
		{
			return true;
		}

		return false;
	}

	public void close() throws RemoteException
	{
		UnicastRemoteObject.unexportObject(this, true);
	}
}
