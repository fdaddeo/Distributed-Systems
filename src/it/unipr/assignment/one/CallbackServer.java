package it.unipr.assignment.one;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CallbackServer 
{
	private static final int PORT = 1099;
	private static final int MAX = 200;
	private static final int MIN = 10;
	
	public static void main(final String[] args) throws Exception 
	{
	    Registry registry = LocateRegistry.createRegistry(PORT);
	    Set<PriceWriter> writers = new CopyOnWriteArraySet<>();
	    Subscribe service = new SubscribeImpl(writers);
	    
	    Random random = new Random();

	    registry.rebind("subscribe", service);
	    
	    while (true)
	    {
	    	int randomPrice = random.nextInt(MAX - MIN) + MIN;
	    	
	    	try
	    	{
	    		for (PriceWriter w : writers)
	    		{
	    			w.setPrice(randomPrice);
	    			System.out.println("Sending price: " + randomPrice);
	    		}
	    		
	    		Thread.sleep(1000);
	    		
	    		for (PriceWriter w : writers)
	    		{
	    			if (w.getPurchaseOffer() >= w.getPrice())
	    			{
	    				w.addPurchaseDone();
	    				System.out.println("Offer received and accepted: " + w.getPurchaseOffer());
	    			}
	    		}
	    	}
	    	catch (Exception e)
	    	{
	    		System.out.println(e);
	    	}
	    }
	}
}
