package it.unipr.assignment.two;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable
{
    private static final long SLEEPTIME = 3000;

    private static final int MAX = 200;
    private static final int MIN = 10;

    private Server server;
    private Socket socket;
	private Random random;

    private int randomPrice;

    public ServerThread(final Server server, final Socket socket)
    {
        this.server = server;
        this.socket = socket;

        this.random = new Random();
    }

    public void run()
    {
        ObjectInputStream is = null;
        ObjectOutputStream os = null;

        try
        {
            is = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        while (true)
        {
            try
            {
				int randomPrice = this.random.nextInt(MAX - MIN) + MIN;
                Offer price = new Offer(randomPrice);
                
                System.out.println("Sending price: " + randomPrice);

                if (os == null)
                {
                    os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
                }
                
                os.writeObject(price);
                os.flush();

                Object offer = is.readObject();

                if (price instanceof Offer)
                {
                    Offer status = (Offer) offer;

                    if (status.getOffer() >= randomPrice)
                    {
                        status.setOfferAccepted(true);
                    }
                    else 
                    {
                        status.setOfferAccepted(false);
                    }

                    os.writeObject(status);
                }

                Thread.sleep(SLEEPTIME);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
