/* info about class structures for DependencyFinder,
 * Translator, and InheritanceBuilder
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

import java.io.File;
import xtc.tree.GNode;

public class ClassStruct {

    String fullPath;
    String packageName;
    String name;
    String superClass;
    GNode n; // of Class

    public ClassStruct(String path, String packName, String className, String superClassName, GNode gn) {
        fullPath = path;
        packageName = packName;
        name = className;
        superClass = superClassName;
        n = gn;
    }

    /*      comparison by name and package      */
    public boolean equals (ClassStruct c) {
        return (this.name.equals(c.name)) && (this.packageName.equals(c.packageName)) ;
    }
    
    public boolean fromSameFile (ClassStruct c) {
        return (this.fullPath.equals(c.fullPath));
    }


    /* @return just name of file (ie ImportFile from ImportFile.java,
        * used for cpp import headers */
    public String cppName () {
        String jName = (new File(fullPath)).getName();
        return jName.replace(".java","");
    }
}