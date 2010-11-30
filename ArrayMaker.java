package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

//import xtc.util.Tool;
/**
 * Translates a java array to a C array.
 * 
 */ 
public class ArrayMaker {
	/** 
	 * Takes a FieldDeclaration node of part of an array
	 * declaration and translates it.
	 * 
	 * @param GNode arguments
	 * @return void
	 */ 
	public ArrayMaker (GNode arguments) {
		arrayStringMaker (arguments);
	}

	private void arrayStringMaker (GNode n) {
		Node node = n;
		new Visitor() {
			boolean	inDeclaration = false,
				inExpression = false,
				inArrayOf = true,
				inDimenstions = false;
			int size;
			String typeO, type2;
			public void visitFieldDeclaration (GNode n) {
				inDeclaration = true;
				visit(n);
				inDeclaration = false;
			}
			public void visitNewArrayExpression (GNode n) {
				inExpression = true;
				visit(n);
			}
			public void visitPrimitiveType (GNode n) {
				if (inArrayOf) {
					typeO = n.getString(0);
					System.out.println("Type set to\t\t"+typeO);
					if (typeO.equals("int32_t")) type2="Int";
					else type2 = typeO;
					n.set(0,"ArrayOf"+type2);
					inArrayOf = false;
				}
				visit(n);
			}
			public void visitQualifiedIdentifier (GNode n) {
				if (inArrayOf) {
					typeO = n.getString(0);
					System.out.println("Type set to\t\t"+typeO);
					n.set(0,"ArrayOf"+typeO);
					inArrayOf = false;
				}
				visit(n);
			}
			public void visitDimensions (GNode n) {
				n.set(0,"[]");
				visit(n);
			}
			public void visitIniegerLiteral (GNode n) {
				//Gives array dimensions
				size = Integer.parseInt(n.getString(0));
				visit(n);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}	
}