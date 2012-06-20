#include <iostream>

#include "cast_dataLayout.h"




int32_t main(int32_t argc, char *argv[]){

	cast forward_main = new __cast();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}