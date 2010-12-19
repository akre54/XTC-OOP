/**@Paige Ponzeka
   COMPILED AND RUNS 12-8-2010*/
/**Runs Basic Primitive type instances
   Test the Boundaries of Each Number-*/

public class demo
{
	public static void main (String[] args)
	{
		/**Tests Basic Expressions*/
		//Addititve Expression (Int)
		int three= 1+2; //CRASHES ON THIS
		int five = three +2;
		int eleven= 2+3+3;
		System.out.println("Int:" + eleven);
		System.out.println("Eight:" +three+five);
		
		//Additive Expression (Float)
		float dec= 18932434+234234;
		float thums = 444+33+333+234234;
		System.out.println("Float" + thums);
		
		//Addititve Expression(double)
		double decimal= 1.89+.01;
		double threenums = 1.80+.04+.05+.01;
		System.out.println("Threenums:" +threenums);
		
		
		//Muliplicative Expression(Int)
		int mult = 9*3;
		int threemult= 3*3*3;
		System.out.println("Threemult:"+threemult);
		
		//Multiplicative Expression(Float)
		float multlarge=10*299;
		float threeLarge= 10*299*3;
		System.out.println("threeLarge:" +threeLarge);
		
		//Multiplicative Expression (double)
		double deciMult= .09*5;
		double deciMany=.08*.06*1;
		System.out.println("double:" +deciMult);
		
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
		
	}
}