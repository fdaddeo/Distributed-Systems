package it.unipr.assignment.two;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Random;

public final class Client
{
    private static final int SPORT = 4444;
    private static final String SHOST = "localhost";

    private static final int MAX = 200;
    private static final int MIN = 10;

    public void run()
    {
        try
        {
            Socket client = new Socket(SHOST, SPORT);
            Random random = new Random();
            
            ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream is = null;

            int purchasesDone = 0;

            while (true)
            {
                if (is == null)
                {
                    is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
                }

                Object object = is.readObject();

                if (object instanceof Price)
                {
                    Price receivedPriceObject = (Price) object;
                    int randomOffer = random.nextInt(MAX - MIN) + MIN;
                    int priceReceived = receivedPriceObject.getServerPrice();

                    Offer clientOffer = new Offer(priceReceived, randomOffer);

                    System.out.println("Price: " + priceReceived + " received, offer generated is: " + randomOffer);

                    if (randomOffer >= priceReceived)
                    {
                        os.writeObject(clientOffer);
                        os.flush();

                        System.out.println("Sending offer...");
                    }
                    else
                    {
                        clientOffer.setClientOffer(0);

                        os.writeObject(clientOffer);
                        os.flush();
                    }
                }
                else if (object instanceof Response)
                {
                    Response serverResponse = (Response) object;

                    if (serverResponse.getOfferAccepted())
                    {
                        purchasesDone++;
                        System.out.println("Offer Accepted");
                    }
                    else
                    {
                        System.out.println("Offer Refused");
                    }
                }

                if (purchasesDone > 9)
                {
                    CloseConnectionMessage closeConnection = new CloseConnectionMessage(true);

                    os.writeObject(closeConnection);
                    os.flush();
                    break;
                }
            }

            client.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args)
    {
        new Client().run();
    }
}
