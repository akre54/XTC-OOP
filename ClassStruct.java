/* info about class structures for DependencyFinder,
 * Translator, and InheritanceBuilder
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;

public class ClassStruct {

    String filePath, packageName, className, superClass;
    ArrayList<FileDependency> fileDependencies;
    GNode classNode;
    Node fileNode;
    String rootPackage, rootFile; // updated in -translate, after -finddependencies is complete

    public ClassStruct(String filePath, String packageName, String className,
            String superClass, ArrayList<FileDependency> fileDependencies, GNode classNode, Node fileNode) {
        this.filePath = filePath;
        this.packageName = packageName;
        this.className = className;
        this.superClass = superClass;
        this.fileDependencies = fileDependencies;
        this.classNode = classNode;
        this.fileNode = fileNode;
    }

    /**  comparison by name and package     */
    @Override
    public boolean equals (Object o) {
        if (o instanceof ClassStruct) {
            ClassStruct c = (ClassStruct)o;
            return (this.className.equals(c.className)) && (this.packageName.equals(c.packageName)) ;
        } else
            throw new RuntimeException("bad cast in ClassStruct equals");
    }

    @Override
    public int hashCode() {
        return (className + packageName).hashCode();
    }
    
    public boolean fromSameFile (ClassStruct c) {
        return (this.filePath.equals(c.filePath));
    }

    /**  @return "xtc.oop.Foo" --> ArrayList of "xtc", "oop", "Foo",
	  *	or empty list if blank package name			 */
    public ArrayList<String> getPackage() {
	 	if (!packageName.equals("")) // if not blank
        return new ArrayList<String>(java.util.Arrays.asList(packageName.split("\\.")));
		else
			return new ArrayList<String>();
    }
}

/** Origin of a dependency, used for tracking call heirarchy */
enum DependencyOrigin {
    ROOTFILE, IMPORT, IMPORTEDPACKAGE, CURRENTPACKAGE, CURRENTDIRECTORY
}

