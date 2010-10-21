package xtc.oop;

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
	int dimension;
	String size;
	boolean oneLine = false;
	boolean isExpression = false;
	boolean isArray = false;

	/** 
	 * Takes a FieldDeclaration node of part of an array
	 * declaration and translates it.
	 * 
	 * @param GNode arguments
	 * @return void
	 */ 
	public ArrayMaker (GNode arguments) {
		toPrint=new StringBuffer();
		arrayStringMaker (arguments);
	}

	/** 
	 * Takes a node of some array type and
	 * translates it.
	 * 
	 * @param arguments gnode of containing an array
	 * @param arg boolean isExpression Set equal to true if node is at expression statement.
	 */ 
	public ArrayMaker (GNode arguments, boolean arg) {
		toPrint = new StringBuffer();
		isExpression = arg;
		if (isExpression) {
			arrayExpressionStringMaker (arguments);
		} else {
			arrayStringMaker (arguments);
		}
	}

	private void arrayStringMaker (GNode n) {
		Node node = n;
		toPrint.append("ArrayOf");
		new Visitor() {
			public void visitType (GNode n) {
				//Leads to PrimitiveType
				visit(n);
			}
			public void visitQualifiedIdentifier (GNode n) {
				visit(n);
			}
			public void visitPrimitiveType (GNode n) {
				//Type of the array
				if (oneLine) {
					toPrint.append(" = new __Array<");
					String temp = n.getString(0);
					if (temp.equals("int")) {
						toPrint.append("int32_t>");
					} else if (temp.equals("class")) {
						toPrint.append("Class>");
					} else if (temp.equals("object")) {
						toPrint.append("Object>");
					}
				} else {
					toPrint.append(n.getString(0));
				}
				visit(n);
			}
			public void visitDimensions (GNode n) {
				//Number of dimensions
				//dimension = n.lastIndexOf(n);
				visit(n);
			}
			public void visitDeclarators (GNode n) {
				//Leads to Declarator
				visit(n);
			}
			public void visitDeclarator (GNode n) {
				//Has the name of the array
				toPrint.append(n.getString(0));
				visit(n);
			}
			public void visitExpressionStatement (GNode n) {
				visit(n);
			}
			public void visitExpression (GNode n) {
				visit(n);
			}
			public void visitNewArrayExpression (GNode n) {
				oneLine = true;
				visit(n);
			}
			public void visitConcreteDimensions (GNode n) {
				//leads to IntergerLiteral
				visit(n);
			}
			public void visitIniegerLiteral (GNode n) {
				//Gives array dimensions
				toPrint.append("("+n.getString(0)+")");
				visit(n);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
		toPrint.append(";");
	}

	private void arrayExpressionStringMaker (GNode n) {
		Node node = n;
		new Visitor() {
			public void visitType (GNode n) {
				//Leads to PrimitiveType
				visit(n);
			}
			public void visitQualifiedIdentifier (GNode n) {
				visit(n);
			}
			public void visitPrimitiveType (GNode n) {
				//Type of the array
				toPrint.append(" = new __Array<");
				String temp = n.getString(0);
				if (temp.equals("int")) {
					toPrint.append("int32_t>");
				} else if (temp.equals("class")) {
					toPrint.append("Class>");
				} else if (temp.equals("object")) {
					toPrint.append("Object>");
				}
				visit(n);
			}
			public void visitDimensions (GNode n) {
				//Number of dimensions
				//dimension = n.lastIndexOf(n);
				visit(n);
			}
			public void visitPrimaryIdentifier (GNode n) {
				//Has the name of the arry being set
				toPrint.append(n.getString(0));
				visit(n);
			}
			public void visitExpressionStatement (GNode n) {
				visit(n);
			}
			public void visitExpression (GNode n) {
				visit(n);
				// visits primary identifier then the epression type
				toPrint.append(n.getString(1));
			}
			public void visitNewArrayExpression (GNode n) {
				isArray = true;
				visit(n);
			}
			public void visitConcreteDimensions (GNode n) {
				//leads to IntergerLiteral
				visit(n);
			}
			public void visitIniegerLiteral (GNode n) {
				//Gives array dimensions
				toPrint.append("("+n.getString(0)+")");
				visit(n);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
		toPrint.append(";");
	}
	/** 
	 * Returns the complete translation of a Java array
	 * 
	 * @return StringBuffer
	 */ 
	public StringBuffer getStringBuffer () {
		return toPrint;
	}
	/** 
	 * Returns whether or not an expression statement was
	 * working on an array
	 * @return boolean
	 */ 
	public boolean getIsArray () {
		return isArray;
	}

}