/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
 * edits (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
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
import java.util.LinkedList;
import java.util.HashMap;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import java.util.ArrayList;

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
        HashMap<DependencyFinder,Boolean> dependencies;
        HashMap<ClassStruct,Boolean> classes;
		HashMap<File,ArrayListClassStruct> files;

	/** Create a new translator. */
	public Translator() {
            // do nothing 
	}

        public Translator (HashMap<String,Boolean> dependencies,
                    HashMap<ClassStruct,Boolean> classes) {
            this();
            this.dependencies = dependencies;
            this.classes = classes;
        }

	public String getCopy() {
		return "(C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka";
	}

	public String getName() {
		return "Java to C++ Translator";
	}

	public String getExplanation() {
		return "This tool translates a subset of Java to a subset of C++.";
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
		bool("testing2","testing2",false, "tester2 for EWalk");}

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
		return file;
	}

	public Node parse(Reader in, File file) throws IOException, ParseException {
		JavaFiveParser parser =
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}
	private void print

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
						CppPrinter print= new CppPrinter(n,true);
						
					}
					public void visit(Node n)
					{
						for(Object o:n) {
							if(o instanceof Node) dispatch((Node) o);
						}
					}
				}.dispatch(node);
			
			}
		if(runtime.test("testing2"))
		{
				
				
			
		}
		if (runtime.test("translate")) {

                    if (VERBOSE) {
                        runtime.console().p("Begining translation...").pln().flush();
                    }
			files.put(inputFile,new ArrayList<ClassStruct>(0);

                    String fullPathName = "";
                    try { fullPathName = inputFile.getCanonicalPath(); }
                    catch (IOException e) { }

                    // need the original file to be the first in dependencies list
                    if (dependencies.isEmpty()) {
                        dependencies.put(fullPathName, true);
                    }

                    // recursively find dependencies from input file
                    Translator t = new Translator(dependencies, classes);
                    t.run(new String[]{"-no-exit", "-finddependencies", fullPathName});
                    classes = t.classes;
			
					/*** 
					 //---- creates all InheritanceTrees ----
					 
					 int leftTotranslate = classes.size();
					 While(classes.containsValue(false)){
						
						for(ClassStruct c : classes){
							if( c.extension ==null){//*** extends object
								new InheritanceTree(c.pkgs,c.node,Object);
								
								classes.remove(c);
					 
							}
							else{
								InheritanceTree superclass = Object.search(c.pkgs,c.extension);	
								if (superclass!=null)//**extends an already translated class
									new InheritanceTree(c.pkgs,c.node,superclass);
									classes.remove(c);
							}
						 
						}
						//if you didnt translate anything this round then 
						//search in directory for extensions
						if (leftTotranslate == classes.size()){
							for(classStruct c: classes)
								for( classStruct d: dirclasses){
									if (c.extension.equals(d.classname))
										classes.put(d, true);
								}
							}
					 
						}
						leftTotranslate = classes.size();
					 }
					 
					//----- creates all methoddef files first (to check for extra needed dependencies)
					 for(InheritanceTree subclass: Object.subclasses){
						printCPP(subclass);
					 }
					 if (cls.subclasses.size()==0)
					 
					 // have to group by file 
					 // create all methoddefs for classes
					 // write dependencies
					 // create all classdefs for classes
					 
					 
					 
					 
					 ****/
			
			
			
//------------- the rest of translate will be edited to work with hashmap ------------------------
			
					//creates tree root a.k.a. the Object class
                    final InheritanceTree Object = new InheritanceTree();

                    //creates the Class class as subclass of Object class
                    final InheritanceTree Class = new InheritanceTree(Object);

                    final InheritanceBuilder inherit = new InheritanceBuilder(inputFile,
                            (new DependencyFinder(node, fullPathName)).getCppDependencies(DependencyOrigin.IMPORT));

                   
                    final LinkedList<GNode> toTree = new LinkedList<GNode>();

                    new Visitor() {

                        InheritanceTree supr;

                        public void visitCompilationUnit(GNode n) {
                            //Paiges testing class
                            //cppClass classtester=new cppClass(n);
                            visit(n);
                        }

                        public void visitClassDeclaration(GNode n) {
                            //if no extenstion it's superclass is Object
                            supr = Object;
                            visit(n);

                            //if the super class has been defined make the subclass
                            if (supr != null) {
                                inherit.addClassdef((new InheritanceTree(n, supr)));
                            } else {
                                toTree.add(n);
                            }

                        }

                        public void visitExtension(GNode n) {
                            //find's super class
                            //searches for InheritanceTree with same name as extention
                            //returns null if no tree exists yet
                            /**CURRENTLY CRASHES MAKE REMOVED BY PAIGE 11.25
                                                supr = Object.search(n.getNode(0).getNode(0).getString(0));
                                                 */
                        }

                        public void visit(Node n) {
                            for (Object o : n) {
                                if (o instanceof Node) {
                                    dispatch((Node) o);
                                }
                            }
                        }
                    }.dispatch(node); //end of main dispatch

                    //creates the rest of the tree all nodes whose super exists until all
                    //trees created
                    InheritanceTree supr;

                    int i = 0;
                    while (!toTree.isEmpty()) {
                        /**CURRENTLY CRAHES MAKE REMOVED BY PAIGE 11.25
                                        supr = Object.search(toTree.get(i).getNode(3)
                                        .getNode(0).getNode(0).getString(0));

                                        if(supr!=null){
                                        inherit.addClassdef((new InheritanceTree(toTree.get(i),supr)));
                                        toTree.remove(i);
                                        }
                                        else i++;
                                        if (i==toTree.size()) i=0;
                                         */
                    }


                    inherit.close(); // when all nodes are visited and inheritance files are made close files
                    if (VERBOSE) //prints the ast after every translation
                    {
                        runtime.console().format(node).pln().flush();
                    }
			
			
                }//end of runtime.test("Translate") test
                //-----------------------------------------------------------------------

                /* find dependencies of a single file, recursively calling until dependency list is filled */
		if(runtime.test("finddependencies")){
			files.put(inputFile,new ArrayList<ClassStruct>(0);
			String fullPathName = "";
			try { fullPathName = inputFile.getCanonicalPath(); }
			catch (IOException e) { }

			DependencyFinder depend = new DependencyFinder(node, fullPathName);
			dependencies.put(depend, true);

			for (ClassStruct c : depend.getFileClasses())
                        classes.put(c, false);

			Translator t = null;
			for ( String filename : depend.getFileDependencyPaths() ) {

                        // only translate if not translated. dependencies.get(filename) returns
                        // a boolean specifiying whether the file has been translated
                        //if ( !dependencies.containsKey(filename) || !(dependencies.get(filename))) {
				if(!dependencies.containsvalue(filename)){		

					t = new Translator(dependencies, classes);
					t.run( new String[] {"-no-exit", "-finddependencies", filename});

					dependencies.putAll(t.dependencies);
				}
			}
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
                HashMap<ClassStruct,Boolean> classes = new HashMap<ClassStruct,Boolean>();
            
		new Translator(dependencies, classes).run(args);
	}	
}//end of Translator.java
