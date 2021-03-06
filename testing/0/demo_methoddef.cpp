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

#include "demo_dataLayout.h"

#include <sstream>



	Class __demo::__class(){
		static Class k = new __Class(__rt::stringify("demo"),__rt::null());
		return k;
	}
	int32_t __demo::main(int32_t argc,char** args){
int32_t x=1-1;

	}

	__demo_VT __demo::__vtable;

namespace java {
	namespace lang {
	template<>
	Class __Array<demo>::__class() {
		static Class k = new __Class(__rt::stringify("[L.demo"),
								__Array<java::lang::Object>::__class(),
								__demo::__class());
		return k;
	}

	}
} // closing java::lang for arrays
	//===========================================================================

