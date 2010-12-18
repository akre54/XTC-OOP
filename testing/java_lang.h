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

#include "ptr.h"

#include <stdint.h>
#include <string>

namespace java {
  namespace lang {

    // Forward declarations of data layout and vtables.
    struct __Object;
    struct __Object_VT;

    struct __String;
    struct __String_VT;

    struct __Class;
    struct __Class_VT;

    // Definition of convenient type names.
    typedef __rt::Ptr<__Object> Object;
    typedef __rt::Ptr<__String> String;
    typedef __rt::Ptr<__Class> Class;
  }
}

// ==========================================================================

namespace __rt {

  // The function returning the canonical null value.  See comment
  // below for java::lang::__Object::__class() as to why we use a
  // function.
  java::lang::Object null();

  // The virtual destructor for objects, implemented as a template
  // function.  The destructor must be virtual because C++'s delete
  // determines the size of memory to be deallocate baed on the
  // pointer's static type.  Consequently, every translated class
  // needs its own virtual destructor, which simply invokes C++'s
  // delete on the pointer.  Thanks to Louis Sanchez for suggesting
  // the use of a template function instead of generating the same
  // code (modulo type declaration) over and over again.
  template<typename T>
  void __delete(T* ptr) {
    delete ptr;
  }

}

// ==========================================================================

namespace java {
  namespace lang {

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

      // The vtable for java.lang.Object.
      static __Object_VT __vtable;
    };

    // The vtable layout for java.lang.Object.
    struct __Object_VT {
      Class __isa;
      void (*__delete)(__Object*);
      int32_t (*hashCode)(Object);
      bool (*equals)(Object, Object);
      Class (*getClass)(Object);
      String (*toString)(Object);

      __Object_VT()
      : __isa(__Object::__class()),
        __delete(&__rt::__delete<__Object>),
        hashCode(&__Object::hashCode),
        equals(&__Object::equals),
        getClass(&__Object::getClass),
        toString(&__Object::toString) {
      }
    };

    // ======================================================================

    // The data layout for java.lang.String.
    struct __String {
      __String_VT* __vptr;
      std::string data;

      __String(std::string data) 
      : __vptr(&__vtable),
        data(data) {
      }

      // The function retturning the class object representing
      // java.lang.String.
      static Class __class();

      // The methods implemented by java.lang.String.
      static int32_t hashCode(String);
      static bool equals(String, Object);
      static String toString(String);
      static int32_t length(String);
      static char charAt(String, int32_t);

      // The vtable for java.lang.String.
      static __String_VT __vtable;
    };

    // The vtable layout for java.lang.String.
    struct __String_VT {
      Class __isa;
      void (*__delete)(__String*);
      int32_t (*hashCode)(String);
      bool (*equals)(String, Object);
      Class (*getClass)(String);
      String (*toString)(String);
      int32_t (*length)(String);
      char (*charAt)(String, int32_t);

      __String_VT()
      : __isa(__String::__class()),
        __delete(&__rt::__delete<__String>),
        hashCode(&__String::hashCode),
        equals(&__String::equals),
        getClass((Class(*)(String))&__Object::getClass),
        toString(&__String::toString),
        length(&__String::length),
        charAt(&__String::charAt) {
      }
    };

    // The overloaded output operator for java.lang.String.
    inline std::ostream& operator<<(std::ostream& out, String s) {
      return out << s->data;
    }

    // ======================================================================

    // The data layout for java.lang.Class.
    struct __Class {
      __Class_VT* __vptr;
      String name;
      Class parent;
      Class component;
      bool primitive;

      __Class(String name, Class parent,
              Class comp = __rt::null(), bool prim = false)
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

      // The vtable for java.lang.Class.
      static __Class_VT __vtable;
    };

    // The vtable layout for java.lang.Class.
    struct __Class_VT {
      Class __isa;
      void (*__delete)(__Class*);
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
        __delete(__rt::__delete<__Class>),
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
	  //****************** my edits *******************
	  
	  // The completely incomplete data layout for java.lang.Double.
	  struct __Double {
		  
		  static Class __primitiveClass();
	  };
	  // The completely incomplete data layout for java.lang.Bool.
	  struct __Boolean {
		  
