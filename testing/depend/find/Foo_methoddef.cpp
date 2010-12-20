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

#include "Foo_dataLayout.h"

#include <sstream>

namespace depend {
namespace find {


	Class __Foo::__class(){
		static Class k = new __Class(__rt::stringify("depend.find.Foo"),__rt::null());
		return k;
	}
		void __Foo::init(Foo __passedthis) {
			__passedthis->__this = __passedthis;
		}

	int32_t __Foo::main(int32_t argc,char** args){
Bar b=new __Bar();
b->init(b);

	}

	java::lang::String __Foo::m1(){
return __this->var; 

	}

	__Foo_VT __Foo::__vtable;

}}//close depend::find::Foo
	namespace java {
	namespace lang {
		template<>
		Class __Array<depend::find::Foo>::__class() {
			static Class k = new __Class(__rt::stringify("[Ldepend.find.Foo"),
									__Array<java::lang::Object>::__class(),
									depend::find::__Foo::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

namespace depend {
namespace find {
	Class __Bar::__class(){
		static Class k = new __Class(__rt::stringify("depend.find.Bar"),__rt::null());
		return k;
	}
		void __Bar::init(Bar __passedthis) {
			__passedthis->__this = __passedthis;
Foo::m1();
				}

	depend::find::Foo __Bar::m2(){
Foo f=new __Foo();
f->init(f);
return f; 

	}

	__Bar_VT __Bar::__vtable;

}}//close depend::find::Bar
	namespace java {
	namespace lang {
		template<>
		Class __Array<depend::find::Bar>::__class() {
			static Class k = new __Class(__rt::stringify("[Ldepend.find.Bar"),
									__Array<depend::find::Foo>::__class(),
									depend::find::__Bar::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

namespace depend {
namespace find {
}
}
