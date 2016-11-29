import java.util.LinkedList;
import java.util.Queue;

public class ReservationStation 
{
	private int  N = 10;
	private int size;
	private Queue<ReservationStationDetails> queue;
	public ReservationStation() 
	{
		this.queue = new LinkedList<ReservationStationDetails>();
		this.size = 0;
	}
	
	public boolean add(ReservationStationDetails r)
	{
		if(isFull()) return false; 
		queue.add(r);
		size++;
		return true;
	}
	
	public Queue<ReservationStationDetails> getQueue()
	{
		return queue;
	}
	
	public ReservationStationDetails poll()
	{
		if(isEmpty()) return null;
		size--;
		return queue.poll();
	}
	public boolean isFull()
	{
		return size == N;
	}
	public boolean isEmpty()
	{
		return size == 0;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) 
	{
		N = n;
	}

	public int getSize() 
	{
		return size;
	}

	public void setSize(int size) 
	{
		this.size = size;
	}

	public void setQueue(Queue<ReservationStationDetails> queue) 
	{
		this.queue = queue;
	}
	
	
}
