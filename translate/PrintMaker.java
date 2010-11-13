package xtc.oop.translate;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;
/**
 * Correctly translates the java System.out.print and prinln to
 * a string.
 * 
 */ 
public class PrintMaker {
	private boolean isLN = false;
	private StringBuffer toPrint;
	public PrintMaker (GNode arguments, boolean isLine) {
		isLN = isLine;
		toPrint= new StringBuffer();
		coutStringMaker (arguments);
	}
	
	private void coutStringMaker (GNode n) {
		Node node = n;
		toPrint.append("std::cout << ");
		new Visitor() {
			boolean isCallExpression = false;
			public void visitArguments(GNode n) {
				visit(n);
				if (isLN) {
					toPrint.append("<< std::endl;");
				} else {
					toPrint.append(";");
				}
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
 * Returns the complete translation of System.out.print
 * and println.
 * 
 * @return StringBuffer
 */ 
	public StringBuffer getString() {
		return toPrint;
	}
}