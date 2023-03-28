package it.unipr.assignment.three;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class Node 
{
	private static final String BROKER_URL = "tcp://localhost:61616";
	private static final String BROKER_PROPS = "persistent=false&useJmx=false";
	private static final String FILE_PATH = "Conf.txt";

	private static State nodeState;

	public static void main(String[] args) throws Exception 
	{
		int id = Integer.parseInt(args[1]);

		if (id == 0) 
		{
			BrokerService broker = BrokerFactory.createBroker("broker:(" + BROKER_URL + ")?" + BROKER_PROPS);
			broker.start();
		}

		FileReader input = new FileReader(FILE_PATH);
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;
		ArrayList<String> queueNames = new ArrayList<String>();

		while ((myLine = bufRead.readLine()) != null) 
		{
			queueNames.add(myLine);
		}

		bufRead.close();
		input.close();

		if (id == queueNames.size() - 1) 
		{
			nodeState = State.CANDIDATE;
		} 
		else 
		{
			nodeState = State.IDLE;
		}

		String queueName = "queue" + args[1];
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

					if (messageType.equals("ELECTION"))
					{
						sender.sendElectionAck(id, senderId);
						nodeState = State.CANDIDATE;
					}
					else if (messageType.equals("ELECTION_ACK"))
					{
						nodeState = State.IDLE;
					}
					else if (messageType.equals("COORDINATOR"))
					{
						coordinatorId = senderId;
						nodeState = State.REQUESTER;
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
