/* take in a Java file and return a string with
 * its path as a C++ file 
 */

package xtc.oop;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class FileRenamer {
    
   public static void writeToCpp (File infile) {
		try {
	  	 	//System.out.println("getParent: " + infile.getParent());
			//System.out.println("getName: " + infile.getName());		 
			String jname = infile.getName();
			String cname = jname.substring(0,jname.length()-4) + "cpp"; // remove ".java" and add ".cpp"
			
			System.out.println("jname: " + jname + ", cname: " + cname);
			
			FileWriter outstream = new FileWriter(jname.getParent() + cname);
			BufferedWriter out = new BufferedWriter(outstream);

			out.write("works!");
			out.close();
			
		} catch (Exception e) {
	  	    System.out.println("Exception, yo");
		}
    }

	public static void main(String[] args) {
		File testfile = new File("/home/Adam/Downloads/xtc/scr/oop/Translator.java");
		writeToCpp(testfile);
   }

}