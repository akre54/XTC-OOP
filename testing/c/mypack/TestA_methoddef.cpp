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

#include "TestA_dataLayout.h"

#include <sstream>

namespace c {
namespace mypack {


	Class __TestA::__class(){
		static Class k = new __Class(__rt::stringify("c.mypackTestA"),__rt::null());
		return k;
	}
		void __TestA::init(TestA __passedthis,int32_t arg1) {
			__passedthis->__this = __passedthis;
__this->i=arg1;
				}

	__TestA_VT __TestA::__vtable;

}}//close c::mypack::TestA
	namespace java {
	namespace lang {
		template<>
		Class __Array<c::mypack::TestA>::__class() {
			static Class k = new __Class(__rt::stringify("[Lc.mypack.TestA"),
									__Array<java::lang::Object>::__class(),
									c::mypack::__TestA::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

namespace c {
namespace mypack {
}
}
