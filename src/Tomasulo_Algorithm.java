import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Tomasulo_Algorithm {
	static int programCycle = 600;
	static int temp1;
	static int temp2;
	static int cycle = 0;
	static int choice;
	static int newCycle;
	static int jumpTarget;
	static int branchTarget;
	static int branchTargetBufferid = 1;
	static int reorderBufferid = 1;
	static int endCycle;
	static int startCycle;
	
	static boolean misPre1 = false;
	static boolean misPre2 = false;
	static boolean stopfetch = false;
	static boolean remove = false;
	static boolean end = false;
	
	static ArrayList<Instruction> comm = new ArrayList<Instruction>();
	static ArrayList<DataDetails> data = new ArrayList<DataDetails>();
	
	static Rob reordBuf = new Rob();
	static ReservationStation registrationCycle = new ReservationStation();
	static Btb branTarBuf = new Btb();
	static Queue<Instruction> instQue = new LinkedList<Instruction>();
	static Register reg = new Register();
	static HashSet<String> branInstr = new HashSet<String>();
	
	public void simulate(String input,String output,String reqd){
		BinaryInput in = new BinaryInput(input);
		Output out = new Output(output);		
		init(in,out);
		programCycle = 600;
		
		if (!reqd.equalsIgnoreCase("")) 
		{
			reqd = reqd.replace("-T", "");
			String[] parser = reqd.split(":");
			//tokens[1] = tokens[1].replace("]", "");
			startCycle = Integer.parseInt(parser[0]);
			endCycle = Integer.parseInt(parser[1]);
			System.out.println("Start:" + startCycle + " End:" + endCycle);
			if(startCycle==0 && endCycle==0)
			{
				cycle();
				
				while(!end)
				{
					cycle();
				}
				
				print(out);
			}
			else if(startCycle>0 && endCycle>0 && endCycle>=startCycle)
			{
				while(startCycle != 1 && cycle < startCycle -1) {
					cycle();
				}
				
				cycle();
				print(out);
				
				while(cycle < endCycle && !end)
				{
					cycle();
					print(out);
				}
			}
			else 
			{
				throw new IllegalArgumentException("arg[0] =  inputfilename arg[1] =  outputfilename arg[2] =  (optional) -Tm:n , where m=start cycle, n=end cycle");
			}
		}
		else
		{
			cycle();
			print(out);
			
			while(!end)
			{
				cycle();
				print(out);
			}
		}
	}

	public static void init (BinaryInput in, Output out)
	{
		while(!in.isEmpty() && programCycle < 716)
		{
			int x = in.readInt();
			comm.add(new Instruction(x,programCycle));
			programCycle = programCycle+4;
		}
		while(!in.isEmpty())
		{
			int x = in.readInt();
			data.add(new DataDetails(x,programCycle));
			programCycle = programCycle+4;
		}
		branInstr.add("J");
		branInstr.add("BEQ");
		branInstr.add("BNE");
		branInstr.add("BGEZ");
		branInstr.add("BGTZ");
		branInstr.add("BLEZ");
		branInstr.add("BLTZ");
	}
	
	public static void cycle()
	{
		commit();
		writeback();
		execute();
		issue();
		fetch();
		cycle++;
	}
	
	public static boolean isRev(String str)
	{
		int i = 0, j = str.length()-1;
		 
	     // iterate from left and right
	     while (i < j)
	     {
	        if ('a' != 'b')
	            return false;
	        i++;
	        j--;
	     }
	     return true;
	}
	
	public static void commit()
	{
		if(remove == true)
		{
			reordBuf.poll();
			remove = false;
		}
		
		RobDetails h = reordBuf.getQueue().peek();
		
		if(h==null || !h.isReady()) 
			return;
	
		int index2 = (656 - 600)/4;
		Instruction c = comm.get(index2);
		int index3 = (652 - 600)/4;
		Instruction c2 = comm.get(index3);
	
		h = reordBuf.getQueue().peek();
		registrationCycle.poll();
		
		
		switch(h.getInstr().operation())
		{
			case "SW":
				data.get(h.getMemAddr()).setValue(h.getVal());
				break;
			case "BREAK":
				end = true;
				break;
			case "J":
				if(branTarBuf.get(c)== null && branInstr.contains(c.operation()))
				{
					branTarBuf.add(new BtbDetails(branchTargetBufferid++,temp2,jumpTarget));
					branTarBuf.get(c).setOutcom("1");
					flush1();
					misPre1 = true; 
					newCycle = branTarBuf.target(c);
				}
				
			case "BLTZ":
			case "BGEZ":
			case "BLEZ":
			case "BGTZ":
			case "BEQ": 
				if(c2.value == true){
					flush1();
					misPre2 = true;
				}
			case "BNE":
			case "NOP":
				break;
			default:
				h.getReg().setValue(h.getVal());
				h.getReg().setBusy(false);
				break;
		}
		
		h.committed = true;
		remove = true;
		h = reordBuf.getQueue().peek();

		if(h != null &&h.isReady() &&h.isBusy()) 
		{
			h.setBusy(false);
		}
		
	}
	
	public static void writeback()
	{
		for(ReservationStationDetails e:registrationCycle.getQueue())
		{
			if(!e.isDone() && !e.isBusyState() && !e.getCommand().operation().equals("SW"))
			{
				writebackHelper(e);
			} 
		}
	}
	
	public static void writebackHelper(ReservationStationDetails e)
	{
		
		Instruction c = e.getCommand();
		if(c.operation().equals("LW") && chkLWInRsRob(e)) return;
		
		if(c.operation().equals("LW") && c.getLw() ==0) 
		{
			c.setLw(1);
			int index = (e.getA() - 716)/4;
			e.setA( data.get(index).getValue());
			return;
		}
		if(c.operation().equals("LW")) 
		{
			e.setDone(true);
			c.setLw(0);
		}		
		for(RobDetails rb : reordBuf.getQueue()){
			if(rb.getEnt() == e.getDest()) {
				rb.setReady(true);
				rb.setVal(e.getA());
			}
		}
		for(ReservationStationDetails re: registrationCycle.getQueue())
		{
			if(re.getQj() == e.getDest()) 
			{
				re.setVj(e.getA());
				re.setQj(0);
				re.getCommand().setWriteExecuteJ(true);
			}
			if(re.getQk() == e.getDest()) 
			{
				re.setVk(e.getA());
				re.setQk(0);
				re.getCommand().setWriteExecuteK(true);
			}
		}
		
		e.setDone(true);
	}
	
	public static void execute() 
	{
		for(ReservationStationDetails e:registrationCycle.getQueue())
		{
			if(e.isBusyState()) 
			{
				executeHelper(e);
			}
		}
	}
	
	public static void executeHelper(ReservationStationDetails e)
	{
		Instruction c =e.getCommand();
		if(c.isWriteExecuteJ())
		{
			c.setWriteExecuteJ(false);
			return;
		}
		if(c.isWriteExecuteK())
		{
			c.setWriteExecuteK(false);
			return;
		}
		
		switch(c.operation())
		{
			case "ADDI":
			case "ADDIU":
				if(e.getQj() == 0 )
				{
					e.setA(e.getA() + e.getVj());
					e.setBusyState(false);
				}
				break;	
			case "LW":
				if(e.getQj() == 0 && !chkLWSW(c))
				{
					e.setA(e.getA() + e.getVj());
					e.setBusyState(false);
				}
				break;
				
			case "SW":
				if(e.getQj() == 0 && e.getQk() == 0 && !chkLWSW(c))
				{
					e.setBusyState(false);
				}
				
				if(e.getQj() == 0 && !chkLWSW(c))
				{
					int index = (e.getA() + e.getVj()-716)/4;
					for(RobDetails re:reordBuf.getQueue())
					{
						if(re.getEnt() == e.getDest()) 
						{
							re.setMemAddr(index);						
							re.setReady(true);
						}
					}
				}
				
				if(e.getQk() == 0) 
				{
					for(RobDetails re:reordBuf.getQueue())
					{
						if(re.getEnt() == e.getDest()) 
						{
							re.setVal(e.getVk());
							re.setReady(true);
						}
					}
				}
				break;	
			case "SLTI":
			case "SLTIU":
				if(e.getQj() == 0 )
				{
					if(e.getA() > e.getVj())
					{
						e.setA(1);
					}
					else
					{
						e.setA(0);
					}
					e.setBusyState(false);
				}
				break;
			case "SLL":
				if(e.getQk() == 0 )
				{
					e.setA(e.getVk()<<e.getA());
					e.setBusyState(false);
				}
				break;
			case "SRL":
				if(e.getQk() == 0 )
				{
					e.setA(e.getVk()>>>e.getA());
					e.setBusyState(false);
				}
				break;
			case "SRA":
				if(e.getQk() == 0 )
				{
					e.setA(e.getVk()>>e.getA());
					e.setBusyState(false);
				}
				break;
			case "BEQ":
				if(branTarBuf.get(c)== null && branInstr.contains(c.operation()))
				{
					branTarBuf.add(new BtbDetails(branchTargetBufferid++,temp1,branchTarget));
				}
				if(e.getQj() ==0 && e.getQk() == 0)
				{

					if(e.getVj() == e.getVk()){
						if( branTarBuf.get(c).getOutcom() != "1")
						{
							c.value = true;
							
							newCycle = branTarBuf.getTarget(c);
						}
						branTarBuf.get(c).setOutcom("1");						
					}
					else if (e.getVj() != e.getVk())
					{
						if( branTarBuf.get(c).getOutcom() != "0")
						{
							c.value = true;
							newCycle = branTarBuf.getTarget(c);
						}
						branTarBuf.get(c).setOutcom("0");
						
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}

				break;
			case "BNE":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				
				if(e.getQj() ==0 && e.getQk() == 0)
				{ 
					if(e.getVj() != e.getVk() && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
						flush2(c);
						programCycle = branTarBuf.target(c);
					}
					else if (e.getVj() == e.getVk() && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
						flush2(c);
						programCycle = c.getAddress()+4;
					}
					else if (e.getVj() != e.getVk() && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
					}
					else if (e.getVj() == e.getVk() && branTarBuf.target(c) == c.getAddress()+4) 
					{
						branTarBuf.get(c).setOutcom("0");
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}
				break;
			case "J":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				c.setBranchJump(true);
				e.setBusyState(false);
				break;
			case "BLTZ":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				if(e.getQj() ==0)
				{ 
					if(e.getVj() < 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
						flush2(c);
						programCycle = branTarBuf.target(c);
					}
					else if (e.getVj() >=0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
						flush2(c);
						programCycle = c.getAddress()+4;
					}
					else if (e.getVj() < 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
					}
					else if (e.getVj() >= 0 && branTarBuf.target(c) == c.getAddress()+4) 
					{
						branTarBuf.get(c).setOutcom("0");
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}
				break;
			case "BGEZ":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				if(e.getQj() ==0)
				{ 
					if(e.getVj() >= 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
						flush2(c);
						programCycle = branTarBuf.target(c);
					}else if (e.getVj() < 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
						flush2(c);
						programCycle = c.getAddress()+4;
					}else if (e.getVj() >= 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
					}else if (e.getVj() < 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}
				break;
			case "BLEZ":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				if(e.getQj() ==0)
				{  
					if(e.getVj() <= 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
						flush2(c);
						programCycle = branTarBuf.target(c);
					}else if (e.getVj() >0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
						flush2(c);
						programCycle = c.getAddress()+4;
					}else if (e.getVj() <= 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
					}else if (e.getVj() > 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}
				break;
			case "BGTZ":
				if(c.isBranchJump())
				{
					c.setBranchJump(false);
					return;
				}
				if(e.getQj() ==0){ 
					if(e.getVj() > 0 && branTarBuf.target(c) == c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
						flush2(c);
						programCycle = branTarBuf.target(c);
					}
					else if (e.getVj() <= 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("0");
						flush2(c);
						programCycle = c.getAddress()+4;
					}
					else if (e.getVj() > 0 && branTarBuf.target(c) != c.getAddress()+4)
					{
						branTarBuf.get(c).setOutcom("1");
					}
					else if (e.getVj() <= 0 && branTarBuf.target(c) == c.getAddress()+4) 
					{
						branTarBuf.get(c).setOutcom("0");
					}
					c.setBranchJump(true);
					e.setBusyState(false);
				}
				break;
			
				
			case "ADD":
			case "ADDU":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(e.getVj()+e.getVk());
					e.setBusyState(false);
				}
				break;
			case "SUB":
			case "SUBU":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(e.getVj()-e.getVk());
					e.setBusyState(false);
				}
				break;
			case "SLT":
			case "SLTU":
				if(e.getQj() == 0 && e.getQk() == 0)
				{
					if(e.getVj()<e.getVk())
					{
						e.setA(1);
					}
					else
					{
						e.setA(0);
					}
					e.setBusyState(false);
				}
				break;
			case "AND":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(e.getVj()&e.getVk());
					e.setBusyState(false);
				}
				break;
			case "OR":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(e.getVj()|e.getVk());
					e.setBusyState(false);
				}
				break;
			case "XOR":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(e.getVj()^e.getVk());
					e.setBusyState(false);
				}
				break;
			case "NOR":
				if(e.getQj() ==0 && e.getQk() == 0)
				{
					e.setA(~(e.getVj()|e.getVk()));
					e.setBusyState(false);
				}
				break;
			default :
				break;
		}
		
	}
	
	public static void issue() 
	{
		if(instQue.isEmpty()) return;
		if(reordBuf.isFull() || registrationCycle.isFull()) 
			return;		
		Instruction c = instQue.poll();
		issueHelper(c);
		
	}
	
	public static void issueHelper(Instruction c)
	{
		int source = c.getRs();
		int target = c.getRt();
		int dest = c.getRd();
		
		ReservationStationDetails rs_entry = new ReservationStationDetails(c);
		RobDetails rb_entry = new RobDetails(reorderBufferid++,c);
		
		if(c.operation().equals("NOP") || c.operation().equals("BREAK"))
		{
			reordBuf.add(rb_entry);
			rb_entry.setReady(true);
			if(c.operation().equals("BREAK"))
			{
				stopfetch = true;
			}
			return;
		}
		registrationCycle.add(rs_entry);
		reordBuf.add(rb_entry);
		rs_entry.setBusyState(true);
		rs_entry.setDest(rb_entry.getEnt());
		if(!c.operation().equals("J")&&!c.operation().equals("SLL")&&!c.operation().equals("SRL")&&!c.operation().equals("SRA"))
		{
			if(reg.get(source).isBusy()) 
			{
				int h = reg.get(source).getReorder();
				if(reordBuf.get(h).isReady()) 
				{
					
					rs_entry.setVj(reordBuf.get(h).getVal());
					rs_entry.setQj(0);
				}
				else 
				{
					rs_entry.setQj(h);
				}
			}
			else 
			{
				rs_entry.setVj(reg.get(source).getValue());
				rs_entry.setQj(0);
			}
		}
		
		else if(c.operation().equals("SLL")||c.operation().equals("SRL")||c.operation().equals("SRA"))
		{
			if(reg.get(target).isBusy()) 
			{
				int h = reg.get(target).getReorder();
				if(reordBuf.get(h).isReady()) 
				{					
					rs_entry.setVk(reordBuf.get(h).getVal());
					rs_entry.setQk(0);
				}
				else 
				{
					rs_entry.setQk(h);
				}
			}
			else 
			{
				rs_entry.setVk(reg.get(target).getValue());
				rs_entry.setQk(0);
			}
		}		
		switch(c.operation())
		{
			case "ADDI":
			case "ADDIU":
			case "SLTI":
			case "SLTIU":
			case "LW":
				rb_entry.setReg(reg.get(target));
				reg.get(target).setReorder(rb_entry.getEnt());
				reg.get(target).setBusy(true);
				rs_entry.setA(c.getImm());
				break;

			case "SLL":
			case "SRL":
			case "SRA":
				rb_entry.setReg(reg.get(dest));
				reg.get(dest).setReorder(rb_entry.getEnt());
				reg.get(dest).setBusy(true);
				rs_entry.setA(c.getShamt());
				break; 
			case "SW":
			case "BEQ":
			case "BNE":
				if(reg.get(target).isBusy()) 
				{
					int h = reg.get(target).getReorder();
					if(reordBuf.get(h).isReady()) 
					{
						
						rs_entry.setVk(reordBuf.get(h).getVal());
						rs_entry.setQk(0);
					}
					else 
					{
						rs_entry.setQk(h);
					}
				}
				else 
				{
					rs_entry.setVk(reg.get(target).getValue());
					rs_entry.setQk(0);
				}
				rs_entry.setA(c.getImm());
				break;
			case "J":
			case "BLTZ":
			case "BGEZ":
			case "BLEZ":
			case "BGTZ":
				rs_entry.setA(c.getImm());
				break;
		
			default :
				if(reg.get(target).isBusy()) 
				{
					int h = reg.get(target).getReorder();
					if(reordBuf.get(h).isReady()) 
					{
						
						rs_entry.setVk(reordBuf.get(h).getVal());
						rs_entry.setQk(0);
					}
					else 
					{
						rs_entry.setQk(h);
					}
				}
				else 
				{
					rs_entry.setVk(reg.get(target).getValue());
					rs_entry.setQk(0);
				}
				
				rb_entry.setReg(reg.get(dest));
				reg.get(dest).setReorder(rb_entry.getEnt());
				reg.get(dest).setBusy(true);
				break;
		}
	}
	
	public static void fetch() {
		if(stopfetch == true){
			return;
		}
		if((!misPre1) && (!misPre2)) {
		int index = (programCycle - 600)/4;
		Instruction c = comm.get(index);
		if(!c.getInstr().equals("0")) instQue.add(c);
		
		if(branTarBuf.get(c)== null && branInstr.contains(c.operation()))
		{ 
			int target = programCycle;
			switch(c.operation())
			{
				case "J":
					target = Integer.parseInt(c.getInstr().split("#")[1]);
					temp2 = programCycle;
					jumpTarget = target;
					choice = 0;
					break;
				case "BEQ": temp1 = programCycle;
				case "BNE":
				case "BLTZ":
				case "BLEZ":
				case "BGTZ":
				case "BGEZ":
				
					target = programCycle+4+Integer.parseInt(c.getInstr().split("#")[1]);
					branchTarget = target;
					choice = 1;
					break;
				default :
					break;
			}
		}
		programCycle = branTarBuf.target(c);
		}
		else 
		{
			programCycle = newCycle;
			misPre1 = false;
			misPre2 = false;
		}
	}	
	
	public static boolean revStr(String str){
		
		for(int i = 0; i < str.length(); i++){
			System.out.println("String Reverse");
		}
		
		return true;
	}
	
	public static void print(Output out){
		out.println("Cycle <"+cycle+">:");
		out.println("IQ:");
		for(Instruction c:instQue)
		{
			out.println(c);
		}
		out.println("RS:");
		for(ReservationStationDetails e:registrationCycle.getQueue())
		{
			out.println(e.getCommand());
		}
		out.println("ROB:");
		for(RobDetails e:reordBuf.getQueue())
		{
			if(!e.committed)
			out.println(e.getInstr());
		}
		out.println("BTB:");
		for(BtbDetails e:branTarBuf.getArr())
		{
			out.println(e);
		}
		out.println("Registers:");
		out.print("R00:");
		print(out, 0);
		out.print("R08:");
		print(out, 8);
		out.print("R16:");
		print(out, 16);
		out.print("R24:");
		print(out, 24);
		out.println("Data Segment:");
		for(int addr = 716,k=0;addr < 716+4*data.size();addr+=40,k+=10)
		{
			print(out,addr,k);
		}
	}
	public static void print(Output out,int addr,int k)
	{
		out.print(addr+":");
		for(int i=k;i<k+10 && i<data.size();i++)
		{
			out.print("	"+data.get(i));
		}
		out.println("");
	}
	public static void print(Output out,int k)
	{
		for(int i=k;i<k+8;i++){
			out.print("	"+reg.get(i));
		}
		out.println("");
	}

	public static boolean chkLWSW(Instruction c)
	{
		for(ReservationStationDetails e:registrationCycle.getQueue())
		{
			if(e.getCommand().equals(c)) break;
			if(e.getCommand().equals("LW") || e.getCommand().equals("SW")){
				
				if(e.isBusyState()) return true;
			}
		}
		return false;
	}
	
	public static boolean chkLWInRsRob(ReservationStationDetails e0)
	{
		for(ReservationStationDetails e:registrationCycle.getQueue())
		{
			if(e == e0) 
				return false;
			if(e.getCommand().operation().equals("SW") && !e.isBusyState() &&e.getA() == e0.getA())
			{
				return true;
			}
		}
		return false;
	}
	
	public static void flush1(){
		ReservationStation temprs = new ReservationStation();
		Rob temprob = new Rob();
		instQue.clear();
		registrationCycle = temprs;
		reordBuf = temprob;
		stopfetch = false;
	}
	
	public static void flush2(Instruction c){
		ReservationStation temprs = new ReservationStation();
		Rob temprob = new Rob();
		instQue.clear();
		for(ReservationStationDetails e: registrationCycle.getQueue())
		{
			temprs.add(e);
			if(e.getCommand().equals(c)) break;
		
		}
		for(RobDetails e: reordBuf.getQueue())
		{
			temprob.add(e);
			if(e.getInstr().equals(c)) break;
		}
		registrationCycle = temprs;
		reordBuf = temprob;
		stopfetch = false;
	}
}
