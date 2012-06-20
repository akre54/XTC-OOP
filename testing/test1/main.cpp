#include <iostream>

#include "test1_dataLayout.h"




int32_t main(int32_t argc, char *argv[]){

	test1 forward_main = new __test1();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}