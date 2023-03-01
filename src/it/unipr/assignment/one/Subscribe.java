package it.unipr.assignment.one;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Subscribe extends Remote 
{
	void subscribe(final PriceWriter w) throws RemoteException;
}
