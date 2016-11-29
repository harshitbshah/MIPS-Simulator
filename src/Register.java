import java.util.ArrayList;

public class Register {
	public static int N =32;
	public static ArrayList<RegisterDetails> registers;
	
	public Register(){
		registers = new ArrayList<RegisterDetails>();
		for(int i=0;i<N;i++){
			registers.add(new RegisterDetails());
		}
	}
	
	public RegisterDetails get(int i){
		return registers.get(i);
	}
	
}
