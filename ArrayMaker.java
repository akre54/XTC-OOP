package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;
/**
 * Translates a java array to a C array.
 * 
 */ 
public class ArrayMaker {
	private StringBuffer toPrint;
	public void ArrayMaker (GNode arguments) {
		arrayStringMaker (arguments);
	}

	private void arrayStringMaker (GNode n) {
		Node node = n;
		toPrint.append("cout << ");
		new Visitor() {
			boolean isCallExpression = false;
			boolean hasStrings = false;
			public void visitArguments(GNode n) {
				visit(n);
				toPrint.append(";");
			}
			public void visitStringLiteral (GNode n) {
				toPrint.append(n.getString(0));
				visit(n);
			}
			public void visitAdditiveExpression (GNode n) {
				if (n.getString(1).equals("+")) {
					visit(n.getNode(0));
					toPrint.append("<< ");
					visit(n.getNode(2));
				} else { // subtraction
					visit(n.getNode(0));
					toPrint.append("- ");
					visit(n.getNode(2));
				}
			}
			public void visitMultiplicativeExpression (GNode n) {
				if (n.getString(1).equals("*")) { //multiplication
					visit(n.getNode(0));
					toPrint.append("* ");
					visit(n.getNode(2));
				} else { // division
					visit(n.getNode(0));
					toPrint.append("/ ");
					visit(n.getNode(2));
				}
			}
			public void visitCallExpression (GNode n) {
				isCallExpression = true;
				visit(n);
				isCallExpression = false;
			}
			
			public void visitPrimaryIdentifier (GNode n) {
				if (isCallExpression) {
					toPrint.append(n.getString(0)+".toString()");
				} else {
					toPrint.append(n.getString(0));
				}
				visit(n);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
/** 
 * Returns the complete translation of a Java array
 * 
 * @return StringBuffer
 */ 
	public StringBuffer getStringBuffer () {
		return toPrint;
	}
}