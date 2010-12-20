
/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
 * Created by Paige Ponzeka
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package xtc.oop;

import xtc.lang.JavaFiveParser;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

//code needs for recording logs
import java.util.logging.*;
import java.io.*;

import xtc.util.Tool;
/**A "Stupid" printer, it only prints the basic cases and checks for requried grammar syntax, it assumes
 that all the code given to it has already been processed. This will print the required braces and brackets but 
 not much further*/
public class CppPrinter extends Visitor
{
	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("CppPrinterLog");
	private StringBuilder printer; //a StringBuilder that stores the code translated by the print
	private boolean DEBUG = false;
	/*A global boolean that keeps track of the current modifier status*/
	private boolean isPrivate; 	
	private boolean isArguments; //checks to see if we're currently in an argument subtree
	private boolean isReturn; //checks to see if there is a StringLiteral inside a return statement
	private boolean isInstance;
	private boolean isPrint;
	private String primIdentifier;
	private String declared; //string that stores declared variable for NewclassExpressions
	/*Default constructor that should be used by all classes, intializes sringbuilder, and calls visit on given bode*/
	public CppPrinter(GNode n)
	{
		isArguments=false;
		declared = "";
		//System.out.println(n.toString());
		if(n!=null){
		if(DEBUG)System.out.println(n.toString());
		}
//		setupLog("CppPrinter");
		printer = new StringBuilder(); //intialize Stringbuilder
		isInstance=false;
		primIdentifier ="";
		isPrivate =false; //sets false by default since structs are public by default
		visit(n); //visit the given node (starts all the visiting)
	}

	/**consturctor used that sets the debugging option*/
	public CppPrinter(GNode n, boolean debug)
	{
//		setupLog("CppPrinter");
		DEBUG= debug;
		DEBUG =false;
//		logger.fine("Writing to the Log");
		printer = new StringBuilder(); //intialize Stringbuilder
		isPrivate =false; //sets false by default since structs are public by default
		isInstance=false;
		primIdentifier="";
		isArguments=false;

		visit(n); //visit the given node (starts all the visiting)
	}
	/**Only prints out a special case if isTReturn Flag is set to true otherwise does normal behavor*/
	public void visitStringLiteral(GNode n)
	{
		if(isReturn) //check to see if we are current in a return subtree
			print("new __String("+n.getString(0)+")"); //print out the proper code
		else//otherwise just visit the tree as normal
			visit(n);
	}
	
	/**visit cast expression and print the c++ version of the java AST values*/
	public void visitCastExpression(GNode n)
	{
		print("(");
		//visit the cast type
		visitChildren(n, 0,1,"");
		print(")");
		//visit the next batch of code
		visitChildren(n, 1,n.size(),"");
	}
	//prints brackets
	public void visitSubscriptExpression(GNode n)
	{
		//visit child 
		visitChildren(n, 0,1,"");
		//print bracket
		print("->__data[");
		
		//visit 2nd child
		visitChildren(n,1,n.size(),"");
		//close bracket
		print("]");
	}
	public void visitLogicalNegationExpression(GNode n)
	{
		print("!");
	}
	/**visit cast expression on primitive types and print the c++ equivalent */
	public void visitBasicCastExpression(GNode n)
	{
		print("(");
		//visit the cast type
		visitChildren(n, 0,1,"");
		print(")");
		//visit the next batch of code
		visitChildren(n, 1,n.size(),"");
	}
	/***********************Expressions***********************/
	/**Visit a conditional expression and print the c++ equivalent**/
	public void visitConditionalExpression(GNode n)
	{
		print("(");
		//visit the expression Node
		visitChildren(n, 0,1,"");
		//append the default strings not in the AST
		print(") ? ");
		//get the rest node
		visitChildren(n, 1,n.size(),":");
	}	
	/** Visit the selection expression i.e. java.lang*/
	public void visitSelectionExpression(GNode n)
	{
			//prints the selection expression
		Node prim = n.getNode(0);
		dispatch(prim);
		//print a . at the end of each expression
		print("::");	
		Object o =n.get(1);
		if(o instanceof String) //make sure the object o is an string
		{
			print((String)o);
		}
		else { //other wise its a Node and call dispatch on it to visit it
			dispatch((Node) o);
		}
	}
	/**calls the default binary behavor on relational expressions */
	public void visitRelationalExpression(GNode n)
	{
		setBinary(n);		
	}
	/**prints default c++ code and calls default binary behavor on Equality Expressions */
	public void visitEqualityExpression(GNode n)
	{
		print("(");		
		setBinary(n);		
		print("){ \n");
	}//end of visitCallExpression method
	
