package it.unipr.assignment.one;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OfferServerInterface extends Remote
{
    void subscribe(final OfferClientInterface clientRef) throws RemoteException;
    void unregister(final OfferClientInterface clientRef) throws RemoteException;
    void setServerPrice(final int price) throws RemoteException;
    boolean evaluatePurchaseOffer(final int purchaseOffer) throws RemoteException;
    void close() throws RemoteException;
}
