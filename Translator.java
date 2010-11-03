/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
 * Edited by Patrick Hammer
<<<<<<< HEAD
 * Test Edited by Paige
=======
>>>>>>> 5da2b25a04c1f9340af211ca5b1ab8296bcb9d2b
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

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import java.util.*;

import xtc.lang.JavaFiveParser;

import xtc.parser.ParseException;
import xtc.parser.Result;
import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 *
 * @author P.Hammer
 * @author A.Krebs
 * @author L. Pelka
 * @author P.Ponzeka
 * @version 1
 */
public class Translator extends Tool {
	public final boolean DEBUG=true;

	File inputFile = null;

	/** Create a new translator. */
	public Translator() {
		// Nothing to do.
	}

	public String getCopy() {
		return "(C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka";
	}

	public String getName() {
		return "Java to C++ Translator";
	}

	public String getExplanation() {
		return "This tool translates a subset of Javat to a subset of C++.";
	}

	public void init() {
		super.init();
		
		// Declare command line arguments.
		runtime.
			bool("printJavaAST", "printJavaAST", false,
				 "Print the Java AST.").
			bool("countMethods", "optionCountMethods", false,
				 "Print the number of method declarations.").
			bool("translate", "translate", false,
				 "Translate .java file to c++.").
			bool("file", "file", false,
				 "Output to a file").
			bool("testing","testing",false,"Run some Test cases.");
	}

	public void prepare() {
		super.prepare();

		// Perform consistency checks on command line arguments.
	}

	public File locate(String name) throws IOException {
		File file = super.locate(name);
		if (Integer.MAX_VALUE < file.length()) {
			throw new IllegalArgumentException(file + ": file too large");
		}
		inputFile = file;
		//System.out.println("using this method");
		return file;
	}

	public Node parse(Reader in, File file) throws IOException, ParseException {
		JavaFiveParser parser =
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}

