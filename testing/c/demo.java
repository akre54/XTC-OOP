package c;
import c.mypack.*;

public class demo
{

	int i = 5;

	public demo() {
		m1();
	}

	void m1() {
		System.out.println(i);
	}

	public static void main(String[] args) {
		demo d= new demo();
		TestA a = new TestA();
		System.out.println(a.i);
		a.i = 12;
		System.out.println(a.i);
	}

}