	/** calls default binary behavior on Expression */
	public void visitExpression(GNode n)
	{
		setBinary(n);
	}
	/** calls default unary behavior on Expression */
	public void visitUnaryExpression(GNode n)
	{
		setUnary(n);
	}
	/**calls default unary behavior on UnaryExpression*/
	public void visitUnaryExpressionNotPlusMinus(GNode n)
	{
		setUnary(n);
	}
	/**calls default unary behavior*/
	public void visitPreDecrementExpression(GNode n)
	{
		setUnary(n);
	}
	/**calls default unary behavior */
	public void visitPreIncrementExpression(GNode n)
	{
		setUnary(n);
	}
	/**calls default unary behavior */
	public void visitPostfixExpression(GNode n)
	{
		setUnary(n);
	}
	/**calls default unary behavior */
	public void visitShiftExpression(GNode n)
	{
		setUnary(n);
	}
	/**calls default binary behavior */
	public void visitConditionalOrExpression(GNode n)
	{
		setBinary(n);
	}
	/**calls default binary behavior */
	public void visitConditionalAndExpression(GNode n)
	{
		setBinary(n);
	}
	/**calls default binary behavior */
	public void visitInclusiveOrExpression(GNode n)
	{
		setBinary(n);
	}
	/**calls default binary behavior */
	public void visitExculsiveOrExpression(GNode n)
	{
		setBinary(n);
	}
	/**calls default binary behavior */
	public void visitAndExpression(GNode n)
	{
		setBinary(n);
	}
	/**visits the 3rd node of Expression and dispatches on it to print the subtree properly */
	public void visitExpression(Node n)
	{
		Node b=n.getNode(3);
		dispatch(b);
	}
	/**calls the default binary behavior*/
	public void visitAdditiveExpression(GNode n)
	{
		setBinary(n);
	}
	/**replaces "this" with __this for propery c++ conversion*/
	public void visitThisExpression(GNode n)
	{
		print("__this.");
		visit(n);
	}
	/**Assumes that all "smart' code has been handled in EWalk does nothing but print code *place holder**/
	public void visitSuperExpression(GNode n)
	{
		print("getsuperclass(__this)->");
		visit(n);
	}
	/**set the default binary behavior See setBinary*/
	public void visitMultiplicativeExpression(GNode n)
	{
		setBinary(n);
	}	
	/*******************Statements ******************************/
	/**print java asser code currently not a priority*/
	public void visitAssertStatement(GNode n)
	{
		print("assert ");
		visitChildren(n,0,1,"");
		print(":");
		visitChildren(n,1,n.size(),"");
		print("; \n");
	}
	/**print java sychronizedstatement currently not a priority */
	public void visitSychronizedStatement(GNode n)
	{
		print("sychronized (");
		visitChildren(n,0,1,"");
		print("){\n");
		visitChildren(n,1,n.size(),"");
		print("}\n");
	}
	/**print java labeled statement */
	public void visitLabeledStatement(GNode n)
	{
		print(n.getString(0)+":\n");
		visitChildren(n,1,n.size(),"");
	}
	/**print c++ break statement  call setBreCon to get code if inside of break statement*/
	public void visitBreakStatement(GNode n)
	{
		print("break");
		setBreCon(n);
	}
	/**visit contrinue stats ment and call setBreCon to get code if any inside of continue statement*/
	public void visitContinueStatement(GNode n)
	{
		print("continue");
		setBreCon(n);
	}
	/**get code inside of trycatchfinallystatement*/
	public void visitTryCatchFinallyStatement(GNode n)
	{
		print("try {\n");
		Node block = n.getNode(0);
		dispatch(block);
		print("} ");
		visitChildren(n, 1, n.size(), "");
	}
	/**print throw statement (handling excpetions? */
	public void visitThrowStatement(GNode n)
	{
		print("throw ");
		visitChildren(n, 0, n.size(), "");
		print(";\n}\n");
	}
	/**print the c++ return statement
	 add a flag*/
	public void visitReturnStatement(GNode n)
	{
		print("return ");
		//set return flag true
		isReturn=true;
		visitChildren(n, 0, n.size(), "");
		print("; \n");
		isReturn=false;
	}
	/**Visit Expression append a ;*/
	public void visitExpressionStatement(GNode n)
	{
		visitChildren(n, 0, n.size(), "");
		print(";\n");
	}
	/**visit switch statement and below case clause*/
	public void visitSwitchStatement(GNode n)
	{
		print("switch(");
		visitChildren(n, 0, 1, "");
		print(")");
		print("{\n");
		visitChildren(n, 1, n.size(), "");
		print("}\n");
	}
	/**visit the case clause usually inside a switch statement*/
	public void visitCaseClause(GNode n)
	{
		print("case ");
		visitChildren(n, 0, 1, "");
		print(":");
		visitChildren(n, 1, n.size(), "");
	}
	/**visit if/Else statements nodes aka Conditional statments
	 Else statements are nested so this handling has to be a bit more inteligent*/
	public void visitConditionalStatement(GNode n)
	{
		print("if(");
		visitChildren(n, 0, 1, "");
		print("){\n");
		visitChildren(n, 1, 2, "");
		print("}\n");
		for(int i=2;i<n.size();i++)
		{
			Object o = n.get(i);
			//make sure the node isn't null (there isn't an else statement)
			if (o!=null) {
				print("else ");
				Node cond =n.getNode(i);
				if(cond.getName().equals("Block")){
					print("{ \n");
					dispatch(cond);
					print("} \n");
				}
				else
				{	
					dispatch(cond);
				}
			}
		}
	}	
	////////////////Loops////////////////////////
	/**visiting the Do While statement in the AST*/
	public void visitDoWhileStatement(GNode n)
	{
		print("do{\n");
		visitChildren(n, 0, 1, "");
		print("}while(");
		visitChildren(n, 1, n.size(), "");
		print(");\n");
	}
	/**visit the regular while statement */
	public void visitWhileStatement(GNode n)
	{
		print("\n while(");
		Object param=n.get(0);
		checkInstance(param);
		print("){\n");
		visitChildren(n, 1, n.size(), "");
		print("} \n");
	}
	/** This is NOT a visitor, it is called in the VisitForStatement to print the right code for a forloop*/
	public void visitForUpdate(Node n)
	{
		Node d= n.getNode(4);
		dispatch(d);
	}
	/**Tis is NOT a visitor, it is called to dispatch on for loop inti properly*/
	public void visitForInit(Node n)
	{
		Node b=n.getNode(0);
		dispatch(b);
		Node c= n.getNode(1);
		dispatch(c);
		Node d= n.getNode(2);
		dispatch(d);	
	}	
	/** Print out the basic ForLoop Control Text*/
	public void visitBasicForControl(GNode n)
	{
		//basic control node
		Node a=n.getNode(0);
		print("(");
		visitForInit(a);
		print(";");
		visitExpression(a);
		print(";");
		visitForUpdate(a);
		print(")");
	}
	/** Vistiro visits the ForLoop AST Node and acts accordingly */
	public void visitForStatement(GNode n)
	{
		print("for");
		visitBasicForControl(n);
		print("{\n");
		visitChildren(n, 1, n.size(), "");
		print("}\n");	
	}	
	/***********************Classes ******************************/
	/**Visit a New ClassExpresions Node i.e. Class c = new Class()*/
	public void visitNewClassExpression(GNode n)
	{
		print("new ");
		for(int i=0;i<n.size();i++)
		{
			if (i==3) {
				print("(");
			}
			if (i==2){
				print("__");
			}
			Object o=n.get(i);
			if(isString(o))
			{
				print(" __"+(String)o);
			}
			else if(isNode(o))
			{
				dispatch((Node) o);
			}
			
			if (i==3) {
				print(")");
			}	
			
		}
		//close the brackets print a new line and then print the init
		print(";\n");
		//  b->init(b,8,"string");
		print(declared);
		print("->init");
		print("(" +declared);
		//check to see if their are any arguemnts
		Node arguments = n.getNode(3);
		if(arguments.size()>0)
		{
			print(",");
			visitChildren(arguments,0,arguments.size(),",");
			
		}
		print(")");
	}
	public void visitPrimaryIdentifier(GNode n)
	{
		print(n.getString(0));
	}
	/**visit call expression where a method is called  could be done on an instance handled in eWalk*/
	public void visitCallExpression(GNode n)
	{
		isPrint=false;
		//check the first child to see if its a primaryIdentifier 
		//put in the special case for Pat's Cout
		Object third= n.get(2);
		
		if(third !=null)
		{
			if(third instanceof String)
			{
				//Node nThird = (Node)third;
				String thirds =n.getString(2);
				//Add special case for System.out.print
				if(third.equals("std::cout<<"))
				{
					isPrint=true;
					//print 2
					print(thirds);
					//print Arguments
					visitChildren(n,3,n.size(),"<<");
					//print 1
					print(n.getString(1));
				}
				else
				{
					boolean hasReciever=false; //boolean flag of recievers i.e. b.m1()
					String primaryid =""; //reciever stored as a string
					visitChildren(n,0,1,"");
					System.out.print(n.toString());
					if(n.get(0)!=null){
						if(n.get(0) instanceof Node )
						   {
							   if (n.getNode(0).getName().equals("PrimaryIdentifier"))
							   { 
								   hasReciever=true;
								   primaryid = n.getNode(0).getString(0);
								   print(primaryid);
							   }
						   }
					}
					//visit all the children minus the arguments
					visitChildren(n, 1, 3, "");
					//visit the arguments
				//	print("(");  NOW HANDLED IN EWALK
					if(hasReciever){ //check to see if there is a reciever b.m1()
						//print(primaryid);
						//if there are any arguments make sure to print a comma
						if(n.getNode(3).size()>0)//arguments Node
						{
							print(",");
						}
					}
					
					visitChildren(n, 3, n.size(), "");
					print(")");
					
				}
				
			}
			//else if (third instanceof Node)
			//{
			//	dispatch((Node)third);
			//}
		}

		/*if(isPrint){
		Object o= n.get(0);
		if (o!=null)
		{
			if(isNode(o))
			{
				Node oNode = (Node)o;
				//if it is a primaryIdentifier print out a check statement
				if (oNode.getName().equals("PrimaryIdentifier") ){
					
					//print("__rt::checkNotNull("+oNode.getString(0)+");\n");
					primIdentifier= oNode.getString(0);
					print(primIdentifier);
					isInstance=true;
				}
				//else its not a PrimaryIdentifier Node dispatch on it as normal
				else{
					dispatch(oNode);
				}
			}
		}
		}*/
		
	}
	/**visit qualifiedIdentifier i.e. custom Objects
	 It is assumed that all the "SMART" work has already been handled in EWalk
	 i.e. prints no "." or "->" etc just prints the __ and the name
	 */
	public void visitQualifiedIdentifier(GNode n)
	{
		for(int i=0; i<n.size();i++)
		{
			String name = n.getString(i);
			print(name);
		}
	}
	/**********************Other***************************/
	/**Visists the argument Node and prints the children with a comma (,) i.e. (a, b,c) also
	 runs a check for the isInstance variable and prints that if true*/
	public void visitArguments(GNode n)
	{
		isArguments=true;
	//	if(isCallExpression)
	//	{
			if(isInstance)
			{
				print(primIdentifier+",");
				isInstance=false;
				primIdentifier="";
			}
			else {
				//print("__this"+ ",");
			}
	//	}

		visitChildren(n, 0, n.size(), ",");
		isArguments=false;
	}
	/**visits the formal parameter node */
	public void visitFormalParameter(GNode n)
	{
		visitChildren(n, 0, n.size(), " ");
	}
	/**visits and print out catch clause*/
	public void visitCatchClause(GNode n)
	{
		print("catch(");
		visitChildren(n, 0, 1, "");
		print("){\n");
		visitChildren(n, 1, n.size(), "");
	}
	/**visit and print default label*/
	public void visitDefaultClause(GNode n)
	{
		//print the proper java code
		print("default");
		print(":");
		
		//visit all of the switch children
		visitChildren(n,0,n.size(),"");
		
	}
	/**visit and print field declarations prints a ; and new line at the end of each node*/
	public void visitFieldDeclaration(GNode n)
	{
		//add a new tab to the front of the declaration
		//print("\t");
		
		//visit all of the field declarations children
		visitChildren(n,0,n.size(),"");
		
		//append the semicolon and new line symbolat the end of each declaration
		print(";\n");
	}
	
