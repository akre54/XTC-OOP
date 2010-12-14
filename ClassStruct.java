/* info about class structures for DependencyFinder,
 * Translator, and InheritanceBuilder
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

import java.io.File;
import java.util.ArrayList;
import xtc.tree.Node;
import xtc.tree.GNode;

public class ClassStruct {

    String filePath;
    String packageName;
    String className;
    String superClass;
    ArrayList<FileDependency> fileDependencies;
    GNode classNode;
    Node fileNode;

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

    /**  @return "xtc.oop.Foo" --> ArrayList of "xtc", "oop", "Foo" */
    public ArrayList<String> getPackage() {
        return new ArrayList<String>(java.util.Arrays.asList(packageName.split("\\.")));
    }
}

/* Origin of a dependency, used for tracking call heirarchy */
enum DependencyOrigin {
    ROOTFILE, IMPORT, IMPORTEDPACKAGE, CURRENTPACKAGE, CURRENTDIRECTORY
}

class FileDependency {
    
    public String fullPath;
    public DependencyOrigin origin;

    public FileDependency(String fullPath, DependencyOrigin origin) {
        this.fullPath = fullPath;
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileDependency) {
            FileDependency other = (FileDependency)o;
            return this.fullPath.equals((other.fullPath));
        }
        return false;
    }


    @Override
    public int hashCode() {
        return fullPath.hashCode();
    }

    public String javaFileName() {
        return (new File(fullPath)).getName();
    }

    /* @return just name of file (ie ImportFile from ImportFile.java,
        * used for cpp import headers */
    public String cppFileName(ArrayList<ClassStruct> c) {
        String directory = DependencyFinder.getNamespaceDirectory(c, fullPath);
        String basename = javaFileName().replace(".java",".cpp");
        return basename + "/" + directory;
    }
    public String hFileName(ArrayList<ClassStruct> c) {
        String directory = DependencyFinder.getNamespaceDirectory(c, fullPath);
        String basename = javaFileName().replace(".java","_dataLayout.h");
        return basename + "/" + directory;
    }
    public String qualifiedName(ArrayList<ClassStruct> c) {
        String namespace = DependencyFinder.getNamespace(c, fullPath);
        String basename = javaFileName().replace(".java","");
        return namespace + "::" + basename;
    }
}

