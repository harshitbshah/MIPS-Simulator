public class BtbDetails {
	private int btbId;
	private int btbPc;
	private int btbTarget;
	private String btbOutcome;
	
	public BtbDetails(int id, int pc, int target){
		this.btbId = id;
		this.btbPc = pc;
		this.btbTarget = target;
		this.btbOutcome = "0";
	}
	public String toString(){
		return "[Entry "+btbId+"]<"+btbPc+","+btbTarget+","+btbOutcome+">";
	}
	
	public boolean equals(BtbDetails e){
		return this.btbPc == e.btbPc;
	}
	
	public int getId() {
		return btbId;
	}
	public void setId(int id) {
		this.btbId = id;
	}
	public int getPc() {
		return btbPc;
	}
	public void setPc(int pc) {
		this.btbPc = pc;
	}
	public int getTarget() {
		return btbTarget;
	}
	public void setTarget(int target) {
		this.btbTarget = target;
	}
	public String getOutcom() {
		return btbOutcome;
	}
	public void setOutcom(String outcom) {
		this.btbOutcome = outcom;
	}
	public int getBtbId() {
		return btbId;
	}
	public void setBtbId(int btbId) {
		this.btbId = btbId;
	}
	public int getBtbPc() {
		return btbPc;
	}
	public void setBtbPc(int btbPc) {
		this.btbPc = btbPc;
	}
	public int getBtbTarget() {
		return btbTarget;
	}
	public void setBtbTarget(int btbTarget) {
		this.btbTarget = btbTarget;
	}
	public String getBtbOutcome() {
		return btbOutcome;
	}
	public void setBtbOutcome(String btbOutcome) {
		this.btbOutcome = btbOutcome;
	}
	
	
	
}
