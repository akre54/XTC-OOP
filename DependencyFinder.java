/* Lists dependencies and classes for input java file. Calling function
 * responsible for all recursion and cyclical dependency resolution.
 *
 * Needs to handle both import statements and all members of package.
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */
package xtc.oop;


import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class DependencyFinder {

    public boolean verbose = false;

   /**
       *  filePaths holds canonical paths to file's dependencies
       *  fileClasses holds ClassStructs with info about each class in file
       */
    private ArrayList<String> filePaths = new ArrayList<String>();
    private ArrayList<ClassStruct> fileClasses = new ArrayList<ClassStruct>();
    private String currentPackage, currentSuperClass, currentFilePath, currentParentDirectory;

    // ignores all dependencies from following top-level superpackages:
    private final java.util.List<String> excludedPackages = java.util.Arrays.asList(new String[]
                    {"java","javax"});


    public DependencyFinder(Node n, String filePath) {

        currentParentDirectory = (new File(filePath)).getParent(); // all dependencies are relative to the translated file
        currentFilePath = filePath;
        currentPackage = "";
        currentSuperClass = "";

        // good idea / bad idea?
        // gatherDirectoryFiles(currentParentDirectory) // since current directory is treated almost like a package

        new Visitor() {

            public void visitPackageDeclaration(GNode g) {

                // list all files in the directory, and add them to our paths list

                Node n = g.getNode(1);

                /* support for wildcards... later
                               if ( n.getString(n.size() -1 ).equals("*") )
                               n.remove(n.size());
                             */


                StringBuilder pathbuilder = new StringBuilder();

                for (int i = 0; i < n.size(); i++) {
                    String breadcrumb = n.getString(i);

                    // if still part of the path
                    if (pathbuilder.length() > 0)
                        pathbuilder.append("/");

                    pathbuilder.append(breadcrumb);
                }

                String pathname = pathbuilder.toString();
                currentPackage = pathname.replace("/",".");

                gatherDirectoryFiles(pathname);
            }

            public void visitImportDeclaration(GNode g) {

                Node n = g.getNode(1);

                if (excludedPackages.contains(n.getString(0))) {
                    return; // don't convert anything in java.*, javax.*, etc.
                }

                StringBuilder pathbuilder = new StringBuilder(); // build filename from recursing tree through its children

                /*
                            // if using the wildcard operator, visit the folder instead
                            try {
                                    if (n.getString(n.getNode(1).size() -1 ).equals("*"))
                                            gatherDirectoryFiles(pathBuilder.toString());
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
				//currentSuperClass = "Object";
		visit(n);
                addClass(className, n);

                // reset superclass to blank
                currentSuperClass = "";
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
    }

    /* Adds all files in directory to filePaths  */
    private void gatherDirectoryFiles(String dirPath) {
        File dir = null;

        try {
            dir = new File(currentParentDirectory, dirPath);
        } catch (NullPointerException e) {
            System.out.println(dirPath + " could not be found. Exists?");
        }

        if (dir.exists()) {
            for (String fileName : dir.list()) {
                if (fileName.endsWith(".java")) {
                    addPath(dirPath + '/' + fileName);
                }
            }
        }
    }

    /*  add file path (as string) to file dependencies Map, using canonical path
        *  rather than absolute to avoid collisions                    */
       private void addPath (String s) {

           try {
              s = (new File(currentParentDirectory, s)).getCanonicalPath();
           } catch (IOException e) { }

           filePaths.add(s);
       }

    /**
        *   add class names (as ClassStruct object) to both class
        *   lists (all classes and file-specific classes)
        */
       void addClass (String className, GNode n) {

           ClassStruct c = new ClassStruct(currentFilePath, currentPackage,
                            className, currentSuperClass, n);
           
           fileClasses.add(c);
       }


      /**
         * @return all paths to each dependent file of the current file
         */
        public ArrayList<String> getFilePaths() {
            return filePaths;
        }

      /**
            * @return classes in the current file
            */
        public ArrayList<ClassStruct> getFileClasses() {
            return fileClasses;
        }

       /**
	     *  todo: use regex to only replace files that end in .java, not just
             *  anywhere in the file (low priority)
             *
             *  @return canonical paths to CPP filenames
             */
        public ArrayList<String> getFileDependencies() {
            ArrayList<String> translatedLocations = filePaths;

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