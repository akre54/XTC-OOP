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
using java::lang::ArrayOfString;
using java::lang::ArrayOfBoolean;
using java::lang::ArrayOfFloat;
using java::lang::ArrayOfDouble;
using java::lang::ArrayOfShort;
using java::lang::ArrayOfLong;
using java::lang::ArrayOfChar;
using __rt::java_cast;

namespace xtc {
namespace oop {

	struct __Test; 
	struct __Test_VT;
	typedef __rt::Ptr<__Test> Test;
	typedef __rt::Ptr<__Array<Test> > ArrayOfTest;


	struct __Rest; 
	struct __Rest_VT;
	typedef __rt::Ptr<__Rest> Rest;
	typedef __rt::Ptr<__Array<Rest> > ArrayOfRest;

//data layout for xtc.oopTest
	struct __Test{ 
		__Test_VT* __vptr;
		int32_t count;
		Test __this;


		__Test():__vptr(&__vtable),count(0){
		//empty constructor. All work done in init
	   }

		void init(Test);
		static Class __class();
		static java::lang::Object m1(Test);
		static java::lang::Object m2();
		static Test m3(Test);
		static Test m4(Test);
		static Test m5(Test,Test);
		static java::lang::Object m6(Test,Test);
		static java::lang::Object m6_1(Test,Rest);
		static java::lang::Object m7(Test,java::lang::Object);
		static java::lang::Object m7_1(Test,java::lang::String);
		static java::lang::Object m7_2(Test,Test);
		static java::lang::Object m7_3(Test,Rest);
		static java::lang::Object m8(Test,Test);
		static java::lang::Object m8_1(Test,Rest);
		static java::lang::Object m8_2(Test,Test,Test);
		static java::lang::Object m8_3(Test,Rest,Test);
		static int32_t main(int32_t, char**);

		static __Test_VT __vtable;
	};

//vtable layout for xtc.oopTest
	struct __Test_VT{
		Class __isa;
		void (*__delete)(__Test*);
		int32_t (*hashCode)(Test);
		bool (*equals)(Test, Object);
		Class (*getClass)(Test);
		String (*toString)(Test);
		java::lang::Object (*m1)(Test);
		Test (*m3)(Test);
		Test (*m4)(Test);
		Test (*m5)(Test, Test);
		java::lang::Object (*m6)(Test, Test);
		java::lang::Object (*m6_1)(Test, Rest);
		java::lang::Object (*m7)(Test, java::lang::Object);
		java::lang::Object (*m7_1)(Test, java::lang::String);
		java::lang::Object (*m7_2)(Test, Test);
		java::lang::Object (*m7_3)(Test, Rest);
		java::lang::Object (*m8)(Test, Test);
		java::lang::Object (*m8_1)(Test, Rest);
		java::lang::Object (*m8_2)(Test, Test, Test);
		java::lang::Object (*m8_3)(Test, Rest, Test);


		__Test_VT():
			__isa(__Test::__class()),
			__delete(&__rt::__delete<__Test>),
			hashCode((int32_t(*)(Test))&__Object::hashCode),
			equals((bool(*)(Test, Object))&__Object::equals),
			getClass((Class(*)(Test))&__Object::getClass),
			toString((String(*)(Test))&__Object::toString),
			m1(&__Test::m1),
			m3(&__Test::m3),
			m4(&__Test::m4),
			m5(&__Test::m5),
			m6(&__Test::m6),
			m6_1(&__Test::m6_1),
			m7(&__Test::m7),
			m7_1(&__Test::m7_1),
			m7_2(&__Test::m7_2),
			m7_3(&__Test::m7_3),
			m8(&__Test::m8),
			m8_1(&__Test::m8_1),
			m8_2(&__Test::m8_2),
			m8_3(&__Test::m8_3){}
	};

//data layout for xtc.oopRest
	struct __Rest{ 
		__Rest_VT* __vptr;
		int32_t count;
		int32_t round;
		Rest __this;


		__Rest():__vptr(&__vtable),count(0),round(0){
		//empty constructor. All work done in init
	   }

		void init(Rest);
		static Class __class();
		static java::lang::Object m1(Rest);
		static java::lang::Object m2();
		static xtc::oop::Test m4(Rest);
		static java::lang::Object m7_4(Rest,xtc::oop::Test);
		static int32_t hashCode(Rest);

		static __Rest_VT __vtable;
	};

//vtable layout for xtc.oopRest
	struct __Rest_VT{
		Class __isa;
		void (*__delete)(__Rest*);
		int32_t (*hashCode)(Rest);
		bool (*equals)(Rest, Object);
		Class (*getClass)(Rest);
		String (*toString)(Rest);
		java::lang::Object (*m1)(Rest);
		Test (*m3)(Rest);
		Test (*m4)(Rest);
		Test (*m5)(Rest, Test);
		java::lang::Object (*m6)(Rest, Test);
		java::lang::Object (*m6_1)(Rest, Rest);
		java::lang::Object (*m7)(Rest, java::lang::Object);
		java::lang::Object (*m7_1)(Rest, java::lang::String);
		java::lang::Object (*m7_2)(Rest, Test);
		java::lang::Object (*m7_3)(Rest, Rest);
		java::lang::Object (*m8)(Rest, Test);
		java::lang::Object (*m8_1)(Rest, Rest);
		java::lang::Object (*m8_2)(Rest, Test, Test);
		java::lang::Object (*m8_3)(Rest, Rest, Test);
		java::lang::Object (*m7_4)(Rest, xtc::oop::Test);


		__Rest_VT():
			__isa(__Rest::__class()),
			__delete(&__rt::__delete<__Rest>),
			hashCode(&__Rest::hashCode),
			equals((bool(*)(Rest, Object))&__Object::equals),
			getClass((Class(*)(Rest))&__Object::getClass),
			toString((String(*)(Rest))&__Object::toString),
			m1(&__Rest::m1),
			m3((Test(*)(Rest))&__Test::m3),
			m4(&__Rest::m4),
			m5((Test(*)(Rest, Test))&__Test::m5),
			m6((java::lang::Object(*)(Rest, Test))&__Test::m6),
			m6_1((java::lang::Object(*)(Rest, Rest))&__Test::m6_1),
			m7((java::lang::Object(*)(Rest, java::lang::Object))&__Test::m7),
			m7_1((java::lang::Object(*)(Rest, java::lang::String))&__Test::m7_1),
			m7_2((java::lang::Object(*)(Rest, Test))&__Test::m7_2),
			m7_3((java::lang::Object(*)(Rest, Rest))&__Test::m7_3),
			m8((java::lang::Object(*)(Rest, Test))&__Test::m8),
			m8_1((java::lang::Object(*)(Rest, Rest))&__Test::m8_1),
			m8_2((java::lang::Object(*)(Rest, Test, Test))&__Test::m8_2),
			m8_3((java::lang::Object(*)(Rest, Rest, Test))&__Test::m8_3),
			m7_4(&__Rest::m7_4){}
	};

}
}
