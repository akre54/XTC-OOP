/*
 * Object-Oriented Programming
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
// The following declaration is necessary to access method
// implementations for java.lang.Object.  It might be better
// to always use fully qualified names and no using declarations.
using java::lang::__Object;
using java::lang::Class;
using java::lang::String;

namespace oop {

  // Forward declarations of data layout and vtables.
  struct __Foo;
  struct __Foo_VT;

  struct __Bar;
  struct __Bar_VT;
  
  // Definition of convenient type names.
  typedef __Foo* Foo;
  typedef __Bar* Bar;

  // ======================================================================

  // The data layout for oop.Foo.
  struct __Foo {
    __Foo_VT* __vptr;
    int32_t i;
    
    __Foo() : __vptr(&__vtable), i(0) {
    };
    
    // --------------------------------------------------------------------
    
    static Class __class();

    // The methods implemented by oop.Foo.
    static String toString(Foo);
    static void m1(Foo);
    static bool m2(Foo);
    static double m3();

    private:
      // The vtable for oop.Foo.
      static __Foo_VT __vtable;
    };

  // The vtable layout for oop.Foo.
  struct __Foo_VT {
    Class __isa;
    int32_t (*hashCode)(Foo);
    bool (*equals)(Foo, Object);
    Class (*getClass)(Foo);
    String (*toString)(Foo);
    void (*m1)(Foo);
    
    __Foo_VT()
    : __isa(__Foo::__class()),
      hashCode((int32_t(*)(Foo))&__Object::hashCode),
      equals((bool(*)(Foo,Object))&__Object::equals),
      getClass((Class(*)(Foo))&__Object::getClass),
      toString(&__Foo::toString),
      m1(&__Foo::m1) {
    }
  };

  // ======================================================================

  // The data layout for oop.Bar.
  struct __Bar {
    __Bar_VT* __vptr;
    int32_t i;
    int32_t j;
    
    __Bar() : __vptr(&__vtable), i(0), j(0) {
    }

    // --------------------------------------------------------------------

    // The function returning the class object representing
    // oop.Bar.
    static Class __class();

    // The instance methods of oop.Bar.
    static void m1(Bar);
    static bool m2(Bar);

  private:
    // The vtable for oop.Bar.
    static __Bar_VT __vtable;
  };

  // The vtable layout for oop.Bar.
  struct __Bar_VT {
    Class __isa;
    int32_t (*hashCode)(Bar);
    bool (*equals)(Bar, Object);
    Class (*getClass)(Bar);
    String (*toString)(Bar);
    void (*m1)(Bar);

    __Bar_VT()
    : __isa(__Bar::__class()),
      hashCode((int32_t(*)(Bar))&__Object::hashCode),
      equals((bool(*)(Bar,Object))&__Object::equals),
      getClass((Class(*)(Bar))&__Object::getClass),
      toString((String(*)(Bar))&__Foo::toString),
      m1(&__Bar::m1) {
    }
  };

}
