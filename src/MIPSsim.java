public class MIPSsim {

	public static void main(String[] args) {

		try {

			if (args.length < 2) {
				System.out.println("Please pass enough arguments");
				System.out.println("arg[0] ->  Input File Name");
				System.out.println("arg[1] ->  Output File Name");
				System.out.println("arg[2] ->  (optional) -Tm:n , where m = Start Cycle, n = End Cycle");
				return;
			}

			String in = args[0];
			String out = args[1];
			String cycle ="";
			
			if(args.length  == 3 ) 
				cycle = args[2];

			Tomasulo_Algorithm tomSim = new Tomasulo_Algorithm();
			tomSim.simulate(in, out, cycle);
			
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
