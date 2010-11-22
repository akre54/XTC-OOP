/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

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

	File inputFile = null;
        HashMap<String,Boolean> dependencies;
        HashMap<ClassStruct,Boolean> classes;

	/** Create a new translator. */
	public Translator() {
            // do nothing
	}

        public Translator (HashMap<String,Boolean> dependencies) {
            this();
            this.dependencies = dependencies;
            //this.classes = classes;
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
			bool("finddependencies", "finddependencies", false,
				 "find all classes we need to translate").	
			bool("testing","testing",false,"Run some Test cases.").
			bool("test","test",false,"Run some Test cases.");	}

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
		final boolean VERBOSE = runtime.test("optionVerbose");
		final boolean SILENT  = runtime.test("optionSilent");
		
		//Some Testing Environments
		if(runtime.test("testing"))
		{
			runtime.console().p("Testing...").pln().flush();
			
			/*Create a new visitor to visit the CompilationUnit */
			new Visitor(){
				public void visitBlock(GNode n)
				{
					//CppPrinter print= new CppPrinter(n);
					//System.out.println(print.getString());
				}
				public void visit(Node n)
				{
					for(Object o:n) {
						if(o instanceof Node) dispatch((Node) o);
						
					}
				}
			}.dispatch(node);
			
		}
		//Some Testing Environments
		if(runtime.test("test"))
		{
			runtime.console().p("Testing Method Overloading...").pln().flush();
			
			/*Create a new visitor to visit the CompilationUnit */
			new Visitor(){
				public void visitBlock(GNode n)
				{
					//CppWalker walk= new CppWalker(n);
					//System.out.println(walk.getString());
				}
				public void visit(Node n)
				{
					for(Object o:n) {
						if(o instanceof Node) dispatch((Node) o);
						
					}
				}
			}.dispatch(node);
			//Print the New AST
			//runtime.console().format(node).pln().flush();
		}
		
		// Handle the translate option
		if (runtime.test("translate")) {

                    if (VERBOSE) {
			runtime.console().p("Begining translation...").pln().flush();
                    }

			runtime.console().p("translating...").pln().flush();


                        // need the original file to be the first in dependencies
                        // list to avoid circular imports
                        if (dependencies.isEmpty()) {
                            try {
                                dependencies.put(inputFile.getCanonicalPath(), true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
			
		
			
                        // creates the import heirarchy
                        DependencyTree dependency = new DependencyTree(node, dependencies, classes);


			//creates tree root a.k.a. the Object class
			final InheritanceTree Object = new InheritanceTree();
			
			//creates the Class class as subclass of Object class
			final InheritanceTree Class = new InheritanceTree(Object);
			
			
			final InheritanceBuilder inherit = new InheritanceBuilder(inputFile,dependency.getFileDependencies());
				/******** cppMethod cprint = new cppMethod(/*methoddec NODE)*/

			final LinkedList<GNode> toTree = new LinkedList<GNode>();
			
			new Visitor() {
				
				InheritanceTree supr;
				
				public void visitCompilationUnit(GNode n){
					//Paiges testing class
					//cppClass classtester=new cppClass(n);
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
					else toTree.add(n);
					
				}
				public void visitExtension(GNode n){
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

			int i=0;
			while(!toTree.isEmpty()){
				
					supr = Object.search(toTree.get(i).getNode(3)
									 .getNode(0).getNode(0).getString(0));
					if(supr!=null){
						inherit.addClassdef((new InheritanceTree(toTree.get(i),supr)));
						toTree.remove(i);
					}
					else i++;
				if (i==toTree.size()) i=0;
				
			}
				
			
			inherit.close(); // when all nodes are visited and inheritance files are made close files

		}//end of runtime.test("Translate") test
		//-----------------------------------------------------------------------

		if(runtime.test("finddependencies")){
		
                    HashMap<ClassStruct,Boolean> classesToTranslate =
                            new HashMap<ClassStruct,Boolean>();
                    
		
		
		
		
		}

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
            
            // start with an empty dependency list
            HashMap<String,Boolean> dependencies = new HashMap<String,Boolean>();
            
            new Translator(dependencies).run(args);
	}	
}//end of Translator.java
