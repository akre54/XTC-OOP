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

#include "java_lang.h"

#include <sstream>
#include <cstring>

namespace java {
  namespace lang {

    // The destructor for java.lang.Object.
    void __Object::__delete(__Object* __this) {
      delete __this;
    }

    // java.lang.Object.hashCode()
    int32_t __Object::hashCode(Object __this) {
      return (int32_t)(intptr_t)__this.raw();
    }

    // java.lang.Object.equals(Object)
    bool __Object::equals(Object __this, Object other) {
      return __this == other;
    }

    // java.lang.Object.getClass()
    Class __Object::getClass(Object __this) {
      return __this->__vptr->__isa;
    }

    // java.lang.Object.toString()
    String __Object::toString(Object __this) {
      // Class k = this.getClass();
      Class k = __this->__vptr->getClass(__this);

      std::ostringstream sout;
      sout << k->__vptr->getName(k)
           << '@' << std::hex << (uintptr_t)__this.raw();
      return __rt::stringify(sout.str());
    }

    // Internal accessor for java.lang.Object's class.
    Class __Object::__class() {
      static Class k = new __Class(__rt::stringify("java.lang.Object"),
                                   __rt::null());
      return k;
    }

    // The vtable for java.lang.Object.  Note that this definition
    // invokes the default no-arg constructor for __Object_VT.
    __Object_VT __Object::__vtable;

    // =======================================================================

    // The destructor for java.lang.String.
    void __String::__delete(__String* __this) {
      delete __this;
    }

    // java.lang.String.hashCode()
    int32_t __String::hashCode(String __this) {
      int32_t hash = 0;

      // Use a C++ operator to iterate over the string's characters.
      for (std::string::iterator itr = __this->data.begin();
           itr < __this->data.end();
           itr++) {
        hash = 31 * hash + *itr;
      }

      return hash;
    }

    // java.lang.String.equals()
    bool __String::equals(String __this, Object o) {
      // Make sure object is a string:
      // if (! o instanceof String) return false;
      Class k = __String::__class();
      if (! k->__vptr->isInstance(k, o)) return false;

      // Do the actual comparison.
      String other = (String)o; // Downcast.
      return __this->data.compare(other->data) == 0;
    }

    // java.lang.String.toString()
    String __String::toString(String __this) {
      return __this;
    }

    // java.lang.String.length()
    int32_t __String::length(String __this) {
      return __this->data.length();
    }

    // java.lang.String.charAt()
    char __String::charAt(String __this, int32_t idx) {
      if (0 > idx || idx >= __this->data.length()) {
        throw IndexOutOfBoundsException();
      }

      // Use std::string::operator[] to get character
      // without duplicate range check.
      return __this->data[idx];
    }

    // Internal accessor for java.lang.String's class.
    Class __String::__class() {
      static Class k = new __Class(__rt::stringify("java.lang.String"),
                                   __Object::__class());
      return k;
    }

    // The vtable for java.lang.String.  Note that this definition
    // invokes the default no-arg constructor for __String_VT.
    __String_VT __String::__vtable;

    // =======================================================================

    // The destructor for java.lang.Class.
    void __Class::__delete(__Class* __this) {
      delete __this;
    }

    // java.lang.Class.toString()
    String __Class::toString(Class __this) {
      if (__this->primitive) {
        return __this->name;
      } else {
        return __rt::stringify("class " + __this->name->data);
      }
    }

    // java.lang.Class.getName()
    String __Class::getName(Class __this) {
      return __this->name;
    }

    // java.lang.Class.getSuperclass()
    Class __Class::getSuperclass(Class __this) {
      return __this->parent;
    }

    // java.lang.Class.isPrimitive()
    bool __Class::isPrimitive(Class __this) {
      return __this->primitive;
    }

    // java.lang.Class.isArray()
    bool __Class::isArray(Class __this) {
      return __rt::null() != __this->component;
    }

    // java.lang.Class.getComponentType()
    Class __Class::getComponentType(Class __this) {
      return __this->component;
    }

    // java.lang.Class.isInstance(Object)
    bool __Class::isInstance(Class __this, Object o) {
      Class k = o->__vptr->getClass(o);

      do {
        if (__this->__vptr->equals(__this, k)) return true;

        k = k->__vptr->getSuperclass(k);
      } while (__rt::null() != k);

      return false;
    }

    // Internal accessor for java.lang.Class' class.
    Class __Class::__class() {
      static Class k = new __Class(__rt::stringify("java.lang.Class"),
                                   __Object::__class());
      return k;
    }

    // The vtable for java.lang.Class.  Note that this definition
    // invokes the default no-arg constructor for __Class_VT.
    __Class_VT __Class::__vtable;

    // =======================================================================

    // Internal accessor for int's class.
    Class __Integer::__primitiveClass() {
      static Class k = new __Class(__rt::stringify("int"),
                                   __rt::null(), __rt::null(), true);
      return k;
    }

    // =======================================================================

    // Template specialization for arrays of ints.
    template<>
    __Array<int32_t>::__Array(const int32_t length)
      : __vptr(&__vtable), length(length), __data(new int32_t[length]) {
      std::memset(__data, 0, length * sizeof(int32_t));
    }

    template<>
    Class __Array<int32_t>::__class() {
      static Class k = new __Class(__rt::stringify("[I"),
                                   __Object::__class(),
                                   __Integer::__primitiveClass());
      return k;
    }

    // Template specialization for arrays of objects.
    template<>
    Class __Array<Object>::__class() {
      static Class k = new __Class(__rt::stringify("[Ljava.lang.Object;"),
                                   __Object::__class(),
                                   __Object::__class());
      return k;
    }

    // Template specialization for arrays of classes.
    template<>
    Class __Array<Class>::__class() {
      static Class k = new __Class(__rt::stringify("[Ljava.lang.Class;"),
                                   __Array<Object>::__class(),
                                   __Class::__class());
      return k;
    }

  }
}

// ===========================================================================

namespace __rt {

  // Function returning the canonical null value.
  java::lang::Object null() {
    static java::lang::Object value(0);
    return value;
  }

}
