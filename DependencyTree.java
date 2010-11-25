/* Finds all dependencies for input jfile, returns ArrayList allPaths
 * Needs to handle both import statements and all members of package.
 *
 *
 * by The Group
 */
package xtc.oop;


import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

//import xtc.util.Tool;

public class DependencyTree {

    public boolean verbose = false;

    /**
      *  filePaths holds all the dependenies  for the current file
      *  allPaths holds all the dependencies for the current file and *its* depedencies
      *
      *  stored as HashMap so we can specify whether each file (key) has
      *  been translated, and store them in a collection
      */
    private HashMap<String,Boolean> allPaths;
    private HashMap<String,Boolean> filePaths = new HashMap<String,Boolean>();
    private HashMap<ClassStruct,Boolean> allClasses;
    private HashMap<ClassStruct,Boolean> fileClasses;


    public DependencyTree(Node n, HashMap<String,Boolean> oldPaths, HashMap<ClassStruct,Boolean> oldClasses) {

        allPaths = oldPaths;
        allClasses = oldClasses;

        new Visitor() {

            public void visitPackageDeclaration(GNode g) {

                // list all files in the directory, and add them to our paths list

                Node n = g.getNode(1);

                /* support for wildcards... later
                if ( n.getString(n.size() -1 ).equals("*") )
                n.remove(n.size());
                 */


                StringBuilder pathbuilder = new StringBuilder();

                if (n.getString(0).equals("java")) {
                    return; // we don't want to convert anything in java.* ...
                }

                for (int i = 0; i < n.size(); i++) {
                    try {
                        String breadcrumb = n.getString(i);

                        // if still part of the path
                        if (pathbuilder.length() > 0)
                            pathbuilder.append("/");

                        pathbuilder.append(breadcrumb);
                        //System.out.println(i + " " + n.getString(i));
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                String pathname = pathbuilder.toString();

                File dir = null;

                try {
                    dir = new File(pathname);
                } catch (NullPointerException e) {
                    System.out.println(pathname + " could not be found. Exists?");
                }

                if (dir.exists()) {
                    for (String fileName : dir.list()) {
                        if (fileName.endsWith(".java")) {
                            // will eventually use Reflection for e.g. finding non-filenamed classes
                            //addPath(Class.forName(pathname + '/' + fileName));

                            addPath(pathname + '/' + fileName);
                            //visitImportDeclaration(pathname + '/' + fileName);
                        }
                    }
                }


            }

            public void visitImportDeclaration(GNode g) {

                Node n = g.getNode(1);

                // only add if not part of java std library, and
                if (n.getString(0).equals("java")) {
                    return;
                }

                StringBuilder pathbuilder = new StringBuilder(); // build filename from recursing tree through its children

                /*
                // if using the wildcard operator, visit the folder instead
                try {
                	if (n.getString(n.getNode(1).size() -1 ).equals("*"))
                		visitPackageDeclaration( (GNode)n );
               	return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 */

                for (int i = 0; i < n.size(); i++) {
                    String breadcrumb = n.getString(i);

                    // if still part of the path
                    if (pathbuilder.length() > 0) {
                        pathbuilder.append("/");
                    }

                    pathbuilder.append(breadcrumb);

                    if (i == (n.size() - 1)) // || n.getNode(1).getString(n.getNode(1).size() -1).equals("java") ) EVENTUALLY NEEDS .java filename SUPPORT
                    {
                        pathbuilder.append(".java");
                    }
                }

                String filename = pathbuilder.toString();

                addPath(filename);
            }
            public void visitClassDeclaration(GNode n){
                String className = n.getString(1);
		visit(n);
            }

            public void visitExtension(GNode n){
		String superClass = n.getNode(0).getNode(0).getString(0);
            }


            public void visit(Node n) {
                for (Object o : n) {
                    if (o instanceof Node) {
                        dispatch((Node) o);
                    }
                }
            } //end of visit method
        }.dispatch(n);

        for (String filename : filePaths.keySet()) {

            // only translate if not translated
            if ( !allPaths.containsKey(filename) || !(allPaths.get(filename))) {
                System.out.println("Now translating " + filename);
                allPaths.put(filename, true);
                new Translator(allPaths).run(new String[]{"-translate", filename});
            }
            /* NOTE: I overloaded the Translator constructor to take a HashMap rather
               than overloading the run method because I thought it looked cleaner.
               if this causes problems let me know.
             */
        }
    }

    /*
     *  add file path (as string) to both the maps of file-specific
     *  and the whole structure dependencies, using canonical path
	  *  rather than absolute to avoid collisions
     */
       private void addPath (String s) {

           try {
           		s = (new File(s)).getCanonicalPath();
           } catch (IOException e) {
               e.printStackTrace();
           }

           filePaths.put(s, false);

           // don't add duplicates
           if (null == allPaths.get(s)) {
           		allPaths.put(s, false);
           }
       }

        /**
         * @return all paths to each dependent file of the whole dependency
         * structure as ArrayList
         */
        public ArrayList<String> getAllPaths() {
            return new ArrayList<String>(allPaths.keySet());
        }


      /**
         * @return all paths to each dependent file of the current file
         */
        public ArrayList<String> getFilePaths() {
            return new ArrayList<String>(filePaths.keySet());
        }

        /**
         * @return all classes to each dependent file of the whole dependency
         * structure as ArrayList of ClassStruct
         */
        public ArrayList<ClassStruct> getAllCLasses() {
            return new ArrayList<ClassStruct>(allClasses.keySet());
        }


      /**
         * @return all classes in the current file
         */
        public ArrayList<ClassStruct> getFileClasses() {
            return new ArrayList<ClassStruct>(fileClasses.keySet());
        }

       /**
	     *  todo: use regex to only replace files that end in .java, not just
        *  anywhere in the file
        *
        *  @return relative paths to CPP filenames
        */

        public ArrayList<String> getFileDependencies() {
            ArrayList<String> translatedLocations = new ArrayList<String>(filePaths.keySet());

            for (int i=0; i<translatedLocations.size(); i++) {

                translatedLocations.set(
                        i,
                        translatedLocations.get(i).replace(".java", "_methoddef.cpp")
                    );

                translatedLocations.set(
                        i,
                        translatedLocations.get(i).replace("/", ".")
                    );
                
                // using "/" as path separator char. If we ever use this on
                // Windows, should probably replace with File.pathSeparator
            }

            return translatedLocations;
        }
}