/* info about class structures for DependencyFinder,
 * Translator, and InheritanceBuilder
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

import java.io.File;
import java.util.ArrayList;
import xtc.tree.GNode;

public class ClassStruct {

    String filePath;
    String packageName;
    String className;
    String superClass;
    ArrayList<FileDependency> fileDependencies;
    GNode n; // of Class

    public ClassStruct(String filePath, String packageName, String className,
            String superClass, ArrayList<FileDependency> fileDependencies, GNode n) {
        this.filePath = filePath;
        this.packageName = packageName;
        this.className = className;
        this.superClass = superClass;
        this.fileDependencies = fileDependencies;
        this.n = n;
    }

    /*      comparison by name and package      */
    public boolean equals (ClassStruct c) {
        return (this.className.equals(c.className)) && (this.packageName.equals(c.packageName)) ;
    }
    
    public boolean fromSameFile (ClassStruct c) {
        return (this.filePath.equals(c.filePath));
    }
}

/* Origin of a dependency, used for tracking call heirarchy */
enum DependencyOrigin {
    IMPORT, PACKAGE, CURRENTDIRECTORY
}

class FileDependency {
    
    public String fullPath;
    public DependencyOrigin origin;

    public FileDependency(String fullPath, DependencyOrigin origin) {
        this.fullPath = fullPath;
        this.origin = origin;
    }

    public boolean equals(FileDependency other) {
        return this.fullPath.equals((other.fullPath));
    }

    public String javaFileName() {
        return (new File(fullPath)).getName();
    }

    /* @return just name of file (ie ImportFile from ImportFile.java,
        * used for cpp import headers */
    public String cppFileName() {
        return javaFileName().replace(".java",".cpp");
    }

}

