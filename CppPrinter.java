
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
/*To Include:

o.name="cat"
 ifStatement

ContinueStatement
ThrowStatement
SynchronizedStatement
TryStatement
AssetStatement

 
 ConditionalExpression (? Expression : ConditionalExpression ()) ????
 ConditionalOrExpression -Binary
 CondtionalAndExpression -Binary
 IncliudisveOrExpression -Binary
 ExclusiveOrExpression -Binary
 AndExpression -Binary
 EqualityExpression -Binary
 ShiftExpression -Unary?
 UnaryExpression -Unary
 PreIncrementExpression -Unary
 PreDecrementExpresison -Unary
 UnaryExpressionNotPlusMinus -Unary
 PostFixExpression -Unary
 CastExpression ??? -Unary
 PrimaryExpression ??
 
 AllocationExpression ???
 StatementExpression??
 */
public class CppPrinter extends Visitor{
	private StringBuilder printer;
	public final boolean DEBUG = false;
	public CppPrinter(GNode n)
	{
		printer = new StringBuilder();
		visit(n);
		if(DEBUG){System.out.println(printer);}
	}
	public void visitReturnStatement(GNode n)
	{
		printer.append("return ");
		visit(n);
		printer.append("; \n");
	}
	public void visitBasicForControl(GNode n)
	{
		//basic control node
		Node a=n.getNode(0);
		printer.append("(");
		visitForInit(a);
		visitExpression(a);
		printer.append(";");
		visitForUpdate(a);
		printer.append(")");
	}
	public void visitForStatement(GNode n)
	{
		printer.append("For");
		visitBasicForControl(n);
		printer.append("{\n");
		Node f= n.getNode(1);
		visit(f);
		printer.append("}\n");	
	}
	public void visitStatement(GNode n)
	{
		//dispatch(n);
	}
	public void visitExpressionStatement(GNode n)
	{
		visit(n);
		printer.append(";\n");
	}
	public void visitExpression(Node n)
	{
		Node b=n.getNode(3);
		dispatch(b);
		//visit(b);
	}
	public void visitAdditiveExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitSwitchStatement(GNode n)
	{
		printer.append("switch(");
		Node h= n.getNode(0);
		dispatch(h);
		printer.append(")\n");
		printer.append("{\n");
		for(int i=1; i<(n.size());i++)
		{
			Node cases= n.getNode(i);
			dispatch(cases);
		}
		printer.append("}\n");
	}
	public void visitCaseClause(GNode n)
	{
		printer.append("case");
		Node theCase= n.getNode(0);
		dispatch(theCase);
		printer.append(": ");
		Node theExp= n.getNode(1);
		dispatch(theExp);	
		Node break1= n.getNode(2);
		dispatch(break1);	
	}
	public void visitDefaultClause(GNode n)
	{
		printer.append("default");
		printer.append(": ");
		Node theExp= n.getNode(0);
		dispatch(theExp);	
		Node break1= n.getNode(1);
		dispatch(break1);	
		
	}
	public void visitBreakStatement(GNode n)
	{
		printer.append("break;\n");
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
	public void visitForUpdate(Node n)
	{
		Node d= n.getNode(4);
		dispatch(d);
	}
	
	public void visitDoWhileStatement(GNode n)
	{
		printer.append("Do{\n");
		Node block= n.getNode(0);
		dispatch(block);
		printer.append("}while(");
		Node expression= n.getNode(1);
		dispatch(expression);
		printer.append(");\n");
	}
	public void visitWhileStatement(GNode n)
	{
		printer.append("\nWhile");
		visit(n);
		printer.append("} \n");
	}
	public void visitRelationalExpression(GNode n)
	{
		setBinary(n);		
	}
	public void visitEqualityExpression(GNode n)
	{
		printer.append("(");		
		setBinary(n);		
		printer.append("){ \n");
	}//end of visitCallExpression method
	public void visitExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitFieldDeclaration(GNode n)
	{
		visit(n);
		printer.append("\n");
	}
	public void visitModifier(GNode n)
	{
	}
	public void visitDeclarator(GNode n)
	{
		printer.append(" " +n.getString(0));
		if(n.get(1)!=null)
		{
			printer.append(n.getString(1));
		}
		if(n.get(2)!=null)
		{
			printer.append(" = ");
			Node newNode= n.getNode(2);
			dispatch(newNode);
		}
		printer.append(";");
	}
	public void visit(Node n)
	{
		for (int i=0; i<n.size(); i++) {
			Object k=n.get(i);
			if(k instanceof Node)
			{
			}
			else {
				if(!((n.getName().equals("MethodDeclaration"))||(n.getName().equals("ConstructorDeclaration"))))
				{
					if(n.getString(i)!=null)
					{		////add the string to the printer
						printer.append(n.getString(i));
					}
				}
			}
		}
		for(Object o:n) {
			if(o instanceof Node) dispatch((Node) o);
		}
	}	
	public void setBinary(GNode n)
	{
		Node q= n.getNode(0);
		dispatch(q);
		printer.append(n.getString(1));		
		Node r= n.getNode(2);
		dispatch(r);	
	}
	public StringBuilder getString()
	{
		return printer;
	}	
}
/*class MainPrinter extends CppPrinter{
//	public MainPrinter(GNode n)
//	{
		//visit(n);
		
		//if debug mode is one print the printer string
	//	if(DEBUG){System.out.println(printer);}
//	}
	public void visit(Node n)
	{
		for (int i=0; i<n.size(); i++) {
			Object k=n.get(i);
			if(k instanceof Node)
			{
			}
			else {
				if(n.getString(i)!=null)
				{		////add the string to the printer
					//printer.append(n.getString(i));
				}
			}
		}
		for(Object o:n) {
			if(o instanceof Node) dispatch((Node) o);
			//else{
			//System.out.println(n.getName());
			//}
		}
	}
}*/
/*Definitions 
 -Unary ((OPERATION) VALUE)
 -Binary (VALUE (OPERATION) VALUE)
 -
 */

/*LinkedList<String> operand;
 LinkedList<String> operator;
 boolean makeStack=false;*/

/*Override the visit method to correctly visit the Binary and Unary expression and print them*/

//OLD CODE HERE
//when you hit an expression create two stacks (1 Operand Stack and
//1 Operator Stack. When you reach the end of that sub tree (after visit(n) 
//put code to pop and push off the stack until they're empty
//System.out.print("(");

/*makeStack=true;
 operand= new LinkedList<String>();
 operator= new LinkedList<String>();
 if (n.get(0)!=null) {
 Node q= n.getNode(0);
 //System.out.print(q.getName());
 visit(q);
 }
 operator.add(n.getString(1));//	System.out.print(n.getString(1));
 if(n.get(2)!=null)
 {
 Node r= n.getNode(2);
 //System.out.print(r.getName());
 visit(r);
 }
 //	System.out.print("; \n");
 //	visit(n);
 
 Iterator iter = operator.iterator();
 Iterator operIter=operand.iterator();
 System.out.print(operand);
 System.out.print(operator);*/
//pop and print the stack
/*	while (iter.hasNext()) {
 System.out.print(operIter+ " ");
 operIter.next();
 System.out.print(""+iter);
 iter.next();
 System.out.print(""+operIter);
 operIter.next();
 }*/
//System.out.print("; \n");
//	makeStack=false;


/*public void visitAdditiveExpression(GNode n)
 {
 
 if (n.get(0)!=null) {
 Node q= n.getNode(0);
 //System.out.print(q.getName());
 visit(q);
 }
 operator.add(n.getString(1));//	System.out.print(n.getString(1));
 if(n.get(2)!=null)
 {
 Node r= n.getNode(2);
 //System.out.print(r.getName());
 visit(r);
 }
 //	System.out.print("; \n");
 //visit(n);
 
 
 
 }*/

/*

 */
