public class demo {
	public static void main (String [] args) {
		beta b = new beta();
		String a = b.m2();
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