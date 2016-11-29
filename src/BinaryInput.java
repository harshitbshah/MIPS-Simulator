import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



public final class BinaryInput {
    private BufferedInputStream buffInpStr;      
    private static final int eof = -1;   
    private int buff;

    public BinaryInput(String s) 
    {

        try 
        {
            File file = new File(s);
            if (file.exists()) 
            {
                FileInputStream fis = new FileInputStream(file);
                buffInpStr = new BufferedInputStream(fis);
                fillBuffer();
            }
        }
        catch (IOException ioe) 
        {
            System.err.println("Could not open " + s);
        }
    }
    
    private void fillBuffer() 
    {
        try 
        { 
        	buff = buffInpStr.read();  
        }
        catch (IOException e) 
        { 
        	System.err.println("EOF"); buff = eof; 
        }
    }    
    public char readChar() 
    {
        int x = buff;
        fillBuffer();
        return (char) (x & 0xff);
    }
    
    public int readInt() 
    {
    	int x = 0;
        for (int i = 0; i < 4; i++) 
        {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }
    
    public boolean isEmpty()
    {
    	return buff == eof;
    }

	public BufferedInputStream getBuffInpStr() {
		return buffInpStr;
	}

	public void setBuffInpStr(BufferedInputStream buffInpStr) {
		this.buffInpStr = buffInpStr;
	}

	public int getBuff() {
		return buff;
	}

	public void setBuff(int buff) {
		this.buff = buff;
	}

	public static int getEof() {
		return eof;
	}
    
    
}
