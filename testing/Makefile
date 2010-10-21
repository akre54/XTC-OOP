SHELL = /bin/sh

.SUFFIXES:
.SUFFIXES: .c .o .h .cpp

PRE = demo
OBJS = main.cpp *_dataLayout.h *_methoddef.cpp ../java_lang.h ../java_lang.cpp
CC = g++
DEBUG = -g
FLAGS = $(DEBUG)

all : tester

tester : $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o tester

clean:
	rm -f *.o *.h *.cpp *~
	java xtc.oop.Translator -translate $(PRE).java
	$(CC) $(LFLAGS) $(OBJS) -o tester

translate:
	\java xtc.oop.Translator -translate $(PRE).java
	$(CC) $(LFLAGS) $(OBJS) -o tester
