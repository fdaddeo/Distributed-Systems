package it.unipr.assignment.one;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class OfferClientInterfaceImpl extends UnicastRemoteObject implements OfferClientInterface 
{
    private static final long serialVersionUID = 1L;
	
	private final int MAX = 200;
	private final int MIN = 10;

	private Random random;
	private OfferServerInterface serverReference;

	private int purchasesDone;

	public OfferClientInterfaceImpl(OfferServerInterface serverRef) throws RemoteException
	{
		this.serverReference = serverRef;
		this.random = new Random();
		this.purchasesDone = 0;
	}

	public void setServerPrice(final int price) throws RemoteException
	{
		int purchaseOffer = this.random.nextInt(this.MAX - this.MIN) + this.MIN;

		System.out.println("Price: " + price + " received, offer generated is: " + purchaseOffer);

		if (purchaseOffer >= price)
		{
			boolean offerAccepted = serverReference.evaluatePurchaseOffer(purchaseOffer);

			System.out.println("Sending offer...");

			if (offerAccepted)
			{
				this.purchasesDone++;
				System.out.println("Offer " + purchaseOffer + " accepted");
			}
			else
			{
				System.out.println("Offer " + purchaseOffer + " refused");
			}
		}

		if (this.purchasesDone > 9)
		{
			serverReference.unregister(this);
			UnicastRemoteObject.unexportObject(this, true);

			System.out.println("Client Terminated");
		}
	}
}
