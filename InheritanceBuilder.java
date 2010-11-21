package xtc.oop;
import java.util.ArrayList;

import java.io.File;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/*
 * 
 *
 *creates a c++ class datalayout headerfile and a c++ method definition .cpp file
 *then populates these files with the proper information stored in each InheritanceTree object
 *
 *
 */
public class InheritanceBuilder{
	public final boolean DEBUG=false;
	
	CppCreator h_classdef;
	CppCreator cpp_methoddef;
	private File source;

	
	InheritanceBuilder(File jfile,ArrayList<String> files){
		/*
		 *creates new cc file h_classdef
		 *copies start of translation.h into h_classdef
		 */
		source = jfile;
		h_classdef = (new CppCreator(jfile,"_dataLayout","h"));
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
						 
						 "#include \"java_lang.h\"\n"+

						 "using java::lang::Object;\n"+
						 "using java::lang::__Object;\n"+
						 "using java::lang::Class;\n"+

						 "using java::lang::__Class;\n"+
						 "using java::lang::String;\n"+
						 "using java::lang::ArrayOfInt;\n"+
						 "using java::lang::ArrayOfObject;\n"+
						 "using java::lang::ArrayOfClass;\n"+

						 
						 "namespace xtc {\n"+
						 "\tnamespace oop{\n\n"+
						 "\ttypedef std::string String;\n"
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
							
							"#include \""+h_classdef.cFile.getName()+"\"\n\n");
					// #includes all files its dependent on
						for(int i=0;i<files.size();i++){
							cpp_methoddef.write("#include \""+files.get(i)+"\"\n");
						}
							cpp_methoddef.write("#include <sstream>\n\n"+
											//"using xtc::oop::"+cpp_methoddef.cFile.getName()+";\n"+
							
							"namespace xtc {\n"+
							"\tnamespace oop{\n\n"
							);
		
		
	}
						
	/*
	 * writes to the h_classdef file GNode n's class declaration and Vtable
	 *	same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.h from class notes
	 */
	public void addClassdef(InheritanceTree t){
		
		String ClassName = t.className;
		
		h_classdef.write(
			"\tstruct __"+ClassName+"; \n"+/**/
			"\tstruct __"+ClassName+"_VT;\n\n"+/**/
						 
			"\ttypedef __"+ClassName+"* "+ClassName+";\n\n"+/**/
		
			"\tstruct __"+ClassName+"{ \n"+/**/
			"\t   __"+ClassName+"_VT* __vptr;\n");
						 
				/* FEILDS ---> ex: int x;  */
		        write_all_feilds(t); h_classdef.write("\n\n");
				
				/*CONSTRUCTOR(S)*/
				write_all_constructors(t); 
				
						 
				/* avoid static field initializer fiasco with  __class() */
				//h_classdef.write("\t   static Class __class();\n\n");
		
		        /*  ALL INSTANCE METHODS    */
				write_all_methods(t);  h_classdef.write("\n\n"+
						 
				/*create instance of VTABLE*/
				"\t   private: \n\t   static __"+ClassName+"_VT __vtable;\n"+/**/
			"\t};\n\n\n"+                      
		/*-------------------------end of struct __ClassName in .h file-------------------------*/	
						 
		/* ---------------------start of stuct __ClassName_VT in .h file -------------------*/
			
			"\tstruct __"+ClassName+"_VT{\n");
				
				/* DECLARE METHOD PTRS ---> ",methodreturnType (*methodname)(methodparameters)",\n" */
				write_all_method_ptrs(t); h_classdef.write("\n\n"+/**/
						
				/*  VT CONSTRUCTOR*/		 
				"\t   __"+ClassName+"_VT():\n");
						 
				/* INITIALIZE METHOD PTRS ---> methodname"(&__"+ClassName+"::"+methodname+"),\n" */
				write_assign_method_ptrs(t); h_classdef.write( /**/
				"{}\n"+
						 
						 
			"\t};\n\n"/* -----------end of stuct __ClassName_VT in .h file -------------------*/
						 
		);// end of writing
		
		
		// define method in methoddef.cpp
		addMethodDec(t);
					
	}//end of addClassdef
	
	
	/**
	 * will write a string of all feild declarations in this class.
	 */
	private void write_all_feilds(InheritanceTree t){
		
		//loops through fields and prints out in proper syantax
		for(int index =0;index<t.fields.size();index++){
			InstanceField f = t.fields.get(index);
			
			for(int j=0; j<f.modifiers.size();j++){
				h_classdef.write("\t   "+f.modifiers.get(j)+": ");
			}
			
			h_classdef.write(f.type+" "+f.var_name+";\n");
			
		}
		
	}
	
	
	/**
	 * will write all the constructor's into the datalayout for this class.
	 * 
	 *
	 */	
	private void write_all_constructors(InheritanceTree t){
		
		int size = t.constructors.size();
			for(int index =0;index < size ;index++){
				Declaration constr= t.constructors.get(index);
				
				h_classdef.write("\t   ");
				
				//loop through constructor modifiers
				for(int i =0;i<constr.modifiers.size();i++){	
					h_classdef.write(constr.modifiers.get(i)+": ");
				}		
				//write className
				h_classdef.write("__"+t.className+"(");
				
				//loop through formal parameter 
				for(int i =0;i<constr.params.size();i++){
					Fparam fp= constr.params.get(i);
					
					if(i>0)h_classdef.write(",");//comma
					
					//loop through formal parameter's modifiers
					for(int j =0;j<fp.modifiers.size();j++){	
						h_classdef.write( fp.modifiers.get(j)+" ");
					}
					//writes formal parameter's type and name
					h_classdef.write(fp.type+" "+fp.var_name);
				}
				//intialize __vptr
				h_classdef.write("):__vptr(&__vtable)");
				
				// intialize all the instance fields
				for(int i =0; i< t.fields.size();i++){
					InstanceField f = t.fields.get(i);
					
					h_classdef.write(","+f.var_name+"("+f.value+")");
				
				}
				h_classdef.write("{\n\t\t");
			
			//**  cppBlock is called on constructor's block node  **//
			EWalk changes = new EWalk(t,constr,constr.bnode);
				CppPrinter print = new CppPrinter(constr.bnode);
				h_classdef.write(print.getString().toString());//write body of the constructor
				h_classdef.write("\n\t   };\n\n");
			
			}
		
	}
	
	
	/**
	 * will print string of all local method declarations in this class
	 * syntax --->  "static "+ returntype +" "+methodName+" ("+className+","+..other paramaterTypes,..+");\n"
	 *
	 *
	 */		
	private void write_all_methods(InheritanceTree t){
		//loops through local methods and prints out in proper syantax
		int size = t.local.size();
		for(int index = 0; index < size ;index++){
			Declaration method = t.local.get(index);
			
			if (method.name.equals("main")) {
				buildMain(method);
				h_classdef.write("\t   static int32_t "+method.name+"_"+method.overloadNum+"(int32_t, char**);\n");
			}
			else{
				//h_classdef.write("\t   ");
				for(int i=0;i<method.modifiers.size();i++){
					h_classdef.write("\t"+method.modifiers.get(i)+": \n");
				}
				h_classdef.write("\t   static "+method.returntype+" "+method.name+"_"+method.overloadNum+"(");
	
				for(int j=0; j<method.params.size();j++){
					
					if(j==0)// print first param without ","
						h_classdef.write(method.params.get(j).type);

					else h_classdef.write(","+method.params.get(j).type);
				
				}
				h_classdef.write(");\n");
			}
		}
	}

	/** 
	 * Handles main method.
	 * Creates a file called main.cpp that contains:
	 * main(int argc, char *argv[])
	 * 
	 *updates the declaration with proper parameters
	 *
	 * @param GNode A method declaration GNode
	 */ 
	private void buildMain(Declaration n) {
		CppCreator mainWriter = new CppCreator(source, "main.cpp");
		//change parameters for c++
		
		n.returntype = "int32_t";
		String arrayName = n.params.get(0).var_name;
		n.params.get(0).type="int32_t";
		n.params.get(0).var_name="argc";
		n.params.add(new Fparam(new ArrayList<String>(0), "char**",arrayName));
		
		//create the main.cpp
		mainWriter.write("#include <iostream>\n\n"+
						 "#include \""+h_classdef.cFile.getName()+"\"\n\n"+
						 "using namespace xtc::oop;\n\n\n"
						 +"int32_t main(int32_t argc, char *argv[]){\n\n\t"
						 +n.ownerClass+" NAMEmain = new __"+n.ownerClass+"();\n\t"
						 +"NAMEmain->main"+"_"+n.overloadNum+"(argc,argv);\n\treturn 0;\n}");
		mainWriter.close();
	}
	
	
	
	/**
	 * will print a string of all methods ptr declarations from superclass's Vtable
	 * +  method ptr declarations of PUBLIC NON-STATIC methods in this class. 
	 *
	 * syntax --->  ",methodreturnType (*methodname)(methodparameters)",\n"
	 */	
	private void write_all_method_ptrs(InheritanceTree t){
		
		//ptr for __class()
		h_classdef.write("\t\tClass __isa;\n");
		//loops through vtable and prints out in proper syntax
		for(int index =1;index<t.Vt_ptrs.size();index++){
			Declaration method = t.Vt_ptrs.get(index);
			
			h_classdef.write("\t\t"+method.returntype+" (*"+method.name+"_"+method.overloadNum+")("+t.className);
			
			for(int j=1; j<method.params.size();j++){
				h_classdef.write(", "+method.params.get(j).type);
			}
			h_classdef.write(");\n");
			
		}
	}
	
	
	/*
	 * prints out string of all initalized method ptrs from superclass's Vtable 
	 * + initialized method ptrs of public and protected methods in this class to the .h file.
	 *
	 * syntax --->  methodname"(&__"+ClassName+"::"+methodname+"),\n"
	 * or syntax ---> methodname+"(("+returntype+"(*)("+classname+","+...other params+"))&__"+ownerclass+"::"+methodname+")"
	 *
	 */	
	private void write_assign_method_ptrs(InheritanceTree t){
		//ptr for __class()
		h_classdef.write("\t\t   "+t.Vt_ptrs.get(0).name+"(__"+t.Vt_ptrs.get(0).ownerClass+"::__class())");
		
		//loops through vtable and prints out in proper syntax
		for(int index =1;index<t.Vt_ptrs.size();index++){
			Declaration method = t.Vt_ptrs.get(index);
			
			//syntax for an overridden method
			if((method.ownerClass).equals(t.className)){
				
				h_classdef.write(",\n\t\t   "+method.name+"_"+method.overloadNum+"(&__"+t.className+"::"+method.name+"_"+method.overloadNum+")");
			}
			//syntax for a method that needs a this class casting
			else{
				h_classdef.write(",\n\t\t   "+method.name+"_"+method.overloadNum+"(("+method.returntype+"(*)("+t.className);
				
				for(int j=1; j<method.params.size();j++){
					h_classdef.write(", "+method.params.get(j).type);
				}
				h_classdef.write("))&__"+method.ownerClass+"::"+method.name+"_"+method.overloadNum+")");
			
			}
			
		}
	}

	
	/**
	 *writes to the ccp_methoddef all of GNode's methods functionality
	 *same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.cc from class notes
	 * syntax -->  returntype+" __"+className+"::__"+methodname+"("+className+"__this,"+other params+"){"
	 *    CALLS TO CPPMETHOD to build the body
	 *	 then ends method "}"
	 */
	private void addMethodDec(InheritanceTree t){
		
		//writes the __class() method
		cpp_methoddef.write("\t"+t.local.get(0).returntype+" __"+t.className+
							"::"+t.local.get(0).name+"(){\n\t"+
							"\n\tstatic Class k = new __Class(__rt::stringify(\"xtc.oop."+t.className+"\"),__rt::null());"+
							"\n\treturn k;\n\t"+
							"}\n");

		
	//--- adds all methods to METHODDEF	
		int size = t.local.size();
		for(int index=1;index<size;index++){
			Declaration method = t.local.get(index);

				//method syntax
				cpp_methoddef.write("\t"+method.returntype+" __"+t.className+
									"::"+method.name+"_"+method.overloadNum+"(");
		
				for(int i=0;i<method.params.size();i++){
					Fparam param= method.params.get(i);
					
					//first param without ","
					if(i==0)cpp_methoddef.write(param.type+" "+param.var_name);
					else cpp_methoddef.write(","+param.type+" "+param.var_name);

				}
				//calls to CppMethod to create the body of the method
				cpp_methoddef.write("){\n");
			
				//**  cppBlock is called on method's block node  **//
				//cppMethod mblock = new cppMethod(t.local.get(index).mnode);
			EWalk changes = new EWalk(t,method,method.bnode);
				CppPrinter mblock=new CppPrinter(method.bnode);
				cpp_methoddef.write(mblock.getString().toString());//write body of the method
				cpp_methoddef.write("\n\t   }\n\n");
			
				cpp_methoddef.write("\n\n");
		}
		// invokes Vtable constructor
		cpp_methoddef.write("\t__"+t.className+"_VT __"+t.className+"::__vtable;\n\n"+
							"\t//===========================================================================\n\n");
	}
	/*
	 *closes both files
	 *
	 */
	public void close(){
		h_classdef.write("\t}//ends oop namespace\n}//ends xtc namespace");
		h_classdef.close();
		cpp_methoddef.write("\t}//ends oop namespace\n}//ends xtc namespace");
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



