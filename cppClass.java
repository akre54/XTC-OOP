
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
	
	public final boolean DEBUG = true;
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
		StringBuilder constructor = setConstructor(n);
		classString.append(constructor);
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
class cppConstructor extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder methodString;
	cppConstructor(GNode n)
	{
		methodString= new StringBuilder();
		visit(n);
	}
	public void visitConstructorDeclaration(GNode n){
		methodString.append("\t"+ getConstructorName(n)+"("+getParameters(n)+")" +"{ \n");
		StringBuilder fields= setFields(n);
		methodString.append(fields);
		methodString.append("\t \t" +getExpressionStatements(n)+"\n");
		methodString.append("\t} \n");	
	}
	public String getConstructorName(GNode n)
	{
		return n.getString(2);
	}
	public StringBuilder getParameters(GNode n)
	{
		//creates a new cppParameters class
		cppParameters param = new cppParameters(n);
		return param.getString();
		
	}//end of getParameters method
	/**
	 @return StringBuilder that gets the primitive type of a method
	 */
	public StringBuilder getPrimitiveType(GNode n)
	{
		//creates a new cppPrimitiveType class
		cppPrimitiveType pType = new cppPrimitiveType(n);
		return pType.getString();
	}
	/**
	 @return StringBuilder that gets the Qualified Identifier of a method
	 */
	public StringBuilder getQualIden(GNode n)
	{
		//creates a new cppQualifiedIdentifier class
		cppQualifiedIde qi= new cppQualifiedIde(n);
		return qi.getString();
	}
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public StringBuilder getString()
	{
		return methodString;
	}
	/**
	 @return StringBuilder that returns the fields of a method
	 */
	public StringBuilder setFields(GNode n)
	{
		//creates a new cppFieldDeclaration class
		cppFieldDeclaration methodFields= new cppFieldDeclaration(n);
		return methodFields.getString();
	}
	/**
	 creates the expressionStatement class
	 **Review this **
	 */
	public StringBuilder getExpressionStatements(GNode n)
	{
		cppExpressionStatement cppEx= new cppExpressionStatement(n);
		return cppEx.getString();
	}
	
}
/**Creates a Class that searches through the MethodDeclaration Nodes in a subtree
 *Also takes a MethodDeclaration Class*/
class cppMethod extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder methodString;
	/**
	 visits all the nodes in a ClassDeclaration Node or visits a single MethodDeclaration Node
	 */
	cppMethod(GNode n)
	{
		//check to see if given node is a MethodDeclaration node or a classDeclaration
		String nodeName= n.getName();
		methodString=new StringBuilder();
		if(DEBUG){System.out.println("NodeName:"+nodeName);}
		if(nodeName.equals("ClassDeclaration"))
		{
			visit(n);
		}
		else if (nodeName.equals("MethodDeclaration"))
		{
			if(DEBUG){System.out.println("Given Method Node");}
			getMethodDetails(n);	
		}
	}//end of cppMethod constructor
	
	public void visitMethodDeclaration(GNode n) {
		
		methodString.append("\t" + setType(n)+" "+ getMethodName(n)+"("+getParameters(n)+")" +"{ \n");
		getMethodDetails(n);

	}//end of visitClassDeclaration Method
	public StringBuilder getConditionalStatement(GNode n)
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
	public void getMethodDetails(GNode n)
	{
		StringBuilder fields= setFields(n);
		methodString.append(fields);
		methodString.append("\t \t" +getExpressionStatements(n)+"\n");
		methodString.append("\t \t" +getSwitchStatements(n)+"\n");
		methodString.append(getForStatements(n));
		methodString.append(getWhileLoop(n));
		methodString.append(getDoWhileStatement(n));
		methodString.append(getConditionalStatement(n));
		methodString.append("\t\t"+getBlock(n)+" \n");
		methodString.append("\t} \n");		
	}
	public StringBuilder getBlock(GNode n)
	{
			cppBlock block =new cppBlock(n);
			return block.getString();
	}
	/**
	 @return <code>StringBuilder</code> that is the type for the method
	 */
	public StringBuilder setType(GNode n)
	{
		StringBuilder type= new StringBuilder();
		
		Node node=n.getNode(2); //VoidType, Type 
		Object o=n.get(2);
		StringBuilder s= new StringBuilder();
		if(node.hasName("Type")) //check to see if the node is a Type subtree
		{
			s.append(getPrimitiveType((GNode)node));
			s.append(getQualIden((GNode)node));
		}  
 		else //the type is a VoidType
			s.append(node.getName());
		
		return s;
	}
		/**
	 @return StringBuilder that gets the parameters of a method
	 */
	public StringBuilder getParameters(GNode n)
	{
		//creates a new cppParameters class
		cppParameters param = new cppParameters(n);
		return param.getString();
		
	}//end of getParameters method
	/**
	 @return StringBuilder that gets the primitive type of a method
	 */
	public StringBuilder getPrimitiveType(GNode n)
	{
		//creates a new cppPrimitiveType class
		cppPrimitiveType pType = new cppPrimitiveType(n);
		return pType.getString();
	}
	/**
	 @return StringBuilder that gets the Qualified Identifier of a method
	 */
	public StringBuilder getQualIden(GNode n)
	{
		//creates a new cppQualifiedIdentifier class
		cppQualifiedIde qi= new cppQualifiedIde(n);
		return qi.getString();
	}
	public StringBuilder getSwitchStatements(GNode n)
	{
		cppSwitchStatement switchState = new cppSwitchStatement(n);
		return switchState.getString();
	}
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 checks to make sure the given GNode is actually an instance of methodDeclaration
	 */
	public boolean isMethodDeclaration(GNode n)
	{
		return true;
	}//end of isMethodDeclaration Method
	/**
	 @return the entire string generated for the method
	 */	
	public StringBuilder getString()
	{
		return methodString;
	}
	/**
	 @return String that gets the Method name
	 */
	public String getMethodName(GNode n)
	{
		return n.getString(3);
	}//end of getMethodName
	/**
	 @return StringBuilder that returns the fields of a method
	 */
	public StringBuilder setFields(GNode n)
	{
		//creates a new cppFieldDeclaration class
		cppFieldDeclaration methodFields= new cppFieldDeclaration(n);
		return methodFields.getString();
	}
	/**
	 creates the expressionStatement class
	 **Review this **
	 */
	public StringBuilder getExpressionStatements(GNode n)
	{
		cppExpressionStatement cppEx= new cppExpressionStatement(n);
		return cppEx.getString();
	}
	
}//end of cppMethod class
/**Creates a class that searchs through the ConditionalStatement Subtree
 if/else statements (currently NonFunctioning)*/
class cppConditionalStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppConditionalStatement(GNode n)
	{
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitConditionalStatement(GNode n)
	{
		if(DEBUG){System.out.println("ConditionalStatement");}
		
		//cppConditionalStatement(n);
		//cppConditionalStatement condi = new ConditionalStatement(n);

		//fString.append("\t\t if ("+getRelationalExpression(n)+"){ \n");
		//fString.append("\t\t\t "+getBlock(n)+ "\n\t\t\t }");
	}//end of visitCallExpression method
	public StringBuilder getBlock(GNode n)
	{
		cppBlock block =new cppBlock(n);
		return block.getString();
	}
	public StringBuilder getExpression(GNode n)
	{
		cppExpression express = new cppExpression(n);
		return express.getString();
	}
	public StringBuilder getConditionalExpression(GNode n)
	{
		cppConditionalStatement cond =new cppConditionalStatement(n);
		return cond.getString();
	}
	public StringBuilder getString()
	{
		return fString;
	}
	
}
/**creates a class that explores the ForStatement subtree*/
class cppForStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppForStatement(GNode n)
	{
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitForStatement(GNode n)
	{
		if(DEBUG){System.out.println("ForStatement");}
		fString.append(getBasicControl(n));
		fString.append("\t\t\t"+getBlock(n)+ "\n\t\t\t }");
	}//end of visitCallExpression method
	public StringBuilder getBlock(GNode n)
	{
		cppBlock block =new cppBlock(n);
		return block.getString();
	}
	public StringBuilder getBasicControl(GNode n)
	{
		cppBasicControl basicControl=new cppBasicControl(n);
		return basicControl.getString();
	}
	public StringBuilder getString()
	{
		return fString;
	}
}
/**Creates a class that explores the WhileStatement Subtree*/
class cppWhileStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppWhileStatement(GNode n)
	{
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitWhileStatement(GNode n)
	{
		if(DEBUG){System.out.println("WhileStatement");}
		fString.append("\n\t\t while ( "+getEqualityExpression(n)+"){\n");
		fString.append("\t\t\t "+getBlock(n)+ "\n\t\t\t }");
	}//end of visitCallExpression method
	public StringBuilder getEqualityExpression(GNode n)
	{
		cppEqualityExpression equal = new cppEqualityExpression(n);
		return equal.getString();
	}
	public StringBuilder getBlock(GNode n)
	{
		cppBlock block =new cppBlock(n);
		return block.getString();
	}
	public StringBuilder getString()
	{
		return fString;
	}
	
}
/**Creates a class that explores the DoWhileStatement Subtree*/
class cppDoWhileStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppDoWhileStatement(GNode n)
	{
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitDoWhileStatement(GNode n)
	{
		if(DEBUG){System.out.println("DoWhileStatement");}
		fString.append("\n\t\t do { \n" +"\t\t\t"+getBlock(n)+"\n");
		fString.append("\t\t }while ( "+getEqualityExpression(n)+ ")\n\t\t\t }");
	}//end of visitCallExpression method
	public StringBuilder getEqualityExpression(GNode n)
	{
		cppEqualityExpression equal = new cppEqualityExpression(n);
		return equal.getString();
	}
	public StringBuilder getBlock(GNode n)
	{
		cppBlock block =new cppBlock(n);
		return block.getString();
	}
	public StringBuilder getString()
	{
		return fString;
	}
	
}
/**creates a class that explores the Block Subtree*/
class cppBlock extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppBlock(GNode n)
	{
		
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitBlock(GNode n)
	{
		if(DEBUG){System.out.println("Block");}
		fString.append(getExpression(n));
		fString.append(getExpressionStatement(n));
	}//end of visitCallExpression method
	public StringBuilder getExpressionStatement(GNode n)
	{
		cppExpressionStatement express = new cppExpressionStatement(n);
		return express.getString();
		
	}
	public StringBuilder getExpression(GNode n)
	{
		cppExpression express= new cppExpression(n);
		return express.getString();
	}
	public StringBuilder getString()
	{	
		return fString;
	}
}
/**creates a class that explores the RelationsExpression Subtree*/
/*class cppRelationalExpression extends Visitor
{
	public final boolean DEBUG = false;
	private StringBuilder fString;		
	cppRelationalExpression(GNode n)
	{
		fString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitRelationalExpression(GNode n)
	{
		if(DEBUG){System.out.println("RelationalExpression");}
		if(DEBUG){System.out.println(n.getString(1));}
		fString.append(getPrimaryIdentifier(n)+ " " +n.getString(1) +" "+getLiteral(n));
		//fString.append(getLiteral(n));
		//sString.append("switch("+getPrimaryIdentifier(n)+"){\n");
		
	}//end of visitCallExpression method
	public StringBuilder getPrimaryIdentifier(GNode n)
	{
		
		cppPrimaryIdentifier prim=new cppPrimaryIdentifier(n);
		return prim.getString();
	}
	public StringBuilder getLiteral(GNode n)
	{
		
		cppLiteral lit =new cppLiteral(n,2);
		return lit.getString();
	}
	public StringBuilder getString()
	{
		return fString;
	}
}*/
/**creates a calss that explores the BasicControl subtree*/
class cppBasicControl extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder bString;		
	cppBasicControl(GNode n)
	{
		bString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitBasicForControl(GNode n)
	{
		if(DEBUG){System.out.println("BasicControl");}
		bString.append("\t\t for("+getType(n)+" "+getDeclarator(n)+ ";" +getRelationalExpression(n)+ ";"+ getExpressionList(n)+"){\n");
	}//end of visitCallExpression method
	public StringBuilder getModifiers(GNode n)
	{
		cppModifier mod= new cppModifier(n);
		return mod.getString();
	}
	public StringBuilder getDeclarator(GNode n)
	{
		cppDeclarator decl = new cppDeclarator(n);
		return decl.getString();
	}
	public StringBuilder getRelationalExpression(GNode n)
	{
		cppRelationalExpression rel = new cppRelationalExpression(n);
		return rel.getString();
	}
	public StringBuilder getExpressionList(GNode n)
	{
		cppExpressionList expression=new cppExpressionList(n);
		return expression.getString();
	}
	public StringBuilder getType(GNode n)
	{
		cppType type=new cppType(n);
		return type.getString();
	}
	public StringBuilder getString()
	{
		return bString;
	}
}
/**creates a class that expores the ExpressionList subtree*/
class cppExpressionList extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder bString;		
	cppExpressionList(GNode n)
	{
		bString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitExpressionList(GNode n)
	{
		if(DEBUG){System.out.println("ExpressionList");}
		//sString.append("switch("+getPrimaryIdentifier(n)+"){\n");
		bString.append(getPostFixExpression(n));
		
	}//end of visitCallExpression method
	public StringBuilder getPostFixExpression(GNode n)
	{
		cppPostFixExpression post= new cppPostFixExpression(n);
		return post.getString();
	}
	public StringBuilder getString()
	{
		return bString;
	}
}
/**creates a class that explores the PostfixExpression subtree*/
class cppPostFixExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder bString;		
	cppPostFixExpression(GNode n)
	{
		bString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitPostfixExpression(GNode n)
	{
		if(DEBUG){System.out.println("PostfixExpression");}
		if(DEBUG){System.out.println(n.getString(1));}
		bString.append(getPrimaryIdentifier(n)+n.getString(1));
		//sString.append("switch("+getPrimaryIdentifier(n)+"){\n");
		
	}//end of visitCallExpression method
	public StringBuilder getPrimaryIdentifier(GNode n)
	{
		cppPrimaryIdentifier iden= new cppPrimaryIdentifier(n);
		return iden.getString();
	}
	public StringBuilder getString()
	{
		return bString;
	}
}
/**creates a class that explores the EqualityExpression Subtree*/
class cppEqualityExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder bString;		
	cppEqualityExpression(GNode n)
	{
		bString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitEqualityExpression(GNode n)
	{
		if(DEBUG){System.out.println("EqualityExpression");}
		bString.append(getPrimaryIdentifier(n)+""+n.getString(1)+ ""+ getLiteral(n,2));
	}//end of visitCallExpression method
	public StringBuilder getPrimaryIdentifier(GNode n)
	{
		cppPrimaryIdentifier iden= new cppPrimaryIdentifier(n);
		return iden.getString();
	}
	public StringBuilder getLiteral(GNode n ,int location)
	{
		cppLiteral lit = new cppLiteral(n, location);
		return lit.getString();
		
	}
	public StringBuilder getString()
	{
		return bString;
	}
	
}
/**Creates a class the explores the switch statement subtree*/
class cppSwitchStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder sString;		
	cppSwitchStatement(GNode n)
	{
		sString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitSwitchStatement(GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
		sString.append("switch("+getPrimaryIdentifier(n)+"){\n");
		sString.append(getCaseClause(n));
		sString.append("\t\t\t}\n");
	}//end of visitCallExpression method	
	public StringBuilder getPrimaryIdentifier(GNode n)
	{
		cppPrimaryIdentifier primaryId = new cppPrimaryIdentifier(n);
		return primaryId.getString();	
	}
	public StringBuilder getCaseClause(GNode n)
	{
		cppCaseClause clause=new cppCaseClause(n);
		return clause.getString();
	}
	public StringBuilder getString()
	{
		return sString;
	}
}
/**creates a class that exlores the CaseClause subtree*/
class cppCaseClause extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder cString;		
	cppCaseClause(GNode n)
	{
		cString= new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitCaseClause(GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
		cString.append("\t\t\t case "+getLiteral(n)+ ": ");
		cString.append(getExpressionStatements(n)+ " ");
		cString.append(getBreakStatement(n)+ "\n");
	}//end of visitCallExpression method
	public StringBuilder getLiteral(GNode n)
	{
		cppLiteral literal=new cppLiteral(n);
			return literal.getString();
	}
	public StringBuilder getExpressionStatements(GNode n)
	{
		cppExpressionStatement expression=new cppExpressionStatement(n);
			return expression.getString();
	}
	public StringBuilder getBreakStatement(GNode n){
			cppBreakStatement breakstatement=new cppBreakStatement(n);
			return breakstatement.getString();
	}
	public StringBuilder getString()
	{
			return cString;
	}
}
/**creates a new class that explores the cppBreakStatement Subtree.
 There's a currently ignored null value within the breakStatement subtree*/
