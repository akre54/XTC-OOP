package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;
/** 
 * Translates conditional statments like if-else.
 *  
 */ 
class ConditionalMaker {
	private boolean doubleBlock = false;
	String opporator;
	StringBuffer toPrint;
	public ConditionalMaker (GNode n) {
		toPrint=new StringBuffer();
		conditionalStringMaker (n);
	}
	
	private void conditionalStringMaker (GNode n) {
		Node node = n;
		toPrint.append("if (");
		new Visitor() {
			public void visitEqualityExpression (GNode n) {
				cppEqualityExpression equal = new cppEqualityExpression(n);
				toPrint.append(equal.getString());
			}
			public void visitRelationalExpression (GNode n) {
				visit(n);
			}
			public void visitConditionalStatement (GNode n) {
				doubleBlock = false;
				toPrint.append("\nelse if (");	
				visit(n);
				toPrint.append(")");
			}
			public void visitBlock (GNode n) {
				if (doubleBlock) {
					toPrint.append("\nelse {");	
				} else {
					doubleBlock = true;
				}
				toPrint.append("{");
				cppBlock bk = new cppBlock(n);
				toPrint.append(bk.getString());
				toPrint.append("}");
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