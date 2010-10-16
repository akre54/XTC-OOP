package xtc.oop;

import java.io.File;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/*
 * edited by Liz Pelka 10-15-2010
 *
 *creates a c++ class defintion headerfile and a c++ method definition .cpp file
 *also creates an ArrayList<String> for each class to simulate virtual tables
 *these ArrayLists will actually hold method ptr and ptr assignment statements
 *for easy writing to .h file VTable
 */
public class InheritanceBuilder{
	
	
	CppCreator h_classdef;
	CppCreator cpp_methoddef;
	//ArrayList<String>; 

	
	InheritanceBuilder(File jfile){
		/*
		 *creates new cc file h_classdef
		 *copies start of translation.h into h_classdef
		 */
		h_classdef = (new CppCreator(jfile,"_classdef","h"));
		h_classdef.write("/* Object-Oriented Programming\n"+
						  "* Copyright (C) 2010 Robert Grimm\n"+
						  "*\n"+
						  "*Edited by Liz Pelka\n"+
						  "*\n"+
						  "* This program is free software; you can redistribute it and/or\n"+
						  "* modify it under the terms of the GNU General Public License\n"+
						  "* version 2 as published by the Free Software Foundation.\n"+
						  "*\n"+
						  "* This program is distributed in the hope that it will be useful,\n"+
						  "* but WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
						  "* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"+
						  "* GNU General Public License for more details.\n"+
						  "*\n"+
						  "* You should have received a copy of the GNU General Public License\n"+
						  "* along with this program; if not, write to the Free Software\n"+
						  "* Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,\n"+
						  "* USA.\n"+
						  "*/\n\n"+
						 
						 "#pragma once\n\n"+
						 
						 "#include <stdint.h>\n"+
						 "#include <string>\n\n"+
						 
						 "namespace oop {\n\n"
						 );
							 
		
		/*
		 *creates new cc file cc_methoddef
		 *copies start of translation.cc into cc_classdef
		 */
		cpp_methoddef =(new CppCreator(jfile,"_methoddef","cpp"));
		cpp_methoddef.write("/* Object-Oriented Programming\n"+
							"* Copyright (C) 2010 Robert Grimm\n"+
							"*\n"+
							"*Edited by Liz Pelka\n"+
							"*\n"+
							"* This program is free software; you can redistribute it and/or\n"+
							"* modify it under the terms of the GNU General Public License\n"+
							"* version 2 as published by the Free Software Foundation.\n"+
							"*\n"+
							"* This program is distributed in the hope that it will be useful,\n"+
							"* but WITHOUT ANY WARRANTY; without even the implied warranty of\n"+
							"* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"+
							"* GNU General Public License for more details.\n"+
							"*\n"+
							"* You should have received a copy of the GNU General Public License\n"+
							"* along with this program; if not, write to the Free Software\n"+
							"* Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,\n"+
							"* USA.\n"+
							"*/\n\n"+
							
							"#include \""+h_classdef.cFile.getName()+"\"\n\n"+
							
							"#include <sstream>\n\n"+
							
							"namespace oop {\n\n"
							);
		
		
	}
						
