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
	public final boolean DEBUG=false;
	
	CppCreator h_classdef;
	CppCreator cpp_methoddef;


	
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
	public void addClassdef(GNode n,InheritanceTree t){
		
		String ClassName = t.className;
		
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
				
				write_all_methods(t);  h_classdef.write("\n\n"+
						 
				/*create instance of VTABLE*/
				"private: \n static __"+ClassName+"_VT __vtable;\n"+/**/
			"};\n\n\n"+                      
		/*-------------------------end of struct __ClassName in .h file-------------------------*/	
						 
		/* ---------------------start of stuct __ClassName_VT in .h file -------------------*/
			
			"struct __"+ClassName+"_VT{\n");
				
				/* DECLARE METHOD PTRS ---> ",methodreturnType (*methodname)(methodparameters)",\n" */
				write_all_method_ptrs(t); h_classdef.write("\n\n"+/**/
						
						 
				"__"+ClassName+"_VT():\n");
						 
						/* INITIALIZE METHOD PTRS ---> methodname"(&__"+ClassName+"::"+methodname+"),\n" */
				write_assign_method_ptrs(t); h_classdef.write( /**/
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
					String type = n.getString(0);
					if(type.equals("int"))type="int_32_t";
					if(type.equals("boolean"))type="bool";
					h_classdef.write(type+" ");
					
				}
			}
			public void visitDeclarator(GNode n){//variable name
				if(is_field){
					h_classdef.write(n.getString(0));

					//visit(n);
				}
			}
			public void visitQualifiedIdentifier(GNode n){//type
				if(is_field){
					h_classdef.write(n.getString(0)+" ");

				//visit(n);
				
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
	 * will print string of all method declarations in this class
	 * syntax --->  "static "+ returntype +" "+methodName+" ("+className+","+..other paramaterTypes,..+");\n"
	 *
	 *
	 */		
	private void write_all_methods(InheritanceTree t){
		//loops through local methods and prints out in proper syantax
		for(int index =1;index<t.local.size();index++){
			h_classdef.write("static "+t.local.get(index).returntype+" "+t.local.get(index).name+" ("+t.className);
			
			for(int j=0; j<t.local.get(index).params.size();j++){
				h_classdef.write(", "+t.local.get(index).params.get(j));
			}
			h_classdef.write(");\n");

		}
	}
	
	
	/*
	 * will print a string of all methods ptr declarations from superclass's Vtable
	 * +  method ptr declarations of PUBLIC NON-STATIC methods in this class. 
	 *
	 * syntax --->  ",methodreturnType (*methodname)(methodparameters)",\n"
	 */	
	private void write_all_method_ptrs(InheritanceTree t){
		//ptr for __class()
		h_classdef.write("Class __isa;\n");
		//loops through vtable adn prints out in proper syntax
		for(int index =1;index<t.Vt_ptrs.size();index++){
			h_classdef.write(t.Vt_ptrs.get(index).returntype+" (*"+t.Vt_ptrs.get(index).name+")("+t.className);
			
			for(int j=0; j<t.Vt_ptrs.get(index).params.size();j++){
				h_classdef.write(", "+t.Vt_ptrs.get(index).params.get(j));
			}
			h_classdef.write(");\n");
			
		}
	}
	
	
	/*
	 * prints out string of all initalized method ptrs from superclass's Vtable 
	 * + initialized method ptrs of public methods in this class to the .h file.
	 *
	 * syntax --->  methodname"(&__"+ClassName+"::"+methodname+"),\n"
	 * or syntax ---> methodname+"(("+returntype+"(*)("+classname+","+...other params+"))&__"+ownerclass+"::"+methodname+")"
	 *
	 */	
	private void write_assign_method_ptrs(InheritanceTree t){
		//ptr for __class()
		h_classdef.write(t.Vt_ptrs.get(0).name+"(__"+t.Vt_ptrs.get(0).ownerClass+"::__class())");
		
		//loops through vtable and prints out in proper syntax
		for(int index =1;index<t.Vt_ptrs.size();index++){
			//syntax for an overwritten method
			if((t.Vt_ptrs.get(index).ownerClass).equals(t.className)){
				h_classdef.write(",\n"+t.Vt_ptrs.get(index).name+"(&__"+t.className+"::"+t.Vt_ptrs.get(index).name+")");
			}
			//syntax for a method that needs a this class casting
			else{
				h_classdef.write(",\n"+t.Vt_ptrs.get(index).name+"(("+t.Vt_ptrs.get(index).returntype+"(*)("+t.className);
				
				for(int j=0; j<t.Vt_ptrs.get(index).params.size();j++){
					h_classdef.write(", "+t.Vt_ptrs.get(index).params.get(j));
				}
				h_classdef.write("))&__"+t.Vt_ptrs.get(index).ownerClass+"::"+t.Vt_ptrs.get(index).name+")");
			
			}
			
		}
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
 

 
 
 
 */