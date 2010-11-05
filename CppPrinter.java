
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

SynchronizedStatement

AssetStatement

 
 ConditionalExpression (? Expression : ConditionalExpression ()) ????


 CastExpression ??? 
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
	public void visitLabeledStatement(GNode n)
	{
		printer.append(n.getString(0)+":\n");
		for(int i=1;i<n.size();i++)
		{
			Object o=n.get(i);
			if(o instanceof String)
			{
			}
			else if(o instanceof Node)
			{
				dispatch((Node) o);
			}
		}	
	}
	public void visitNewClassExpression(GNode n)
	{
		printer.append("new ");
		for(int i=0;i<n.size();i++)
		{
			if (i==3) {
				printer.append("(");
			}
			Object o=n.get(i);
			if(o instanceof String)
			{
				printer.append(" "+(String)o);
			}
			else if(o instanceof Node)
			{
				dispatch((Node) o);
			}
			if (i==3) {
				printer.append(")");
			}			
		}
	}
	public void visitArguments(GNode n)
	{
		for (int i=0; i<n.size(); i++) {
			
			dispatch(n.getNode(i));
			if(i!=(n.size()-1))
			{
				printer.append(", ");
			}
		}
	}
	public void visitFormalParameter(GNode n)
	{
		for(int i=0;i<n.size();i++)
		{
			Object o=n.get(i);
			if(o instanceof String)
			{
				printer.append(" "+(String)o);
			}
			else if(o instanceof Node)
			{
				dispatch((Node) o);
			}
		}
	}
	public void visitConditionalExpression(GNode n)
	{
		printer.append("CONDITIOANL");
	}
	public void visitUnaryExpression(GNode n)
	{
		setUnary(n);
	}
	public void visitUnaryExpressionNotPlusMinus(GNode n)
	{
		setUnary(n);
	}
	public void visitPreDecrementExpression(GNode n)
	{
		setUnary(n);
	}
	public void visitPreIncrementExpression(GNode n)
	{
		setUnary(n);
	}
	public void visitPostfixExpression(GNode n)
	{
		setUnary(n);
	}
	public void visitShiftExpression(GNode n)
	{
		setUnary(n);
	}
	public void visitConditionalOrExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitConditionalAndExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitInclusiveOrExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitExculsiveOrExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitAndExpression(GNode n)
	{
		setBinary(n);
	}
	public void visitTryCatchFinallyStatement(GNode n)
	{
		
		printer.append("try {\n");
		Node block = n.getNode(0);
		dispatch(block);
		printer.append("} ");
		for (int i=1; i<n.size(); i++) {
			Node catch1 = n.getNode(i);
			dispatch(catch1);
		}
		
	}
	public void visitThrowStatement(GNode n)
	{
		printer.append("throw ");
		visit(n);
		printer.append(";\n}\n");
	}
	public void visitCatchClause(GNode n)
	{
		printer.append("catch(");
		Node io = n.getNode(0);
		dispatch(io);
		printer.append("){\n");
		Node block = n.getNode(1);
		dispatch(block);
	}
	public void visitReturnStatement(GNode n)
	{
		printer.append("return ");
		visit(n);
		printer.append("; \n");
	}
	public void visitExpression(Node n)
	{
		Node b=n.getNode(3);
		dispatch(b);
		//visit(b);
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
		printer.append("(");
		visitForInit(a);
		printer.append(";");
		visitExpression(a);
		printer.append(";");
		visitForUpdate(a);
		printer.append(")");
	}
	public void visitForStatement(GNode n)
	{
		printer.append("for");
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
	public void visitConditionalStatement(GNode n)
	{
		printer.append("if");
		Node express=n.getNode(0);
		dispatch(express);
		//printer.append("{\n");
		Node block = n.getNode(1);
		dispatch(block);
		//printer.append("}\n");
		
	}
	public void visitCallExpression(GNode n)
	{
		printer.append("CALLEXPRESSION");
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
		printer.append("break");
		setBreCon(n);
	}
	public void visitContinueStatement(GNode n)
	{
		printer.append("continue");
		setBreCon(n);
	}
	public void visitDoWhileStatement(GNode n)
	{
		printer.append("do{\n");
		Node block= n.getNode(0);
		dispatch(block);
		printer.append("}while(");
		Node expression= n.getNode(1);
		dispatch(expression);
		printer.append(");\n");
	}
	public void visitWhileStatement(GNode n)
	{
		printer.append("\n while");
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
		printer.append(";\n");
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
		printer.append("");
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
	public void setBreCon(GNode n)
	{
		for (int i=0; i<n.size(); i++) {
			Object o=n.get(i);
			if (o!=null) {
				//printer.append("(");
			}
			if(o instanceof String)
			{
				printer.append(" "+(String)o);
			}
			else if(o instanceof Node)
			{
				dispatch((Node) o);
			}
			if (o!=null) {
				if(i!=(n.size()-1)){
					//	printer.append(",");
				}
				else{
					//	printer.append(")");
				}				   
			}
		}
		printer.append(";\n");
	}
	public void setBinary(GNode n)
	{
		Node q= n.getNode(0);
		dispatch(q);
		printer.append(n.getString(1));		
		Node r= n.getNode(2);
		dispatch(r);	
	}
	public void setUnary(GNode n)
	{
		for(int i=0;i<n.size();i++)
		{
			Object k=n.get(i);
			if(k instanceof Node)
			{
				dispatch((Node)k);
			}
			else {
					if(n.getString(i)!=null)
					{		////add the string to the printer
						printer.append(n.getString(i));
					}
			}
		}
	}
	public StringBuilder getString()
	{
		return printer;
	}	
}

