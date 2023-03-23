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
                Price price = new Price(randomPrice);
                
                System.out.println("Sending price: " + randomPrice);

                if (os == null)
                {
                    os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
                }
                
                os.writeObject(price);
                os.flush();

                Object object = is.readObject();

                if (object instanceof Offer)
                {
                    Offer clientOffer = (Offer) object;
                    Response serverResponse = new Response(randomPrice, clientOffer.getClientOffer(), false);

                    if (clientOffer.getClientOffer() >= randomPrice)
                    {
                        serverResponse.setOfferAccepted(true);
                    }

                    os.writeObject(serverResponse);
                    os.flush();
                }
                else if (object instanceof CloseConnectionMessage)
                {
                    CloseConnectionMessage clienteCloseConnection = (CloseConnectionMessage) object;

                    if (clienteCloseConnection.getCloseConnection())
                    {
                        this.server.addClientServed();

                        if (this.server.getPool().getActiveCount() == 1 && this.server.getCliendServed() >= 3)
                        {
                            this.server.close();
                        }

                        this.socket.close();
                        break;
                    }
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
