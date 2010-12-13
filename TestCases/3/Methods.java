
/**@Paige Ponzeka*/
/**Tests
 Returning Methods , Method Calls */
public class Methods
{
	public static void main (String[] args)
	{
		int four = getFour();
		System.out.println("You just got " +getFour() + "From Another Method");
		String message = getHi();
		System.out.print("You Just got " +getHit()+ "From another Method");
		Object o =getObj();
		System.out.print("You just got " + o + "From another method");
		
	}
	public int getFour()
	{
		return 4;
	}
	public String getHi()
	{
		return "Hi";
	}
	public Object getObj()
	{
		Object j = new Object();
		return j;
	}
	public void newMethod(int k)
	{
		System.out.println("You just sent " + k + " To New Method");
	}
	
}