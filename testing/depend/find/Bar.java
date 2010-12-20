package depend.find;

public class Bar extends Foo{

	Bar(){
	
		Foo.m1();
	}
	public static Foo m2(){
		Foo f=new Foo();
		return f;}

}