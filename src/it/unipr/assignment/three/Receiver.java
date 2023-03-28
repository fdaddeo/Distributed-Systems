package it.unipr.assignment.three;

import javax.jms.JMSException;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver 
{
	private final String BROKER_URL = "tcp://localhost:61616";
	private String queueName;
	private QueueReceiver receiver;
	private ActiveMQConnection connection = null;

	public Receiver(String queueName) throws JMSException 
	{
		this.queueName = queueName;

		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(this.BROKER_URL);

		this.connection = (ActiveMQConnection) cf.createConnection();

		this.connection.start();

		QueueSession session = this.connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		Queue queue = session.createQueue(this.queueName);

		this.receiver = session.createReceiver(queue);
	}

	public Message receive(long waitTime) throws JMSException 
	{
		return this.receiver.receive(waitTime);
	}

	public void close() throws JMSException 
	{
		if (this.connection != null) {

			this.connection.close();
		}
	}
}
