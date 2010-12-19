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

#include <iostream>
#include <sstream>


using namespace java::lang;

int main(void) {
  // Object o = new Object();
  Object o = new __Object();

  std::cout << "o.toString() : "
            << /* o.toString() */ o->__vptr->toString(o)
            << std::endl;

  // Class k = o.getClass();
  __rt::checkNotNull(o);
  Class k = o->__vptr->getClass(o);

  __rt::checkNotNull(k);
  std::cout << "k.getName()  : "
            << /* k.getName() */ k->__vptr->getName(k)
            << std::endl
            << "k.toString() : "
            << /* k.toString() */ k->__vptr->toString(k)
            << std::endl;

  // Class l = k.getClass();
  __rt::checkNotNull(k);
  Class l = k->__vptr->getClass(k);

  __rt::checkNotNull(l);
  std::cout << "l.getName()  : "
            << /* l.getName() */ l->__vptr->getName(l)
            << std::endl
            << "l.toString() : "
            << /* l.toString() */ l->__vptr->toString(l)
            << std::endl;

  // if (k.equals(l)) {
  __rt::checkNotNull(k);
  if (k->__vptr->equals(k, (Object)l)) {
    std::cout << "k.equals(l)" << std::endl;
  } else {
    std::cout << "! k.equals(l)" << std::endl;
  }

  // if (k.equals(l.getSuperclass())) {
  __rt::checkNotNull(k);
  __rt::checkNotNull(l);
  if (k->__vptr->equals(k, (Object)l->__vptr->getSuperclass(l))) {
    std::cout << "k.equals(l.getSuperclass())" << std::endl;
  } else {
    std::cout << "! k.equals(l.getSuperclass())" << std::endl;
  }

  // if (k.isInstance(o)) {
  __rt::checkNotNull(k);
  if (k->__vptr->isInstance(k, o)) {
    std::cout << "o instanceof k" << std::endl;
  } else {
    std::cout << "! (o instanceof k)" << std::endl;
  }

  // if (l.isInstance(o)) {
  __rt::checkNotNull(l);
  if (l->__vptr->isInstance(l, o)) {
    std::cout << "o instanceof l" << std::endl;
  } else {
    std::cout << "! (o instanceof l)" << std::endl;
  }

  // -----------------------------------------------------------------------

  std::cout << "-------------------------------------------------------------"
            << "-----------------"
            << std::endl;

  // String s1 = "Rose";
  String s1 = __rt::stringify("Rose");

  // String s2 = "Robb";
  String s2 = __rt::stringify("Robb");

  String sdf3 = __rt::stringify(({std::ostringstream sout;
		sout << "foo" << s1 << s2;
	  sout.str();
	  }));

  std::cout <<"---------"<< sdf3 << std::endl;


  // s1.equals(o)
  if (s1->__vptr->equals(s1, o)) {
    std::cout << "s1.equals(o)" << std::endl;
  } else {
    std::cout << "! s1.equals(o)" << std::endl;
  }

  // s1.equals(s2)
  if (s1->__vptr->equals(s1, (Object)s2)) {
    std::cout << "s1.equals(s2)" << std::endl;
  } else {
    std::cout << "! s1.equals(s2)" << std::endl;
  }

  // s1.length() == s2.length()
  if (s1->__vptr->length(s1) == s2->__vptr->length(s2)) {
    std::cout << "s1.length() == s2.length()" << std::endl;
  } else {
    std::cout << "s1.length() != s2.length()" << std::endl;
  }

  // s1.charAt(1) == s2.charAt(1)
  if (s1->__vptr->charAt(s1, 1) == s2->__vptr->charAt(s2, 1)) {
    std::cout << "s1.charAt(1) == s2.charAt(1)" << std::endl;
  } else {
    std::cout << "s1.charAt(1) != s2.charAt(1)" << std::endl;
  }

  // s1.charAt(2) == s2.charAt(2)
  if (s1->__vptr->charAt(s1, 2) == s2->__vptr->charAt(s2, 2)) {
    std::cout << "s1.charAt(2) == s2.charAt(2)" << std::endl;
  } else {
    std::cout << "s1.charAt(2) != s2.charAt(2)" << std::endl;
  }

  // String s3 = s1 + " and " + s2;
  std::ostringstream temp;
  temp << s1 << " and " << s2;
  String s3 = __rt::stringify(temp.str());

  // System.out.println(s3);
  std::cout << s3 << std::endl;

  // -----------------------------------------------------------------------

  std::cout << "-------------------------------------------------------------"
            << "-----------------"
            << std::endl;

  // int[] a = new int[5];
  ArrayOfInt a = new __Array<int32_t>(5);

  std::cout << "a[2]  : " << (*a)[2] << std::endl;

  // a[2] = 4;
  std::cout << "a[2] <- 4;" << std::endl;

  __rt::checkNotNull(a);
  (*a)[2] = 4;

  std::cout << "a[2]  : " << (*a)[2] << std::endl;

  std::cout << "-------------------------------------------------------------"
            << "-----------------"
            << std::endl;

  // Class[] b = new Class[2];
  ArrayOfClass b = new __Array<Class>(2);

  std::cout << "b[1]  : " << (*b)[1].raw() << std::endl;

  // b[1] = b.getClass().getSuperClass();
  std::cout << "b[1] <- b.getClass().getSuperclass();" << std::endl;
  
  __rt::checkNotNull(b);
  Class t1 = b->__vptr->getClass(b);
  
  __rt::checkNotNull(t1);
  Class t2 = t1->__vptr->getSuperclass(t1);

  __rt::checkNotNull(b);
  __rt::checkArrayStore(b, t2);
  (*b)[1] = t2;

  std::cout << "b[1]  : " << (*b)[1].raw() << std::endl;

  std::cout << std::endl << "Happy, happy, joy, joy!" << std::endl;
  return 0;
}
