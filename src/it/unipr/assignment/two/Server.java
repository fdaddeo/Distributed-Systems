package it.unipr.assignment.two;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server 
{
	private static final int COREPOOL = 5;
	private static final int MAXPOOL =  100;
	private static final long IDLETIME = 5000;

	private static final int SPORT = 4444;

	private ServerSocket socket;
	private ThreadPoolExecutor pool;

	private ArrayList<Socket> openedSockets;

	public Server() throws IOException
	{
		this.socket = new ServerSocket(SPORT);
		this.openedSockets = new ArrayList<Socket>();
	}

	private void run()
	{
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		while (true)
		{
			try
			{
				Socket s = this.socket.accept();

				this.openedSockets.add(s);
				this.pool.execute(new ServerThread(this, s));
			}
			catch (Exception e)
			{
				break;
			}
		}

		this.pool.shutdown();
	}

	public ThreadPoolExecutor getPool()
	{
		return this.pool;
	}

	public void removeSocketFromList(Socket s)
	{
		this.openedSockets.remove(s);

		if (this.openedSockets.size() == 0)
		{
			close();
		}
	}

	public void close()
	{
		try
		{
			this.socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) throws IOException
	{
		new Server().run();
	}

}