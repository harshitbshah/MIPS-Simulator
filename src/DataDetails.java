public class DataDetails {
	private int progCntr;
	private int dataVal;
	
	public DataDetails(int value, int pc){
		this.setPc(pc);
		this.setValue(value);
	}

	public int getValue() {
		return dataVal;
	}

	public void setValue(int value) {
		this.dataVal = value;
	}

	public int getPc() {
		return progCntr;
	}

	public void setPc(int pc) {
		this.progCntr = pc;
	}
	
	public String toString(){
		return ""+dataVal;
	}

	public int getProgCntr() {
		return progCntr;
	}

	public void setProgCntr(int progCntr) {
		this.progCntr = progCntr;
	}

	public int getDataVal() {
		return dataVal;
	}

	public void setDataVal(int dataVal) {
		this.dataVal = dataVal;
	}
	
	
}
