public class demo{
	public static void main(String args[]) {
		Presentation p = new Presentation();

		System.out.println(p.m5()); // should print 5
		System.out.println(p.m2(3,"passed"));
		System.out.println(p.m1()); // should print 1, not 5
		
	}
		public int m1(){ return 5; }
		public String m2(int i,String s){ return "yup"; }
		public void m3(boolean is){ System.out.println(m1()); }
		private void m7(boolean is){ System.out.println(m1()); }
		static double m4(){ return 3.2; }
}

class Presentation extends demo {
	Presentation(){}	
	public String m5(){ return "" + super.m1(); }
	private void m6(){ System.out.println(m5()); }
	public int m1(){ return 1; }
}
