#!/bin/sh
# a shell script (oopTest.sh, eg) that runs Translator on a file,
# runs the file and its translated version, then uses unix sdiff
# tool to compare the output of both files
#
# for OOP fall 10 by The Group
 
echo "Enter jfile name (minus .java): "
read jfile
 
make translate

java JFileTester jfile
# javac $jfile.java && java $jfile > jfileout.txt
 
# cfile and jfile have the same base name
$cfile = $jfile
./tester.out > cppfileout.txt

#sdiff will output both files to command line, more useful here than diff
sdiff -w jfileout.txt cppfileout.txt
