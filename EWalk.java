package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import java.util.ArrayList;
import xtc.oop.ArrayMaker;

/**
 * Translates stuff.
 * 
 */ 
public class EWalk
{
	boolean VERBOSE = true;
	public InheritanceTree tree;
	public Declaration method;
	public boolean isInstance; //check for callExpression (needed for chaining)
	EWalk (final InheritanceTree treeClass, final Declaration treeMethod, GNode n) {
		if (VERBOSE) System.out.println("EWalk Called");
		tree=treeClass;
		method=treeMethod;
		eWalker(n);
	}

	private void eWalker (final GNode n) {
		Node node = n;
		new Visitor() {
			boolean inCall = false,
				isPrint = false,
				isPrintln = false,
				isString = false,
				isArray = false,
				isPrintString = false;
			StringBuffer fcName;
			ArrayList<String> fcNameList;
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
			public void visitExpression(GNode n)
			{
				Node primary = n.getNode(0);
				String instanceName=primary.getString(0);
				Node castex = n.getNode(2);//get the third node
				if(castex.getName().equals("CastExpression"))//see if its a castexpression
					{
						visitCastExpression(castex,instanceName);
					}
			}
			/**NOTE: This is not a VISIT method but my own created method*/
			public void visitCastExpression(Node n, String name) {
				//get and variable and the new types
				Node qual = n.getNode(0);
				String type =qual.getString(0);
				
				//call the intheritiecne tree method
				method.update_type(name,type);
			}
			/**Visit a Call expression and call the necessary inheritence checks*/
			public void visitCallExpression (GNode n) {
				if(VERBOSE) System.out.println("\nVisiting a Call Expression node:");
				inCall = true; //start looking for fully qualified name
				fcNameList = new ArrayList<String>();
				fcName = new StringBuffer();
				visit(n);
				fcName.append("__vptr->"+n.getString(2));
				fcNameList.add(n.getString(2));
				Node arguments=n.getNode(3);							 
				//get the type for every argument in the argument Node
				ArrayList<String> argumentList = new ArrayList<String>();
				if(VERBOSE) System.out.print("New Array List Created...\t\t");
				for(int i=0;i<arguments.size();i++)	{
					argumentList.add(getType(arguments.getNode(i)));
				} //what about a method call here?
				if(VERBOSE) System.out.println("DONE");
				int size = fcNameList.size();
				String methodName = fcNameList.get(size-1);
				String className = fcNameList.get(size-2);
				fcNameList.remove(size-1);
				fcNameList.remove(size-2);
				//String newMethod=tree.search_for_method(isInstance,method,argumentList,methodName);
				if(VERBOSE) System.out.println("Fully qualified name:\t\t\t"+fcName);
				//n = n.ensureVariable(n); //does not work, alters my referance to the origonal tree.
				if (fcName.toString().contains("System->out->__vptr->print")) {
					isPrint = true;
					if (fcName.toString().contains("System->out->__vptr->println")) {
						isPrintln = true;
					}
					fcName = new StringBuffer(); //clear the fcName
					fcName.append("std::cout<<");
					if(VERBOSE) {
						System.out.print("Translating special case:\t\tSystem.out.print");
						if (isPrintln) System.out.print("ln");
						System.out.println("");
					}
				}
				else {
					//other
				}
				if(VERBOSE) System.out.println("Size of n is\t\t\t\t"+n.size());
				if(VERBOSE) System.out.println("Setting n[0] to:\t\t\t"+fcName);
				if(isPrintln) {
					n.set(0,null);
					n.set(2,fcName.toString());
					n.set(1,"<<std::endl");
				}
				else {
					n.set(0,null);
					n.set(2,fcName.toString());
				}
				visit(n.getNode(3));
				visit(n.getNode(3));
				isPrint=isPrintln=false;
				if(VERBOSE) System.out.println("isPrint reset to:\t\t\t"+isPrint);
			}
			public void visitSelectionExpression (GNode n) {
				visit(n);	
				if (inCall);
				fcName.append(n.getString(1)+"->");
				fcNameList.add(n.getString(1));
				
			}
			public void visitPrimaryIdentifier (GNode n) {
				if (inCall) {
					inCall = false;
					fcName.append(n.getString(0)+"->");
					fcNameList.add(n.getString(0));
				} else {
					//Do something?
				}
				visit(n);
			}
			public void visitAdditiveExpression (GNode n) {
				if(isPrint) {
					if(isPrintString) n.set(1,"<<");
				}
				visit(n);
			}
			public void visitModifier (GNode n) {
				String temp = n.getString(0);
				if (temp.equals("final")) n.set(0,"const");
			}
			public void visitFieldDeclaration (GNode n) {
				visit(n);
				if (isString) {
					visit(n);
					isString = false;
				}
				if (isArray) {
					if(VERBOSE) System.out.println("Entering array");
					ArrayMaker goArray = new ArrayMaker (n);
					isArray = false;
				}
			}
			public void visitDimensions (GNode n) {
				if(!isArray) {
					if(VERBOSE) System.out.println("Setting array flag to true...");
					isArray = true;
				}
				visit(n);
			}
			public void visitNewArrayExpression (GNode n) {
				if(!isArray) {
					if(VERBOSE) System.out.println("Setting array flag to true...");
					isArray = true;
					visit(n);
				}
			}
			public void visitQualifiedIdentifier (GNode n) {
				if (!isString) {
					String temp = n.getString(0);
					if (temp.equals("String")) isString = true;
				}
				visit(n);
			}
			public void visitStringLiteral (GNode n) {
				if (isString) {
					String temp = n.getString(0);
					n.set(0,"__rt::stringify("+temp+")");
					isString = false; //make sure it only happens once
				}
				if (isPrint) isPrintString = true;
				visit(n);
			}
			public String getType(Node n) {
				//check for primitative types
				if (n.getName().equals("IntegerLiteral")) {
					return "int";
				}
				else if(n.getName().equals("StringLiteral")){
					return "String";
				}
				else if(n.getName().equals("BooleanLiteral"))
				{
					return "boolean";
				}
				else if(n.getName().equals("NullLiteral"))
				{
					return "null";
				}
				else if(n.getName().equals("FloatingPointLiteral"))
				{
					return "float";
				}
				else if(n.getName().equals("CharLiteral"))
				{
					return "char";
				}
				else{ //return the name of the primaryIdentifier
					//call the method in the inheritence tree to get the type of the primaryIden
					//	return method.search_for_type(n.getString(0));
					return "null";
				}
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
}