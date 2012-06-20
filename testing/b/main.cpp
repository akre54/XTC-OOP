#include <iostream>

#include "demo_dataLayout.h"




int32_t main(int32_t argc, char *argv[]){

	demo forward_main = new __demo();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}