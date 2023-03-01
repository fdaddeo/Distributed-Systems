package it.unipr.assignment.one;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Random;

public class CallbackClient
{	
	private static final int MAX = 200;
	private static final int MIN = 10;
	
	public static void main(final String[] args) throws Exception
	{
		Registry registry = LocateRegistry.getRegistry();
	    PriceWriter w = new PriceWriterImpl();
	    Subscribe service = (Subscribe) registry.lookup("subscribe");
	    
	    Random random = new Random();

	    service.subscribe(w);
	    
	    do
	    {
	    	int purchaseOffer = random.nextInt(MAX - MIN) + MIN;
	    	
	    	if (purchaseOffer >= w.getPrice())
	    	{
	    		w.setPurchaseOffer(purchaseOffer);
	    		System.out.println("Offer done: " + w.getPurchaseOffer() + " - Price is: " + w.getPrice());
	    	}
	    } 
	    while (w.getPurchaseDone() > 9);
	    
	    System.out.println("Client closed.");
	}
}
