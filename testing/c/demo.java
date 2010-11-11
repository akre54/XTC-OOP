public class demo
{
	public static void main(String[] args) {
		init();
	}
	
	public static void init ()  { 
		mypack.TestA testa = new mypack.TestA (4);
		mypack.TestB testb = new mypack.TestB (31.3); 
		System.out.println("Prod = " + (testa.i * testb.x));
	}
}