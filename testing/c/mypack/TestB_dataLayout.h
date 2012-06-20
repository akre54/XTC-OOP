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

#pragma once

#include "java_lang.h"
#include "TestA_dataLayout.h"

using java::lang::Object;
using java::lang::__Object;
using java::lang::Class;
using java::lang::__Class;
using java::lang::String;
using java::lang::__String;
using java::lang::__Array;
using java::lang::ArrayOfInt;
using java::lang::ArrayOfObject;
using java::lang::ArrayOfClass;

namespace c {
namespace mypack {

	struct __TestA; 
	struct __TestA_VT;
	typedef __rt::Ptr<__TestA> TestA;
	typedef __rt::Ptr<__Array<TestA> > ArrayOfTestA;


	struct __TestB; 
	struct __TestB_VT;
	typedef __rt::Ptr<__TestB> TestB;
	typedef __rt::Ptr<__Array<TestB> > ArrayOfTestB;

//data layout for c.mypackTestB
	struct __TestB{ 
		__TestB_VT* __vptr;
		double x;
		TestB __this;


		__TestB(double y):__vptr(&__vtable),x(x){
		//empty constructor. All work done in init
	   }

		void init(TestB,double);
		static Class __class();

		static __TestB_VT __vtable;
	};

//vtable layout for c.mypackTestB
	struct __TestB_VT{
		Class __isa;
		void (*__delete)(__TestB*);
		int32_t (*hashCode)(TestB);
		bool (*equals)(TestB, Object);
		Class (*getClass)(TestB);
		String (*toString)(TestB);


		__TestB_VT():
			__isa(__TestB::__class()),
			__delete(&__rt::__delete<__TestB>),
			hashCode((int32_t(*)(TestB))&__Object::hashCode),
			equals((bool(*)(TestB, Object))&__Object::equals),
			getClass((Class(*)(TestB))&__Object::getClass),
			toString((String(*)(TestB))&__Object::toString){}
	};

}
}
