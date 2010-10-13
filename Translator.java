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

import xtc.lang.JavaFiveParser;

import xtc.parser.ParseException;
import xtc.parser.Result;

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
 * @version $Revision$
 */
public class Translator extends Tool {

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
				 "Translate .java file to c++.");
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
		// Handle the translate option
		if (runtime.test("translate")) {
			runtime.console().p("translating...").pln().flush();
			new Visitor() {
				public void visitCompilationUnit(GNode n) {
					visit(n);
					//runtime.console().p("Number of methods: ").p(count).pln().flush();
				}

			
				
				//EDITED BY PAIGE PONZEKA 10/11/10 CLASSES
				public void visitClassDeclaration(GNode n){
					runtime.console().p(n.getName()).pln().flush(); //xtc.tree.node
						
					runtime.console().p(n.getString(1)).pln().flush(); //prints the name of the class name 
				
					new Visitor(){
						
						public void visitCompilationUnit(GNode n) {
							visit(n);
						}
						
						public void visitModifier(GNode n) {
							//runs a for loop for ALL the children of the Qualified Identifier Subtree
							for(int i=0;i<(n.size());i++)
							{
								//here we have to insert file readers and create the subfolders for the import declaration
								runtime.console().p(n.getString(i)).pln().flush(); //xtc.tree.node
							}
							visit(n);
						}
						public void visitClassBody(GNode n)
						{
							runtime.console().p(n.getName()).pln().flush(); //prints the name of the class name 
							new Visitor(){
								
								public void visitCompilationUnit(GNode n) {
									visit(n);
								}
								public void visitConstructorDeclaration(GNode n) {
									runtime.console().p(n.getName()).pln().flush(); //prints the name of the class name 
									runtime.console().p(n.getString(2)).pln().flush(); //prints the name of the constructor
									new Visitor(){
										
										public void visitCompilationUnit(GNode n) {
											visit(n);
										}
										
										public void visitModifier(GNode n) {
											//runs a for loop for ALL the children of the Qualified Identifier Subtree
											for(int i=0;i<(n.size());i++)
											{
												//here we have to insert file readers and create the subfolders for the import declaration
												runtime.console().p(n.getString(i)).pln().flush(); //xtc.tree.node
											}
											
											
											visit(n);
										}
										public void visit(Node n) {
											for (Object o : n) if (o instanceof Node) dispatch((Node)o);
										}
										}.dispatch(n);
									visit(n);
								}
								public void visitMethodDeclaration(GNode n) {
									runtime.console().p(n.getName()).pln().flush(); //xtc.tree.node
									runtime.console().p(n.getString(3)).pln().flush(); //xtc.tree.node
									visit(n);
								}	
								public void visit(Node n) {
									for (Object o : n) if (o instanceof Node) dispatch((Node)o);
								}
								
							}.dispatch(n);
							visit(n);
						}
						public void visit(Node n) {
							for (Object o : n) if (o instanceof Node) dispatch((Node)o);
						}
						
						
					}.dispatch(n);
					
				visit(n);
					
					
				}
				
				//Edited by Paige 10/11/10 IMPORTS
				public void visitImportDeclaration(GNode n) {
					//visit the import declaration nodes and get the folders and files
						runtime.console().p(n.getName()).pln().flush(); //xtc.tree.node
					
					//create a new visitor to visit only the subtree of the Import Declarations
					new Visitor(){
						
						public void visitCompilationUnit(GNode n) {
							visit(n);
						}
						
						
						
						
						public void visitQualifiedIdentifier(GNode n) {
							//runs a for loop for ALL the children of the Qualified Identifier Subtree
							for(int i=0;i<(n.size());i++)
							{
								//here we have to insert file readers and create the subfolders for the import declaration
								runtime.console().p(n.getString(i)).pln().flush(); //xtc.tree.node
							}
							visit(n);
						}
						
						public void visit(Node n) {
							for (Object o : n) if (o instanceof Node) dispatch((Node)o);
						}
						
					}.dispatch(n);
					
					visit(n);
				}
				//end of edited by paige 10/11/10
				
				
				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}
			}.dispatch(node);
		}
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

	}
	
	/**
	 * Run the translator with the specified command line arguments.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		new Translator().run(args);
	}	
}
