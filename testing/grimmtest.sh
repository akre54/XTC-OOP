#!/bin/sh
# a shell script that runs Translator on Grimm's Test.java and
# Rest.java files, runs the file and its translated version, 
# then uses unix sdiff tool to compare the output of both files
#
# for OOP fall 10 by The Group
cd $(pwd -P) # expand path to absolute path 
. ../../../../setup.sh # source xtc
clear
echo "Java to C++ Translation of Grimm's Test.java and Rest.java"
D="xtc/oop";

echo compiling...
cd ..
make
if [ $? -ne '0' ] # if did not make well exit!
then
   	exit $?
fi

cd testing/
echo Cleaning
cd $D
echo
make -f ../../Makefile clean

#copy java_lang to directory
cp ../../java_lang.cpp .
cp ../../java_lang.h .
cp ../../ptr.h .

#echo Test.java:
#cat Test.java | nl
make -f ../../GrimmMakefile PRE=Test TFLAGS='-verbose'
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

