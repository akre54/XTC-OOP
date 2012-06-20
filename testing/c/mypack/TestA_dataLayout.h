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
#include "TestB_dataLayout.h"

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

//data layout for c.mypackTestA
	struct __TestA{ 
		__TestA_VT* __vptr;
		int32_t i;
		TestA __this;


		__TestA(int32_t arg1):__vptr(&__vtable),i(0){
		//empty constructor. All work done in init
	   }

		void init(TestA,int32_t);
		static Class __class();

		static __TestA_VT __vtable;
	};

//vtable layout for c.mypackTestA
	struct __TestA_VT{
		Class __isa;
		void (*__delete)(__TestA*);
		int32_t (*hashCode)(TestA);
		bool (*equals)(TestA, Object);
		Class (*getClass)(TestA);
		String (*toString)(TestA);


		__TestA_VT():
			__isa(__TestA::__class()),
			__delete(&__rt::__delete<__TestA>),
			hashCode((int32_t(*)(TestA))&__Object::hashCode),
			equals((bool(*)(TestA, Object))&__Object::equals),
			getClass((Class(*)(TestA))&__Object::getClass),
			toString((String(*)(TestA))&__Object::toString){}
	};

}
}
