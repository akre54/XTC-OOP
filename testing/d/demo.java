import java.applet.Applet;
import mypack.*;
import mypack.extrapack.*;

public class demo
{
	public static void main(String[] args) {
		init();
	}
	
	public static void init() {
		TestA testa = new TestA (4);
		TestB testb = new TestB (31.3); 
		TestC testc = new TestC (false);
		if(testc.flag)
			System.out.println ("testa.i = "+testa.i);
		else
			System.out.println ("testa.x = "+testb.x); 
	}
}