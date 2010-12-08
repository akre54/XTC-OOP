package xtc.oop;


import java.io.*;
/**@author Paige Ponzeka */

/**Implements Runs some Test Cases based on assigned level
  Currently has no error checking... Ignores anything that is not a java file*/
public class Tester
{
	private final String folderName= "TestCases"; //the default location for all the test cases
	private String[][] test={
	{"0","Exit","Type Zero To Exit"},
	{"1","Basic", "Primitive,Strings,Print,Arrays"},
	{"2","LoopsConditonals","If/Else,Switch,Loops etc"},
	{"3","Methods","Returning Method,Method Calls"},
	{"4","Instances","Instances,Recievers,Arrays of Objects"},
	{"5","MethodChaining","On Instance"},
	{"6","Dependency",""},
	{"7","Inheritance",""}

	};
	/**Calling This constructor will prompt you for all the test cases*/	
	public Tester()
	{
		//print out the array of test Cases
		System.out.println("Here are the Current Test Levels \n");
		for(int r=0;r<test.length;r++)
		{
			System.out.print("\t\t");
			int rlength=test[r].length;
			for(int k=0;k<rlength;k++)
			{
				System.out.print(test[r][k]);
				if(k!=rlength-1)
				{
					System.out.print(" - ");
				}
			}
			System.out.println();
		}
		//print out the user message
		System.out.print("Type a Number to Pick a Level (0 To Exit): ");
		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String levelAsString = null;
		
		//  read the level from the command-line
		try {
			levelAsString = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error!");
			System.exit(1);
		}
		//try to convert to number
		//if given failrs or zero exit
		try
		{
			// the String to int conversion
			int level = Integer.parseInt(levelAsString.trim());
			
			
			//check to see if its Zero
			// print out the value after the conversion
			//System.out.println("int i = " + i);
			if(level==0)
			{
				System.out.println("Operation Cancelled");
			}
			else { // call the level 
				runTest(level);
			}

		}
		catch (NumberFormatException nfe)
		{
			System.out.println("ERROR: " + nfe.getMessage());
			System.exit(1);
		}
	}
/**Constructor Runs only current level*/

	public Tester(int level)
	{
		
		System.out.print(test[0][1]);
		runTest(level);
	}
	/**Constructor recursivly runs up the levels 1,2,3,4 etc*/
	public Tester(int level, boolean all)
	{
		//if all is true
		if(all)
		{//for each level given run a call to this(n)
			for(int i=1;i<=level;i++)
			{
				runTest(i);
			}
		}
	}
	/**runs the test on the given level calling translator on each file in the level*/
	public void runTest(int level)
	{
		String currentFolder= folderName+"/"+level+"/";
		//do all the files in that level
		//calls a file reader to get all the files in a folder with the ending .java
		String[] filenames= getFilesInLevel(level);
		System.out.println("\n\n\n\n\nRunning- Level: "+level + "\t File:"+filenames[0]+"\n");
		//for each file call translator .java making sure to send the file name as an argument
		String[] arguments= new String[2];
		arguments[0]="-printJavaAST";
		System.out.println("LENGTH"+filenames.length);
		for(int i=0;i<filenames.length;i++)
		{
			System.out.println("File"+filenames[i]);
			arguments[1]=currentFolder+filenames[i];
		
			//Translator t=new Translator();
			new Translator().run(arguments);
			System.out.println("Args ran");
		}
	}
	/**@return String[] of all the files in the given subdirectory, 
	 filter to make sure only files and end in .java are included */
	public String[] getFilesInLevel(int level)
	{
		File dir=new File(folderName+"/"+level);
		  FilenameFilter onlyJava = new FileListFilter("", "java");
		String[] files = dir.list(onlyJava);
		/*if(files==null){
			//dir doesn't exist as a directory
		//}
		//else {
			//for (int i=0; i<files.length; i++) {
				//get the name of the file 
			//	System.out.println(files[i]);
				//String filename=children[i];
		//	//}
		}*/
		return files;
	}
}

/** // Define a filter for java source files beginning with F
 */
class FileListFilter implements FilenameFilter {
	private String name; 
	
	private String extension; 
	
	public FileListFilter(String name, String extension) {
		this.name = name;
		this.extension = extension;
	}
	
	public boolean accept(File directory, String filename) {
		boolean fileOK = true;
		
		if (name != null) {
			fileOK &= filename.startsWith(name);
		}
		
		if (extension != null) {
			fileOK &= filename.endsWith('.' + extension);
		}
		return fileOK;
	}
} 
