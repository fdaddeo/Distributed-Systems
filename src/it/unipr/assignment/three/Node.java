package it.unipr.assignment.three;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
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

	private static State nodeState;

	public static void main(String[] args) throws Exception 
	{
		int id = Integer.parseInt(args[0]);

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
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

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

		int coordinatorId = 0;

		while (true) 
		{
			Message msg = receiver.receive(5000);

			if (msg == null)
			{
				if (nodeState == State.CANDIDATE)
				{
					nodeState = State.COORDINATOR;
					coordinatorId = id;
					sender.sendCoordinatorMsg(id);
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

					System.out.println("Message: " + messageType + " received.");

					if (messageType.equals("ELECTION"))
					{				
						sender.sendElectionAck(id, senderId);
						nodeState = State.CANDIDATE;

						System.out.print("Now I am a candidate");
					}
					else if (messageType.equals("ELECTION_ACK"))
					{						
						nodeState = State.IDLE;

						System.out.println("Now I am in idle state.");
					}
					else if (messageType.equals("COORDINATOR"))
					{
						coordinatorId = senderId;
						nodeState = State.REQUESTER;

						System.out.println("Now I am a requester.");
					}
				}
			}
			
			switch (nodeState) 
			{
				case IDLE:
					break;
				
				case CANDIDATE:
					if (id == queueNames.size() - 1)
					{
						sender.sendCoordinatorMsg(id);
						nodeState = State.COORDINATOR;
						coordinatorId = id;
					}
					else
					{
						sender.sendElectionMsg(id);
					}
					break;

				default:
					break;
			}
		}
	}
}