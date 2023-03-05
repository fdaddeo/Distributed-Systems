package it.unipr.assignment.one;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OfferClientInterface extends Remote
{
    void setServerPrice(final int price) throws RemoteException;
}
