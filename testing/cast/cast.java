public class cast{


	public static void main(String args[]){

		/*String s = "RAWR";
		int f=5;
		Object o = (Object)f;
		System.out.println(o.toString());*/
		Magoo ma = new Magoo();
		Mister mi = new Mister(4);
		mi.printer();
		ma=(Magoo)mi;
		Mister mr = (Mister)mi;
		ma.printer1();
		
	}
	}
class Magoo{
	public Magoo()
	{
		
	}
	public void printer1()
	{
		System.out.println("I Am A MAGOO");
	}
}
class Mister extends Magoo
{
	public Mister(int h)
	{
	}
	public void printer()
	{
		System.out.println("I Am Mister!");
	}
	
}