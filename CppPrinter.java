
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

import xtc.util.Tool;
/**A "Stupid" print, it just prints everything it sees and assumes that every String inside the AST is proper C++ code, there are special catches and cases
 for brackets, etc but there is no intelligent code in here */
public class CppPrinter extends Visitor
{
	private StringBuilder printer; //a StringBuilder that stores the code translated by the print
	public boolean DEBUG = false;
	public boolean isPrivate; //a global boolean that keeps track of the current modifier status
	public CppPrinter(GNode n)
	{
		printer = new StringBuilder(); //intialize Stringbuilder
		isPrivate =false; //sets false by default since structs are public by default
		visit(n); //visit the given node (starts all the visiting)
	}
	public CppPrinter(GNode n, boolean debug)
	{
		DEBUG= debug;
		printer = new StringBuilder(); //intialize Stringbuilder
		isPrivate =false; //sets false by default since structs are public by default
		visit(n); //visit the given node (starts all the visiting)
	}
	/*visit cast expression and print the c++ version of the java AST values*/
	public void visitCastExpression(GNode n)
	{
		print("(");
		//visit the cast type
		visitChildren(n, 0,1,"");
		print(")");
		//visit the next batch of code
		visitChildren(n, 1,n.size(),"");
	}
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
	/*Visit a conditional expression and print the c++ equivalent**/
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
		print(".");
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
	/*calls default unary behavior*/
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
	//visits the 3rd node of Expression and dispatches on it to print the subtree properly */
	public void visitExpression(Node n)
	{
		Node b=n.getNode(3);
		dispatch(b);
	}
	/*calls the default binary behavior*/
	public void visitAdditiveExpression(GNode n)
	{
		setBinary(n);
	}
	/*replaces "this" with __this for propery c++ conversion*/
	public void visitThisExpression(GNode n)
	{
		print("__this.");
		visit(n);
	}
	/**Assumes that all "smart' code has been handled in EWalk does nothing but print code *place holder**/
	public void visitSuperExpression(GNode n)
	{
		print("superclass.");
		visit(n);
	}
	/**set the default binary behavior*/
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
	/*print c++ break statement  call setBreCon to get code if inside of break statement*/
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
		/*for (int i=1; i<n.size(); i++) {
			
			Node catch1 = n.getNode(i);
			dispatch(catch1);
		}*/
	}
	//print throw statement (handling excpetions? */
	public void visitThrowStatement(GNode n)
	{
		print("throw ");
		visitChildren(n, 0, n.size(), "");
		print(";\n}\n");
	}
	/**print the c++ return statement */
	public void visitReturnStatement(GNode n)
	{
		print("return ");
		visitChildren(n, 0, n.size(), "");
		print("; \n");
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
	public void visitCaseClause(GNode n)
	{
		print("case");
		visitChildren(n, 0, 1, "");
		print(":");
		visitChildren(n, 1, n.size(), "");
	}
	/*visit if/Else statements nodes aka Conditional statments*/
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
	public void visitDoWhileStatement(GNode n)
	{
		print("do{\n");
		visitChildren(n, 0, 1, "");
		print("}while(");
		visitChildren(n, 1, n.size(), "");
		print(");\n");
	}
	public void visitWhileStatement(GNode n)
	{
		print("\n while(");
		Object param=n.get(0);
		checkInstance(param);
		print("){\n");
		visitChildren(n, 1, n.size(), "");
		print("} \n");
	}
	public void visitForUpdate(Node n)
	{
		Node d= n.getNode(4);
		dispatch(d);
	}
	public void visitForInit(Node n)
	{
		Node b=n.getNode(0);
		dispatch(b);
		Node c= n.getNode(1);
		dispatch(c);
		Node d= n.getNode(2);
		dispatch(d);	
	}	
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
	public void visitForStatement(GNode n)
	{
		print("for");
		visitBasicForControl(n);
		print("{\n");
		visitChildren(n, 1, n.size(), "");
		print("}\n");	
	}	
	/***********************Classes ******************************/
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
	}
	/**visit call expression where a method is called */
	public void visitCallExpression(GNode n)
	{
		//visit all the children minus the arguments
		visitChildren(n, 0, 3, "");
		
		//visit the arguments
		print("(");
		visitChildren(n, 3, n.size(), "");
		print(")");
	}
	/**visit qualifiedIdentifier i.e. custom Objects
	 It is assumed that all the "SMART" work has already been handled in EWalk
	 i.e. prints no "." or "->" etc just prints the __ and the name
	 */
	public void visitQualifiedIdentifier(GNode n)
	{
		//visit every child
		//visitChildren(n, 0, n.size(), "");
		for(int i=0; i<n.size();i++)
		{
			String name = n.getString(i);
			//print("__");
			print(name);
			
		}
	}
	/**********************Other***************************/
	public void visitArguments(GNode n)
	{
		visitChildren(n, 0, n.size(), ",");
	}
	public void visitFormalParameter(GNode n)
	{
		visitChildren(n, 0, n.size(), " ");
	}
			
	public void visitCatchClause(GNode n)
	{
		print("catch(");
		visitChildren(n, 0, 1, "");
		print("){\n");
		visitChildren(n, 1, n.size(), "");
	}
	public void visitDefaultClause(GNode n)
	{
		//print the proper java code
		print("default");
		print(":");
		
		//visit all of the switch children
		visitChildren(n,0,n.size(),"");
		
	}
	public void visitFieldDeclaration(GNode n)
	{
		//add a new tab to the front of the declaration
		//print("\t");
		
		//visit all of the field declarations children
		visitChildren(n,0,n.size(),"");
		
		//append the semicolon and new line symbol
		print(";\n");
	}
	public void visitInitializer(GNode n)
	{
		print("static");
		
		visitChildren(n,1,n.size(),"");
	}
	public void visitLocalVariableDeclaration(GNode n)
	{
		print("final");
		Node type= n.getNode(1);
		for(int i=2;i<n.size();i++)
		{
			visitChildren(n,2,n.size()," , ");
		}	
	}
	/**Visits the modifier node, checks for isPrivate condition to see the current modifier of the scope
	 Also currently ignores protected*/
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
	
	/**Visit the declarators */
	public void visitDeclarator(GNode n)
	{
		print(" " +n.getString(0));
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
			checkInstance(two);
		}
	}
	public void visitType(GNode n)
	{
		visitChildren(n, 0, n.size(), "");
		//print(" ");
	}
	/**edited visitor method checks the instance of every node to decide whether to visit or print*/						   
	public void visit(Node n)
	{
		for(Object o:n) {
			checkInstance(o);
		}
	}
	/**method that visits all the children of a node from start to finish and calls check instance
	 @param Node n is the parent node
	 @param int start is the starting child (0 for all)
	 @param int end is the ending child (n.size for all)
	 @param string seperator is the seperating string value (i.e. " ", "," etc)
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
	
	
	/**Checks instances of a given node and calls appropiate action*/
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
	public void setBreCon(GNode n)
	{
		//checks for calls inside break and continue statements and prints those values

		for (int i=0; i<n.size(); i++) {//for every child of N get it and check to see if its an instance of string or Node
			Object o=n.get(i);
			if (o!=null) {
			}
			
			if(o instanceof String)//if object o is a string print it
				print(" "+(String)o);
			else if(o instanceof Node) //if its a node call dispatch on it
				dispatch((Node) o);
	
			if (o!=null) {
				if(i!=(n.size()-1)){
				}
							   
			}
		}
		print(";\n"); //append a new link 
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
		for(int i=0;i<n.size();i++) //for every child in N check its instance and act acoordingl
		{
			Object k=n.get(i);
			if(k instanceof Node) //visit Nodees
			{
				dispatch((Node)k);
			}
			else { //else print the strings
					if(n.getString(i)!=null)
					{		////add the string to the print
						print(n.getString(i));
					}
			}
		}
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
}

