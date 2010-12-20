public class demo {
	public static void main (String [] args) {
		beta b = new beta();
		String a = b.m1().m2(); //should print out ok
		System.out.println(a);
	}
}
class beta {
	beta () {
	}
	public beta m1(){
		beta a= new beta ();
		return a;
	}
	public String m2() {
		return "ok";
	}
}