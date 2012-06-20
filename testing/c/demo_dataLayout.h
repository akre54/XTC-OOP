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

#include "mypack/TestA_dataLayout.h"
#include "mypack/TestB_dataLayout.h"

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
using c::mypack::TestA;
using c::mypack::__TestA;
using c::mypack::TestA;
using c::mypack::__TestA;
using c::mypack::TestB;
using c::mypack::__TestB;
using c::mypack::TestB;
using c::mypack::__TestB;

namespace c {

	struct __demo; 
	struct __demo_VT;
	typedef __rt::Ptr<__demo> demo;
	typedef __rt::Ptr<__Array<demo> > ArrayOfdemo;

//data layout for cdemo
	struct __demo{ 
		__demo_VT* __vptr;
		int32_t i;
		demo __this;


		__demo():__vptr(&__vtable),i(5){
		//empty constructor. All work done in init
	   }

		void init(demo);
		static Class __class();
		static void m1(demo);
		static int32_t main(int32_t, char**);

		static __demo_VT __vtable;
	};

//vtable layout for cdemo
	struct __demo_VT{
		Class __isa;
		void (*__delete)(__demo*);
		int32_t (*hashCode)(demo);
		bool (*equals)(demo, Object);
		Class (*getClass)(demo);
		String (*toString)(demo);
		void (*m1)(demo);


		__demo_VT():
			__isa(__demo::__class()),
			__delete(&__rt::__delete<__demo>),
			hashCode((int32_t(*)(demo))&__Object::hashCode),
			equals((bool(*)(demo, Object))&__Object::equals),
			getClass((Class(*)(demo))&__Object::getClass),
			toString((String(*)(demo))&__Object::toString),
			m1(&__demo::m1){}
	};

}
