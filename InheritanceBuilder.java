/* creates a c++ class datalayout headerfile and a c++ method definition .cpp file
 * then populates these files with the proper information stored in each InheritanceTree object
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */

package xtc.oop;

import java.io.File;
import java.util.ArrayList;

public class InheritanceBuilder{
	public final boolean DEBUG=false;
	
	CppCreator h_classdef;
	CppCreator cpp_methoddef;
        DependencyFinder dependencies;
        ArrayList<ClassStruct> allClasses;
	
	private File jfile;

	
	InheritanceBuilder(DependencyFinder dependencies, ArrayList<ClassStruct> allClasses){
		/*
		 *creates new cc file h_classdef
		 *copies start of translation.h into h_classdef
		 */
                this.dependencies = dependencies;
                this.allClasses = allClasses;
			
		jfile = new File(dependencies.getFilePath());
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
            //get rid of these calls and have them added to dependentFiles for base file
             "#include \"java_lang.h\"\n");

            // #includes all files its dependent on, then using declares them
            for (String importDeclaration : dependencies.getCppIncludeDecs(allClasses, DependencyOrigin.IMPORT) ) {
                    h_classdef.write(importDeclaration+"\n");
            }

             h_classdef.write(
				 "using java::lang::Object;\n"+
             "using java::lang::__Object;\n"+
             "using java::lang::Class;\n"+
             "using java::lang::__Class;\n"+
             "using java::lang::String;\n"+
             "using java::lang::__String;\n"+
             "using java::lang::__Array;\n"+
             "using java::lang::ArrayOfInt;\n"+
             "using java::lang::ArrayOfObject;\n"+
             "using java::lang::ArrayOfClass;\n");
            for (String usingDeclaration : dependencies.getCppUsingDeclarations(allClasses)) {
                h_classdef.write(usingDeclaration+"\n");
                //h_classdef.write(DependencyFinder.getNamespace(dependencies.getFileClasses(), dependencies.getFilePath())+"\n");
            }

            for(String p : dependencies.getPackageToNamespace()){
                    h_classdef.write("namespace "+p+" {\n");
            }

