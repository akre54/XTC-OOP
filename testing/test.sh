#!/bin/sh
# a shell script that runs Translator on a file,
# runs the file and its translated version, then uses unix sdiff
# tool to compare the output of both files
#
# for OOP fall 10 by The Group
cd $(pwd -P) # expand path to absolute path 
. ../../../../setup.sh
clear
echo "Java to C++ Translation"
D="1";
while [ $D ]; do
echo "Enter the filename (____.java , default is demo)"
read P
if [ $P ]; then
	echo
else
	P="demo";
fi
echo "Enter the testing directory (return blank to quit):"
read D
if [ $D ]; then

	echo compiling...
	cd ..
	make
	if [ $? -ne '0' ] # if did not make well exit!
	then
	   	exit $?
	fi

	cd testing/
	echo Cleaning directory $D/
	cd $D
	if [ $? -ne '0' ]
	then
		echo "bad path name"
	   	exit $?
	fi

	echo

	#clean out previous translations
	make -f ../Makefile clean

	#recursively copy java_lang to each directory
	for directory in `find . -type d`
	do
			cp ../java_lang.cpp $directory
			cp ../java_lang.h $directory
			cp ../ptr.h $directory
	done

	PACKAGE=$(egrep ^package $P.java | cut -d ' ' -f2 | cut -d ';' -f1) # get root file's package, used for classpaths
	if [ "$?" -eq "0" ]
		then
		echo pkg name: $PACKAGE
		make -f ../Makefile PRE=$P TFLAGS='-verbose' PACKAGE=$PACKAGE
	else
		echo no package
		make -f ../Makefile PRE=$P TFLAGS='-verbose'
	fi
	if [ $? -eq '0' ] # made successfully!
		then
	  echo "Comparing output files:"
		echo
		#sdiff will output both files to command line, more useful here than diff
		sdiff java.out.txt cpp.out.txt
		echo
		echo "DONE"
	fi
	echo
	echo
	cd ../
	PRE="";
else
	D="";
fi
done

