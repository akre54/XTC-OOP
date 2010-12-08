
/**@Paige Ponzeka
 COMPILED AND RUNS 12-8-2010*/
/**Runs Basic Primitive type instances
 Test the Boundaries of Each Number-*/
public class Basic
{
	public static void main (String[] args)
	{
		
		//byte
		byte smallest=-128;
		byte largest;
		largest=127;
		
		//short
		short smaller=-32768;
		short larger=32767;
	
		//Ints
		int small=-2147483648;
		int large;
		large=2147483647;
				
		//long
		long tiny=-92233;
		long huge;
		huge= 92233;
		
		//float
		float teeny=9223;
		float giant;
		giant =922337203;
		//double
		
		//Booleans
		boolean flase = false;
		boolean tue;
		tue = true;
				
		//char
		char character='A';
		char car;
		car = 'z';
		
		/**Tests Basic Expressions*/
		//Addititve Expression (Int)
		int three= 1+2;
		int five = three +2;
		int eleven= 2+3+3;
		
		//Additive Expression (Float)
		float dec= 18932434+234234;
		float thums = 444+33+333+234234;
		
		//Addititve Expression(double)
		double decimal= 1.89+.01;
		double threenums = 1.80+.04+.05+.01;
		
		
		//Muliplicative Expression(Int)
		int mult = 9*3;
		int threemult= 3*3*3;
		
		//Multiplicative Expression(Float)
		float multlarge=10*299;
		float threeLarge= 10*299*3;
		
		//Multiplicative Expression (double)
		double deciMult= .09*5;
		double deciMany=.08*.06*1;
		
		//modulous expression
		int modnum= 8 % 3;
		
		//PreDecrement Expression
		int decrement = --modnum;
		
		//PreIncrement Expression
		int preincrement=++modnum;
		//PostDecrement Expression
		int postdcrement= modnum--;
		//post Increment Expression
		int postincrement=modnum++;
		
		
		//Unary Expression
		int negative = -9;
		
			
		//STRINGS
		//Basic String
		String k = "K";
		
		//Int Concated to String
		String intCon= "" +9;
		
		//String Concating
		String concat= "Bull" +"Dog";
		
		
		
		//SYSTEM.OUT.PRINT
		//Plain text - Print
		System.out.print(concat);
		
		//Plain Text - Println
		System.out.println("HELLO");
		
		//Concating Sttrings Print
		System.out.print("RAWR" + " I'm" +" A " +"Dinosaur");
		
		//Concating Strings Println
		System.out.println("Hello" + "Seattle");
		
		//Basic Expressions inside Println
		System.out.print(""+1+9);
		
		/**Test PRimitive Arrays*/
		//byte
		byte[] BytesArray= new byte[4];
		//short
		short[] shortsArray= new short[5];
				
		//Ints
		int[] intarray = new int[5];
		
		//long
		long[] longArray;
		longArray=new long[4];
		
		//float
		float[] floatArray;
		floatArray= new float[6];
		
		
		//double
		double[] doubleArray;
		doubleArray= new double[4];
		
		//Booleans
		boolean[] booleanArray= new boolean[2];
		
		
		//char
		char[] charArray= new char[3];
		
		//Object
		Object[] objectArray = new Object[5];
		
		//Class
		Class[] classArray = new Class[9];
				
	}
}
		