	/** print static modifier */
	public void visitInitializer(GNode n)
	{
		print("static");
		
		visitChildren(n,1,n.size(),"");
	}
	/*print final**/
	public void visitLocalVariableDeclaration(GNode n)
	{
		print("final");
		Node type= n.getNode(1);
		for(int i=2;i<n.size();i++)
		{
			visitChildren(n,2,n.size()," , ");
		}	
	}
	/**Visits the modifier node checks to see the  current modifier condition and the curret scope condition and prints out the 
	 modifier accordingly, also prints everything other then public and private in the else statement */
	public void visitModifier(GNode n)
	{
		//run check to see if isPrivate is one
		//visit every modifier of the given variable
		for(int i =0; i<n.size(); i++)
		{
			String modifier = n.getString(i);
			//if the modifier is a public and isPrivate is one print it
			if(modifier.equals("public")&& isPrivate)
			{//if the modifier is public and the current modifier status is set to private
				print("public: \n");
				isPrivate = false;
			}
			else if(modifier.equals("private") && (!isPrivate))
			{//if the modifier is private and the current modifier status is set to private
				print("private: \n");
				isPrivate = true;
			}
			else if (!(modifier.equals("public"))&&(!(modifier.equals("private")))&&(!(modifier.equals("protected")))){
				//if the modifiers are anything but public, private, protected just print them normally
				print(modifier+ " ");
			}
		}
	}
	
