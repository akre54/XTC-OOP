
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
//tweek methods to take a single given GNode
//functionality for arrays- find the arrays- every time i come across an array declare them differently
//look in new java_lang.cc .cpp .h all the definition for arrays - just write now call a class that creates a print

//new array writer class

//write a class that transverses
/*features etc
	if/else
	for loops
	while loops
	return statements
	public static final
	modifiers
	arrays -arrays writer class
		-check for arrays
	making objects of classes
	-missing visitors
	System.out.println
 -get values outside of Methods - getClassDeclarations outside of the Method Strings without 
 getting values inside a MethodDeclaration

*/


//throw class callcheck for System.out.println - Pat's going to finish that class
/*
 For Liz:
	-wants to pass you a method Declaration node
		-wants to return in stringtranslated form all of the contents of the method
		-already hands all the declaration and parameters
	-*/

/**Takes a classDeclaration GNode and  generates the basic class values*/
public class cppClass extends Visitor{ 
	
	public final boolean DEBUG = false;
	private StringBuilder classString = new StringBuilder();
	/**
	 Constructor takes an Compilation Unit and searches all ClassDeclarations in the AST
	 */
	cppClass(Node n)
	{
		//System.out.println("Lalalal");
		visit(n);
		if(DEBUG){System.out.println(classString);}
		
	}//end of cppClass constructor
	
	/**
	 Visit a ClassDeclaration Node
	 */
	public void visitClassDeclaration(GNode n) {
		
		/**
		 Append the Class text to the classString StringBuilder To create a new class
		 */
		 
		classString.append("Class "+ getClassName(n)+ "{ \n");
		classString.append(setFields(n));
		//StringBuilder constructor = setConstructor(n);
		//classString.append(constructor);
		StringBuilder methods=setMethods(n);
		classString.append(methods+ "\n");
		classString.append("} \n");
		
	}//end of visitClassDeclaration Method
	
	/**
	 Visit Each Node in the Tree 
	 */
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public StringBuilder setConstructor(GNode n){
		cppConstructor constr=new cppConstructor(n);
		return constr.getString();
	}
	
	/**
	 Checks to make sure the given GNode is actually an instance of ClassDeclaration
	 **Currently just returns true
	 */
	public boolean isClassDeclaration(GNode n)
	{
		return true;
	}//end of isClassDeclaration method
	
	/**
	 @return Stringbuilder that gets the fields inside a class declaration (but outside methods)
	 */
	public StringBuilder setFields(GNode n)
	{
		//creates a new cppFieldDeclaration class
		cppFieldDeclaration classFields= new cppFieldDeclaration(n);
		return classFields.getString();
	}
	public StringBuilder getSwitchStatements(GNode n)
	{
		cppSwitchStatement switchState = new cppSwitchStatement(n);
		return switchState.getString();
	}	public StringBuilder getConditionalStatement(GNode n)
	{
		cppConditionalStatement cond=new cppConditionalStatement(n);
		return cond.getString();
	}
	public StringBuilder getForStatements(GNode n)
	{
		cppForStatement forstate=new cppForStatement(n);
		return forstate.getString();
	}
	public StringBuilder getWhileLoop(GNode n)
	{
		cppWhileStatement whilestate =new cppWhileStatement(n);
		return whilestate.getString();
	}
	public StringBuilder getDoWhileStatement(GNode n)
	{
		cppDoWhileStatement doWhile = new cppDoWhileStatement(n);
		return doWhile.getString();
	}
	/**
	 @return the className of the given classDeclaration's node
	 */	
	public String getClassName(GNode n)
	{
		return n.getString(1);
		
	}//end of getClassName method
	
	/**
	 sets the methods for the ClassDeclaration at GNode n
	 */	public StringBuilder setMethods(GNode n)
	{
		//create a new cppMethod Class
		cppMethod aMethod = new cppMethod(n);
		return aMethod.getString();
	}//end of setMethods method
}//end of cppClass
/**Creates a Class that searches through the MethodDeclaration Nodes in a subtree
 *Also takes a MethodDeclaration Class*/
class cppMethod {
	public final boolean DEBUG = false;
	private StringBuilder methodString;
	/**
	 visits all the nodes in a ClassDeclaration Node or visits a single MethodDeclaration Node
	 */
	public cppMethod(GNode n)
	{
		//check to see if given node is a MethodDeclaration node or a classDeclaration
		String nodeName= n.getName();
		methodString=new StringBuilder();
		if (nodeName.equals("MethodDeclaration"))
		{
				CppPrinter print =new CppPrinter(n);
		}
	}//end of cppMethod constructor
	public StringBuilder getString()
	{
		return methodString;
	}
	
}//end of cppMethod class
