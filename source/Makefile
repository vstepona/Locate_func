# Makefile
# 
# Kevin Whalen, Vid
# Due: 2014-02-24

all: compile run

compile: locate_func.java
	javac locate_func.java

run: LocateFunc.class
	java LocateFunc "/home/zkevin/workspace/labs/StructureOfProgrammingLanguages/Locate_func/source/testSourceFiles"

help: usage
usage:
	@echo "Usage:"
	@echo "  make all		# Compiles and runs demonstration."
	@echo "  make compile"
	@echo "  make run		# Run using demonstration input."
	@echo "  make help		# or `make usage` for this information."
	@echo "  make clean		# Removes binary files (.class)."

clean:
	rm -f *.class

