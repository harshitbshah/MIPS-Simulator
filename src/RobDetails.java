public class RobDetails {
	private int ent;
	private boolean busy;
	private Instruction instr;
	private RegisterDetails reg;
	private int val;
	private boolean ready;
	private int memAddr;
	public boolean committed = false;
	
	public RobDetails (int entry, Instruction command){
		this.ent = entry;
		this.busy = true;
		this.instr = command;
		this.ready = false;
	}
	
	public int getEnt() {
		return ent;
	}
	public void setEnt(int entry) {
		ent = entry;
	}
	public boolean isBusy() {
		return busy;
	}
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	public Instruction getInstr() {
		return instr;
	}
	public void setInstr(Instruction instruction) {
		this.instr = instruction;
	}
	public RegisterDetails getReg() {
		return reg;
	}
	public void setReg(RegisterDetails reg) {
		this.reg = reg;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public int getMemAddr() {
		return memAddr;
	}

	public void setMemAddr(int memAddr) {
		this.memAddr = memAddr;
	}

	public boolean isCommitted() {
		return committed;
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
	
	
}
