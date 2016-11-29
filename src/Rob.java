import java.util.LinkedList;
import java.util.Queue;

public class Rob {
	private int N = 6;
	private int size ;
	private Queue<RobDetails> queue;
	public Rob () {
		this.queue = new LinkedList<RobDetails>();
		this.size = 0;
	}
	
	public Queue<RobDetails> getQueue(){
		return queue;
	}
	
	public int size(){
		return size;
	}
	
	public RobDetails get(int rob_id){
		for(RobDetails r:queue){
			if(r.getEnt() == rob_id) return r;
		}
		return null;
	}
	
	public boolean add(RobDetails r){
		if(isFull()) return false;
		this.queue.add(r);
		size++;
		return true;
	}
	public RobDetails poll(){
		if(isEmpty()) return null;
		size--;
		return queue.poll();
	}
	public boolean isFull(){
		return size == N;
	}
	public boolean isEmpty(){
		return size == 0;
	}
}
