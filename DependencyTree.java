/* Finds all dependencies for input jfile, returns ArrayList allPaths
 * Needs to handle both import statements and all members of package.
 *
 * Possibly called from Translate method to return all files, then for over
 * each of the files to translate files with no dependencies first
 *
 * to do:
 * how to avoid circular dependencies?
 * how to decide what what has no dependencies, and store it that way
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

    private ArrayList<String> allPaths = new ArrayList<String>();

    public boolean visited = false; // if dependency already translated
	 
	 /*
	 public DependencyTree(GNode n, String currPath, ArrayList<String> parentPaths) {
		allPaths.append(parentPaths);
		
		DependencyTree(n);	
	 
	 }
	 */

    public DependencyTree(Node n) {

        new Visitor() {

             public void visitPackageDeclaration(GNode n) {

                // list all files in the directory, and add them to our paths list
					 
					 /* support for wildcards... later
					 if ( n.getNode(1).getString(n.getNode(1).size() -1 ).equals("*") )
					 	n.getNode(1).remove(n.getNode(1).size());
					*/
                
                StringBuilder pathbuilder = new StringBuilder();
					 
					 if (n.getNode(1).getString(0).equals("java"))
						return;

                for (int i=0; i<3; i++) {
                    try {
						  		String breadcrumb = n.getNode(1).getString(i); 
						  
						  		// if still part of the path
						  		if (pathbuilder.length() > 0) {
									pathbuilder.append("/");
								}
									
						  		pathbuilder.append(breadcrumb);
								System.out.println(i + " " +n.getNode(1).getString(i));
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
                            //allPaths.add(Class.forName(pathname + '/' + fileName));
                            
									 
									 allPaths.add(pathname + '/' + fileName);
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
                // if using the wildcard operator, visit the folder
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

                // don't add duplicates
                if (!allPaths.contains(filename)) {
                    allPaths.add(filename);
                }
            }

            public void visit(Node n) {
                for (Object o : n) {
                    if (o instanceof Node) {
                        dispatch((Node) o);
                    }
                }
            } //end of visit method
        }.dispatch((Node)n);
	 		  
		  for (String filename : allPaths) {

			  // only translate if not translated
		  	  if (!visited) {
			  	  System.out.println("Now translating " + filename);
				  new Translator().run( new String[] { "-translate", filename } );
				  visited = true;
			  } 
		  }
	}

}