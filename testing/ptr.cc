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

#include "ptr.h"

int main() {

  __rt::Ptr<int> p = new int(5);
  __rt::Ptr<float> q = new float(3.4);

  __rt::Ptr<int, __rt::ArrayPolicy> r = new int[5];

  // Compiles but only makes sense for translated upcasts.
  r = __rt::Ptr<int, __rt::ArrayPolicy>(q);

  std::cout << *p << ", " << *q << ", " << ( p == q ) << std::endl;

  return 0;
}
