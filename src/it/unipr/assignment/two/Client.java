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

                Object price = is.readObject();

                if (price instanceof Offer)
                {
                    Offer offer = (Offer) price;
                    int randomOffer = random.nextInt(MAX - MIN) + MIN;

                    System.out.println("Price: " + offer.getPrice() + " received, offer generated is: " + randomOffer);

                    if (randomOffer >= offer.getPrice())
                    {
                        offer.setOffer(randomOffer);

                        os.writeObject(offer);
                        os.flush();

                        System.out.println("Sending offer...");
                    }
                }

                Object status = is.readObject();

                if (status instanceof Offer)
                {
                    Offer offerStatus = (Offer) status;

                    if (offerStatus.getOfferAccepted())
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
