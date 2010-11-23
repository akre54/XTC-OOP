/*
 *  info about class structures for DependencyTree.java
 */

package xtc.oop;

import xtc.tree.GNode;

public class ClassStruct {

    String fullPath;
    String packageName;
    String name;
    String extensions;
    GNode n; // of Class

    public ClassStruct(String path, String packName, String className, String classExtensions, GNode gn) {
        fullPath = path;
        packageName = packName;
        name = className;
        extensions = classExtensions;
        n = gn;
    }

}