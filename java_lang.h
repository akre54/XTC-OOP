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

#include <stdint.h>
#include <string>

namespace java {
  namespace lang {

    // Forward declarations of data layout and vtables.
    struct __Object;
    struct __Object_VT;

    struct __Class;
    struct __Class_VT;

    // Definition of convenient type names.
    typedef __Object* Object;
    typedef __Class* Class;
    typedef std::string String; // We re-use C++ strings.

    // ======================================================================

    // The data layout for java.lang.Object.
    struct __Object {
      __Object_VT* __vptr;

      __Object()
      : __vptr(&__vtable) {
      };

      // --------------------------------------------------------------------

      // The function returning the class object representing
      // java.lang.Object.
      //
      // We use a function instead of a static field to avoid C++'s
      // "static initialization fiasco".  C++ does not specify the
      // order, in which static fields are initialized.  However,
      // the class object for some type T must point to the class
      // object for its direct superclass S, i.e., the class object
      // for S must be created before the class object for T.
      // The function enforces this ordering by allocating the
      // class object on first invocation and returning the same
      // object on subsequent invocations.
      static Class __class();

      // The methods implemented by java.lang.Object.
      static int32_t hashCode(Object);
      static bool equals(Object, Object);
      static Class getClass(Object);
      static String toString(Object);

    private:
      // The vtable for java.lang.Object.
      static __Object_VT __vtable;
    };

    // The vtable layout for java.lang.Object.
    struct __Object_VT {
      Class __isa;
      int32_t (*hashCode)(Object);
      bool (*equals)(Object, Object);
      Class (*getClass)(Object);
      String (*toString)(Object);

      __Object_VT()
      : __isa(__Object::__class()),
        hashCode(&__Object::hashCode),
        equals(&__Object::equals),
        getClass(&__Object::getClass),
        toString(&__Object::toString) {
		}
    };

    // ======================================================================

    // The data layout for java.lang.Class.
    struct __Class {
      __Class_VT* __vptr;
      String name;
      Class parent;

      __Class(String name, Class parent)
      : __vptr(&__vtable),
        name(name),
        parent(parent) {
      }

      // --------------------------------------------------------------------

      // The function returning the class object representing
      // java.lang.Class.
      static Class __class();

      // The instance methods of java.lang.Class.
      static String toString(Class);
      static String getName(Class);
      static Class getSuperclass(Class);
      static bool isInstance(Class, Object);

    private:
      // The vtable for java.lang.Class.
      static __Class_VT __vtable;
    };

    // The vtable layout for java.lang.Class.
    struct __Class_VT {
      Class __isa;
      int32_t (*hashCode)(Class);
      bool (*equals)(Class, Object);
      Class (*getClass)(Class);
      String (*toString)(Class);
      String (*getName)(Class);
      Class (*getSuperclass)(Class);
      bool (*isInstance)(Class, Object);

      __Class_VT()
      : __isa(__Class::__class()),
        hashCode((int32_t(*)(Class))&__Object::hashCode),
        equals((bool(*)(Class,Object))&__Object::equals),
        getClass((Class(*)(Class))&__Object::getClass),
        toString(&__Class::toString),
        getName(&__Class::getName),
        getSuperclass(&__Class::getSuperclass),
        isInstance(&__Class::isInstance) {
      }
    };

  }
}
