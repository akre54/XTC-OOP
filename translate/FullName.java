package xtc.oop.translate;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.oop.translate.*;
import xtc.oop.InheritanceTree;
import xtc.oop.Declaration;


/**
 * This class handels fully qualified names.
 *
 * @author P.Hammer
 * @version v1.0
 */
public class FullName {
	private StringBuffer buffer;
	public FullName (String input) {
		buffer = new StringBuffer(input);
	}	
	public void append (String input) {
		buffer.append(input);
	}
	public void appendToFront (String input) {
		buffer.insert(0, input);
	}
	public String returnName() {
		return buffer.toString();
	}
}

