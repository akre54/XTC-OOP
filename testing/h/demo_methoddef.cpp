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
int32_t three=1+2;
int32_t five=three+2;
int32_t eleven=2+3+3;
std::cout<<"Int:"<<eleven <<std::endl;
std::cout<<"Eight:"<<three<<five <<std::endl;
float dec=18932434+234234;
float thums=444+33+333+234234;
std::cout<<"Float"<<thums <<std::endl;
double decimal=1.89+.01;
double threenums=1.80+.04+.05+.01;
std::cout<<"Threenums:"<<threenums <<std::endl;
int32_t mult=9*3;
int32_t threemult=3*3*3;
std::cout<<"Threemult:"<<threemult <<std::endl;
float multlarge=10*299;
float threeLarge=10*299*3;
std::cout<<"threeLarge:"<<threeLarge <<std::endl;
double deciMult=.09*5;
double deciMany=.08*.06*1;
std::cout<<"double:"<<deciMult <<std::endl;
int32_t modnum=8%3;
int32_t decrement=--modnum;
int32_t preincrement=++modnum;
int32_t postdcrement=modnum--;
int32_t postincrement=modnum++;
int32_t negative=-9;

	}

	__demo_VT __demo::__vtable;

//close demo
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

