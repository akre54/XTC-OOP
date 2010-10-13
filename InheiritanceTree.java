/* The file we call Translator on (foo.java) is the root
 * and its children are its dependancies, each have fields for path, and
 * whether they've been translated by the Translator
 *
 * v1 by Adam Krebs
 */

package xtc.oop;

import xtc.lang.javacc.syntaxtree.ImportDeclaration;
import java.util.ArrayList;

public class InheiritanceTree {
	
	private GNode root;
	private ArrayList<InheiritanceTree> children;

	public String path;
	public bool visited; // specifies if this dependancy already translated
	
	public InheiritanceTree(GNode n) {
		root = n;
		children = new ArrayList<InheiritanceTree>;


		// buildTree(n.getJFile()); 	// constructor should eventually build the whole tree

		path = n.getJFile.getParent(); // JFile needs a File field, or else the File field has to come from somewhere else
		translated = false;
	}

	public void addChild(InheiritanceTree child) {
		children.add(child);
	}

	public ArrayList<InheiritanceTree> getChildren() {
		return children;
	}


	/* pseudocode for now... needs a JavaFile class which has the ImportDeclarations (might extend JavaAST have a File field)
	public static void buildTree(JavaFile jfile) {

		for (ImportDeclaration dependency : jfile) {
			t.addChild(new Inheiritancetree(dependancy));
		}

	}

	*/

}


/* possibility of what a JavaFile class would look
 * like, if we chose to extend File
*/
class JavaFile extends File {

	public JavaFile(String pathname) {
		super(pathname); // calls File constructor
	}

	public getDependencies() {
		
		return (new Node(this)).instanceOf(ImportDeclaration);
	}

}
