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


	struct __cast; 
	struct __cast_VT;
	typedef __rt::Ptr<__cast> cast;
	typedef __rt::Ptr<__Array<cast> > ArrayOfcast;


	struct __Magoo; 
	struct __Magoo_VT;
	typedef __rt::Ptr<__Magoo> Magoo;
	typedef __rt::Ptr<__Array<Magoo> > ArrayOfMagoo;


	struct __Mister; 
	struct __Mister_VT;
	typedef __rt::Ptr<__Mister> Mister;
	typedef __rt::Ptr<__Array<Mister> > ArrayOfMister;

//data layout for cast
	struct __cast{ 
		__cast_VT* __vptr;
		cast __this;


		__cast():__vptr(&__vtable){};

		void init(cast __passedthis){
			__this = __passedthis;
		};		static Class __class();
		static int32_t main(int32_t, char**);

		static __cast_VT __vtable;
	};

//vtable layout for cast
	struct __cast_VT{
		Class __isa;
		void (*__delete)(__cast*);
		int32_t (*hashCode)(cast);
		bool (*equals)(cast, Object);
		Class (*getClass)(cast);
		String (*toString)(cast);


		__cast_VT():
			__isa(__cast::__class()),
			__delete(&__rt::__delete<__cast>),
			hashCode((int32_t(*)(cast))&__Object::hashCode),
			equals((bool(*)(cast, Object))&__Object::equals),
			getClass((Class(*)(cast))&__Object::getClass),
			toString((String(*)(cast))&__Object::toString){}
	};

//data layout for Magoo
	struct __Magoo{ 
		__Magoo_VT* __vptr;
		Magoo __this;


		__Magoo():__vptr(&__vtable){//empty constructor. All work done in init
	   }

		void init(Magoo __passedthis) {
			__this = __passedthis;
		}

		static Class __class();
		static void printer1(Magoo);

		static __Magoo_VT __vtable;
	};

//vtable layout for Magoo
	struct __Magoo_VT{
		Class __isa;
		void (*__delete)(__Magoo*);
		int32_t (*hashCode)(Magoo);
		bool (*equals)(Magoo, Object);
		Class (*getClass)(Magoo);
		String (*toString)(Magoo);
		void (*printer1)(Magoo);


		__Magoo_VT():
			__isa(__Magoo::__class()),
			__delete(&__rt::__delete<__Magoo>),
			hashCode((int32_t(*)(Magoo))&__Object::hashCode),
			equals((bool(*)(Magoo, Object))&__Object::equals),
			getClass((Class(*)(Magoo))&__Object::getClass),
			toString((String(*)(Magoo))&__Object::toString),
			printer1(&__Magoo::printer1){}
	};

//data layout for Mister
	struct __Mister{ 
		__Mister_VT* __vptr;
		Mister __this;


		__Mister(int32_t h):__vptr(&__vtable){//empty constructor. All work done in init
	   }

		void init(Mister __passedthis,int32_t h) {
			__this = __passedthis;
		}

		static Class __class();
		static void printer(Mister);

		static __Mister_VT __vtable;
	};

//vtable layout for Mister
	struct __Mister_VT{
		Class __isa;
		void (*__delete)(__Mister*);
		int32_t (*hashCode)(Mister);
		bool (*equals)(Mister, Object);
		Class (*getClass)(Mister);
		String (*toString)(Mister);
		void (*printer1)(Mister);
		void (*printer)(Mister);


		__Mister_VT():
			__isa(__Mister::__class()),
			__delete(&__rt::__delete<__Mister>),
			hashCode((int32_t(*)(Mister))&__Object::hashCode),
			equals((bool(*)(Mister, Object))&__Object::equals),
			getClass((Class(*)(Mister))&__Object::getClass),
			toString((String(*)(Mister))&__Object::toString),
			printer1((void(*)(Mister))&__Magoo::printer1),
			printer(&__Mister::printer){}
	};

