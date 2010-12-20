public class demo {
	public static void main (String [] args) {
		beta lmao = new beta();
		lmao.m3();
		String r = lmao.m2().m3();
		lmao.m3();
		String a = lmao.m1().m2().m3(); //should print out ok
		System.out.println(r);
	}
}
class beta {
	beta () {
	}
	public beta m1(){
		beta a= new beta ();
		return a;
	}
	public beta m2() {
		beta b = new beta();
		return b;
	}
	public String m3(){
		return "thisssss";
	}
	
}