package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * Translates stuff.
 * 
 */ 
public class EWalk extends Visitor
{
	boolean VERBOSE = true;
	EWalk (final InheritanceTree treeClass, final Declaration treeMethod, GNode n) {
		if (VERBOSE) System.out.println("EWalk Called");
		eWalker(treeClass, treeMethod, n);
	}

	private void eWalker (final InheritanceTree treeClass,final Declaration treeMethod, final GNode n) {
		Node node = n;
		new Visitor() {
			boolean inCall = false, isPrint = false, isPrintln = false;
			StringBuffer fcName;
			public void visitPrimitiveType (GNode n) {
				String type = n.getString(0);
				if (type.equals("int")) {
					n.set(0,"int32_t");
					if (VERBOSE) System.out.println("changing int to int32_t");
				} if (type.equals("boolean")) {
					n.set(0,"bool");
					if (VERBOSE) System.out.println("changing boolean to bool");
				}
				visit(n);
			}
			public void visitCallExpression (GNode n) {
				inCall = true; //start looking for fully qualified name
				fcName = new StringBuffer();
				visit(n);
				fcName.append(n.getString(2));
				if(VERBOSE) System.out.println("fully qualified name: "+fcName);
   				//Object garbage = n.remove(0); //having trouble removing nodes!!!
			
				if (fcName.toString().contains("System.out.print")) {
					isPrint = true;
					if (fcName.toString().contains("System.out.println")) {
						isPrintln = true;
						n.add("<< std:endl;");
					}
					fcName = new StringBuffer(); //clear the fcName
					fcName.append("std::cout<<");
					if(VERBOSE) {
						System.out.print("Translating System.out.print");
						if (isPrintln) System.out.print("ln");
						System.out.println("");
					}
				}
				else {
					//deal with more complex stuff
				}
				n.set(2,fcName); //error in cpp printer, prints println see demo.out
				isPrint=isPrintln=false;
			}
			public void visitSelectionExpression (GNode n) {
				visit(n);			
				//if (inCall) 
				fcName.append(n.getString(1)+".");
			}
			public void visitPrimaryIdentifier (GNode n) {
				if (inCall) {
					inCall = false;
					fcName.append(n.getString(0)+".");
				} else {
					//Do something?
				}
				visit(n);
			}
			public void visitAdditiveExpression (GNode n) {
				if(isPrint) n.set(1,"<<");
				visit(n);
			}
			public void visitModifiers (GNode n) {
				//String temp = n.getString(0)

			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
}