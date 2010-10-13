/* The file we call Translator on (foo.java) is the root
 * and its children are its dependancies, each have fields for path, and
 * whether they've been translated by the Translator
 *
 * v1 by Adam Krebs
 */

package xtc.oop;

import java.util.ArrayList;

public class InheiritanceTree {
	
	private GNode root;
	private ArrayList<InheiritanceTree> children;

	public String path;
	public bool visited; // specifies if this dependancy already translated
	
	public InheiritanceTree(GNode n) {
		root = n;
		children = new ArrayList<InheiritanceTree>;


		// buildTree(n); 	// constructor should eventually build the whole tree

		path = n.getFile.getParent(); // GNode needs a File field, or else the File field has to come from somewhere else
		translated = false;
	}

	public void addChild(InheiritanceTree child) {
		children.add(child);
	}

	public ArrayList<InheiritanceTree> getChildren() {
		return children;
	}


	/* pseudocode for now... will hook up all later when we decide on a final format	
	public static void buildTree(InheiritanceTree t) {

		jfile = getFileAndDependencies(t);

		for (dependencies : jfile) {
			t.addChild(new Inheiritancetree(dependancy));
		}

	}

	*/

}
