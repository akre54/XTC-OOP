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
#include <cstring>

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
      Class component;
      bool primitive;

    __Class(String name, Class parent, Class comp = 0, bool prim = false)
      : __vptr(&__vtable),
        name(name),
        parent(parent),
        component(comp),
        primitive(prim) {
      }

      // --------------------------------------------------------------------

      // The function returning the class object representing
      // java.lang.Class.
      static Class __class();

      // The instance methods of java.lang.Class.
      static String toString(Class);
      static String getName(Class);
      static Class getSuperclass(Class);
      static Class getComponentType(Class);
      static bool isPrimitive(Class);
      static bool isArray(Class);
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
      bool (*isPrimitive)(Class);
      bool (*isArray)(Class);
      Class (*getComponentType)(Class);
      bool (*isInstance)(Class, Object);

      __Class_VT()
      : __isa(__Class::__class()),
        hashCode((int32_t(*)(Class))&__Object::hashCode),
        equals((bool(*)(Class,Object))&__Object::equals),
        getClass((Class(*)(Class))&__Object::getClass),
        toString(&__Class::toString),
        getName(&__Class::getName),
        getSuperclass(&__Class::getSuperclass),
        isPrimitive(&__Class::isPrimitive),
        isArray(&__Class::isArray),
        getComponentType(&__Class::getComponentType),
        isInstance(&__Class::isInstance) {
      }
    };

    // ======================================================================

    // The completely incomplete data layout for java.lang.Integer.
    struct __Integer {
      // Instance fields would go here.

      // The function returning the class object representing
      // primitive ints.
      static Class __primitiveClass();
    };

    // ======================================================================

    // For simplicity, we use C++ inheritance for exception types
    // and throw them by value (see below).  In other words, the
    // translator does not support user defined exceptions and simply
    // uses a few built-in classes.
    class Throwable {
    };

    class Exception : public Throwable {
    };

    class RuntimeException : public Exception {
    };

    class NullPointerException : public RuntimeException {
    };

    class ArrayStoreException : public RuntimeException {
    };

    class ClassCastException : public RuntimeException {
    };

    class IndexOutOfBoundsException : public RuntimeException {
    };

    class ArrayIndexOutOfBoundsException : public IndexOutOfBoundsException {
    };

    // ======================================================================

    // Forward declarations of data layout and vtables.
    template <typename T>
    struct __Array;

    template <typename T>
    struct __Array_VT;

    // Definition of convenient type names.
    typedef __Array<int32_t>* ArrayOfInt;
    typedef __Array<Object>* ArrayOfObject;
    typedef __Array<Class>* ArrayOfClass;

    // The data layout for arrays.
    template <typename T>
    struct __Array {
      __Array_VT<T>* __vptr;
      const int32_t length;
      T* __data;

      __Array(const int32_t length)
      : __vptr(&__vtable), length(length), __data(new T[length]) {
        std::memset(__data, 0, length * sizeof(T));
      }

      static Class __class();

      T& operator[](int idx) {
        if (0 > idx || idx >= length) throw ArrayIndexOutOfBoundsException();
        return __data[idx];
      }

      const T& operator[](int idx) const {
        if (0 > idx || idx >= length) throw ArrayIndexOutOfBoundsException();
        return __data[idx];
      }

    private:
      static __Array_VT<T> __vtable;
    };

    // The vtable layout for arrays.
    template <typename T>
    struct __Array_VT {
      typedef __Array<T>* Array;

      Class __isa;
      int32_t (*hashCode)(Array);
      bool (*equals)(Array, Object);
      Class (*getClass)(Array);
      String (*toString)(Array);

      __Array_VT()
      : __isa(__Array<T>::__class()),
        hashCode((int32_t(*)(Array))&__Object::hashCode),
        equals((bool(*)(Array,Object))&__Object::equals),
        getClass((Class(*)(Array))&__Object::getClass),
        toString((String(*)(Array))&__Object::toString) {
      }

    };

    // The header file declares each template (see above) just as for
    // regular C++ classes.  However, since the compiler needs to know
    // how to instantiate each template, the header file also defines
    // each template.

    // The vtable for arrays.  Note that this definition uses the
    // the default no-arg constructor.
    template <typename T>
    __Array_VT<T> __Array<T>::__vtable;

    // But where is the definition of __class()???

    // ======================================================================

    // Template function to check against null values.
    template<typename T>
    void __checkNotNull(T o) {
      if (0 == o) throw NullPointerException();
    }

    // Template function to check array indices.
    template<typename T>
    void __checkArrayIndex(__Array<T>* array, int32_t index) {
      if (0 > index || index >= array->length) {
        throw ArrayIndexOutOfBoundsException();
      }
    }

    // Template function to check array stores.
    template<typename T, typename U>
    void __checkArrayStore(__Array<T>* array, U object) {
      if (0 != object) {
        Class arraytype = array->__vptr->getClass(array);
        Class eltype = arraytype->__vptr->getComponentType(arraytype);

        if (! eltype->__vptr->isInstance(eltype, (Object)object)) {
          throw ArrayStoreException();
        }
      }
    }

  }
}