	/*
	 * writes to the h_classdef file GNode n's class declaration and Vtable
	 *	same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.h from class notes
	 */
	public void addClassdef(/*passes the classdeclaration (maybe AST node)*/GNode n){
		
		String ClassName = n.getString(1);
		
		h_classdef.write(
			"struct __"+ClassName+"; \n"+/**/
			"struct __"+ClassName+"_VT;\n\n"+/**/
						 
			"typedef __"+ClassName+" = "+ClassName+";\n\n"+/**/
		
			"struct __"+ClassName+"{ \n"+/**/
			"__"+ClassName+"_VT __vptr;\n");
						 
				/* FEILDS ---> ex: int x;  */
				write_all_feilds(n); h_classdef.write("\n\n"+
				
				/*CONSTRUCTOR initializer syntax  */
				ClassName+"():__vpt(&__vtable)"+ write_all_assigned_feilds(n)+"{};\n\n"+
						 
				/*  */
				"static Class __class();\n\n");/**/
				
				write_all_methods(n);  h_classdef.write("\n\n"+
						 
				/*create instance of VTABLE*/
				"private: \n static __"+ClassName+"_VT __vtable;\n"+/**/
			"};\n\n\n"+                      
		/*-------------------------end of struct __ClassName in .h file-------------------------*/	
						 
		/* ---------------------start of stuct __ClassName_VT in .h file -------------------*/
			
			"struct __"+ClassName+"_VT{\n"+
				
				/* DECLARE METHOD PTRS ---> ",methodreturnType (*methodname)(methodparameters)",\n" */
				write_all_method_ptrs(n)+"\n\n"+/**/
						
						 
				"__"+ClassName+"_VT():\n"+
						 
						/* INITIALIZE METHOD PTRS ---> methodname"(&__"+ClassName+"::"+methodname+"),\n" */
						write_assign_method_ptrs(n)+/**/
				"{}\n"+
						 
						 
			"};\n\n"/* -----------end of stuct __ClassName_VT in .h file -------------------*/
						 
		);// end of writing
					
	}//end of addClassdef
	
	
	/*
	 * will return a concatinated string of all feild declarations in GNode n
	 */
	private void write_all_feilds(GNode n){
		Node node = n;
		new Visitor(){
			
			boolean is_field = false;
			
			
			public void visitClassBody(GNode n){
				visit(n);
			}
			public void visitFieldDeclaration(GNode n){
				is_field = true;
				visit(n);
				is_field = false;
				h_classdef.write(";\n");
			}
			//if not looking in FieldDeclaration's subtree ignore methods
				
			public void visitModifier(GNode n){
				if(is_field){		
					h_classdef.write(n.getString(0)+" ");
						visit(n);
				}
					
			}
			public void visitPrimitiveType(GNode n){
				if(is_field){
					h_classdef.write(n.getString(0)+" ");
					visit(n);
				}
			}
			public void visitDeclarator(GNode n){//variable name
				if(is_field){
					h_classdef.write(n.getString(0)+" ");

					visit(n);
				}
			}
			public void visitQualifiedIdentifier(GNode n){//type
				if(is_field){
				h_classdef.write(n.getString(0)+" ");

				visit(n);
				
				}
			}
			
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(node);
		
	}
	
	
	/*
	 * will return a concatinated string of all initalized feilds in GNode n
	 * syntax --->   ","feildname+"("+initial value+")"
	 */	
	private String write_all_assigned_feilds(GNode n){
		
		
		
		return ",feild(initalize)";
	}
	
	
	/*
	 * will return a concatinated string of all method declarations in GNode n
	 * syntax --->  "static "+ returntype +" "+methodName+" ("+className+","+..other paramaterTypes,..+");\n"
	 *
	 *
	 */		
	private void write_all_methods(GNode n){
		Node node = n;

		final String classname = n.getString(1);

		new Visitor(){
			String s = "";
			String retrn="";
			String methodname="";
			String params = "";
			boolean is_fparam=false;
			
			/*public void visitClassBody(GNode n){
				
				System.out.println(n.getName());
				visit(n);
				System.out.println("---END-"+n.getName());
			}*/
			public void visitMethodDeclaration(GNode n){
				retrn="";
				methodname = n.getString(3);
				params = "";
				System.out.println(n.getName()+"---"+n.getString(3));
				visit(n);
				System.out.println("---END-"+n.getName());
				h_classdef.write("static "+ retrn +" "+methodname+" ("+classname+""+params+");\n");

			}
			/*public void visitModifiers(GNode n){
					System.out.println(n.getName());
					visit(n);
					System.out.println("---END-"+n.getName());

			}
			public void visitModifier(GNode n){

				System.out.println(n.getName()+"---"+n.getString(0));
				visit(n);
				System.out.println("---END-"+n.getName());

				
			}*/
			public void visitFormalParameter(GNode n){
				is_fparam = true;
					System.out.println(n.getName()+"---"+n.getString(3));
					visit(n);
					System.out.println("---END-"+n.getName());
				is_fparam = false;

			}
			/*public void visitFormalParameters(GNode n){
			
					System.out.println(n.getName());
					visit(n);
					System.out.println("---END-"+n.getName());

					
				
			}*/
			public void visitVoidType(GNode n){
				retrn = "void";
				System.out.println(n.getName()+"---"+n.getName());
				visit(n);
				System.out.println("---END-"+n.getName());

			
			}
			/*public void visitType(GNode n){
				System.out.println(n.getName());
				visit(n);
				System.out.println("---END-"+n.getName());

				
			}*/
			public void visitQualifiedIdentifier(GNode n){
				if(is_fparam) params = params+","+n.getString(0);
				else retrn = n.getString(0);
				
				System.out.println(n.getName()+"---"+n.getString(0));
				visit(n);
				System.out.println("---END-"+n.getName());


			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
			
			
		}.dispatch(node);	
	}
	
	
	/*
	 * will return a concatinated string of all methods ptr declarations from superclass's Vtable
	 * +  method ptr declarations of PUBLIC NON-STATIC methods in GNode n's class 
	 * syntax --->  ",methodreturnType (*methodname)(methodparameters)",\n"
	 *
	 * also creates a new ArrayList<String> that has :
	 *		all elements of the ArrayList GNode n's ExtenstionType() a.k.a. Superclass
	 *   +  all String representations of method prt declarations with syntax above^ from this class
	 */	
	private String write_all_method_ptrs(GNode n){
		return "method prts,\n";
	}
	
	
	/*
	 * will return a concatinated string of all initalized method ptrs from superclass's Vtable 
	 * + initialized method ptrs of public methods in GNode n's class
	 * syntax --->  methodname"(&__"+ClassName+"::"+methodname+"),\n"
	 *
	 * also creates a new ArrayList<String> that has :
	 *		all elements of the ArrayList GNode n's ExtenstionType() a.k.a. Superclass
	 *   +  all String representations of method prt declarations with syntax above^ from this class
	 */	
	private String write_assign_method_ptrs(GNode n){
		return "method prt(initialized),\n";
	}
	
	
	/*
	 *writes to the ccp_methoddef all of GNode's methods functionality
	 *same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.cc from class notes
	 *
	 */
	public void addMethodDec(GNode n){
	}
	public void close(){
		h_classdef.write("}//ends oop namespace");
		h_classdef.close();
		cpp_methoddef.write("}//ends oop namespace");
		cpp_methoddef.close();
	}
	//--------------end of methods -------------------------
	
	
}// end of InheritanceBuilder

/************ note on Vistors ***************

-include only the visit "whatever" methods for 
 nodes that will supply information 
-overused nodes like modifier can have boolean ranges
 for the specific action dependent on parent nodes
-these parent nodes' visit methods should be included
 as well with up and down switches on the boolean ranges
 
*******************************************/



/*
MY LOGIC FOR INHERITANCE
 
 1. a arraylist of string will exist for every class declaration of a leaf on the inheritance hiearchy 
 2  somehow a the classes need to be filled in from object down to leaf 
			(!! a superclass must be defined before a subclass!!)
 3. when we visit all class declarations call addClassdef() for only every class that extends object or class?
		- save each class you defined
		- now call addClassdef() on all classes that extend those classes
		- save all those...
 4.  every class automaticly extends object and the class ToString method if not overwritten (class without extends)
 5. every classs NEEDS a vtable bc it inherits from object and may be the superclass for another class
 
 class Inheritance builder
	feilds
		-h_classdef		
		-cpp_methoddef
		-
	constructor
		- creates cpp and h files
		-
		-
	methods
		-public addClassdef
			*
			*
		-	private String write_all_feilds(){
		-	private String write_all_assigned_feilds(){
		-	private String write_all_methods(){
		-	private String write_all_method_ptrs(){

 
 >one instance for each run of the translator
 >
 
 
 
 1 find out how many children method dec has
 2 if node.getString children 
 3 create #children recursive routines
 
 
 
 
 
 */