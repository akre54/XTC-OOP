
public class test1 {

  public static final Object R1 = new Object();
  public static final Object R2 = new Object();
  public static final Object R3 = new Object();
  public static final Object R4 = new Object();

  public int count;

  public Test() {
    count = 0;
  }

  public Object m1() {
    return R1;
  }

  public static Object m2() {
    return R3;
  }

  public Test m3() {
    count++;
    return this;
  }

  public Test m4() {
    count++;
    return this;
  }

  public Test m5(Test t) {
    return t.m3().m4();
  }

  public Object m6(Test t) {
    return R1;
  }

  public Object m6(Rest r) {
    return R2;
  }

  public Object m7(Object o) {
    return R3;
  }

  public Object m7(String s) {
    return R4;
  }

  public Object m7(Test t) {
    return R1;
  }

  public Object m7(Rest r) {
    return R2;
  }

  public Object m8(Test t) {
    return R1;
  }

  public Object m8(Rest r) {
    return R2;
  }

  public Object m8(Test t1, Test t2) {
    return R3;
  }

  public Object m8(Rest r, Test t) {
    return R4;
  }

  public static void main(String[] args) {
    Test t;
    Rest r;
    Object o = null;

    int test = 0;
    int success = 0;


    if ((R1 != null) && (R1 != R2) && (R1 != R3) && (R1 != R4)) {
      System.out.println("PASS Object.<init>()");
      success++;
    } else {
      System.out.println("FAIL Object.<init>()");
    }
    test++;

    // -----------------------------------------------------------------------

    r = new Rest();
    o = r.m1();

    if (R2 == o) {
      System.out.println("PASS r.m1()");
      success++;
    } else {
      System.out.println("FAIL r.m1()");
    }
    test++;

    // -----------------------------------------------------------------------

    t = r;

    if (t == r) {
      System.out.println("PASS t == r");
      success++;
    } else {
      System.out.println("FAIL t == r");
    }
    test++;

	
	System.out.println();
	System.out.println(success + " out of " + test + " tests have passed.");
  }
	
}
