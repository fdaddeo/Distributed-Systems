package it.unipr.assignment.three;

import java.io.File;
import java.io.FileNotFoundException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class Node 
{
	private static final String BROKER_URL = "tcp://localhost:61616";
	private static final String BROKER_PROPS = "persistent=false&useJmx=false";
	private static final String FILE_PATH = "config.txt";

	private static final long EXECUTION_TIME = 1000;
	private static final long DEFAULT_WAIT_TIME = 5000;
	private static final long MAX_WAIT = 1000;
	private static final long MIN_WAIT = 500;
	
	private static final int MAX = 100;
	private static final int MIN = 0;
	private static final int H = 20;
	private static final int K = 1;

	private static State nodeState;

	public static void main(String[] args) throws Exception 
	{
		int id = Integer.parseInt(args[0]);
		long waitTime = DEFAULT_WAIT_TIME;
		boolean resourceOccupied = false;
		ArrayList<Integer> requests = new ArrayList<Integer>();

		if (id == 0) 
		{
			BrokerService broker = BrokerFactory.createBroker("broker:(" + BROKER_URL + ")?" + BROKER_PROPS);
			broker.start();
		}

		URL fileUrl = Node.class.getResource(FILE_PATH);
		ArrayList<String> queueNames = new ArrayList<String>();

		try 
		{
			File myObj = new File(fileUrl.getPath());
			Scanner myReader = new Scanner(myObj);
			
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				queueNames.add(data);
			}
			myReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

		Random random = new Random();

		if (id == queueNames.size() - 1) 
		{
			nodeState = State.CANDIDATE;
		} 
		else 
		{
			nodeState = State.IDLE;
		}

		String queueName = "queue" + args[0];
		Receiver receiver = new Receiver(queueName);
		Sender sender = new Sender(queueNames);

		int coordinatorId = -1;

		while (true) 
		{
			int randomInt = random.nextInt(MAX - MIN) + MIN;

			if (randomInt < K)
			{
				nodeState = State.DEAD;
				resourceOccupied = false;
			}
			else if (randomInt < K + H && nodeState == State.DEAD)
			{
				receiver.flushQueue();
				nodeState = State.CANDIDATE;
			}
			
			switch (nodeState) 
			{
				case IDLE:
					if (coordinatorId != -1)
					{
						randomInt = random.nextInt(MAX - MIN) + MIN;
						System.out.println("IDLE: generated " + Integer.toString(randomInt));

						// 50% propability
						if (randomInt < 50)
						{
							nodeState = State.REQUESTER;
							System.out.println("IDLE: passing to REQUESTER state");
						}
					}
					break;

				case REQUESTER:
					System.out.println("REQUESTER: sending request message");
					sender.sendRequestMsg(id, coordinatorId);
					System.out.println("REQUESTER: passing to WAITER state");
					nodeState = State.WAITER;
				
				case WAITER:
					waitTime = random.nextLong(MAX_WAIT - MIN_WAIT) + MIN_WAIT;
					System.out.println("WAITER: waiting " + Long.toString(waitTime));
					break;

				case USER:
					System.out.println("USER: now in critical section");
					Thread.sleep(EXECUTION_TIME); // Do something
					System.out.println("USER: leaving critical section");
					sender.sendTerminatedExecutionMsg(id, coordinatorId);
					Thread.sleep(random.nextLong(MAX_WAIT - MIN_WAIT) + MIN_WAIT); // Random timeout
					System.out.println("USER: passing to IDLE state");
					nodeState = State.IDLE;
					break;

				case CANDIDATE:
					System.out.println("CANDIDATE: sending ELECTION message");
					sender.sendElectionMsg(id);
					// if (id == queueNames.size() - 1)
					// {
					// 	System.out.println("CANDIDATE: biggest ID, passing to COORDINATOR state");
					// 	nodeState = State.COORDINATOR;
					// 	coordinatorId = id;
					// 	resourceOccupied = false;
					// 	sender.sendCoordinatorMsg(id);
					// }
					// else
					// {
					// 	System.out.println("CANDIDATE: sending ELECTION message");
					// 	sender.sendElectionMsg(id);
					// }
					break;

				case COORDINATOR:
					if (requests.size() != 0 && !resourceOccupied)
					{
						System.out.println("COORDINATOR: sending PERMISSION message to node " + Integer.toString(requests.get(0)));
						sender.sendPermissionMsg(id, requests.get(0));
						requests.remove(0);
						resourceOccupied = true;
					}
					break;

				case DEAD:
					System.out.println("DEAD: waiting...");
					Thread.sleep(1000);
					continue;

				default:
					break;
			}

			Message msg = receiver.receive(waitTime);
			waitTime = DEFAULT_WAIT_TIME;

			if (msg == null)
			{
				if (nodeState == State.CANDIDATE)
				{
					nodeState = State.COORDINATOR;
					coordinatorId = id;
					resourceOccupied = false;
					requests.clear();
					sender.sendCoordinatorMsg(id);

					System.out.println("Now I am the coordinator.");
				}
				else if (nodeState == State.WAITER)
				{
					nodeState = State.CANDIDATE;
				}
			}
			else
			{
				if (msg instanceof TextMessage)
				{
					TextMessage messageReceived = (TextMessage) msg;
					String[] parsedMessage = messageReceived.getText().split(":");

					int senderId = Integer.parseInt(parsedMessage[0]);
					String messageType = parsedMessage[1];

					System.out.println("Message: " + messageType + " received from " + Integer.toString(senderId));

					if (messageType.equals("ELECTION"))
					{				
						sender.sendElectionAck(id, senderId);
						nodeState = State.CANDIDATE;
					}
					else if (messageType.equals("ELECTION_ACK"))
					{
						coordinatorId = -1;			
						nodeState = State.IDLE;
					}
					else if (messageType.equals("COORDINATOR"))
					{
						coordinatorId = senderId;
						nodeState = State.IDLE;
					}
					else if (messageType.equals("REQUEST"))
					{
						requests.add(senderId);
					}
					else if (messageType.equals("PERMISSION"))
					{
						nodeState = State.USER;
					}
					else if (messageType.equals("EX_TERMINATED"))
					{
						resourceOccupied = false;
					}
				}
			}
		}
	}
}