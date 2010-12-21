#include <iostream>

#include "xtc_oop_dataLayout.h"

using namespace xtc::oop;


int32_t main(int32_t argc, char *argv[]){

	Test forward_main = new __Test();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}