class cppBreakStatement extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder bString;	
	cppBreakStatement(GNode n)
	{
		bString= new StringBuilder();
		visit(n);	
	}
	public StringBuilder getString()
	{
		return bString;
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitBreakStatement(GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
		bString.append("break;");
	}//end of visitCallExpression method
}
/**creates an class that explored the CallExperssion subtree*/
class cppExpressionStatement extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder pString;	
	private boolean isOut;
	private String print;
	private boolean isSystem;
	private boolean isSystemOutPrint; //checks the system.out.print case
	private StringBuilder SystemOut; //System.out Text
	private GNode arguments;
	private boolean isArray;
	public ArrayMaker arraym;
	private GNode  expression;
	 private boolean foundSwitchStatement;
	/**
	 
	 */
	cppExpressionStatement(GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}

			pString=new StringBuilder();
			isOut=false;
			isSystem=false;
			isSystemOutPrint=false;
				arguments=null;
			foundSwitchStatement=false;
			expression =n;
			visit(n);
	
		
	}
	public void visitNewArrayExpression(GNode n)
	{
		isArray=true;
		arraym = new ArrayMaker (n, true);
		pString.append(arraym.getStringBuffer());
	}
	public void visitExpression(GNode n)//not being reached
	{
		//pString.append("ZOMG!");
		cppExpression express=new cppExpression(n);
		isArray=express.isArray;
		pString.append(express.getString());
		pString.append(isArray);
		if( isArray)
		{
			getArray(n);
		}
		//cppSelectionExpression select =new cppSelectionExpression(n);
		//System.out.println(select.getString());
	}
	public StringBuffer getArray(GNode n)
	{
		ArrayMaker arraym= new ArrayMaker(n, true);
		return arraym.getStringBuffer();
	}
	public void visitExpressionStatement(GNode n)
	{
		StringBuilder callString = new StringBuilder();
		callString = getCallExpression(n);
		if(!isSystemOutPrint)
		{
			pString.append(callString);

		}
	 }
	public void visitSwitchStatement(GNode n)
	 {
		 foundSwitchStatement=true;
	 }
	/*public void visitCallExpression(GNode n)
	{
		//getCallExpression(n);
		if(DEBUG){System.out.println(n.getString(2));}
		print=n.getString(2);
		getSelectionExpression(n);
		cppArguments cppargs = new cppArguments(n);
		arguments=cppargs.getArguments();
		testSystemOut();
		if(!isSystemOutPrint)
		{
			pString.append(n.getString(2));
		}
		pString.append(getCallExpression(n));
		
		//testSystemOut();
	}*///end of visitCallExpression method
	
	/***
	 method that creates a new cppSelectionExpression class that checks the isOut and isSystem cases
	 */
	/*public void getSelectionExpression(GNode n)
	{
		cppSelectionExpression cppCall = new cppSelectionExpression(n);
		isOut=cppCall.isOut();
		isSystem=cppCall.isSystem();
	}*/
	public void visit(Node n) {
		for (Object o : n){
			
			if (o instanceof Node && !foundSwitchStatement) dispatch((Node)o);
		}
	} //end of visit method
	public StringBuilder getCallExpression(GNode n)
	{
		cppCallExpression call = new cppCallExpression(n);
		return call.getString();
	}
	/**+
	 @return the classExpression String
	 */
	public StringBuilder getString()
	{
		//pString.append);
		return pString;
	}
	/**
	 @return the SystemOutPrint boolean that returns if the SYstem.out.Prinln is in the classExpression
	 */
	public boolean isSystemOutPrint()
	{
		return isSystemOutPrint;
	}
	/** 
	 @return get the System.out.Print Command print or Println 
	 */
	public String getPrint()
	{
		return print;
	}
	/*****
	 Test case that calls the SystemPrint class if The System Out Print values are true
	 */
	public void testSystemOut()
	{
		if(isSystem){
			if(isOut){
				if(print.compareTo("println")==0){
					isSystemOutPrint=true;
					//call constructor for System.out.println
					if(DEBUG){System.out.println(arguments.getName());}
					SystemPrint printLn = new SystemPrint(arguments, true);
					if(DEBUG){System.out.println(printLn.getString());}
					pString.append(printLn.getString());
					
				}
				else if(print.compareTo("print")==0){
					isSystemOutPrint=true;
					if(DEBUG){System.out.println(arguments.getName());}
					//call constructor for System.out.print
					SystemPrint print = new SystemPrint(arguments, false);
					if(DEBUG){System.out.println(print.getString());}
					pString.append(print.getString());
				}
				else {
					//something is wrong or a case test is missing
					System.out.println("Error: System.out but no Print or Println check values");
				}
				
			}
		}
		
	}
	
}//end of cppExpressionStatemnet class
/** Class that vists and explores the CallExpression Subtree 
 current glitch prints out "Out" in System.out.println*/
class cppCallExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder cString;	
	private boolean isOut;
	private String print;
	private boolean isSystem;
	private boolean isSystemOutPrint; //checks the system.out.print case
	private StringBuilder SystemOut; //System.out Text
	private GNode arguments;
	private boolean foundSwitchStatement;
	
	cppCallExpression(GNode n)
	{
		isOut=false;
		isSystem=false;
		isSystemOutPrint=false;
		arguments=null;
		foundSwitchStatement=false;
		cString= new StringBuilder();
		visit(n);
		//testSystemOut();

			}
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node && !foundSwitchStatement) dispatch((Node)o);
		}
	} //end of visit method
	public void visitSwitchStatement(GNode n)
	{
		foundSwitchStatement=true;
	}
	
	public void visitCallExpression(GNode n)
	{
		print=n.getString(2);
		cppArguments cppargs = new cppArguments(n);
		arguments=cppargs.getArguments();
		if(DEBUG){System.out.println(n.getName());}
		if(!isSystemOutPrint)
		{
			cString.append(getSelectionExpression(n));
			
		}
	}//end of visitCallExpression method
	public StringBuilder getPrimaryIdentifier(GNode n)
	{
		cppPrimaryIdentifier prim = new cppPrimaryIdentifier(n);
		return prim.getString();
	}
	public String getExpressionName(GNode n)
	{
		return n.getString(2);
	}
	public StringBuilder getArguments(GNode n)
	{
		cppArguments arguments = new cppArguments(n);
		return arguments.getString();
	}
	public StringBuilder getSelectionExpression(GNode n)
	{
		cppSelectionExpression cppCall = new cppSelectionExpression(n);
		isOut=cppCall.isOut();
		isSystem=cppCall.isSystem();
		testSystemOut(n);
		return cppCall.getString();
	}
	/**
	 @return the SystemOutPrint boolean that returns if the SYstem.out.Prinln is in the classExpression
	 */
	public boolean isSystemOutPrint()
	{
		return isSystemOutPrint;
	}
	/** 
	 @return get the System.out.Print Command print or Println 
	 */
	public String getPrint()
	{
		return print;
	}
	/******
	 Test case that calls the SystemPrint class if The System Out Print values are true
	 */
	public void testSystemOut(GNode n)
	{
		if(isSystem){
			if(isOut){
				if(print.compareTo("println")==0){
					isSystemOutPrint=true;
					//call constructor for System.out.println
					if(DEBUG){System.out.println(arguments.getName());}
					SystemPrint printLn = new SystemPrint(arguments, true);
					if(DEBUG){System.out.println(printLn.getString());}
					cString.append(printLn.getString()+"//");
					
				}
				else if(print.compareTo("print")==0){
					isSystemOutPrint=true;
					if(DEBUG){System.out.println(arguments.getName());}
					//call constructor for System.out.print
					SystemPrint print = new SystemPrint(arguments, false);
					if(DEBUG){System.out.println(print.getString());}
					cString.append(print.getString()+ "//");
				}
				else {
					//something is wrong or a case test is missing
						System.out.println("Error: System.out but no Print or Println check values");
				}
				
			}
		}
		else {
			//run a check for a primaryIdentifier
			//String s;
			//String s=n.getString(0);
			Node node=n.getNode(0);
			if(node !=null) //primaryIdentifier node is not empty
			{
				cString.append(getPrimaryIdentifier(n) + "->__vptr->"+ getExpressionName(n)+"(");;
			}
			else{
				
				cString.append(getExpressionName(n)+"(");
			
			}
			//run a check for 
			
			
			
			//cString.append("Arguements:"+getArguments(n));
			String arguments =getArguments(n).toString();
			//System.out.println("Arguments:"+arguments);
		   if(arguments.equals(""))
			   {
				   cString.append("); \n");
				}
			else {
				//cString.append(","+getArguments(n)+");\n");
			}

		}

		
	}
	
	public StringBuilder getString()
	{
		return cString;
	}
	
}
/**class the visits the arguments subtree*/
class cppArguments extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder aString;
	private GNode arguments;
	cppArguments(GNode n)
	{
		aString=new StringBuilder();
		visit(n);
	}
	/**
	 @return the Arguments GNode
	 */
	public GNode getArguments()
	{
		return arguments;
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	public void visitArguments (GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
		arguments=n;
		aString.append(getLiteral(n));
		if(DEBUG){System.out.println(aString);}
	}
	public StringBuilder getLiteral(GNode n)
	{
		cppLiteral2 lit = new cppLiteral2(n);
		return lit.getString();
	}
	public StringBuilder getString()
	{
		return aString;
	}
}//end of cppArguments method
/**Class that visits and explores the SelectionExpression subtree*/
class cppSelectionExpression extends Visitor{
	public final boolean DEBUG = true;
	private StringBuilder sString;
	private boolean isOut;
	private boolean isSystem;
	private GNode arguments; 
	cppSelectionExpression(GNode n)
	{
		if(DEBUG){System.out.println("Selection Expression");}
		sString=new StringBuilder();
		isOut=false;
		isSystem=false;
		visit(n);
	}
	
