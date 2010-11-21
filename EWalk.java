package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import java.util.ArrayList;
//import xtc.oop.*;

/**
 * Translates stuff.
 * 
 */ 
public class EWalk extends Visitor
{
	public InheritanceTree tree;
	public Declaration method;
	public boolean isInstance; //check for callExpression (needed for chaining)
	EWalk (InheritanceTree treeClass, Declaration treeMethod, GNode n) {
		//set the given methods 
		tree=treeClass;
		method=treeMethod;
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
			//////Paige's Code///////////////
			/**Checks for an expression Where a variable's type will abtually be changed*/
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
			 public void visitCastExpression(Node n, String name)
			 {
				 //get and variable and the new types
				 Node qual = n.getNode(0);
				 String type =qual.getString(0);
			 
				 //call the intheritiecne tree method
				 method.update_type(name,type);
			 }
			 /**Visit a Call expression and call the necessary inheritence checks*/
			public void visitCallExpression(GNode n)
			{
				//get the instance name, or changed expression or null
				Node primary =n.getNode(0);
				String instanceName;
				if (primary!=null) { //check to see what node is there (if any)
					if(primary.getName().equals("PrimaryIdentifier"))
					{
						isInstance=true;
						instanceName=primary.getString(0);
					}
					else if (primary.getName().equals("CallExpression")) {
						
						visit(primary);
					}
				}
				
				//get the method name
				String methodName = n.getString(2);
				Node arguments=n.getNode(3);
							 
				//get the type for every argument in the argument Node
							 //**This might change to a linked list?
				ArrayList<String> argumentList = new ArrayList<String>();
				System.out.println("New Array List Created...\n");
				for(int i=0;i<arguments.size();i++)
				{
						argumentList.add(getType(arguments.getNode(i)));
				}
				//send a check to Inheritence builder to check for the proper method
			    String newMethod=tree.search_for_method(isInstance,method,argumentList,methodName);
				//reset is instance
				isInstance=false;
			   //replace the methodName with the proper name gotten from the Search_For_Method
				//remove the current method name
				n.remove(2);
				//place the new method name in its place
				n.set(2,newMethod);
				visit(n);
			}
			/**Helper method that returns type strings for method declarations*/
			public String getType(Node n)
			{
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
					return method.search_for_type(n.getString(0));
				}
			}			
			///////End of Paige's Code///////
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
}