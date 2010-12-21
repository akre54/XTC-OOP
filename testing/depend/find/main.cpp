#include <iostream>

#include "depend_find_dataLayout.h"

using namespace depend::find;


int32_t main(int32_t argc, char *argv[]){

	Foo forward_main = new __Foo();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}