package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

//import xtc.util.Tool;
/**
 * Translates a java array to a C array.

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
				n.getNode(1).getNode(0).set(0,"ArrayOf"+visitPrimitiveType((GNode)n.getNode(1).getNode(0)));
				visit(n);
				inDeclaration = false;
			}
			public void visitNewArrayExpression (GNode n) {
				inExpression = true;
				n.set(0,"__Array<"+n.getNode(0).get(0).toString()+">");
				n.set(1,"("+visitConcreteDimensions((GNode)n.get(1))+")");
						
				visit(n);
			}
			public String visitPrimitiveType (GNode n) {
				typeO = n.getString(0);
				System.out.println("Type set to\t\t"+typeO);
				if (typeO.equals("int32_t")) return "Int";
				if (typeO.equals("int")) return "Int";
				return typeO;
			}
			public void visitQualifiedIdentifier (GNode n) {
				if (inArrayOf) {
					typeO = n.getString(0);
					System.out.println("Type set to\t\t"+typeO);
					n.set(0,"ArrayOf"+typeO);
					inArrayOf = false;
				} else {
					String type = n.getString(0);
					n.set(0,"__Array<"+type+">");
				}
				visit(n);
			}
			public void visitDimensions (GNode n) {
				n.set(0,"");
				visit(n);
			}

			public int visitIniegerLiteral (GNode n) {
				//Gives array dimensions
				return Integer.parseInt(n.getString(0));
			}
			public int visitConcreteDimensions( GNode n) {
				return visitIniegerLiteral((GNode)n.get(0));
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}	
}