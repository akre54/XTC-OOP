/*
 * edited by Liz 10-14-2010
 *creates a c++ class defintion headerfile and a c++ method definition .cpp file
 *also creates an ArrayList<String> for each class to simulate virtual tables
 *these ArrayLists will actually hold method ptr and ptr assignment statements
 *for easy writing to .h file VTable
 */
public class InheritanceBuilder{
	
	
	CppCreator h_classdef;
	CppCreator cpp_methoddef;
	ArrayList<String>; 

	
	InheritanceBuilder(File jfile){
		/*
		 *creates new cc file h_classdef
		 *copies start of translation.h into h_classdef
		 */
		h_classdef = (new CppCreator(jfile,"h")).cFile;
		h_classdef.write("/*
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
						 
						 #pragma once
						 
						 #include \"java_lang.h\"
						 
						 using java::lang::Object;
						 // The following declaration is necessary to access method
						 // implementations for java.lang.Object.  It might be better
						 // to always use fully qualified names and no using declarations.
						 using java::lang::__Object;
						 using java::lang::Class;
						 using java::lang::String;
						 
						 namespace oop {
						 ");//do not forget that } is needed to close namespace after all classes defined
		
		/*
		 *creates new cc file cc_methoddef
		 *copies start of translation.cc into cc_classdef
		 */
		cpp_methoddef =(new CppCreator(jfile)).cFile;
		cpp_methoddef.write("/*
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
							
							#include "java_lang.h"
							
							#include <sstream>
							
							namespace oop {
							");//do not forget that } is need to close namespace after all methods defined
		
	}
						
	/*
	 * writes to the h_classdef file Gnode n's class declaration and Vtable
	 *	same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.h from class notes
	 */
	public static void addClassdef(/*passes the classdeclaration (maybe AST node)*/Gnode n){
		
		String Classname = classDeclaration.getName();
		
		h_classdef.write(
			"struct __"+ClassName+"; \ln"+/**/
			"struct __"+ClassName+"_VT;\ln"+/**/
						 
			"typedef __"+ClassName+" = "+Classname+";\ln"+/**/
		
			"struct __"+ClassName+"{ \ln"+/**/
				"__"+ClassName+"_VT __vptr;\ln"+
						 
				/* FEILDS ---> ex: int x;  */
				write_all_feilds(n)+"\ln"+
				
				/*CONSTRUCTOR initializer syntax  */
				ClassName+"():__vpt(&__vtable)"+ write_all_assigned_feilds(n)+"{};\ln"+
						 
				/*  */
				"static Class __class();\ln"+/**/
				
				write_all_methods(n)+"/ln"+
						 
				/*create instance of VTABLE*/
				"private:  static __"+ClassName+"_VT __vtable;\ln"+/**/
			"};\ln"+                      
		/*-------------------------end of struct __ClassName in .h file-------------------------*/	
						 
		/* ---------------------start of stuct __ClassName_VT in .h file -------------------*/
			
			"struct __"+ClassName+"_VT{\ln"+
				
				/* DECLARE METHOD PTRS ---> ",methodreturnType (*methodname)(methodparameters)",\ln" */
				write_all_method_ptrs(n)+/**/
						
						 
				"__"+ClassName+"_VT():\ln"+
						 
						/* INITIALIZE METHOD PTRS ---> methodname"(&__"+ClassName+"::"+methodname+"),\ln" */
						write_assign_method_ptrs(n)+/**/
				"{}\ln"+
						 
						 
			"};"/* -----------end of stuct __ClassName_VT in .h file -------------------*/
						 
		);// end of writing
			
	}//end of addClassdef
	
	
	/*
	 * will return a concatinated string of all feild declarations in Gnode n
	 */
	private String write_all_feilds(Gnode n){
	}
	
	
	/*
	 * will return a concatinated string of all initalized feilds in Gnode n
	 * syntax --->   ","feildname+"("+initial value+")\ln"
	 */	
	private String write_all_assigned_feilds(Gnode n){
	}
	
	
	/*
	 * will return a concatinated string of all method declarations in Gnode n
	 * 
	 */		
	private String write_all_methods(Gnode n){
	}
	
	
	/*
	 * will return a concatinated string of all methods ptr declarations from superclass's Vtable
	 * +  method ptr declarations of public methods in Gnode n's class 
	 * syntax --->  ",methodreturnType (*methodname)(methodparameters)",\ln"
	 *
	 * also creates a new ArrayList<String> that has :
	 *		all elements of the ArrayList Gnode n's ExtenstionType() a.k.a. Superclass
	 *   +  all String representations of method prt declarations with syntax above^ from this class
	 */	
	private String write_all_method_ptrs(Gnode n){
	}
	
	
	/*
	 * will return a concatinated string of all initalized method ptrs from superclass's Vtable 
	 * + initialized method ptrs of public methods in Gnode n's class
	 * syntax --->  methodname"(&__"+ClassName+"::"+methodname+"),\ln"
	 *
	 * also creates a new ArrayList<String> that has :
	 *		all elements of the ArrayList Gnode n's ExtenstionType() a.k.a. Superclass
	 *   +  all String representations of method prt declarations with syntax above^ from this class
	 */	
	private String write_assign_method_ptrs(Gnode n){
	}
	
	
	/*
	 *writes to the ccp_methoddef all of Gnode's methods functionality
	 *same structure as http://cs.nyu.edu/rgrimm/teaching/fa10-oop/1007/java_lang.cc from class notes
	 *
	 */
	public static void addMethodDec(Gnode n){
	}
}


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
 */