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
    private ArrayList<FileDependency> fileDependencies = new ArrayList<FileDependency>();
    private ArrayList<ClassStruct> fileClasses = new ArrayList<ClassStruct>();
    private String currentPackage, currentSuperClass, currentFilePath, currentParentDirectory;

    // ignores all dependencies from following top-level superpackages:
    private final java.util.List<String> excludedPackages = java.util.Arrays.asList(new String[]
                    {"java","javax"});


    public DependencyFinder(Node n, String filePath) {

        currentParentDirectory = (new File ((new File(filePath)).getParent())).getParent(); // all dependencies are relative to the translated file
        currentFilePath = filePath;
        currentPackage = "";
        currentSuperClass = "";

        new Visitor() {

            public void visitPackageDeclaration(GNode g) {

                // list all files in the directory, and add them to our paths list

                Node n = g.getNode(1);

                StringBuilder pathbuilder = new StringBuilder();

                for (int i = 0; i < n.size(); i++) {
                    String breadcrumb = n.getString(i);

                    // if still part of the path
                    if (pathbuilder.length() > 0)
                        pathbuilder.append("/");

                    pathbuilder.append(breadcrumb);
                }

                String path  = pathbuilder.toString();
                currentPackage = path.replace("/",".");
					 
					 // add explicit imports first, then package, then current directory
					 gatherDirectoryFiles(path, DependencyOrigin.CURRENTPACKAGE);
					 
					 /*  Needed??
					 // add all members of current directory. Will not add files already added (e.g. explicitly declared package)
				    gatherDirectoryFiles(currentPackage, DependencyOrigin.CURRENTDIRECTORY); // since current directory is treated almost like a package
				    */
            }

            public void visitImportDeclaration(GNode g) {

                Node n = g.getNode(1);

                if (excludedPackages.contains(n.getString(0))) {
                    return; // don't convert anything in java.*, javax.*, etc.
                }

                StringBuilder pathbuilder = new StringBuilder(); // build filename from recursing tree through its children

                for (int i = 0; i < n.size(); i++) {
                    String breadcrumb = n.getString(i);

                    // if still part of the path
                    if (pathbuilder.length() > 0) {
                        pathbuilder.append("/");
                    }

                    pathbuilder.append(breadcrumb);

                    if (i == (n.size() - 1)) {
						  
						  		// if using the wildcard operator, visit the folder instead
          		         // e.g. import A.B.*;
	                     if (null != g.get(2) && g.getString(2).equals("*")) {
                        	gatherDirectoryFiles(pathbuilder.toString(), DependencyOrigin.IMPORTEDPACKAGE);
									return;
                    		}
								
                       	pathbuilder.append(".java");
                    }
                }

                String filename = pathbuilder.toString();

                addPath(filename, DependencyOrigin.IMPORT);
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
    private void gatherDirectoryFiles(String dirPath, DependencyOrigin origin) {
        File dir = null;

        try {
            dir = new File(currentParentDirectory, dirPath);
        } catch (NullPointerException e) {
            System.out.println(dirPath + " could not be found. Exists?");
            String failsafe = Object.class.getProtectionDomain().getCodeSource().getLocation().toString();
				dir = new File(failsafe);
        }

        if (dir.exists()) {
            for (String fileName : dir.list()) {
                if (!fileName.equals(currentFilePath) && fileName.endsWith(".java")) {
                    addPath(dirPath + '/' + fileName, origin);
                }
            }
        }
    }

    /*  add file path (as string) to file dependencies Map, using canonical path
        *  rather than absolute to avoid collisions                    */
       private void addPath (String path, DependencyOrigin origin) {

           try {
              path = (new File(currentParentDirectory, path)).getCanonicalPath();
           } catch (IOException e) { }

           fileDependencies.add(new FileDependency(path, origin));
       }

    /**
        *   add class names (as ClassStruct object) to both class
        *   lists (all classes and file-specific classes)
        */
       void addClass (String className, GNode n) {

           ClassStruct c = new ClassStruct(currentFilePath, currentPackage,
                            className, currentSuperClass, fileDependencies, n);
           fileClasses.add(c);
       }


        /**
           * @return all called dependent files of the current file including package and import
           */
        public ArrayList<FileDependency> getFileDependencies() {
            ArrayList<FileDependency> paths = new ArrayList<FileDependency>();

            for (FileDependency d : fileDependencies) {
                // the FileDependency .equals() method only compares by file paths, which allows us to use the .contains() method of ArrayList
                // we can then compare the dependency precedence to make sure we have dependencies correctly ordered and no duplicates
                if (!paths.contains(d) ) {
                    paths.add(d);
                } else {
                    switch (d.origin) {
                        case IMPORT:
                            if (paths.get(paths.indexOf(d)).origin.compareTo(DependencyOrigin.IMPORT) < 0) {
                                paths.remove(d);
                                paths.add(d);
                            }
                            break;
                        case IMPORTEDPACKAGE:
                            if (paths.get(paths.indexOf(d)).origin.compareTo(DependencyOrigin.IMPORTEDPACKAGE) < 0) {
                                paths.remove(d);
                                paths.add(d);
                            }
                            break;
                        case CURRENTPACKAGE:
                            if (paths.get(paths.indexOf(d)).origin.compareTo(DependencyOrigin.IMPORTEDPACKAGE) < 0) {
                                paths.remove(d);
                                paths.add(d);
                            }
                            break;
                        case CURRENTDIRECTORY:
                            break; // don't add it if we've explicitly added it already
                    }
                }
            }

            return paths;
        }

      /**
           * @return all paths to each explicitly called dependent file of the current file
           */
        public ArrayList<FileDependency> getFileImportDepPaths() {
            ArrayList<FileDependency> paths = new ArrayList<FileDependency>();

            for (FileDependency d : fileDependencies) {
                if (d.origin == DependencyOrigin.IMPORT || d.origin == DependencyOrigin.IMPORTEDPACKAGE) {
                    paths.add(d);
                }
            }

            return paths;
        }

        /**
           * @return all paths to each package-defined dependent file of the current file
           */
        public ArrayList<FileDependency> getFilePkgDepPaths() {
            ArrayList<FileDependency> paths = new ArrayList<FileDependency>();

            for (FileDependency d : fileDependencies) {
                if (d.origin == DependencyOrigin.CURRENTPACKAGE || d.origin == DependencyOrigin.CURRENTDIRECTORY) {
                    paths.add(d);
                }
            }
            return paths;
        }

        public String getPackageName() {
            return currentPackage;
        }

        /** Get dependices sorted by specific origin (i.e. get just package
                * imports or just includes) */
        public ArrayList<String> getCppIncludeDecs(DependencyOrigin origin) {

            ArrayList<String> files = new ArrayList<String>();
            for (FileDependency d : fileDependencies) {
                if (d.origin == origin) {
                    switch (origin) {
                        case IMPORT:
                            files.add("#include \"" + d.hFileName() + "\"");
                            break;
                        /*case IMPORTEDPACKAGE:
	                          files.add("using " + currentPackage.replaceAll("\\.", "::") + ";");
	                          break; */
                        case CURRENTPACKAGE:
                            files.add("namespace " + currentPackage.replaceAll("\\.", "::") + ";");
                            break;
								/* case CURRENTDIRECTORY: 
									????
									break;
								*/
                    }
                }
            }

            return files;
        }

        /** Get using declarations for each explicitly imported dependency
                  * e.g. through import A.B.Foo;            */
        public ArrayList<String> getCppUsingDeclarations() {

            ArrayList<String> files = new ArrayList<String>();
            for (FileDependency d : fileDependencies) {
                if (d.origin == DependencyOrigin.IMPORT) {
                    files.add("using " + d.qualifiedName(fileClasses) + "");
                }
            }

            return files;
        }

        /** Get using declarations for each explicitly imported package dependency  *
                *  e.g. through import A.B.*;       */
        public ArrayList<String> getCppUsingDirectives() {

            ArrayList<String> files = new ArrayList<String>();
            for (FileDependency d : fileDependencies) {
                String namespace = getNamespace(fileClasses, d.fullPath);
                if (d.origin == DependencyOrigin.IMPORTEDPACKAGE && 
                        !files.contains(namespace)) { // don't add duplicates
                    files.add(namespace);
                }
            }

            return files;
        }

        public String getFilePath() {
            return currentFilePath;
        }

      /**
            * @return classes in the current file
            */
        public ArrayList<ClassStruct> getFileClasses() {
            return fileClasses;
        }

        /**
                * @return "xtc.oop.Foo" --> ArrayList of "xtc", "oop", "Foo"
                */
        public ArrayList<String> getPackageToNamespace() {		
           return new ArrayList<String>(java.util.Arrays.asList(currentPackage.split("\\.")));
        }

        /** allows us to use Set .contains() method, compare by file path only */
        @Override
        public boolean equals (Object o) {
            if (o instanceof String)
                return this.currentFilePath.equals(o);
            else {
                DependencyFinder other = (DependencyFinder)o;
                return this.currentFilePath.equals(other.currentFilePath);
            }
        }

        public static ArrayList<String> getImports(ArrayList<ClassStruct> classes, String filename) {
            ArrayList<String> imports = new ArrayList<String>();

            // really ugly, but works....
            for (ClassStruct c : classes) {
                if (c.filePath.equals(filename)) {
                    for (FileDependency f : c.fileDependencies)
                        if (!imports.contains(f.fullPath))
                            imports.add(f.fullPath);
                }
            }

            return imports;
        }
        
        /**
               * @return package name as using directory
               * i.e. package xtc.oop.B; becomes using namespace xtc::oop::B;
               */
        public static String getNamespace(ArrayList<ClassStruct> classes, String filename) {
            for (ClassStruct c : classes) {
                if (c.filePath.equals(filename)) {
                    return c.packageName.replaceAll("\\.", "::");
                }
            }
            assert false; // should never reach here
            return "";
        }

        public static ArrayList<String> getUsingDeclarations(ArrayList<ClassStruct> classes, String filename) {
            ArrayList<String> usings = new ArrayList<String>();

            for (ClassStruct c : classes) {
                if (c.filePath.equals(filename)) {
                    usings.add( c.packageName.replaceAll("\\.", "::")
                            + "::" + c.className + ";" );
                }
            }
            return usings;
        }
}