	public void visitSelectionExpression(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(1));}
		String s = n.getString(1);
		if(s.compareTo("out")==0)
		{
			isOut=true;
		}
		sString.append(getLiteral(n)+".");
		sString.append(n.getString(1));
	}//end of visitSelectionExpression Method
	/**
	 returns the getArguments GNode
	 */
	public GNode getArguments()
	{
		if (arguments!=null)
		{
			return arguments;
		}
		else {
			System.out.println("ERROR!");
			return null;
		}
		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	public StringBuilder getLiteral(GNode n)
	{
		cppLiteral2 literal =new cppLiteral2(n);
		return literal.getString();
	}
	/**
	 creates a new cppPrimaryIdentifer class
	 */
	public StringBuilder getPrimaryIdentifer(GNode n)
	{
		cppPrimaryIdentifier PrimId= new cppPrimaryIdentifier(n);
		isSystem=PrimId.isSystem();
		return PrimId.getString();
	}
	/**
	 @return the isSystem Boolean for the System.out.print test case
	 */
	public boolean isSystem()
	{
		return isSystem;
	}
	/**
	 @return the isOut boolean for the System.out.print test case
	 */
    public boolean isOut()
	{
		return isOut;
	}
	/**
	 @return StringBuilder string for the entire class
	 */
	public StringBuilder getString()
	{
		return sString;
	}
}//end of cppCallExpression Class
/**a class that visits and explores the PRimaryIdentifier Node and subtree*/
class cppPrimaryIdentifier extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder pString;
	private boolean isSystem;
	private boolean foundSwitchStatement;
	cppPrimaryIdentifier(GNode n)
	{
		pString= new StringBuilder();
		foundSwitchStatement=false;
		isSystem=false;
		visit(n);
	}
	public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		String s=n.getString(0);
				if(s.compareTo("System")==0)
		{
			isSystem=true;
		}
		else {
			pString.append(n.getString(0));

		}

	}
	public void visitSwitchStatement(GNode n)
	{
		foundSwitchStatement=true;
	}	
	public void visit(Node n){
		if(DEBUG){System.out.println(n.getName());}
		for (Object o : n) if (o instanceof Node &&!foundSwitchStatement) dispatch((Node)o);	
	}
	/**
	 @return the entire PrimaryIdentifier StringBuilder
	 */
	public StringBuilder getString()
	{
		return pString;
	}
	/**
	 @return the isSystem boolean for the System.out.print test case
	 */
	public boolean isSystem()
	{
		return isSystem;
	}
}//end of cppPrimaryIdentifer class
/**class the visits and explores the FormalParameters subtree*/
class cppParameters extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder pString;	
	
	cppParameters(GNode n)
	{
		pString= new StringBuilder();
		visit(n);
	}//end cppParameters constructor
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	
	public void visitFormalParameters(GNode n) {
		if(DEBUG){System.out.println(n.getName());}
		if(n.size() > 0)
		{
			if(DEBUG){System.out.println("Greater Than Zero");}
			cppSubParameters subPar = new cppSubParameters(n);
			pString.append(subPar.getString());			
		}
	}//end of visitFormalParameters
	/**
	 @return StringBuilder class string
	 */
	public StringBuilder getString(){
		return pString;
	}//end getString method
}//end of cppParameters class
/**class the visits and explores the FormalParameters subclass*/
class cppSubParameters extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder pString;	
	
	cppSubParameters (GNode n)
	{
		pString=new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	
	public void visitFormalParameter(GNode n) {
		if(DEBUG){System.out.println(n.getName());}
		//Visit the Modifiers SubTree
		cppModifier mod= new cppModifier(n);
		pString.append(mod.getString()+ " ");
		//Visit the Type SubTree
		cppType type = new cppType(n);
		pString.append(type.getString()+ " ");
		// get the parameterName
		pString.append(getParameterName(n));
	}//end of visitClassDeclaration Method
	/**
	 @return the parameter name
	 */
	public String getParameterName(GNode n)
	{
		return n.getString(3);	
	}
	/**
	 @return StringBuilder for the cppSubParameter class
	 */
	public StringBuilder getString()
	{
		return pString;
	}//end of getString method
}//end of cppSubParameters class
/**class the visits and explores the FieldDeclaration subtree 
 put in a new ArrayWriter that writes the Array values*/