             for (ClassStruct c : dependencies.getFileClasses()) {
                h_classdef.write("\n\tstruct __" + c.className + "; \n"
                        +/**/ "\tstruct __" + c.className + "_VT;\n"
                        +/**/ "\ttypedef __rt::Ptr<__" + c.className + "> " + c.className + ";\n"
                        + "\ttypedef __rt::Ptr<__Array<" + c.className + "> > ArrayOf" + c.className + ";\n\n");
            }
							 
		
		/*
		 *creates new cc file cc_methoddef
		 *copies start of translation.cc into cc_classdef
		 */
		cpp_methoddef =(new CppCreator(jfile,"_methoddef","cpp"));
		cpp_methoddef.write(
							"/* Object-Oriented Programming\n"+
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
				
							"#include <sstream>\n\n");
							for(String p: dependencies.getPackageToNamespace()){
								cpp_methoddef.write("namespace "+p+" {\n");
							}
							cpp_methoddef.write("\n\n");

	}//end of constructor
	/*
	 * writes to the h_classdef file GNode n's class declaration and Vtable
	 *	same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.h from class notes
	 */
	public void addClassdef(InheritanceTree t){
		StringBuilder s=new StringBuilder();
		String ClassName = t.className;
		h_classdef.write("//data layout for ");
		for(String p: dependencies.getPackageToNamespace()){
			h_classdef.write(p+".");
			s.append(p+".");
		}
		String pack=s.toString();
		h_classdef.write(ClassName);
		h_classdef.write(
			"\n\tstruct __"+ClassName+"{ \n"+/**/
			"\t\t__"+ClassName+"_VT* __vptr;\n");
						 
				/* FEILDS ---> ex: int x;  */
		        write_all_feilds(t); h_classdef.write("\n\n");
				
				/*CONSTRUCTOR(S)*/
				write_all_constructors(t); 
				
						 
				/* avoid static field initializer fiasco with  __class() */
				//h_classdef.write("\t   static Class __class();\n\n");
		
		        /*  ALL INSTANCE METHODS    */
				write_all_methods(t);

                                h_classdef.write("\n\n"+
				/*create instance of VTABLE*/
				"\n\t\tstatic __"+ClassName+"_VT __vtable;\n"+/**/
			"\t};\n\n//vtable layout for "+pack+ClassName+"\n"+                      
		/*-------------------------end of struct __ClassName in .h file-------------------------*/	
						 
		/* ---------------------start of stuct __ClassName_VT in .h file -------------------*/
			
			"\tstruct __"+ClassName+"_VT{\n");
				
				/* DECLARE METHOD PTRS ---> ",methodreturnType (*methodname)(methodparameters)",\n" */
				write_all_method_ptrs(t); h_classdef.write("\n\n"+/**/
						
				/*  VT CONSTRUCTOR*/		 
				"\t\t__"+ClassName+"_VT():\n");
						 
				/* INITIALIZE METHOD PTRS ---> methodname"(&__"+ClassName+"::"+methodname+"),\n" */
				write_assign_method_ptrs(t); h_classdef.write( /**/
				"{}\n"+
						 
						 
			"\t};\n\n"/* -----------end of stuct __ClassName_VT in .h file -------------------*/
						 
		);// end of writing
		
		//now create .cpp file
		addMethodDec(t);
					
	}//end of addClassdef
	
	
	/**
	 * will write a string of all feild declarations in this class.
	 */
	private void write_all_feilds(InheritanceTree t) {
            //loops through fields and prints out in proper syantax
            for (InstanceField f : t.fields) {
                for(String modifier : f.modifiers) {
                   // h_classdef.write("\t   "+modifier+": ");
                }
                h_classdef.write("\t\t"+f.type+" "+f.var_name+";\n");
            }
	}
	
	
	/**
	 * will write all the constructor's into the datalayout for this class.
	 * 
	 *
	 */	
	private void write_all_constructors(InheritanceTree t){
		
            for (Declaration constr : t.constructors) {
                h_classdef.write("\t\t");

                //loop through constructor modifiers
                for (String modifier : constr.modifiers){
                    //h_classdef.write(modifier+": ");
                }
                //write className
                h_classdef.write("__"+t.className);
                if(constr.overloadNum!=0)
                        h_classdef.write("_"+constr.overloadNum);
                h_classdef.write("(");

                //loop through formal parameter
                for (int i=0;i<constr.params.size();i++) {
                    Fparam fp = constr.params.get(i);

                    if (i>0) h_classdef.write(","); //comma

                    //loop through formal parameter's modifiers
                    for (String modifier : fp.modifiers) {
                        h_classdef.write(modifier+" ");
                    }
                    //writes formal parameter's type and name
                    h_classdef.write(fp.type+" "+fp.var_name);
                }
                //intialize __vptr
                h_classdef.write("):__vptr(&__vtable)");

                // intialize all the instance fields
                for (InstanceField f : t.fields) {
                    h_classdef.write(","+f.var_name+"("+f.value+")");
                }
                h_classdef.write("{\n\t\t\t");//3 tabs for Ewalk

                //**  EWalk is called on constructor's block node  **//
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
            for (Declaration method : t.local) {
                if (method.name.equals("main")) {
                    buildMain(method);
                    h_classdef.write("\t\tstatic int32_t "+method.name);
                    if(method.overloadNum!=0)
                            h_classdef.write("_"+method.overloadNum);
                    h_classdef.write("(int32_t, char**);\n");
                }
                else {
                    //h_classdef.write("\t   ");
                    for (String modifier : method.modifiers) {
                       //h_classdef.write("\t"+modifier+": \n");
                    }
                    h_classdef.write("\t\tstatic "+method.returntype+" "+method.name);
                    if(method.overloadNum!=0)
                            h_classdef.write("_"+method.overloadNum);
                    h_classdef.write("(");

                    for (int j=0; j<method.params.size();j++) {

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
		CppCreator mainWriter = new CppCreator(jfile, "main.cpp");
		//change parameters for c++
		
		n.returntype = "int32_t";
		String arrayName = n.params.get(0).var_name;
		n.params.set(0,new Fparam("int32_t","argc"));
		n.params.add(new Fparam("char**",arrayName));
		
		//create the main.cpp
		mainWriter.write("#include <iostream>\n\n"+
						 "#include \""+h_classdef.cFile.getName()+"\"\n\n");
                                                 ArrayList<String> namespace = dependencies.getPackageToNamespace();
						 for(String p : namespace){
							 int size = namespace.size();
							 if (p.equals(namespace.get(0))) mainWriter.write("using namespace "+p);
							 else mainWriter.write("::"+p);
							 if (p.equals(namespace.get(size-1))) mainWriter.write(";");
						 }
						 mainWriter.write("\n\n\n"
						 +"int32_t main(int32_t argc, char *argv[]){\n\n\t"
						 +n.ownerClass+" NAMEmain = new __"+n.ownerClass+"();\n\t"
						 +"NAMEmain->main");
						if(n.overloadNum!=0)
							mainWriter.write("_"+n.overloadNum);
						mainWriter.write("(argc,argv);\n\treturn 0;\n}");
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
		//ptr for __delete()
		h_classdef.write("\t\tvoid (*__delete)(__"+t.className+"*);\n");
		//loops through vtable and prints out in proper syntax
		int size = t.Vt_ptrs.size();
		for (int i=2;i<size;i++) {
			Declaration method = t.Vt_ptrs.get(i);
			h_classdef.write("\t\t"+method.returntype+" (*"+method.name);
			if(method.overloadNum!=0)
				h_classdef.write("_"+method.overloadNum);
			h_classdef.write(")(");
			int fpsize = method.params.size();
			for (int j=0;j<fpsize;j++) {
				String type = method.params.get(j).type;
				
				if (j==0) h_classdef.write(t.className);
				else h_classdef.write(", "+type);
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
		h_classdef.write("\t\t\t"+t.Vt_ptrs.get(0).name+"(__"+t.Vt_ptrs.get(0).ownerClass+"::__class())");
		
		//loops through vtable and prints out in proper syntax
		int size = t.Vt_ptrs.size();
		for (int i=1;i<size;i++) {
			Declaration method = t.Vt_ptrs.get(i);
                    //syntax for an overridden method
                    if ((method.ownerClass).equals(t.className)) {

                        h_classdef.write(",\n\t\t\t"+method.name);
                        if(method.overloadNum!=0)
                                h_classdef.write("_"+method.overloadNum);
                        h_classdef.write("(&__"+t.className+"::"+method.name);
                        if(method.overloadNum!=0)
                                h_classdef.write("_"+method.overloadNum);
                        h_classdef.write(")");
                    }
                    //syntax for a method that needs a this class casting
                    else{
                        h_classdef.write(",\n\t\t\t"+method.name);
                        if (method.overloadNum!=0)
                            h_classdef.write("_"+method.overloadNum);
                        h_classdef.write("(("+method.returntype+"(*)(");

						int fpsize = method.params.size();
                        for (int j=0;j<fpsize;j++) {
							String type = method.params.get(j).type;
							if (j==0) h_classdef.write(t.className);
                            else h_classdef.write(", "+type);
                        }
                        h_classdef.write("))&__"+method.ownerClass+"::"+method.name);
                        if (method.overloadNum!=0)
                            h_classdef.write("_"+method.overloadNum);
                        h_classdef.write(")");
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
	public void addMethodDec(InheritanceTree t){
		
		//writes the __class() method
		cpp_methoddef.write("\t"+t.local.get(0).returntype+" __"+t.className+
							"::"+t.local.get(0).name+"(){"+
							"\n\t\tstatic Class k = new __Class(__rt::stringify(\"xtc.oop."+t.className+"\"),__rt::null());"+
							"\n\t\treturn k;\n\t"+
							"}\n");
		//writes the __delete() method
		cpp_methoddef.write("\t"+t.local.get(1).returntype+" __"+t.className+
							"::"+t.local.get(1).name+"(__"+t.className+"* __this){\n\t\t"+
							"delete __this;\n\t"+
							"}\n");
		
	//--- adds all methods to METHODDEF	
		int size = t.local.size();
		for (int j =2;j<size;j++) {
			Declaration method= t.local.get(j);
                    //method syntax
                    cpp_methoddef.write("\t"+method.returntype+" __"+t.className+
                                                            "::"+method.name);
                    if(method.overloadNum!=0)
                            cpp_methoddef.write("_"+method.overloadNum);
                    cpp_methoddef.write("(");
					
                    for(int i=0;i<method.params.size();i++){
                        Fparam param = method.params.get(i);

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
                    cpp_methoddef.write("\n\t}\n\n");

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
		for(String p: dependencies.getPackageToNamespace()){
			h_classdef.write("}\n");
			cpp_methoddef.write("}\n");
		}
		cpp_methoddef.close();
		h_classdef.close();
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



