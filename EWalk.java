package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

//import xtc.oop.*;

/**
 * Translates stuff.
 * 
 */ 
public class EWalk extends Visitor
{
	EWalk (InheritanceTree treeClass, Declaration treeMethod, GNode n) {
		eWalker(n);
	}


	private void eWalker (GNode n) {
		Node node = n;
		new Visitor() {
			public void visitPrimitiveType (GNode n) {
				String type = n.getString(0);
				System.out.println("EWalk Called");
				if (type.equals("int")) {
						n.set(0,"int32_t");
						System.out.println("changing int to int32_t");
				}
				visit(n);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
}