class cppFieldDeclaration extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder fieldString;
	private boolean isArray;
	private boolean isArray2;
	private boolean foundMethod; //for Fields outside of the methods
	private boolean foundConstructor; //for fields outside of constructors
	private boolean isClassDeclaration; //for creating new classes
	private StringBuilder modifier;
	private StringBuilder type;
	private StringBuilder declarator;
	/**Takes Block and sets the fields */
	cppFieldDeclaration(GNode n){
		isArray=false;
		isArray2=false;
		fieldString=new StringBuilder();
		modifier= new StringBuilder();
		type=new StringBuilder();
		declarator= new StringBuilder();
		foundMethod=false;
		foundConstructor=false;
		visit (n);
	}//end of cppField Constructor
	public void visitFieldDeclaration(GNode n) {
		//fix set modifier
		setModifier(n);
		setDeclarator(n);
		setType(n);
		if(isArray)
		{
			if(DEBUG){System.out.println("THIS IS AN ARRAY!!!");}
			//call ArrayWritere that writes the entire array here
			ArrayMaker arraym=new ArrayMaker(n);
			//System.out.println("ARRAY:"+n.getName());
			fieldString.append("\t\t"+arraym.getStringBuffer()+"\n");
			//fieldString.append("\t\t ARRAY GOES HEREEEEEEE;\n");
		}
		else if(isArray2)
		{
			if(DEBUG){System.out.println("THIS IS AN ARRAY!!!");}
			//call ArrayWritere that writes the entire array here
			//ArrayMaker arraym=new ArrayMaker(n);
			//System.out.println("ARRAY:"+n.getName());
		//	fieldString.append("\t\t"+arraym.getStringBuffer()+"\n");
			//fieldString.append("\t\t ARRAY GOES HEREEEEEEE;\n");		
		}
		else{//its not an array just write the text normally
			if(isClassDeclaration)
			{
				fieldString.append("\t\t"+modifier+" "+type+" "+declarator
						/*getNewClassExpression(n)+"("+*//*getArguments(n)*/
								   +"; \n");
			}
			else{//just write the text normally
				fieldString.append("\t\t"+modifier+" "+type+" "+declarator+"; \n");
			}
		}
		
		
		   
	}//end of visitClassDeclaration Method
	public void visitMethodDeclaration(GNode n)
	{
		foundMethod=true;
	}
	public void visitConstructorDeclaration(GNode n)
	{
		foundConstructor=true;
	}
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node && !foundMethod) dispatch((Node)o);
		}
	} //end of visit method
	/**
	 @return StringBuilder of class
	 */
	public StringBuilder getString()
	{
		return fieldString;
	}
	/**
	 checks to make sure the given GNode is a FieldDeclaration
	 */
	public boolean isFieldDeclaration(GNode n)
	{
		return true;
	}
	/**
	 @return FieldName String (Not Working)
	 */
	public String getFieldName(GNode n)
	{
		//inside Declarator
		return " ";
	}
	/**
	 @return StringBuilder Type of FieldDeclaration (makes a new cppType class)
	 */
	public void setType(GNode n)
	{
		//creates a new cppType class and gets the string
		cppType classType = new cppType(n);
		type= classType.getType();
	}
	/**
	 @return StringBuilder of the Modifier of the FieldDeclaration
	 */
	public void setModifier(GNode n)
	{
		cppModifier modifiers=new cppModifier(n);
		modifier= modifiers.getString();
		//first child of field Declaration
	}
	/**
	 @return StringBuilder declarator
	 creates a new cppDeclarator class
	 */
	public void setDeclarator(GNode n)
	{
		cppDeclarator decl= new cppDeclarator(n);
		isArray=decl.isArray();
		isClassDeclaration=decl.isClassDeclaration();
		declarator= decl.getString();
	}
}//end of cppFieldDeclaration Class
/**class that visits and explores the Declarator subtree*/
class cppDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;
	private boolean isArray;
	public boolean isClassDeclaration;
	cppDeclarator(GNode n)
	{
		isArray=false;
		declaratorString=new StringBuilder();
		declaratorString.append(getDeclarator(n));
		visit(n);
	}//end of cppDeclarator constructor
	
	public void visitDeclarators(GNode n) {
		//typeString.append(getPrimitiveType(n));
		if(DEBUG){System.out.println("Declarators");};
		
	}//end of visitClassDeclaration Method
	/**
	 @return StringBuilder that creates a new cppSubDeclarator class 
	 and returns the string
	 */
	public StringBuilder getDeclarator(GNode n)
	{
		cppSubDeclarator subDecl = new cppSubDeclarator(n);
		isArray=subDecl.isArray();
		isClassDeclaration=subDecl.isClassDeclaration();
		return subDecl.getString();
		
	}//end of setDeclarator method
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return StringBuilder class getString method
	 */
	public StringBuilder getString()
	{
		return declaratorString;
	}
	public boolean isClassDeclaration()
	{
		return isClassDeclaration;
	}
	public boolean isArray()
	{
		return isArray;
	}
}//end of cppDeclarator type
/**class the visits and explores the Declarator subtree*/
class cppSubDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;
	private boolean isArray;
	public boolean isClassDeclaration;
	private StringBuilder classEx;
	cppSubDeclarator(GNode n)
	{
		declaratorString = new StringBuilder();
		isArray=false;
		isClassDeclaration = false;
		classEx=new StringBuilder();
		visit(n);
	}
	public void visitDeclarator(GNode n) {
		declaratorString.append(n.getString(0));
		if(DEBUG){System.out.println("Declarator");};
		//StringBuilder classEx =new StringBuilder();
		classEx.append(getNewClassExpression(n));
		classEx.append(getCallExpression(n));
		cppArray cArray= new cppArray(n);
		isArray=cArray.isArray();
		//getLiteral(n);	
		getExpression(n);
	}//end of visitClassDeclaration Method
	public StringBuilder getCallExpression(GNode n)
	{
		cppCallExpression call = new cppCallExpression(n);
		return call.getString();
	}
	public StringBuilder getNewClassExpression(GNode n){
		cppNewClassExpression classExpress = new cppNewClassExpression(n);
		//isClassDeclaration=classExpress.isClassDeclaration();
		isClassDeclaration=true;
		return classExpress.getString();
	}
	public boolean isClassDeclaration()
	{
		return isClassDeclaration;
	}
	/**checks the node if an expression create a new cppExpression 
	 otherwise call the getLiteral Method*/
	public void getExpression(GNode n)
	{
		if(n.getNode(2)!=null)
		{
			Node sub=n.getNode(2);
			//System.out.println(sub.getName());
			if(DEBUG){System.out.println("size: "+sub.size());}
			if(sub.size()>1)
			{
				cppExpression cppEx = new cppExpression(n);
				if(DEBUG){System.out.println("Expression:"+cppEx.getString()+"\n");}
				declaratorString.append("= "+cppEx.getString());
				if(isClassDeclaration)
				{
					declaratorString.append(classEx);
				}			
			}
			else {
				getLiteral(n);			
			}
		}
	}
	/**
	 creates a new cppLiteral class and get the cppLiteral object string
	 */
	public void getLiteral(GNode n)
	{
		if(DEBUG){System.out.println("PRENODE:"+n.getName());}
		cppLiteral cppLit =new cppLiteral(n,1);
		declaratorString.append("= "+cppLit.getString());		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public boolean isArray()
	{
		return isArray;
	}
	/**
	 @return StringBuilder class
	 */
	public StringBuilder getString()
	{
		return declaratorString;
	}
	
}//end of cppSubDeclarator method
/** Class that visits and explores the NewClassExpression Subtree*/
class cppNewClassExpression extends Visitor{
	public final boolean DEBUG =false;
	private StringBuilder aString;
	public boolean isClassDeclaration;
	cppNewClassExpression (GNode n){
		isClassDeclaration =false;
		aString=new StringBuilder();
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitNewClassExpression(GNode n) {
		isClassDeclaration =true;
		if(DEBUG){System.out.println("Class Expression");}
		aString.append("new __"+getQualIden(n)+ "(" +getArguments(n)+")");
		//aString.append("NEW CLASS DECLARATION HEREEEEEE"/*getPrimitiveType(n)+ "="*/);
		//aString.append("new"+getArguments(n)+ ")");
	}//end of visitClassDeclaration Method
	public StringBuilder getPrimitiveType(GNode n){
		cppPrimitiveType type = new cppPrimitiveType(n);
		if(DEBUG){System.out.println(type.getString());}
		return type.getString();
	}
	public boolean isClassDeclaration()
	{
		return isClassDeclaration;
	}
	public StringBuilder getQualIden(GNode n)
	{
		cppQualifiedIde qual = new cppQualifiedIde(n);
		return qual.getString(); 
	}
	public StringBuilder getString()
	{
		return aString;
	}
	public StringBuilder getArguments(GNode n)
	{
		cppArguments arguments =new cppArguments(n);
		return arguments.getString();
	}
}
/**class the visits and explores the NewArrayExpression Subtree*/
class cppArray extends Visitor{
	public final boolean DEBUG =false;
	private boolean isArray; 
	private StringBuilder aString;
	
