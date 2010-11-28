/*
 *  info about class structures for DependencyTree.java
 */

package xtc.oop;

import xtc.tree.GNode;
import java.util.ArrayList;

public class ClassStruct {

    String fullPath;
    String packageName;
    String name;
    ArrayList<String> extensions;
    GNode n; // of Class

    public ClassStruct(String path, String packName, String className, GNode gn) {
        fullPath = path;
        packageName = packName;
        name = className;
        extensions = new ArrayList<String>();
        n = gn;
    }

    /*      comparison by name and package      */
    public boolean equals (ClassStruct c) {
        return (this.name == c.name) && (this.packageName == c.packageName) ;
    }

    public void addExtension(String ex) {
        this.extensions.add(ex);
    }

}