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

#include "cast_dataLayout.h"

#include <sstream>



Class __cast::__class(){
  static Class k = new __Class(__rt::stringify("cast"),__rt::null());
  return k;
}
int32_t __cast::main(int32_t argc,char** args){
  Magoo ma=new __Magoo();
  ma->init(ma);
  Mister mi=new __Mister(4);
  mi->init(mi,4);
  mi->__vptr->printer(mi);
  ma=({__rt::java_cast<Mister,Magoo>(mi); });

  Mister mr=({__rt::java_cast<Mister,Mister>(mi); });
  ma->__vptr->printer1(ma);

}

	__cast_VT __cast::__vtable;

namespace java {
  namespace lang {
	template<>
	Class __Array<cast>::__class() {
	  static Class k = new __Class(__rt::stringify("[L.cast"),
								   __Array<java::lang::Object>::__class(),
								   __cast::__class());
	  return k;
	}

  }
} // closing java::lang for arrays
//===========================================================================

Class __Magoo::__class(){
  static Class k = new __Class(__rt::stringify("Magoo"),__rt::null());
  return k;
}
void __Magoo::printer1(Magoo __this){
  std::cout<<"I Am A MAGOO"<<std::endl;

}

__Magoo_VT __Magoo::__vtable;

namespace java {
  namespace lang {
	template<>
	Class __Array<Magoo>::__class() {
	  static Class k = new __Class(__rt::stringify("[L.Magoo"),
								   __Array<java::lang::Object>::__class(),
								   __Magoo::__class());
	  return k;
	}

  }
} // closing java::lang for arrays
//===========================================================================

Class __Mister::__class(){
  static Class k = new __Class(__rt::stringify("Mister"),__rt::null());
  return k;
}
void __Mister::printer(Mister __this){
  std::cout<<"I Am Mister!"<<std::endl;

}

__Mister_VT __Mister::__vtable;

namespace java {
  namespace lang {
	template<>
	Class __Array<Mister>::__class() {
	  static Class k = new __Class(__rt::stringify("[L.Mister"),
								   __Array<Magoo>::__class(),
								   __Mister::__class());
	  return k;
	}

  }
} // closing java::lang for arrays
//===========================================================================