	cppArray (GNode n){
		aString=new StringBuilder();
		isArray=false;
		visit(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public void visitNewArrayExpression(GNode n) {
		if(DEBUG){System.out.println("ARRAY_ NEW!");}
		isArray=true;
		getPrimitiveType(n);
		getSize(n);
	}//end of visitClassDeclaration Method
	public StringBuilder getPrimitiveType(GNode n){
		cppPrimitiveType type = new cppPrimitiveType(n);
		if(DEBUG){System.out.println(type.getString());}
		return type.getString();
	}
	public boolean isArray()
	{
		return isArray;
	}
	public StringBuilder getSize(GNode n)
	{
		cppConcreteDimensions cDim=new cppConcreteDimensions(n);
		if(DEBUG){System.out.println(cDim.getSize(n));}
		return cDim.getSize(n);
	}
}
/**Class that visists and explores ContreteDimensions*/
class cppConcreteDimensions extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder cString;
	cppConcreteDimensions(GNode n)
	{
		cString= new StringBuilder();
		visit(n);
	}
	public void visitConcreteDimensions(GNode n)
	{
		getSize(n);
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	public StringBuilder getString()
	{
		return cString;
	}
	public StringBuilder getSize(GNode n)
	{
		cppLiteral cppLit = new cppLiteral(n);
		return cppLit.getString();
	}
}
/** class that visits and explores the Literal subtree.*/
class cppLiteral2 extends Visitor{
	public final boolean DEBUG =true;
	private StringBuilder lString;
	cppLiteral2(GNode n)
	{
		lString = new StringBuilder();
		visit(n);
	}
	/**
	 @return StringBuilder for class 
	 */
	public StringBuilder getString()
	{
		return lString;
	}//end of getString
	public void setlString(String l)
	{
	//	String str= lString.toString();
	//	if(str.equals("")) //if the string is just append to it
	//	{
			lString.append(l);
	//	}
	//	else { //else the string has another value on it append to it
			
	//		lString.append(","+l);
	//	}

	}
	/**
	 @return boolean that checks if the loop should break because of given counter and locaiton
	 */
	public void visitBooleanLiteral(GNode n)
	{
		//if(DEBUG){System.out.println(n.getString(0));}
		//foundLiteral=true;
		//count++;
		setlString(n.getString(0));		
		//if(checkBreak()){return;}
		//cppExpression cppEx = new cppExpression(n);
	}
	public void visitIntegerLiteral(GNode n)
	{
		lString.append(n.getString(0));
		//setlString(n.getString(0));	
	}
	public void visitStringLiteral(GNode n)
	{
		setlString(n.getString(0));	
	}	
	public void visitPrimaryIdentifier(GNode n)
	{
		setlString(n.getString(0));		
	}	
	/*public void visitAdditiveExpression(GNode n)
	{
		foundExpression=true;
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		foundExpression=true;	
	}	
	public void visitExpression(GNode n)
	{
		foundExpression=true;	
	}	
	public void visitRelationalExpression(GNode n)
	{
		foundExpression=true;	
	}	*/

	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node) dispatch((Node)o);
		}
		
	}
}//end of visit method	
	/*public void interface Visitor<R, E extends Throwable> {
	 
	 R visitLiteral(GNode n) throws E;
	 R visitAdditionExpression(GNode n) throws E;
	 R visitMultiplicativeExpression(GNode n) throws E;
	 
	 }*/	
	
	
	/*
	 For Declarations: 
	 -StringLiteral (literally ="thisString")
	 BooleanLiteral
	 -IntegerLiteral
	 -FloatingPointLiteral
	 -CharacterLiteral
	 NullLiteral
	 Literal?
	 MultiplicativeExpression -Even for Division (just changes sign in the middle)
	 IntergerLiteral(4) 
	 *
	 IntegerLiteral(3)
	 AdditiveExpression
	 IntegerLiteral(4)
	 +
	 IntergerLiteral(3)
	 AdditiveExpression - even for Subtraction (just chages sign in the middle
	 PrimaryIdentifier("additionTest")
	 +
	 PrimaryIdentifier("multiplicationTest")
	 AdditiveExpression
	 IntergerLiteral(12)
	 +
	 MultiplicativeExpression
	 IntegerLiteral(3)
	 /
	 IntegerLiteral(2)
	 AdditiveExpression
	 StringLiteral("te")
	 +
	 StringLiteral("xt")
	 
	 //create method that takes the GNode of the expression
	 //make a visitor for the literals (base)
	 //make a visitor for Expression
	 */
	
