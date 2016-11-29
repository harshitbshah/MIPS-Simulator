import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Output 
{
	
    private static final String CHARSET_NAME = "UTF-8";

    private PrintWriter out;

    public Output(String s) 
    {
        try 
        {
            OutputStream os = new FileOutputStream(s);
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        }
        catch (IOException e) 
        { 
        	e.printStackTrace(); 
        }
    }

    public void close()
    {
    	out.close(); 
    }

    public void print(String s){
    	out.print(s);
    	out.flush();
    	System.out.print(s);
    }

    public void println(String s)
    {
    	out.println(s);
    	out.flush();
    	System.out.println(s);
    }

    public void println(Instruction c)
    {
    	String s = c.toString();
    	println(s);
    }
    
    public void println(BtbDetails e)
    {
    	String s = e.toString();
    	println(s);
    }
    
    public void print(RegisterDetails c)
    {
    	String s = c.toString();
    	out.print(s);
    }

	public PrintWriter getOut() 
	{
		return out;
	}

	public void setOut(PrintWriter out) 
	{
		this.out = out;
	}

	public static String getCharsetName() 
	{
		return CHARSET_NAME;
	}
    
    

}
