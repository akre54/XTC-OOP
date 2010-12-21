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

#include "xtc_oop_dataLayout.h"

#include <sstream>

namespace xtc {
namespace oop {


	Class __Test::__class(){
		static Class k = new __Class(__rt::stringify("xtc.oop.Test"),__rt::null());
		return k;
	}
		void __Test::init(Test __passedthis) {
			__passedthis->__this = __passedthis;
__this->count=0;
				}

	java::lang::Object __Test::m1(Test __this){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m2(){
Object o=new __Object();
return o; 

	}

	Test __Test::m3(Test __this){
__this->count++;
return __this; 

	}

	Test __Test::m4(Test __this){
__this->count++;
return __this; 

	}

	Test __Test::m5(Test __this,Test t){
return ({Test a=t->__vptr->m3(t);
 a->__vptr->m4(a);
}); 

	}

	java::lang::Object __Test::m6(Test __this,Test t){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m6_1(Test __this,Rest r){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m7(Test __this,java::lang::Object o){
Object k=new __Object();
return k; 

	}

	java::lang::Object __Test::m7_1(Test __this,java::lang::String s){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m7_2(Test __this,Test t){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m7_3(Test __this,Rest r){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m8(Test __this,Test t){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m8_1(Test __this,Rest r){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m8_2(Test __this,Test t1,Test t2){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Test::m8_3(Test __this,Rest r,Test t){
Object o=new __Object();
return o; 

	}

	int32_t __Test::main(int32_t argc,char** args){
Test t;
Rest r;
Object o= __rt::null();
int32_t test=0;
int32_t success=0;
test++;
r=new __Rest();
r->init(r);
o=r->__vptr->m1(r);
test++;
t=r;
if((t==r)){
std::cout<<"PASS t == r" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL t == r" <<std::endl;
} 
test++;
test++;
test++;
int32_t h=r->__vptr->hashCode(r);
if((7353==h)){
std::cout<<"PASS 7353 == r.hashCode()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL 7353 == r.hashCode()" <<std::endl;
} 
test++;
String s1=t->__vptr->toString(t);
String s2=r->__vptr->toString(r);
test++;
o=t->__vptr->m1(t);
test++;
		o=__Rest::m2();
test++;
o=r->m2();
test++;
Test tr=r;
o=tr->m2();
test++;
		o=__Test::m2();
test++;
o=t->m2();
test++;
t=new __Test();
t->init(t);
if((t!=r)){
std::cout<<"PASS t != r" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL t != r" <<std::endl;
} 
test++;
test++;
s1=t->__vptr->toString(t);
test++;
o=t->__vptr->m1(t);
test++;
o=t;
if(({ Class k=o->__vptr->getClass(o);
k->__vptr->isInstance(k,o);})){
std::cout<<"PASS o instanceof Test" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL o instanceof Test" <<std::endl;
} 
test++;
if(({ Class k=o->__vptr->getClass(o);
k->__vptr->isInstance(k,o);})){
std::cout<<"PASS o instanceof Object" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL o instanceof Object" <<std::endl;
} 
test++;
if(!({ Class k=o->__vptr->getClass(o);
k->__vptr->isInstance(k,o);})){
std::cout<<"PASS ! (o instanceof String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL ! (o instanceof String)" <<std::endl;
} 
test++;
test++;
o=t->m2();
test++;
if((0==t->count)){
std::cout<<"PASS Test.<init>()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL Test.<init>()" <<std::endl;
} 
test++;
if(((0==r->round) && (0==r->count))){
std::cout<<"PASS Rest.<init>()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL Rest.<init>()" <<std::endl;
} 
test++;
({Test a=t->__vptr->m3(t);
 a->__vptr->m4(a);
});
if((2==t->count)){
std::cout<<"PASS t.m3().m4()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL t.m3().m4()" <<std::endl;
} 
test++;
({Test a=r->__vptr->m3(r);
 a->__vptr->m4(a);
});
if(((1==r->round) && (1==r->count))){
std::cout<<"PASS r.m3().m4()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL r.m3().m4()" <<std::endl;
} 
test++;
t->count=0;
({Test a=t->__vptr->m5(t,t);
Test b=a->__vptr->m3(a);
 b->__vptr->m4(b);
});
if((4==t->count)){
std::cout<<"PASS t.m5(t).m3().m4()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL t.m5(t).m3().m4()" <<std::endl;
} 
test++;
r->count=0;
r->round=0;
({Test a=r->__vptr->m5(r,r);
Test b=a->__vptr->m3(a);
 b->__vptr->m4(b);
});
if(((2==r->round) && (2==r->count))){
std::cout<<"PASS r.m5(r).m3().m4()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL r.m5(r).m3().m4()" <<std::endl;
} 
test++;
t->count=0;
r->count=0;
r->round=0;
({Test a=r->__vptr->m5(r,t);
Test b=a->__vptr->m3(a);
 b->__vptr->m4(b);
});
if((((0==r->round) && (0==r->count)) && (4==t->count))){
std::cout<<"PASS r.m5(t).m3().m4()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL r.m5(t).m3().m4()" <<std::endl;
} 
test++;
o=t->__vptr->m6(t,t);
test++;
o=t->__vptr->m6(t,r);
test++;
o=r->__vptr->m6(r,t);
test++;
o=r->__vptr->m6(r,r);
test++;
o=t->__vptr->m7(t,t);
test++;
o=t->__vptr->m7(t,r);
test++;
o=t->__vptr->m7(t,o);
test++;
o=t->__vptr->m7(t,s1);
test++;
o=r->__vptr->m7(r,t);
test++;
o=r->__vptr->m7(r,r);
test++;
o=t->__vptr->m8(t,t);
test++;
o=t->__vptr->m8(t,r);
test++;
o=r->__vptr->m8(r,t);
test++;
o=r->__vptr->m8(r,r);
test++;
o=r->__vptr->m8_2(r,t,t);
test++;
o=r->__vptr->m8_2(r,tr,t);
test++;
o=r->__vptr->m8_2(r,r,t);
test++;
Class k1=t->__vptr->getClass(t);
Class k2=r->__vptr->getClass(r);
if((k1!=k2)){
std::cout<<"PASS k1 != k2" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL K1 != k2" <<std::endl;
} 
test++;
test++;
test++;
test++;
test++;
k2=k2->__vptr->getSuperclass(k2);
if((k1==k2)){
std::cout<<"PASS k1 == k2.super()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL K1 == k2.super()" <<std::endl;
} 
test++;
test++;
k1=k1->__vptr->getSuperclass(k1);
k2=k2->__vptr->getSuperclass(k2);
if((k1==k2)){
std::cout<<"PASS k1.super() == k2.super().super()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL K1.super() == k2.super().super()" <<std::endl;
} 
test++;
if(k1->__vptr->equals(k1,k2)){
std::cout<<"PASS k1.super().equals(k2.super().super())" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL k1.super().equals(k2.super().super())" <<std::endl;
} 
test++;
k1=k1->__vptr->getSuperclass(k1);
if(( __rt::null()==k1)){
std::cout<<"PASS null == k1.super().super()" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL null == k1.super().super()" <<std::endl;
} 
test++;
s1=new __String("Hello Kitty #1");
s2=new __String("Hello Kitty #1");
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(String)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<"Hel"<<"lo Kitty #1";
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(String + String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(String + String)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<new __String(({ std::ostringstream sout;
sout <<__rt::stringify("He")<<"ll";
sout.str(); }))<<__rt::stringify("o Kitty #1");
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(String + String + String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(String + String + String)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<__rt::stringify("Hello Kitty #")<<1;
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(String + int)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(String + int)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<__rt::stringify("Hello Kitty #")<<'1';
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(String + char)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(String + char)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<(char)72<<__rt::stringify("ello Kitty #1");
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(char + String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(char + String)" <<std::endl;
} 
test++;
char c=72;
s2=new __String(({ std::ostringstream sout;
sout <<c<<__rt::stringify("ello Kitty #1");
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(char + String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(char + String)" <<std::endl;
} 
test++;
s2=new __String(({ std::ostringstream sout;
sout <<'H'<<__rt::stringify("ello Kitty #1");
sout.str(); }));
if(s1->__vptr->equals(s1,s2)){
std::cout<<"PASS s1.equals(char + String)" <<std::endl;
success++;
}
else { 
std::cout<<"FAIL s1.equals(char + String)" <<std::endl;
} 
test++;
std::cout<<" " <<std::endl;
std::cout<<success<<" out of "<<test<<" tests have passed." <<std::endl;

	}

	__Test_VT __Test::__vtable;

}}//close xtc::oop::Test
	namespace java {
	namespace lang {
		template<>
		Class __Array<xtc::oop::Test>::__class() {
			static Class k = new __Class(__rt::stringify("[Lxtc.oop.Test"),
									__Array<java::lang::Object>::__class(),
									xtc::oop::__Test::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

namespace xtc {
namespace oop {
	Class __Rest::__class(){
		static Class k = new __Class(__rt::stringify("xtc.oop.Rest"),__rt::null());
		return k;
	}
		void __Rest::init(Rest __passedthis) {
			__passedthis->__this = __passedthis;
__this->round=0;
				}

	java::lang::Object __Rest::m1(Rest __this){
Object o=new __Object();
return o; 

	}

	java::lang::Object __Rest::m2(){
Object o=new __Object();
return o; 

	}

	xtc::oop::Test __Rest::m4(Rest __this){
__this->round++;
return __this; 

	}

	java::lang::Object __Rest::m7_4(Rest __this,xtc::oop::Test t){
Object o=new __Object();
return o; 

	}

	int32_t __Rest::hashCode(Rest __this){
return 7353; 

	}

	__Rest_VT __Rest::__vtable;

}}//close xtc::oop::Rest
	namespace java {
	namespace lang {
		template<>
		Class __Array<xtc::oop::Rest>::__class() {
			static Class k = new __Class(__rt::stringify("[Lxtc.oop.Rest"),
									__Array<xtc::oop::Test>::__class(),
									xtc::oop::__Rest::__class());
			return k;
		}

	}
	} // closing java::lang for arrays
	//===========================================================================

namespace xtc {
namespace oop {
}
}
