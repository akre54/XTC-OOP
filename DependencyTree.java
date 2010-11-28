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

public class DependencyTree {

    public boolean verbose = false;

    /**
      *  filePaths holds all the dependenies  for the current file
      *  allPaths holds all the dependencies for the current file and *its* depedencies
      *
      *  stored as HashMap so we can specify whether each file (key) has
      *  been translated, and store them in a collection. Using HashMap
      */
    private HashMap<String,Boolean> allPaths;
    private HashMap<String,Boolean> filePaths = new HashMap<String,Boolean>();
    private HashMap<ClassStruct,Boolean> allClasses;
    private HashMap<ClassStruct,Boolean> fileClasses = new HashMap<ClassStruct,Boolean>();
    private String currentPackage = "";
    private String currentSuperClass = "";
    private String currentFilePath;



    public DependencyTree(Node n, String filePath,
            HashMap<String,Boolean> oldPaths,
            HashMap<ClassStruct,Boolean> oldClasses) {

        currentFilePath = filePath;
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
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                String pathname = pathbuilder.toString();
                currentPackage = pathname.replace("/",".");

                File dir = null;

                try {
                    dir = new File(pathname);
                } catch (NullPointerException e) {
                    System.out.println(pathname + " could not be found. Exists?");
                }

                if (dir.exists()) {
                    for (String fileName : dir.list()) {
                        if (fileName.endsWith(".java")) {

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
                		// should copy code of visitPackageDeclaration, not use it directly, since there are now modifications specific to packages
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
                addClass(className, n);
            }

            public void visitExtension(GNode n){
		currentSuperClass = n.getNode(0).getNode(0).getString(0);
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

                // Translator class should handle all Translations
                //new Translator(allPaths, allClasses).run(new String[]{"-translate", filename});
            }
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
           if (!allPaths.containsKey(s)) {
                allPaths.put(s, false);
           }
       }

    /**
        *   add class names (as ClassStruct object) to both class
        *   lists (all classes and file-specific classes)
        */
       void addClass (String className, GNode n) {

           ClassStruct c = new ClassStruct(currentFilePath, currentPackage,
                            currentSuperClass, className, n);
           
           fileClasses.put(c, false);

           if (!allClasses.containsKey(c))
               allClasses.put(c, false);

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
        public ArrayList<ClassStruct> getAllClassNames() {
            return new ArrayList<ClassStruct>(allClasses.keySet());
        }
        public HashMap<ClassStruct,Boolean> getAllClasses() {
            return allClasses;
        }


      /**
         * @return all classes in the current file
         */
        public ArrayList<ClassStruct> getFileClassNames() {
            return new ArrayList<ClassStruct>(fileClasses.keySet());
        }
        public HashMap<ClassStruct,Boolean> getFileClasses() {
            return fileClasses;
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