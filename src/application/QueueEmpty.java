package application;
@SuppressWarnings("serial")
public class QueueEmpty extends Exception 
{

	public QueueEmpty()
	{
		super();
		System.err.println("Queue is empty");
	}
}