	//-----------------------------------------------------------------------
	public void process(Node node) {
		
		
		//Some Testing Environments
		if(runtime.test("testing"))
		{
			runtime.console().p("Testing...").pln().flush();
			
			/*Create a new visitor to visit the CompilationUnit */
			
			
			//runtime.console().format(node).pln().flush();
			
			new Visitor(){
				LinkedList<String> operand;
				LinkedList<String> operator;
				boolean makeStack=false;
				public void visitMethodDeclaration(GNode n){
				
					//System.out.println(n.getTokenText());
				   //create an interator over all the children
					/*System.out.println(n.getName());
					Iterator nChildren = n.iterator();
					while(nChildren.hasNext())
					{
						Object o= nChildren;
						System.out.println(o.toString());
						if (o==null) {
							System.out.println("NULL");
							nChildren=(Iterator)nChildren.next();
							o= nChildren;
						}
						if (nChildren!=null) {
						
							
							System.out.println("NOOO");
							if(o instanceof GNode)//SOURCE OF NULL POINTER EXCEPTION
							{
								
								System.out.println("GNODE!");
							}
						}
						
						nChildren=(Iterator)nChildren.next();
						
					}
					visit(n);*/
					//System.out.println(n.size());
					//System.out.println("Before Visit:" +n.getName());
					visit(n);
				}
				public void visitWhileStatement(GNode n)
				{
					System.out.print("\nWhile");
					visit(n);
					System.out.print("} \n");
				}
		
				public void visitEqualityExpression(GNode n)
				{
					System.out.print("(");
					if (n.get(0)!=null) {
						Node q= n.getNode(0);
						visit(q);
					}
					System.out.print(n.getString(1));
					if(n.get(2)!=null)
					{
						Node r= n.getNode(2);
						visit(r);
					}
					System.out.print("){ \n");
					//visit(n);
				}
			public void visitExpression(GNode n)
				{
					//when you hit an expression create two stacks (1 Operand Stack and
					//1 Operator Stack. When you reach the end of that sub tree (after visit(n) 
					//put code to pop and push off the stack until they're empty
					//System.out.print("(");
					
					makeStack=true;
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
					System.out.print(operator);
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
					makeStack=false;
				}
				public void visitAdditiveExpression(GNode n)
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
			
					
				
				}
				public void visitFieldDeclaration(GNode n)
				{
					visit(n);
					System.out.print(";\n");
				}
				public void visitDeclarator(GNode n)
				{
					
					//System.out.print("DECLARATOR SIZE:"+n.size());
					System.out.print(" " +n.getString(0));
					//System.out.print(n.getString(1));
					//System.out.print(" = ");
					if(n.get(1)!=null)
					{
						System.out.print(n.getString(1));
					}
					if(n.get(2)!=null)
					{
						System.out.print(" = ");
						Node newNode= n.getNode(2);
						visit(newNode);
					}
					
					
					
				}
			
				public void visit(Node n)
				{
					//get the size of the Node (aka find all the children
					//inn the visit methodget the object of each if its a node call visit(n)
					//System.out.println(n.size());
					for (int i=0; i<n.size(); i++) {
						Object k=n.get(i);
						if(k instanceof Node)
						{
						}
						else {
							if(n.getString(i)!=null)
							{
								if (makeStack) {
									operand.add(n.getString(i));
								}
								else {
									System.out.print(" " +n.getString(i));
								}

								
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
			}.dispatch(node);
		//	}.dispatch(node);
		//	xtc.lang.jeannie.AstSimplifier simple =new xtc.lang.jeannie.AstSimplifier("Java");
		//	xtc.lang.jeannie.CodeGenerator generator = new xtc.lang.jeannie.CodeGenerator();
			/*{
				//new Visitor() {
					//int count = 0;
					
					public Node visitCompilationUnit(GNode n) {
						System.out.println(n.getName());
						runtime.console().format(n).pln().flush();
						return n;
					}
				//	public void visit(Node n) {
				//		System.out.println(n.getName());
				//		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				//	}
					
			};*///.dispatch(node);
		//Node simpleNode =visitCompilationUnit(node);
			
		}
			
		
		
		// Handle the translate option
		if (runtime.test("translate")) {
			runtime.console().p("translating...").pln().flush();
			
			
			
			DependencyTree dependency= new DependencyTree(node);
			//creates tree root a.k.a. the Object class
			final InheritanceTree Object = new InheritanceTree();
			
			//creates the Class class as subclass of Object class
			final InheritanceTree Class = new InheritanceTree(Object);
			
			
			final InheritanceBuilder inherit = new InheritanceBuilder(inputFile,dependency.getFileDependencies());
				/******** cppMethod cprint = new cppMethod(/*methoddec NODE)*/
			final ArrayList<GNode> ToTree = new ArrayList<GNode>(0);
			
			new Visitor() {
				
				InheritanceTree supr;
				
				public void visitCompilationUnit(GNode n){
					//Paiges testing class
					cppClass classtester=new cppClass(n);
					visit(n);
				}
				
				public void visitClassDeclaration(GNode n){
					//if no extenstion it's superclass is Object
					supr=Object;
					visit(n);
					
					//if the super class has been defined make the subclass
					if(supr!=null){
						inherit.addClassdef((new InheritanceTree(n,supr)));
					}
					else ToTree.add(n);
					
				}
				public void visitExtention(GNode n){
					//find's super class
					//searches for InheritanceTree with same name as extention
					//returns null if no tree exists yet
					supr = Object.search(n.getNode(0).getNode(0).getString(0));
				}
				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}
				
			}.dispatch(node); //end of main dispatch
			
			//creates the rest of the tree all nodes whose super exists until all 
			//trees created
			InheritanceTree supr;
			while(!ToTree.isEmpty()){
				for(int i=0;i<ToTree.size();i++){
					supr = Object.search(ToTree.get(i).getNode(3)
									 .getNode(0).getNode(0).getString(0));
					if(supr!=null){
						inherit.addClassdef((new InheritanceTree(ToTree.get(i),supr)));
						ToTree.remove(i);
					}
				}
			}
				
			
			inherit.close(); // when all nodes are visited and inheritance files are made close files

		}//end of runtime.test("Translate") test
		//-----------------------------------------------------------------------

		if (runtime.test("printJavaAST")) {
			runtime.console().format(node).pln().flush();
		}

		if (runtime.test("optionCountMethods")) {
			new Visitor() {
				int count = 0;

				public void visitCompilationUnit(GNode n) {
					visit(n);
					runtime.console().p("Number of methods: ").p(count).pln().flush();
				}

				public void visitMethodDeclaration(GNode n) {
					count++;
					visit(n);
				}

				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}

			}.dispatch(node);
		}

	}//end of process method
	
	/**
	 * Run the translator with the specified command line arguments.
	 *
	 * Uses xtc.util.tool run();
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		new Translator().run(args);
	}	
}//end of Translator.java
