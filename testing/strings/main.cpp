#include <iostream>

#include "strings_dataLayout.h"




int32_t main(int32_t argc, char *argv[]){

	strings forward_main = new __strings();
	forward_main->init(forward_main);
	forward_main->main(argc,argv);
	return 0;
}