	/**Visit the declarators i.e. int i = 5; 
	 if there is */
	public void visitDeclarator(GNode n)
	{
		declared=n.getString(0); //declarator name is at the first child
		print(" " +declared);
		//get the object at position 1 and check to make sure its not null
		Object one = n.get(1);
		checkInstance(one);
		if(one!=null)
		{//check the instance of the object and decide what to do with it
			
			
			checkInstance(one);
			
		}
		//do the same with object at position 2
		Object two = n.get(2);
		if(two!=null)
		{
			print("=");
			//check to see if the node is a string Literal
			if(two instanceof Node)
			{
				Node gTwo = (Node) two;
				if(gTwo.getName().equals("StringLiteral"))
				{
					print("new __String(" +gTwo.getString(0)+")");
				}
				else {
					checkInstance(two);
				}
			}
			else {
				checkInstance(two);
			}

		}
	}
	/**Visit the type node and print it*/
	public void visitType(GNode n)
	{
		visitChildren(n, 0, n.size(), "");
	}
	/**edited visitor method checks the instance of every node to decide whether to visit or print*/						   
	public void visit(Node n)
	{
		if(n!=null){
		for(Object o:n) {
				checkInstance(o);
			
		}
		}
	}
	/**method that visits all the children of a node from start to finish and calls check instance
	 @param Node n is the parent node
	 @param int start is the starting child (0 for all)
	 @param int end is the ending child (n.size for all)
	 @param string seperator is the seperating string value (i.e. " ", "," etc Only prints as a seperator (a,b,c)
	 */
	 public void visitChildren(Node n, int start, int end, String seperator)
	{
		for (int i=start;i<end;i++) {
			//as long as its not the last child or first child append the seperator
			if(i<end && i>start){
				print(seperator);
			}
			//get the node at the given position
			
			Object current = n.get(i);
			//call the instance check on it
			checkInstance(current);	
		}
	}
	
	
	/**Runs instanceof checks on a given node and calls appropiate action*/
	public void checkInstance (Object o)
	{
		//make sure the object isn't null
		if (o != null) {
			//if the given o is a String print it
			if (isString(o)) {
				//print(" ");
				print((String) o);
			}			
			//if the given o is a Node dispatch on it (visit its subtree)
			else if(isNode(o)){
				dispatch((Node)o);
			}
		}
		
	}
	/**returns true if the given Object is a String*/
	public boolean isString(Object o)
	{
		if(o instanceof String)
			return true;
		else			
			return false;
	}
	/**returns true if the given Object is a Node*/
	public boolean isNode(Object o)
	{
		if(o instanceof Node)
			return true;
		else
			return false;			
	}
	public void visitInstanceOfExpression(GNode n)
	{
		//get left side identifier (object to be checked)
		String primary = n.getNode(0).getString(0);
		
		//get right side Class (class to check for instance
		String className = n.getNode(1).getNode(0).getString(0);
		/*
		 if(({ Class k=d->__vptr->getClass(d);
		 std::cout<<"K"<<k->__vptr->toString(k)<<std::endl;
		 k->__vptr->isInstance(k,d);})){
		 std::cout<<"RAWR"<<std::endl;
		 }
		 */
		print("({ Class k=");
		print(primary+"->__vptr->getClass("+primary+");\n");
		print("k->__vptr->isInstance(k,"+primary+");})");
	}
	/**checks for calls inside break and continue statements and prints those values*/
	public void setBreCon(GNode n)
	{
			for (int i=0; i<n.size(); i++) {//for every child of N get it and check to see if its an instance of string or Node
			Object o=n.get(i);
				checkInstance (o);
			
		print(";\n"); //append a new line
			}
	}
	/**for expressions "recursively" calls dispatch on the operands and print the middle operator*/						   
	public void setBinary(GNode n)
	{
		//get the first operand
		Node operand1= n.getNode(0);
		dispatch(operand1);
		print(n.getString(1)); //print the operator		
		//get the second operand
		Node operand2= n.getNode(2);
		dispatch(operand2);	
	}
	
	/**for unary expressions such as -A checks to see intances of children */						   
	public void setUnary(GNode n)
	{
		visitChildren(n, 0,n.size(),"");
	}
	/**A Method that takes a string to be appended to the stringbuilder and also if 
	 debug is set to true it prints to the screen*/
	public void print(String value)
	{
		printer.append(value);
		//print the value to the terminal
		if (DEBUG) {System.out.print(value);}
	}
	/**@return the public stringBuilder */
	public StringBuilder getString()
	{
		return printer;
	}	
	public void setupLog(String name)
	{
		//delete the file if it already exists
		File f = new File(name+".log");
		if(f.exists())//delete it
		{
			f.delete();
		}
		
		try {
			// Create a file handler that write log record to a file called my.log
			FileHandler handler = new FileHandler(name+".log",true);
			
			// Add to the desired logger
			//Logger logger = Logger.getLogger("com.mycompany");
			logger.addHandler(handler);
		} catch (IOException e) {
		}
		
		
	}
}

