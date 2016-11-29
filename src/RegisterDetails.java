public class RegisterDetails {
	private int value;
	private int Reorder;
	private boolean busy;
	
	public RegisterDetails (){
		this.value = 0;
		this.Reorder = -1;
		this.busy = false;
	}
	
	public String toString(){
		return String.valueOf(value);
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getReorder() {
		return Reorder;
	}
	public void setReorder(int reorder) {
		Reorder = reorder;
		this.busy = true;
	}
	public boolean isBusy() {
		return busy;
	}
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	
}
