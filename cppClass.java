
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

*/


//throw class callcheck for System.out.println - Pat's going to finish that class
/*
 For Liz:
	-wants to pass you a method Declaration node
		-wants to return in stringtranslated form all of the contents of the method
		-already hands all the declaration and parameters
	-*/

/**
 Takes a classDeclaration GNode and  generates the basic class values
 */
public class cppClass extends Visitor{ 
	
	public final boolean DEBUG = true;
	private StringBuilder classString = new StringBuilder();
	/**
	 Constructor takes an Compilation Unit and searches all ClassDeclarations in the AST
	 */
	cppClass(Node n)
	{
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
	
	/**
	 Checks to make sure the given GNode is actually an instance of ClassDeclaration
	 **Currently just returns true
	 */
	public boolean isClassDeclaration(GNode n)
	{
		return true;
	}//end of isClassDeclaration method
	
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

/**
 Creates a Class that searches through the MethodDeclaration Nodes in a subtree
 *Also takes a MethodDeclaration Class
 */
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
	
	public void getMethodDetails(GNode n)
	{
		StringBuilder fields= setFields(n);
		methodString.append(fields);
		methodString.append("\t \t" +getExpressionStatements(n)+"\n");
		methodString.append("\t} \n");		
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
	 */	public StringBuilder getString()
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
//put in code for Constructors
/**
 creates an class that explored the CallExperssion subtree
 */
class cppExpressionStatement extends Visitor{
	
	public final boolean DEBUG = true;
	private StringBuilder pString;	
	private boolean isOut;
	private String print;
	private boolean isSystem;
	private boolean isSystemOutPrint; //checks the system.out.print case
	private StringBuilder SystemOut; //System.out Text
	private GNode arguments;
	/**
	 
	 */
	cppExpressionStatement(GNode n)
	{
		pString=new StringBuilder();
		isOut=false;
		isSystem=false;
		isSystemOutPrint=false;
		arguments=null;
		visit(n);
		
	}
	public void visitCallExpression(GNode n)
	{
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
		
		//testSystemOut();
	}//end of visitCallExpression method
	/***
	 method that creates a new cppSelectionExpression class that checks the isOut and isSystem cases
	 */
	public void getSelectionExpression(GNode n)
	{
		cppSelectionExpression cppCall = new cppSelectionExpression(n);
		isOut=cppCall.isOut();
		isSystem=cppCall.isSystem();
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	/**
	 @return the classExpression String
	 */
	public StringBuilder getString()
	{
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
/**
 class the visits the arguments subtree
 */
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
	}
}//end of cppArguments method

/**
 Class that visits and explores the SelectionExpression subtree
 */
class cppSelectionExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder sString;
	private boolean isOut;
	private boolean isSystem;
	private GNode arguments; 
	cppSelectionExpression(GNode n)
	{
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
		getPrimaryIdentifer(n);
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
	/**
	 creates a new cppPrimaryIdentifer class
	 */
	public void getPrimaryIdentifer(GNode n)
	{
		cppPrimaryIdentifier PrimId= new cppPrimaryIdentifier(n);
		isSystem=PrimId.isSystem();
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

/**
 a class that visits and explores the PRimaryIdentifier Node and subtree
 */
class cppPrimaryIdentifier extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder pString;
	private boolean isSystem;
	cppPrimaryIdentifier(GNode n)
	{
		pString= new StringBuilder();
		isSystem=false;
		visit(n);
	}
	public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		String s=n.getString(0);
		pString.append(n.getString(0));
		if(s.compareTo("System")==0)
		{
			isSystem=true;
		}
	}
	public void visit(Node n){
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);	
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
/**
 class the visits and explores the FormalParameters subtree
 */
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

/**
 class the visits and explores the FormalParameters subclass
 */
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

/**
 class the visits and explores the FieldDeclaration subtree 
 */

//put in check for array expression
class cppFieldDeclaration extends Visitor{
	
	public final boolean DEBUG = true;
	private StringBuilder fieldString;
	
	/**Takes Block and sets the fields */
	cppFieldDeclaration(GNode n){
		fieldString=new StringBuilder();
		visit (n);
	}//end of cppField Constructor
	public void visitFieldDeclaration(GNode n) {
		//fix set modifier
		fieldString.append("\t\t"+setModifier(n)+" "+setType(n)+" "+setDeclarator(n)+"; \n");
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
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
	public StringBuilder setType(GNode n)
	{
		//creates a new cppType class and gets the string
		cppType classType = new cppType(n);
		return classType.getType();
	}
	/**
	 @return StringBuilder of the Modifier of the FieldDeclaration
	 */
	public StringBuilder setModifier(GNode n)
	{
		cppModifier modifiers=new cppModifier(n);
		return modifiers.getString();
		//first child of field Declaration
	}
	/**
	 @return StringBuilder declarator
	 creates a new cppDeclarator class
	 */
	public StringBuilder setDeclarator(GNode n)
	{
		cppDeclarator decl= new cppDeclarator(n);
		return decl.getString();
	}
	
}//end of cppFieldDeclaration Class

/**
 class that visits and explores the Declarator subtree
 */

//put in check for NewArray Expression in subtype
class cppDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;
	
	cppDeclarator(GNode n)
	{
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
}//end of cppDeclarator type
/**
 class the visits and explores the Declarator subtree
 */

//put in check for NewArrayExpression subtree in DEclarator tree
class cppSubDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;
	
	cppSubDeclarator(GNode n)
	{
		declaratorString = new StringBuilder();
		visit(n);
	}
	public void visitDeclarator(GNode n) {
		declaratorString.append(n.getString(0));
		if(DEBUG){System.out.println("Declarator");};
		cppArray cArray= new cppArray(n);
		//getLiteral(n);	
		getExpression(n);
	}//end of visitClassDeclaration Method
	/**
	 checks the node if an expression create a new cppExpression 
	 otherwise call the getLiteral Method
	 */
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
		cppLiteral cppLit =new cppLiteral(n);
		declaratorString.append("= "+cppLit.getString());		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/**
	 @return StringBuilder class
	 */
	public StringBuilder getString()
	{
		return declaratorString;
	}
	
}//end of cppSubDeclarator method
/**
 class the visits and explores the NewArrayExpression Subtree
 */
class cppArray extends Visitor{
	public final boolean DEBUG =true;
	public boolean isArray; 
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
	public StringBuilder getSize(GNode n)
	{
		cppConcreteDimensions cDim=new cppConcreteDimensions(n);
		if(DEBUG){System.out.println(cDim.getSize(n));}
		return cDim.getSize(n);
	}
}

/**
 Class that visists and explores ContreteDimensions
 */
class cppConcreteDimensions extends Visitor{
	public final boolean DEBUG=true;
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
/**
 class that visits and explores the Literal subtree.
 */
class cppLiteral extends Visitor{
	public final boolean DEBUG =false;
	private StringBuilder lString;
	private boolean foundLiteral;
	private int location;
	private int count;
	private String litString;
	/**
	 takes a Gnode and gets the first Literal 
	 */
	cppLiteral(GNode n)
	{
		foundLiteral=false;
		lString = new StringBuilder();
		count=0;
		location=1;
		visit(n);
	}//end of cppLiteral Constructor
	/**
	 takes a value and get Literal GNode at the given positon
	 */
	cppLiteral(GNode n, int value)
	{
		foundLiteral=false;
		lString = new StringBuilder();
		location=value;
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
		if(DEBUG){System.out.println("Count: "+count);}
		if(DEBUG){System.out.println("Location: "+location);}		
		if((foundLiteral) && (count==location))
		{
			if(DEBUG){System.out.println("String: "+litString);}
			lString.append(litString);
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 visit BooleanLiteral
	 */
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
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node) dispatch((Node)o);
		}
		
	} //end of visit method	
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
	
}//end of cppLiteral class
/**
 class that visits and explores the Expression subtrees
 */
class cppExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder eString;
	cppExpression(GNode n)
	{
		eString=new StringBuilder();
		visit(n);
	}
	public void visitAdditiveExpression(GNode n)
	{
		//if(DEBUG){System.out.println("+ Expression");};
		//cppLiteral cppLit =new cppLiteral(n);
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
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		//if(DEBUG){System.out.println("/ Expression");};
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
/**
 class that visits the Type subtree
 */
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
/**
 class that visits and explores QualifiedIdentifier Subtree
 */
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
/**
 class the visits and explores the PrimitiveType subtree
 */
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
/**
 class the visits and explores the Modifier class
 */
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
/***NOTTTTTT WORKINGGGGGGGG :( ****/
/**
 Class that visit and explores the subModifier subtree
 */
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
			subModifierString.append(n.getString(i));
		}
	}
}//end of cppsubModifier class