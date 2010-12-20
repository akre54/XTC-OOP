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

#include "Bar_dataLayout.h"

#include <sstream>

namespace depend {
namespace find {


	Class __Bar::__class(){
		static Class k = new __Class(__rt::stringify("depend.findBar"),__rt::null());
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
