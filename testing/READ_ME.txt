Tester instructions
Put your demo.java file into a directory inside testing/ such as testing/a/demo.java
From the testing directory, run ./test.sh.
The program will remove any previous translations (pretty much all files but .java files), translate to cpp, compile the cpp, execute the cpp, save the standard output to cpp.out.txt
The program will then compile and execute the origonal java code and save that output to java.out.txt.
Finally, sdiff wil show the output files side by side.
