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
	echo
	#echo $P.java:
	cat $P.java | nl
	echo
		#clean out previous translations
	make -f ../Makefile clean
	cp ../java_lang.cpp ./
	cp ../java_lang.h ./
	cp ../ptr.h ./
	make -f ../Makefile PRE=$P TFLAGS='-verbose'
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

