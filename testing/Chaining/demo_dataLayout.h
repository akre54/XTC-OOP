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


	struct __demo; 
	struct __demo_VT;
	typedef __rt::Ptr<__demo> demo;
	typedef __rt::Ptr<__Array<demo> > ArrayOfdemo;


	struct __beta; 
	struct __beta_VT;
	typedef __rt::Ptr<__beta> beta;
	typedef __rt::Ptr<__Array<beta> > ArrayOfbeta;

//data layout for demo
	struct __demo{ 
		__demo_VT* __vptr;
		demo __this;


		__demo():__vptr(&__vtable){};

		void init(demo __passedthis){
			__this = __passedthis;
		};		static Class __class();
		static int32_t main(int32_t, char**);

		static __demo_VT __vtable;
	};

//vtable layout for demo
	struct __demo_VT{
		Class __isa;
		void (*__delete)(__demo*);
		int32_t (*hashCode)(demo);
		bool (*equals)(demo, Object);
		Class (*getClass)(demo);
		String (*toString)(demo);


		__demo_VT():
			__isa(__demo::__class()),
			__delete(&__rt::__delete<__demo>),
			hashCode((int32_t(*)(demo))&__Object::hashCode),
			equals((bool(*)(demo, Object))&__Object::equals),
			getClass((Class(*)(demo))&__Object::getClass),
			toString((String(*)(demo))&__Object::toString){}
	};

//data layout for beta
	struct __beta{ 
		__beta_VT* __vptr;
		beta __this;


		__beta():__vptr(&__vtable){
		//empty constructor. All work done in init
	   }

		void init(beta);
		static Class __class();
		static beta m1(beta);
		static beta m2(beta);
		static java::lang::String m3(beta);

		static __beta_VT __vtable;
	};

//vtable layout for beta
	struct __beta_VT{
		Class __isa;
		void (*__delete)(__beta*);
		int32_t (*hashCode)(beta);
		bool (*equals)(beta, Object);
		Class (*getClass)(beta);
		String (*toString)(beta);
		beta (*m1)(beta);
		beta (*m2)(beta);
		java::lang::String (*m3)(beta);


		__beta_VT():
			__isa(__beta::__class()),
			__delete(&__rt::__delete<__beta>),
			hashCode((int32_t(*)(beta))&__Object::hashCode),
			equals((bool(*)(beta, Object))&__Object::equals),
			getClass((Class(*)(beta))&__Object::getClass),
			toString((String(*)(beta))&__Object::toString),
			m1(&__beta::m1),
			m2(&__beta::m2),
			m3(&__beta::m3){}
	};

