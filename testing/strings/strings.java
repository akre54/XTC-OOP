public class strings {
	
	public static void main (String [] args) {
		int success=0;
		int test=0;
		String s1;		
		String s2;   
		

		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(String)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = "Hel" + "lo Kitty #1";
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(String + String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(String + String)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = "He" + "ll" + "o Kitty #1";
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(String + String + String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(String + String + String)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = "Hello Kitty #" + 1;
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(String + int)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(String + int)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = "Hello Kitty #" + '1';
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(String + char)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(String + char)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = (char)72 + "ello Kitty #1";
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(char + String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(char + String)");
		}
		test++;

		// -----------------------------------------------------------------------

		char c = 72;
		s2 = c + "ello Kitty #1";
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(char + String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(char + String)");
		}
		test++;

		// -----------------------------------------------------------------------

		s2 = 'H' + "ello Kitty #1";
		if (s1.equals(s2)) {
			System.out.println("PASS s1.equals(char + String)");
			success++;
		} else {
			System.out.println("FAIL s1.equals(char + String)");
		}
		test++;
		
		System.out.println();
		System.out.println(success + " out of " + test + " tests have passed.");
  
	}
}