		  static Class __primitiveClass();
	  };
	  // The completely incomplete data layout for java.lang.Float.
	  struct __Float {
		  
		  static Class __primitiveClass();
	  };
	  
	  // The completely incomplete data layout for java.lang.Character.
	  struct __Character {
		  
		  static Class __primitiveClass();
	  };
	  
	  // The completely incomplete data layout for java.lang.Long.
	  struct __Long {
		  
		  static Class __primitiveClass();
	  };
	  
	  // The completely incomplete data layout for java.lang.Short.
	  struct __Short {
		  
		  static Class __primitiveClass();
	  };
	  
	  
	  //****************** end my edits ***************
	  
	  

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
    typedef __rt::Ptr<__Array<int32_t> > ArrayOfInt;
    typedef __rt::Ptr<__Array<Object> > ArrayOfObject;
    typedef __rt::Ptr<__Array<Class> > ArrayOfClass;
	  //*********my edits*************
	  typedef __rt::Ptr<__Array<String> > ArrayOfString;
	  typedef __rt::Ptr<__Array<double> > ArrayOfDouble;
	  typedef __rt::Ptr<__Array<bool> > ArrayOfBoolean;
	  typedef __rt::Ptr<__Array<float> > ArrayOfFloat;
	  typedef __rt::Ptr<__Array<char> > ArrayOfChar; //should be an int16_t
	  typedef __rt::Ptr<__Array<long> > ArrayOfLong; //should be an int64_t
	  typedef __rt::Ptr<__Array<short> > ArrayOfShort;//should be an int16_t
	  
	  
    // The data layout for arrays.
    template <typename T>
    struct __Array {
      __Array_VT<T>* __vptr;
      const int32_t length;
      T* __data;

      __Array(const int32_t length)
      : __vptr(&__vtable), length(length), __data(new T[length]) {
        // Only zero out __data for arrays of primitive types.
      }

      static Class __class();

      // The virtual destructor.  We cannot reuse the above template
      // function since arrays require more work on deletion.
      static void __delete(__Array* __this) {
        delete[] __this->__data;
        delete __this;
      }

      T& operator[](int idx) {
        if (0 > idx || idx >= length) throw ArrayIndexOutOfBoundsException();
        return __data[idx];
      }

      const T& operator[](int idx) const {
        if (0 > idx || idx >= length) throw ArrayIndexOutOfBoundsException();
        return __data[idx];
      }

      static __Array_VT<T> __vtable;
    };

    // The vtable layout for arrays.
    template <typename T>
    struct __Array_VT {
      typedef __rt::Ptr<__Array<T> > Array;

      Class __isa;
      void (*__delete)(__Array<T>*);
      int32_t (*hashCode)(Array);
      bool (*equals)(Array, Object);
      Class (*getClass)(Array);
      String (*toString)(Array);

      __Array_VT()
      : __isa(__Array<T>::__class()),
        __delete(&__Array<T>::__delete),
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

  }
}

// ==========================================================================

namespace __rt {

  // Convenience function for converting C++ strings (std::string)
  // into translated Java strings (java::lang::String).
  // The C++ compiler automaticcally converts C string literals
  // (const char *) into C++ stirngs, so we only need one such function.
  inline java::lang::String stringify(std::string s) {
    return new java::lang::__String(s);
  }

  // Template function to check against null values.
  template<typename T>
  void checkNotNull(T o) {
    if (null() == o) throw java::lang::NullPointerException();
  }
  
  // Template function to check array stores.
  template<typename T, typename U>
  void checkArrayStore(Ptr<java::lang::__Array<T> > array, U object) {
    if (null() != object) {
      java::lang::Class arraytype = array->__vptr->getClass(array);
      java::lang::Class eltype = arraytype->__vptr->getComponentType(arraytype);
      
      if (! eltype->__vptr->isInstance(eltype, (java::lang::Object)object)) {
        throw java::lang::ArrayStoreException();
      }
    }
  }

  // Template function to perform Java casts.
  template<typename Target, typename Source>
  Target java_cast(Source other) {
    java::lang::Class k = Target::value_t::__class();

    if (! k->__vptr->isInstance(k, other)) {
      throw java::lang::ClassCastException();
    }

    return Target(other);
  }

}
