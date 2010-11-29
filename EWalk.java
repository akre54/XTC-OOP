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
	public boolean isMethodChaining;//check if methodChaining is enacted
	public String savedReturnType;
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
				
				ArrayList<String> packages=getFcName(n);
				//call the intheritiecne tree method string, arraylist of a new packages and type name
				method.update_type(name,packages,type);
			}
			/*Checks to see if the given node has the name "Call Expression */
			public boolean isCallExpression(Node n)
			{
				if(n!=null)
				{
					System.out.println("IS CALL?"+n.getName());
					if(n.getName().equals("CallExpression"))
				   {
					return true;
				   }
				  
				}
				 return false;
			}
			/**Visit a Call expression and call the necessary inheritence checks*/
			public void visitCallExpression (GNode n) {
				if(VERBOSE) System.out.println("\nVisiting a Call Expression node:");
				inCall = true; //start looking for fully qualified name
				
				String[] methodArray=setMethodInfo(n);
				//new method name to override in the tree
				String newMethod= methodArray[1];
				savedReturnType = methodArray[0];

				//code to replace the old method name with the new method name in the tree	
				//where the current method name is current located as position 2
				n.set(2,newMethod);
				
				//if(VERBOSE) System.out.println("fully qualified name: "+fcName);
   				//Object garbage = n.remove(0); //having trouble removing nodes!!!
				
				
			}
			//does all of Call Expressions Heavy Lifting and returns the newmethod arraylist
			public String[] setMethodInfo(Node n)
			{
				Node firstChild = n.getNode(0);

				String primaryIdentifier;
				if(!isCallExpression(firstChild))
				{
					isMethodChaining=true;
					primaryIdentifier=savedReturnType;
				}
				else
				{
					System.out.println("DEBUG" +n.getName());
					
					primaryIdentifier=n.getString(0);
				}
				
				//visit the arguments node
				Node arguments=n.getNode(3);
				//create a new argumentTypes arraylist call the getarguments method on the arguments node
				ArrayList<String> argumentTypes =getArgumentTypes(arguments);
				if(VERBOSE) System.out.println("New Array List Created...\n");
				
				Node fcName=n.getNode(2);//where will the fcnameList be?
				//gets the FCNameList Node and visits it using the getFCName method
				ArrayList<String> fcNamelist =getFcName(fcName);
				
				//get the method name
				String methodName = n.getString(2);
				//get an array of the method arrtibutes in the inheritance tree (return type and new method name)
				String[] methodArray = getMethodInfo(primaryIdentifier,fcNameList, methodName,argumentTypes);
				return methodArray;
			}
						/**Helper method returns the arguments in an array list*/
			public ArrayList<String> getArgumentTypes(Node n)
			{
				//get the type for every argument in the argument Node
				ArrayList<String> argumentList = new ArrayList<String>();
				for(int i=0;i<n.size();i++)	{
					argumentList.add(getType(n.getNode(i)));
				} //what about a method call here?
				
				/*if the argumentlist is empty return a arraylist of the string 0 */
				if (argumentList.isEmpty()) {
					 argumentList.add("0");
				}
				
				return argumentList;
				
			}
			/**Node that gets the FC name before a method and returns an array list of the fcNamelist*/
			public ArrayList<String> getFcName(Node n){
				
				fcNameList = new ArrayList<String>();
				fcName = new StringBuffer();
				//visit(n);
				fcName.append(n.getString(2));
				fcNameList.add(n.getString(2)); 
				int size = fcNameList.size();
				
				String methodName = fcNameList.get(size-1);
				//String className = fcNameList.get(size-2);//SOURCE OF ARRAY OUT OF BOUNDS
				fcNameList.remove(size-1);
				//fcNameList.remove(size-2);//SOURCE OF ARRAY OUT OF BOUNDS
				if (fcName.toString().contains("System.out.print")) {
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
				return fcNameList;
				
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
			/**helpers method that uses the inheritence tree search for method and returns the string array */
			public String[] getMethodInfo(String Identifier,ArrayList<String> nameList,String name, ArrayList<String> argumentList)
			{
				//method .search for type with packages if you dont send a package its the package you're in
				//what do you send if its your current package
				ArrayList<String> qualities=method.search_for_type(Identifier);//send the primary Identifier
				//get the last value which is the Classname
				String className = qualities.remove(qualities.size());
				//questions WHAT DO I DO WITH the rest? Is that needed?
				
				
				
				//get the inheritance name of 
				InheritanceTree b =tree.root.search(qualities,className); //search takes the current method name?
				//when there are no arguments sends a NullPointerException
				
				//returns an array of string 0= return type and 1=new method string
				return b.search_for_method(isInstance,method,argumentList,name);
			}
			/**Helper method that checks for the types in the subtree and returns them 
			 is currently used when get the types for values in an argument*/
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
				else if (n.getName().equals("CallExpression"))
				{
					//get an array of the method arrtibutes in the inheritance tree (return type and new method name)
					String[] methodArray;					//return the return type gotten from getMethodInfo (located as the first item in the given array)
					methodArray=setMethodInfo(n);
					return methodArray[0];
				}
				else{ //return the name of the primaryIdentifier
					//call the method in the inheritence tree to get the type of the primaryIden
					
					ArrayList<String> packageNType= method.search_for_type(n.getString(0));
					String type = packageNType.remove(packageNType.size());
					return type;
				}
				/**can put support for handling methods inside an argument here (use search for methods
				 to find out what the method will return? Are we storing the return type of a method in inheritence tree?*/
			}
			
			public void visitSuperExpression(GNode n)
			{
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
	}
}