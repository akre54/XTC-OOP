SHELL = /bin/sh

.SUFFIXES:
.SUFFIXES: .c .o .h .cpp

PRE = demo
OBJS = main.cpp $(PRE)_dataLayout.h $(PRE)_methoddef.cpp
CC = g++
DEBUG = -g
FLAGS = $(DEBUG)

all : tester

tester : $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o tester
clean:
	\rm *.o *~ tester
translate:
	\java xtc.oop.Translator -translate $(PRE).java
	$(CC) $(LFLAGS) $(OBJS) -o tester