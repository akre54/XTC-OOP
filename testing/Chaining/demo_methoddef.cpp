/* Object-Oriented Programming
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

#include "demo_dataLayout.h"

#include <sstream>



	Class __demo::__class(){
		static Class k = new __Class(__rt::stringify("demo"),__rt::null());
		return k;
	}
	int32_t __demo::main(int32_t argc,char** args){
beta k1=new __beta();
k1->init(k1);
if("xtc.oop.Test"equals(__thisk1->__vptr->getName(k1))){
std::cout<<"PASS \"xtc.oop.Test\".equals(k1.getName())" <<std::endl;
}
else { 
std::cout<<"FAIL \"xtc.oop.Test\".equals(k1.getName())" <<std::endl;
} 

	}

	__demo_VT __demo::__vtable;

//close demo
	namespace java {
	namespace lang {
		template<>
		Class __Array<demo>::__class() {
			static Class k = new __Class(__rt::stringify("[L.demo"),
									__Array<java::lang::Object>::__class(),
									__demo::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

	Class __beta::__class(){
		static Class k = new __Class(__rt::stringify("beta"),__rt::null());
		return k;
	}
		void __beta::init(beta __passedthis) {
			__passedthis->__this = __passedthis;
		}

	beta __beta::m1(beta __this){
beta a=new __beta();
a->init(a);
return a; 

	}

	beta __beta::m2(beta __this){
beta b=new __beta();
b->init(b);
return b; 

	}

	java::lang::String __beta::m3(beta __this){
return new __String("thisssss"); 

	}

	java::lang::String __beta::getName(beta __this){
return new __String("xtc.oop.Test"); 

	}

	__beta_VT __beta::__vtable;

//close beta
	namespace java {
	namespace lang {
		template<>
		Class __Array<beta>::__class() {
			static Class k = new __Class(__rt::stringify("[L.beta"),
									__Array<java::lang::Object>::__class(),
									__beta::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

