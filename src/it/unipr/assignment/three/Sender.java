package it.unipr.assignment.three;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender 
{
	private final String BROKER_URL = "tcp://localhost:61616";
	private ActiveMQConnection connection = null;
	private QueueSession session = null;

	private ArrayList<QueueSender> senders = new ArrayList<QueueSender>();

	public Sender(ArrayList<String> queueNames) throws JMSException 
	{
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(this.BROKER_URL);

		this.connection = (ActiveMQConnection) cf.createConnection();

		this.connection.start();

		this.session = this.connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		for (String queueName : queueNames) {

			Queue queue = session.createQueue(queueName);
			QueueSender sender = session.createSender(queue);
			senders.add(sender);
		}
	}

	public void sendElectionMsg(final int sender) throws JMSException 
	{		
		for(int i = sender + 1; i < senders.size(); i++) 
		{			
			TextMessage message = this.generateTextMessage(sender, MessageType.ELECTION);
			senders.get(i).send(message);	
		}
	}

	public void sendElectionAck(final int sender, final int receiver) throws JMSException 
	{		
		TextMessage message = this.generateTextMessage(sender, MessageType.ELECTION_ACK);
		senders.get(receiver).send(message);
	}

	public void sendCoordinatorMsg(final int sender) throws JMSException 
	{		
		for(int i = sender - 1; i >= 0; i--)
		{			
			TextMessage message = this.generateTextMessage(sender, MessageType.COORDINATOR);
			senders.get(i).send(message);	
		}
	}

	public void close() throws JMSException 
	{
		if (this.connection != null) 
		{
			this.connection.close();
		}
	}

	private TextMessage generateTextMessage(int sender, MessageType type) throws JMSException
	{
		TextMessage message = this.session.createTextMessage();

		switch (type)
		{
			case ELECTION:
				message.setText(Integer.toString(sender) + ":ELECTION");
				break;

			case ELECTION_ACK:
				message.setText(Integer.toString(sender) + ":ELECTION_ACK");
				break;

			case COORDINATOR:
				message.setText(Integer.toString(sender) + ":COORDINATOR");
				break;

			default:
				break;
		}

		return message;
	}
}
