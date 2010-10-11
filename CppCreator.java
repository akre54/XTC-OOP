package xtc.oop;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

class CppCreator {

    File c_file;
    BufferedWriter writer;


    public CppCreator (File j_file) {
	c_file = convert_to_c(j_file);
	BufferedWriter out = new BufferedWriter(new FileWriter(outfilename));
    }

    private static File convert_to_c (File input) {
	String jname = input.getName();
	String cname = jname.substring(0,jname.length()-4) + "cpp"; // remove ".java" and add ".cpp"

	File cfile = new File(input.getParent(), cname);

	return cfile;
    }

    public void write (String content) {
	
    }
}