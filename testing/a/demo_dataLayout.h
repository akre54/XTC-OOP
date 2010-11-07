/* Object-Oriented Programming
* Copyright (C) 2010 Robert Grimm
*
*Edited by Liz Pelka
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

#include "java_lang.h"
using java::lang::Object;
using java::lang::__Object;
using java::lang::Class;
using java::lang::__Class;
using java::lang::String;
namespace xtc {
	namespace oop{

	typedef std::string String;
	struct __demo; 
	struct __demo_VT;

	typedef __demo* demo;

	struct __demo{ 
	   __demo_VT* __vptr;


	   static Class __class();
	   static int32_t main(int32_t, char**);


	   private: 
	   static __demo_VT __vtable;
	};


	struct __demo_VT{
		Class __isa;
		int32_t (*hashCode)(demo);
		bool (*equals)(demo);
		Class (*getClass)(demo);
		String (*toString)(demo);


	   __demo_VT():
		   __isa(__demo::__class()),
		   hashCode((int32_t(*)(demo))&__Object::hashCode),
		   equals((bool(*)(demo))&__Object::equals),
		   getClass((Class(*)(demo))&__Object::getClass),
		   toString((String(*)(demo))&__Class::toString){}
	};

	}//ends oop namespace
}//ends xtc namespace