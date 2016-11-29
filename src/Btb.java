import java.util.ArrayList;

public class Btb {
	private int btbNo =16;
	private int btbSize ;
	private ArrayList<BtbDetails> btb ;
	public Btb() {
		this.btb = new ArrayList<BtbDetails>();
		this.btbSize = 0;
	}
	public void add(BtbDetails e){
		btb.add(e);
		btbSize++;
	}
	
	public ArrayList<BtbDetails> getArr(){
		return btb;
	}
	
	public BtbDetails get(Instruction c){
		for(BtbDetails e:btb){
			if(e.getPc() == c.getAddress()) return e;
		}
		return null;
	}
		
	public int target(Instruction c){
		BtbDetails e = get(c);
		if(e != null && e.getOutcom().equals("1")){
			return e.getTarget();
		}
		
		return c.getAddress() +4;
	}
	public int getTarget(Instruction c){
		BtbDetails e = get(c);
		if(e != null)		
			return e.getTarget();
		return c.getAddress() + 4;
	}
	public int getBtbNo() {
		return btbNo;
	}
	public void setBtbNo(int btbNo) {
		this.btbNo = btbNo;
	}
	public int getBtbSize() {
		return btbSize;
	}
	public void setBtbSize(int btbSize) {
		this.btbSize = btbSize;
	}
	public ArrayList<BtbDetails> getBtb() {
		return btb;
	}
	public void setBtb(ArrayList<BtbDetails> btb) {
		this.btb = btb;
	} 
	
	
	
}