class cppLiteral extends Visitor{
	public final boolean DEBUG =false;
	private StringBuilder lString;
	private boolean foundLiteral;
	private int location;
	private int count;
	private String litString;
	private boolean countTest;
	private boolean foundExpression;
	/**
	 takes a Gnode and gets the first Literal 
	 */
	cppLiteral(GNode n)
	{
		foundLiteral=false;
		foundExpression =false;
		lString = new StringBuilder();
		//count=0;
		countTest=false;
		//location=1;
		visit(n);
	}//end of cppLiteral Constructor
	/*takes a value and get Literal GNode at the given positon
	 */
	cppLiteral(GNode n, int value)
	{
		foundLiteral=false;
		lString = new StringBuilder();
		location=value;
		countTest=true;
		count=0;
		visit(n);
	}//end of cppLiteral Constructor	
	/**
	 @return StringBuilder for class 
	 */
	public StringBuilder getString()
	{
		return lString;
	}//end of getString
	/**
	 @return boolean that checks if the loop should break because of given counter and locaiton
	 */
	public boolean checkBreak()
	{
		
		if(countTest){
			//if(DEBUG){System.out.println("Count: "+count);}
			//if(DEBUG){System.out.println("Location: "+location);}		
			if((foundLiteral) && (count==location))
			{
				//if(DEBUG){System.out.println("String: "+litString);}
				lString.append(litString);
				return true;
			}
			else {
				return false;
			}
			
		}
		else{
			//if(DEBUG){System.out.println("String: "+litString);}
			//lString.append(litString);
			return false;
		}
	}
	public void visitBooleanLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);		
		if(checkBreak()){return;}
		//cppExpression cppEx = new cppExpression(n);
	}
	public void visitIntegerLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
		//cppExpression cppEx = new cppExpression(n);
	}
	public void visitStringLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
		//cppExpression cppEx = new cppExpression(n);
	}	
	public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
	}	
	public void visitAdditiveExpression(GNode n)
	{
		foundExpression=true;
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		foundExpression=true;	
	}	
	public void visitExpression(GNode n)
	{
		foundExpression=true;	
	}	
	public void visitRelationalExpression(GNode n)
	{
		foundExpression=true;	
	}	
	
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node && !foundExpression) dispatch((Node)o);
		}
	}
}
/** class that visits and explores the Expression subtrees*/
class cppExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder eString;
	public boolean isArray;
	cppExpression(GNode n)
	{
		//if(DEBUG){System.out.println("IN cppEXPRESSION");}
		eString=new StringBuilder();
		isArray=false;
		visit(n);
	}
	cppExpression(GNode n, boolean arrayer)
	{
		eString=new StringBuilder();
		isArray=arrayer;
		visit(n);
	}
	public void visitExpression(GNode n)
	{
		cppSelectionExpression select =new cppSelectionExpression(n);
		eString.append(select.getString());
		eString.append(n.getString(1));
		eString.append(getLiteral(n,2));
	}
	/*public void visitNewArrayExpression(GNode n)
	{
		
	}*/
	public void setExpression(GNode n)
	{
		//if(DEBUG){System.out.println("+ Expression");};
		//cppLiteral cppLit =new cppLiteral(n);
		cppLiteral cppLit =new cppLiteral(n,1);	
		eString.append(cppLit.getString());
		
		if(DEBUG){System.out.println(cppLit.getString());};
		cppExpression cppExp=new cppExpression(n);
		eString.append(cppExp.getString());
		if(DEBUG){System.out.println(n.getString(1));}
		eString.append(n.getString(1));
		cppLiteral cppLit2 =new cppLiteral(n,2);
		eString.append(cppLit2.getString());
		
		if(DEBUG){System.out.println(cppLit2.getString());};
		cppExpression cppExp2=new cppExpression(n);	
		eString.append(cppExp2.getString());
	}
	public void visitSelectionExpression(GNode n)
	{
		cppPrimaryIdentifier id = new cppPrimaryIdentifier(n);
		eString.append(id.getString()+".");
		eString.append(n.getString(1)+" ");
	}
	public void visitAdditiveExpression(GNode n)
	{
		setExpression(n);
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		setExpression(n);	
		
	}
	/*public void visitExpression(GNode n)
	{
		if(DEBUG){System.out.println("PRIM");};
		eString.append("RAWR");
		//cppLiteral cppLit =new cppLiteral(n);
		//Note from patrick: I believe there should be
		//something like this here:
		//cppPrimaryIdentifier id= new cppPrimaryIdentifier(n);
		//eString.append("test"+id.getString());
		cppArray arraym = new cppArray(n);
		isArray=arraym.isArray();
		
		/*if (isArray) {
			eString.append(("ARRAY!"));
			ArrayMaker appArr = new ArrayMaker (n, true);
			cppPrimaryIdentifier ident = new cppPrimaryIdentifier(n);
			eString.append(ident.getString());
			eString.append(appArr.getStringBuffer());
		}
		else{
			cppLiteral cppLit =new cppLiteral(n);	
			eString.append(cppLit.getString());
			if(DEBUG){System.out.println(cppLit.getString());};
			cppExpression cppExp=new cppExpression(n);
			if(DEBUG){System.out.println(n.getString(1));}
			eString.append(n.getString(1));
			cppLiteral cppLit2 =new cppLiteral(n,2);
			eString.append(cppLit2.getString());
			if(DEBUG){System.out.println(cppLit2.getString());};
			cppExpression cppExp2=new cppExpression(n);	
			eString.append(cppExp2.getString());			
		//}
	}	*/
	public void visitRelationalExpression(GNode n)
	{
		setExpression(n);	
	}//end of visitCallExpression method
	
	public StringBuilder getLiteral(GNode n, int location)
	{
		cppLiteral lit =new cppLiteral(n, location);
		return lit.getString();
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	}	
	/**
	 @return StringBuilder class
	 */
	public StringBuilder getString()
	{
		return eString;
	}//end of getString Method
}//end of cppExpression Class
/**class that visits the Type subtree*/
class cppType extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder typeString;
	cppType(GNode n)
	{
		typeString=new StringBuilder();
		visit(n);
	}//end of cppType Constructior
	public void visitType(GNode n) {
		typeString.append(getPrimitiveType(n));
		typeString.append(getQualIden(n));		
		if(DEBUG){System.out.println("TYPEString!:"+ typeString);};
	}//end of visitClassDeclaration Method
	
	/**
	 @return the StringBuilder class String
	 */
	public StringBuilder getString()
	{
		return typeString;
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return PrimitiveType StringBuilder.
	 creates new cppPrimitiveType
	 */
	public StringBuilder getPrimitiveType(GNode n)
	{
		cppPrimitiveType pType = new cppPrimitiveType(n);
		return pType.getString();
	}
	/**
	 @return StringBuilder QualifiedIdentiy.
	 Creates new cppQualifiedIde class
	 */
	public StringBuilder getQualIden(GNode n)
	{
		cppQualifiedIde qi= new cppQualifiedIde(n);
		return qi.getString();
	}
	public StringBuilder getType()
	{
		return typeString;
	}
	
	/******** DO SOMETHING HERE FOR OTHER TYPES   *********/
}//end of cppType class
/**class that visits and explores QualifiedIdentifier Subtree*/
class cppQualifiedIde extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder qString;
	cppQualifiedIde(GNode n)
	{
		qString=new StringBuilder();
		visit(n);
	}
	public void visitQualifiedIdentifier(GNode n) {
		if(DEBUG){System.out.println("Q I");};
		//get the Qualified Identifier
		qString.append(n.getString(0));
		
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return StringBuilder class string
	 */
	public StringBuilder getString()
	{
		return qString;
	}
}//end of cppQualifiedIde class
/**class the visits and explores the PrimitiveType subtree*/
class cppPrimitiveType extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder primitiveTypeString;
	cppPrimitiveType(GNode n)
	{
		primitiveTypeString=new StringBuilder();
		visit(n);
		
	}//end of cppType Constructior
	public void visitPrimitiveType(GNode n) {
		if(DEBUG){System.out.println("Prim TYPE");};
		//get the primitiveType string
		primitiveTypeString.append(n.getString(0));
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return the Stringbuilder class String
	 */
	public StringBuilder getString()
	{
		return primitiveTypeString;
	}
}
/**class the visits and explores the Modifier class*/
class cppModifier extends Visitor{
	
	public final boolean DEBUG=false;
	private StringBuilder modifierString;
	
	cppModifier(GNode n)
	{
		if(DEBUG){System.out.println("IN CppModifier");}
		modifierString=new StringBuilder();
		visit(n);
	}//end of cppModifier Constructor
	public void visitModifiers(GNode n) {
		cppSubModifier submod=new cppSubModifier(n);
		modifierString.append(submod.getString());
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		//if(DEBUG){System.out.println(n.getName());}
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return Stringbuilder class string
	 */
	public StringBuilder getString()
	{
		return modifierString;
	}
	/*public void setModifierName(GNode n)
	 {	//StringBuilder modifiers = new StringBuilder();
	 if(DEBUG){System.out.println(n.getName());}
	 for(int i=0; i<n.size();i++)
	 {
	 modifierString.append(n.getString(i));
	 }
	 //return modifiers;
	 }*/
	
}//end of cppModifier class
/**Class that visit and explores the subModifier subtree*/
class cppSubModifier extends Visitor{
	
	public final boolean DEBUG=false;
	private StringBuilder subModifierString;
	
	cppSubModifier(GNode n)
	{
		//if(DEBUG){System.out.println("IN CppModifier");}
		subModifierString=new StringBuilder();
		visit(n);
	}//end of cppModifier Constructor
	public void visitModifier(GNode n) {
		setSubModifierName(n);
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		if(DEBUG){System.out.println(n.getName());}
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return StringBuilder class String
	 */
	public StringBuilder getString()
	{
		return subModifierString;
	}
	/** 
	 get Modifier names
	 **/
	public void setSubModifierName(GNode n){
		if(DEBUG){System.out.println(n.getName());}
		for(int i=0; i<n.size();i++)
		{
			String s=n.getString(i);
			if (s.equals("final")) {
				s="const";
			}
			subModifierString.append(" "+s);
		}
	}
}//end of cppsubModifier class