SHELL = /bin/sh

.SUFFIXES:
.SUFFIXES: .c .o .h .cpp

PRE = demo
TARGETS = main.cpp *_methoddef.cpp  java_lang.cpp
CC = g++
DEBUG = -g
FLAGS = $(DEBUG)

all : ex javac

#Compile the translated code
compile : translate
	$(CC) $(FLAGS) $(TARGETS) -o $(PRE).out

#Remove previous translations
clean:
	rm -f  *.o *.h *.cpp *~ *.out *.txt

#Run the translator on demo.java
translate:
	java xtc.oop.Translator -translate $(PRE).java

#Execute the translated code
ex:	compile
	./$(PRE).out > cpp.out.txt

javac: $(PRE).java
	javac $(PRE).java
	java -cp . $(PRE) > java.out.txt