
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
import java.util.ArrayList;
public class CppWalker extends Visitor
{
	private StringBuilder printer;
	public final boolean DEBUG = false;
	public String currentClass;
	public InheritanceTree tree;
	public Declaration method;
	public CppWalker(GNode n, InheritanceTree givenTree, Declaration givenMethod)
	{
		tree=givenTree;
		method=givenMethod;
		printer = new StringBuilder();
		visit(n);
		
		if(DEBUG){System.out.println(printer);}
	}
	public void visitExpression(GNode n)
	{
		Node primary = n.getNode(0);
		String instanceName=primary.getString(0);
		
		Node castex = n.getNode(2);
		if(castex.getName().equals("CastExpression"))
		{
			visitCastExpression(castex,instanceName);
		}
	}
	public void visitCastExpression(Node n, String name)
	{
		//update the inheritence tree
		Node qual = n.getNode(0);
		String type =qual.getString(0);
		
		//call the intheritiecne tree method
		method.update_type(name,type);
	}
	//get the current class name
	/***********************Classes ******************************/
	public void visitCallExpression(GNode n)
	{
		//get the instance name
		Node primary =n.getNode(0);
		String instanceName=primary.getString(0);
		//get the method name
		String methodName = n.getString(2);
		printer.append(methodName +"\n");
		Node arguments=n.getNode(3);
		//get the type for every argument in the argument Node
		ArrayList<String> argumentList = new ArrayList<String>();
		printer.append("New Array List Created...\n");
		for(int i=0;i<arguments.size();i++)
		{
			printer.append(getType(arguments.getNode(i))+"\n");
			argumentList.add(getType(arguments.getNode(i)));
		}
	}
	public void visitCastExpression(GNode n)
	{
		//the type for a value has changed update the Inheritence Tree
		//get the instance name
		//get the new type
			visit(n);
	}
	//returns the types of the nodes inside the arguments list
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
	public void visit(Node n)
	{
		for(Object o:n) {
			if(o instanceof Node) dispatch((Node) o);
		}
	}	
	public StringBuilder getString()
	{
		return printer;
	}	
}

