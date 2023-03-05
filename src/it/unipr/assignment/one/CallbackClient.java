package it.unipr.assignment.one;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CallbackClient
{		
	public static void main(final String[] args) throws Exception
	{
		Registry registry = LocateRegistry.getRegistry();
	    OfferServerInterface service = (OfferServerInterface) registry.lookup("subscribe");
	    OfferClientInterface w = new OfferClientInterfaceImpl(service);

	    service.subscribe(w);
	}
}
