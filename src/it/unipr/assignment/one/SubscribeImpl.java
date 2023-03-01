package it.unipr.assignment.one;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.Set;

public class SubscribeImpl extends UnicastRemoteObject implements Subscribe 
{
	private static final long serialVersionUID = 1L;
	
	private Set<PriceWriter> writers;

	public SubscribeImpl(final Set<PriceWriter> s) throws RemoteException
	{
		this.writers = s;
	}
	
	public void subscribe(final PriceWriter w) throws RemoteException 
	{
		this.writers.add(w);
	}

}
