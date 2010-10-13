package xtc.oop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import xtc.util.Tool;
import xtc.util.Runtime;

/** 
 * Creates a cpp file you can write to.
 * Use the write method to strings to the output string and the close method to clos and create the file.
 * 
 */
 
class CppCreator {

    File cFile;
	FileWriter outputWriter;

/** 
 * Create a new cpp file that has the same name and path as the origonal .java file
 *
 * @param jFile a file whose name ends in .java
 */ 
    public CppCreator (File jFile) {
		cFile = convertNameToC (jFile);
		try {
			outputWriter = new FileWriter(cFile);
		}
		catch (IOException a) {
		}
    }

/** 
 * Create a new cpp file that has the same name and path as the origonal .java file.
 * Follows the pathname conventions of the File class: http://download.oracle.com/javase/1.4.2/docs/api/java/io/File.html
 * @param pathName A String that contains the path and file name of a file ending in .java.
 */ 
	public CppCreator (String pathName) {
		cFile = convertNameToC (pathName);
		try {
			outputWriter = new FileWriter(cFile);
		}
		catch (IOException a) {
		}
	}


    private static File convertNameToC (File input) {
		String jname = input.getName ();
		String cname = jname.substring (0,jname.length()-4) + "cpp"; // remove ".java" and add ".cpp"
		File cfile = new File (input.getParent (), cname);
		return cfile;
    }

    private static File convertNameToC (String pathName) {
		String cname = pathName.substring (0,pathName.length()-4) + "cpp"; // remove ".java" and add ".cpp"
		File cfile = new File (pathName);
		return cfile;
    }

	public void write (String input) {
		try {
			outputWriter.write(input);
		}
		catch (IOException a) {
		}
	}

	private void flush () {
		try {
			outputWriter.flush();
		}
		catch (IOException a) {
		}
	}

	public void close () {
		try {
			outputWriter.flush();
			outputWriter.close();
		}
		catch (IOException a) {
			//	super.runtime.error("Problem closing the file "+cFile.getName());
		}
	}
}