/* Finds all dependencies for input jfile, returns ArrayList allPaths
 * Needs to handle both import statements and all members of package.
 *
 * to do:
 * how to avoid circular dependencies?
 *
 * v1 by Adam Krebs
 */
package xtc.oop;


import java.util.ArrayList;
import java.io.File;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

public class DependencyTree {

    public boolean verbose = false;

    /**
      *  filePaths holds all the dependenies  for the current file
      *  allPaths holds all the dependencies for the current file and *its* depedencies
      */
    private ArrayList<String> allPaths = new ArrayList<String>();
    private ArrayList<String> filePaths = new ArrayList<String>();
    public boolean visited = false; // if dependency already translated

/* maybe, possibly used for keeping track of recursing through dependencies, sometime in the future
        public DependencyTree(GNode n, String currPath, ArrayList<String> parentPaths) {
        allPaths.append(parentPaths);

        DependencyTree(n);

        }
*/

    public DependencyTree(Node n) {

        new Visitor() {

         /**
             * takes GNode of PackageDeclaration and adds all files
             * to paths list
             */
            public void visitPackageDeclaration(GNode n) {

                // list all files in the directory, and add them to our paths list

           /* support for wildcards... later
                if ( n.getNode(1).getString(n.getNode(1).size() -1 ).equals("*") )
                n.getNode(1).remove(n.getNode(1).size());
                 */

                StringBuilder pathbuilder = new StringBuilder();

                if (n.getNode(1).getString(0).equals("java")) {
                    return;
                }

                for (int i = 0; i < 3; i++) {
                    try {
                        String breadcrumb = n.getNode(1).getString(i);

                        // if still part of the path
                        if (pathbuilder.length() > 0) {
                            pathbuilder.append("/");
                        }

                        pathbuilder.append(breadcrumb);
                        System.out.println(i + " " + n.getNode(1).getString(i));
                    } catch (IndexOutOfBoundsException e) {
                        // do nothing
                    } catch (Exception e) {
                        // do nothing
                    }

                }

                String pathname = pathbuilder.toString();

                File dir = null;

                try {
                    dir = new File(pathname);
                } catch(NullPointerException x) {
                    //throw new ClassNotFoundException(pathname + " could not be found. Exists?");
                    System.out.println(pathname + " could not be found. Exists?");
                }

                if (dir.exists()) {
                    for (String fileName : dir.list()) {
                        if(fileName.endsWith(".java")) {
                            //addPath(Class.forName(pathname + '/' + fileName));
                            
                            addPath(pathname + '/' + fileName);
                            //visitImportDeclaration(pathname + '/' + fileName);
                        }
                    }
                }


            }

            public void visitImportDeclaration(GNode n) {
				
					 // only add if not part of java std library, and 
					 if (n.getNode(1).getString(0).equals("java"))
					 	return;

                StringBuilder pathbuilder = new StringBuilder(); // build filename from recursing tree through its children

/*
                // if using the wildcard operator, visit the folder instead
					 try {					 	
						if (n.getNode(1).getString(n.getNode(1).size() -1 ).equals("*"))
                    visitPackageDeclaration( (GNode)n.getNode(1) );
						  return;
					 } catch (Exception e) {
					 	System.out.println();
					 	e.printStackTrace();
						
						// does nothing
					 }
*/
						
					for ( int i=0; i < n.getNode(1).size(); i++ ) {
					  		String breadcrumb = n.getNode(1).getString(i); 
						  
					  		// if still part of the path
					  		if (pathbuilder.length() > 0) {
								pathbuilder.append("/");
							}
									
					  		pathbuilder.append(breadcrumb);
														
							if (i == (n.getNode(1).size() -1) ) // || n.getNode(1).getString(n.getNode(1).size() -1).equals("java") ) EVENTUALLY NEEDS .java filename SUPPORT
								pathbuilder.append(".java");
					}
					
					String filename = pathbuilder.toString();

                    addPath(filename);
            }

            public void visit(Node n) {
                for (Object o : n) {
                    if (o instanceof Node) {
                        dispatch((Node) o);
                    }
                }
            } //end of visit method
        }.dispatch((Node)n);
	 		  
		  for (String filename : filePaths) {

			  // only translate if not translated
		  	  if (!visited) {
			  	  System.out.println("Now translating " + filename);
				  new Translator().run( new String[] { "-translate", filename } );
				  visited = true;
			  } 
		  }
	}

    /*
     *  add file path (as string) to both the lists of file-specific and the whole structure dependencies
     * may need to be changed if it doesn't work for the recursive tree
     */
       private void addPath (String s) {
           filePaths.add(s);

           // don't add duplicates
           if (!allPaths.contains(s))
            allPaths.add(s);
       }

      /**
         * @return all paths to each dependent file of the whole dependency structure
         */
        public ArrayList<String> getAllPaths() {
            return allPaths;
        }


      /**
         * @return all paths to each dependent file of the current file
         */
        public ArrayList<String> getFilePaths() {
            return filePaths;
        }

        /**
           *  uses string replacement to convert relative java
           *  filenames (stored in filePaths) to CPP names
           *
           *  todo: use regex to only replace files that end in .java, not just anywhere in the file...
           */
        public ArrayList<String> getFileDependencies() {
            ArrayList<String> translatedLocations = filePaths;

            for (String filename : translatedLocations) {
                filename.replace(".java", "_methoddef.cpp");
                filename.replace( "/" , ".");
            }

            return translatedLocations;
        }
}