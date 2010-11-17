#!/bin/sh
# a shell script that runs Translator on a file,
# runs the file and its translated version, then uses unix sdiff
# tool to compare the output of both files
#
# for OOP fall 10 by The Group
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
	echo Cleaning directory $D/
	cd $D
	echo
	echo $P.java:
	more $P.java
	echo
		#clean out previous translations
	make -f ../Makefile clean
	cp ../java_lang.cpp ./
	cp ../java_lang.h ./
	cp ../ptr.h ./
	make -f ../Makefile PRE=$P
		#sdiff will output both files to command line, more useful here than diff
	sdiff java.out.txt cpp.out.txt
	echo DONE
	cd ../
	PRE="";
else
	D="";
fi
done