SHELL = /bin/sh

.SUFFIXES:
.SUFFIXES: .c .o .h .cpp

OBJS = main.cpp demo_dataLayout.h demo_methoddef.cpp
CC = g++
DEBUG = -g
FLAGS = $(DEBUG)

all : tester

tester : $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o tester.o
clean:
	\rm *.o *~ tester