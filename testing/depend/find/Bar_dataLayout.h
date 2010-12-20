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
#include "Foo_dataLayout.h"

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

	struct __Bar; 
	struct __Bar_VT;
	typedef __rt::Ptr<__Bar> Bar;
	typedef __rt::Ptr<__Array<Bar> > ArrayOfBar;

//data layout for depend.findBar
	struct __Bar{ 
		__Bar_VT* __vptr;
		String var;
		static Object R;
		Bar __this;


		__Bar():__vptr(&__vtable),var(new __String("")),R(new __Object()){
		//empty constructor. All work done in init
	   }

		void init(Bar);
		static Class __class();
		static depend::find::Foo m2();

		static __Bar_VT __vtable;
	};

//vtable layout for depend.findBar
	struct __Bar_VT{
		Class __isa;
		void (*__delete)(__Bar*);
		int32_t (*hashCode)(Bar);
		bool (*equals)(Bar, Object);
		Class (*getClass)(Bar);
		String (*toString)(Bar);


		__Bar_VT():
			__isa(__Bar::__class()),
			__delete(&__rt::__delete<__Bar>),
			hashCode((int32_t(*)(Bar))&__Object::hashCode),
			equals((bool(*)(Bar, Object))&__Object::equals),
			getClass((Class(*)(Bar))&__Object::getClass),
			toString((String(*)(Bar))&__Object::toString){}
	};

}
}
