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
	    Set<OfferClientInterface> clientReferences = new CopyOnWriteArraySet<>();
	    OfferServerInterface service = new OfferServerInterfaceImpl(clientReferences);
	    
	    Random random = new Random();
		int iter_count = 0;

	    registry.rebind("subscribe", service);
	    
	    while (true)
	    {
			try
			{
				int randomPrice = random.nextInt(MAX - MIN) + MIN;

				service.setServerPrice(randomPrice);

				for (OfferClientInterface clientRef : clientReferences)
				{
					clientRef.setServerPrice(randomPrice);
					System.out.println("Sending price: " + randomPrice);
					iter_count++;
				}

				// iter_count for debug
				if (clientReferences.size() == 0 && iter_count > 0)
				{
					break;
				}

				Thread.sleep(3000);
			}
	    	catch (Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }

		service.close();
		System.out.println("Server Terminated");
	}
}
