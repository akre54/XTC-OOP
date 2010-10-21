/* standard class for handling Java File testing. Dynamically
 * loads a class (passed through command args, default "Test"), and
 * pipes its output from System.out to jfileout.txt
 *
 * v1 by A. Krebs
 */
package xtc.oop;

import java.io.*;
import java.lang.reflect.*;

public class JFileTester {

    public static void main(String[] args) {


        /*  redirect standard Java System.out from console to jfileout.txt.
         *  useful for comparing output of Java and C++ files
         */

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("jfileout.txt");
        } catch (IOException e) {
            System.err.println("redirection failed: " + e.getMessage());
        }
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);


        /*    run class        */

        //String className = (!args[0].equals("")) ? args[0] : "Test";
        String className = "xtc.oop." + "Test";

        try {
            invokeMain(className);
        } catch (Exception e) {
            System.out.print("Exception " + e);
            e.printStackTrace();
        }


    }

    public static void invokeMain(String className)
            throws ClassNotFoundException,
            SecurityException,
            NoSuchMethodException,
            IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            ExceptionInInitializerError {
        Class<?> testClass = Class.forName(className);
        String[] options = {"10", "-printJavaAST", "defg"}; // if needed
        Method mainMethod = testClass.getMethod("main", String[].class);
        Object result = mainMethod.invoke(null, (Object) options);
    }
}
