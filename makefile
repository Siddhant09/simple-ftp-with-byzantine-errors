JAVA = java
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
Server.java\
Client.java\
User.java\
Database.java\
Methods.java\
Byzantine.java\
Cryptography.java\

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) */*.class

server: 
	$(JAVA) Server

client:
	$(JAVA) Client