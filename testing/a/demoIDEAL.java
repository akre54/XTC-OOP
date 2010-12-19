

public class demoIDEAL{


	public static void main(String args[]){

		
		int[] intArray =new int[6];
		Object[] objArray = new Object[2];
		Class[] claArray = new Class[3];
		String[] strArray = new String[3];
		
		System.out.println("\ncreated an int[]: "+intArray.getClass());
		System.out.println("created an object[]: "+objArray.getClass());
		System.out.println("created an class[]: "+claArray.getClass());
		System.out.println("created an string[]: "+strArray.getClass());



		System.out.println("\nthe intArray length: "+intArray.length);
		System.out.println("the objArray length: "+objArray.length);
		System.out.println("the claArray length: "+claArray.length);

		System.out.println("\nfor loop sets intArray values:");
		for(int i=0;i<intArray.length;i++){
			intArray[i]= i+3;
			System.out.println("intArray[+"+i+"]=" + intArray[i]);
		}
		
		
		Laptop book = new Laptop();
		Computer machine = new Computer();
		System.out.println("\ncreated an Instance of Laptop: "+book);
		System.out.println("created an Instance of Computer: "+machine);
		
		System.out.println("\nused a Laptop instance method: book.getBLife() = "+ book.getBLife());
		
		System.out.println("\nused a virtual method: machine.getYear() = "+ machine.getYear());
		System.out.println("used same virtual method: book.getYear() = "+ book.getYear());
	
		System.out.print("\nused a virtual method --> machine.overwriteMe(): ");
						machine.overwriteMe();
		System.out.print("overwrote a virtual method --> book.overwriteMe(): ");
						book.overwriteMe();
		System.out.println("\nbook.hashCode() : "+book.hashCode());
		System.out.println("book.equals(machine) : "+book.equals(machine)+"");
		System.out.println("book.getClass() :"+book.getClass()+"");
		System.out.println("book.toString(): "+book.toString()+"");
		System.out.println("book.getName(): "+book.getClass().getName()+"");
		System.out.println("book.getClass().getSuperclass: "+book.getClass().getSuperclass()+"");
		System.out.println("book.getClass().isInstance(machine.getClass()): "+book.getClass().isInstance(machine.getClass())+"");
		System.out.println("\nmachine.hashCode(): "+machine.hashCode()+"");
		System.out.println("machine.equals(book)(): "+machine.equals(book)+"");
		System.out.println("machine.getClass(): "+machine.getClass()+"");
		System.out.println("machine.toString(): "+machine.toString()+"");
		System.out.println("machine.getName(): "+machine.getClass().getName()+"");
		System.out.println("machine.getClass().getSuperclass(): "+machine.getClass().getSuperclass()+"");
		System.out.println("machine.getClass().isInstance(book.getClass()): "+machine.getClass().isInstance(book.getClass())+"");
		

	
	}

}
class Computer{
	public int year_created;
	public boolean used;
	Computer(){
		year_created = 2004;
		used = true;
	}
	public int getYear(){
		return year_created;
	}
	public void reboot(){
		System.out.println("\t*doing reboot stuff*");
	}
	public void overwriteMe(){
		System.out.println("i'm gonna do T H I S in Computer classes");
		
	}
}

class Laptop extends Computer{
	private int battery_life;
	private double weight;
	private String brand;

	Laptop(){
		battery_life = 2;
		weight = 1.5;
		brand = "MacBook";
	}
	public int getBLife(){
		return battery_life;
	}
	public double getWeight(){
		return weight;
	}
	public void overwriteMe(){
		System.out.println("i'm gonna do THIS in Laptop classes");
		
	}


}

