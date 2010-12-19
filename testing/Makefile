SHELL = /bin/sh

.SUFFIXES:
.SUFFIXES: .c .o .h .cpp

PRE = demo
PACKAGE = 
METHODDEFS = $(wildcard */*_methoddef.cpp)
TARGETS = main.cpp *_methoddef.cpp $(METHODDEFS) java_lang.cpp
CC = g++
DEBUG = -g
FLAGS = $(DEBUG)
TFLAGS = #-verbose -printJavaAST

all : ex javac

#Compile the translated code
compile : translate
	$(CC) $(FLAGS) $(TARGETS) -o $(PRE).out

#Remove previous translations
.PHONY : clean
SUBDIRS = $(wildcard */*.o */*.h */*.cpp */*~ */*.out */*.txt)
clean:
	rm -Rfv  *.o *.h *.cpp *~ *.out *.txt $(SUBDIRS)

#Run the translator on demo.java
translate:
	java xtc.oop.Translator $(TFLAGS) -translate $(PRE).java

#Execute the translated code
ex:	compile
	./$(PRE).out > cpp.out.txt

javac: $(PRE).java
ifeq ($(strip $(PACKAGE)),) # if no package
	javac -cp . $(PRE).java
	java -cp . $(PRE) > java.out.txt
else
	javac -cp .. $(PRE).java
	java -cp .. $(PACKAGE).$(PRE) > java.out.txt
endif
