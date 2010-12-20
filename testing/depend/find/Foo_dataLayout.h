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

#include "Bar_dataLayout.h"

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

namespace depend {
namespace find {

	struct __Foo; 
	struct __Foo_VT;
	typedef __rt::Ptr<__Foo> Foo;
	typedef __rt::Ptr<__Array<Foo> > ArrayOfFoo;

//data layout for depend.findFoo
	struct __Foo{ 
		__Foo_VT* __vptr;
		Foo __this;


		__Foo():__vptr(&__vtable){
		//empty constructor. All work done in init
	   }

		void init(Foo);		static Class __class();
		static int32_t main(int32_t, char**);
		static void m1();

		static __Foo_VT __vtable;
	};

//vtable layout for depend.findFoo
	struct __Foo_VT{
		Class __isa;
		void (*__delete)(__Foo*);
		int32_t (*hashCode)(Foo);
		bool (*equals)(Foo, Object);
		Class (*getClass)(Foo);
		String (*toString)(Foo);


		__Foo_VT():
			__isa(__Foo::__class()),
			__delete(&__rt::__delete<__Foo>),
			hashCode((int32_t(*)(Foo))&__Object::hashCode),
			equals((bool(*)(Foo, Object))&__Object::equals),
			getClass((Class(*)(Foo))&__Object::getClass),
			toString((String(*)(Foo))&__Object::toString){}
	};

}
}
