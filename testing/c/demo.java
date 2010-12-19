package c;
import c.mypack.*;

public class demo
{

	int i = 5;

	public demo() {
		m1();
	}

	void m1() {
		System.out.println(i);
	}

	public static void main(String[] args) {
		init();
	}
	
	public static void init ()  { 
		TestA testa = new TestA (4);
		TestB testb = new TestB (31.3); 
		//System.out.println("Prod = " + (testa.i * testb.x));
	}
}
