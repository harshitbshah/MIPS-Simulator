public class Instruction {
	private int opcode;
	private int rs;
	private int rt;
	private int rd;
	private int shamt;
	private int func;
	public boolean value;
	
	private int address;
	private String instruction;
	
	private int imm;
	
	
	private int lw=0;
	
	private boolean writeExecuteJ = false;
	
	private boolean writeExecuteK = false;
	
	private boolean branchJump = true;
	
	private static boolean flag = false;
	
	public Instruction(int i, int addr)
	{
		assert(addr%4 == 0 && addr < 716); 
		String instr = String.format("%32s", Integer.toBinaryString(i)).replace(' ', '0');
		opcode = Integer.parseInt(instr.substring(0, 6), 2);
		rs = Integer.parseInt(instr.substring(6,11),2);
		rt = Integer.parseInt(instr.substring(11, 16),2);
		rd = Integer.parseInt(instr.substring(16, 21),2);
		shamt = Integer.parseInt(instr.substring(21,26),2);
		func = Integer.parseInt(instr.substring(26,32),2);
		
		setAddress(addr);
		Decode(i);
	}

	
	private void Decode(int instr)
	{
		int immed;
		int uimmed;
		int addr;
		int jumpAdd;
		int off;

	    uimmed = instr & 0xffff;
	    
	    immed = uimmed >>15;
	    if (immed == 0) 
	    {
	        immed = uimmed;
	    }
	    else
	    {
	        immed = uimmed|0xffff0000;
	    }
	    
	    off = immed<<2;
	    addr = instr & 0x3ffffff;
	    
	    jumpAdd = addr<<2;
		
		switch(opcode) 
		{
		case 0x00:
            switch (func) 
            {
                case 0x00:
                	if (instr == 0 && flag)
                	{
                		instruction = "0";
                		break;
                	}
                    if (instr == 0) 
                    {
                        instruction = "NOP";
                        break;
                    }
                    instruction = "SLL R"+rd+", R"+rt+", "+shamt;
                    break;
                case 0x02:
                	instruction = "SRL R"+rd+", R"+rt+", "+shamt;
                    break;
                case 0x03:
                	instruction = "SRA R"+rd+", R"+rt+", "+shamt;
                    break;
                case 0x20:
                	instruction = "ADD R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x0d:
                    instruction = "BREAK";
                    flag = true;
                    break;
                case 0x21:
                	instruction = "ADDU R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x22:
                	instruction = "SUB R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x23:
                	instruction = "SUBU R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x24:
                	instruction = "AND R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x25:
                	instruction = "OR R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x26:
                	instruction = "XOR R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x27:
                	instruction = "NOR R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x2a:
                	instruction = "SLT R"+rd+", R"+rs+", R"+rt;
                    break;
                case 0x2b:
                	instruction = "SLTU R"+rd+", R"+rs+", R"+rt;
                    break;
                default:
                    instruction = "unsurported instructions";
                    break;
            }
            break;
        case 0x01:
            if (rs == 0) 
            {
            	instruction = "BLTZ R"+rs+", #"+off;
            	imm = off;
            }
            else if(rs == 1)
            {
            	instruction = "BGEZ R"+rs+", #"+off;
            	imm = off;            	
            }
            break;
        case 0x02:
        	instruction = "J #"+jumpAdd;
        	imm = jumpAdd;
            break;
        case 0x04:
            instruction = "BEQ R" + rs +", R"+rt+", #"+off;
            imm = off;
            break;
        case 0x05:
        	instruction = "BNE R" + rs +", R"+rt+", #"+off;
        	imm = off;
            break;
        case 0x06:
        	instruction = "BLEZ R" + rs +", #"+off;
        	imm = off;
            break;
        case 0x07:
        	instruction = "BGTZ R" + rs +", #"+off;
        	imm = off;
            break;
        case 0x08:
        	instruction = "ADDI R" + rt + ", R" + rs +", #"+immed;
        	imm = immed;
            break;
        case 0x09:
        	instruction = "ADDIU R" + rt + ", R" + rs +", #"+immed;
        	imm = immed;
            break;
        case 0x0a:
        	instruction = "SLTI R" + rt + ", R" + rs +", #"+immed;
        	imm = immed;
            break;
        case 0x0b:
        	instruction = "SLTIU R" + rt + ", R" + rs +", #"+immed;
        	imm = immed;
            break;
        case 0x23:
        	instruction = "LW R" + rt + ", " + immed +"(R"+rs+")";
        	imm = immed;
            break;
        case 0x2b:
        	instruction = "SW R" + rt + ", " + immed +"(R"+rs+")";
        	imm = immed;
            break;
            
        default:
        	instruction = "unsurported instructions";
            break;

		}
	    
	    
	}
	

	public String getInstr()
	{
		return this.instruction;
	}
	
	public String operation() 
	{
		return this.instruction.split(" ")[0];
	}
	
	
	public boolean equals(Instruction c)
	{
		return this.address == c.address;
	}
	
	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) 
	{
		this.opcode = opcode;
	}

	public int getRt() 
	{
		return rt;
	}

	public void setRt(int rt) 
	{
		this.rt = rt;
	}

	public int getRs() 
	{
		return rs;
	}

	public void setRs(int rs) 
	{
		this.rs = rs;
	}

	public int getRd() 
	{
		return rd;
	}

	public void setRd(int rd) 
	{
		this.rd = rd;
	}

	public int getShamt() 
	{
		return shamt;
	}

	public void setShamt(int shamt) 
	{
		this.shamt = shamt;
	}

	public int getFunc() 
	{
		return func;
	}

	public void setFunc(int func) 
	{
		this.func = func;
	}

	public int getAddress() 
	{
		return address;
	}

	public void setAddress(int address) 
	{
		this.address = address;
	}
	
	public String toString()
	{
		return "["+ instruction + "]" ;
		
	}


	public int getImm() 
	{
		return imm;
	}


	public void setImm(int imm) 
	{
		this.imm = imm;
	}


	public int getLw() 
	{
		return lw;
	}


	public void setLw(int lw) 
	{
		this.lw = lw;
	}


	public boolean isBranchJump() 
	{
		return branchJump;
	}


	public void setBranchJump(boolean branchJump) 
	{
		this.branchJump = branchJump;
	}


	public boolean isWriteExecuteJ() 
	{
		return writeExecuteJ;
	}


	public void setWriteExecuteJ(boolean writeExecuteJ) 
	{
		this.writeExecuteJ = writeExecuteJ;
	}


	public boolean isWriteExecuteK() 
	{
		return writeExecuteK;
	}


	public void setWriteExecuteK(boolean writeExecuteK) 
	{
		this.writeExecuteK = writeExecuteK;
	}


	public boolean isValue() 
	{
		return value;
	}


	public void setValue(boolean value) 
	{
		this.value = value;
	}


	public String getInstruction() 
	{
		return instruction;
	}


	public void setInstruction(String instruction) 
	{
		this.instruction = instruction;
	}


	public static boolean isFlag() 
	{
		return flag;
	}


	public static void setFlag(boolean flag) 
	{
		Instruction.flag = flag;
	}

	
	

	
}
