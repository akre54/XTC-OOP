/* info about class structures for DependencyFinder
 *
 * by The Group
 */

package xtc.oop;

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
        return (this.name == c.name) && (this.packageName == c.packageName) ;
    }
}