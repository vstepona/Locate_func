# Makefile
# 
# Kevin Whalen, Vidmantas Steponavicius
# Due: 2014-02-24

compile: LocateFunc.java
	javac -cp ".:javaparser.jar" LocateFunc.java

#	javac LocateFunc.java

run: compile
	java -cp ".:javaparser.jar" LocateFunc

#	java LocateFunc testSourceFiles

test: clean compile
	java -cp ".:javaparser.jar" LocateFunc testSourceFiles/testClass1.java testSourceFiles/testClass2.java

help: usage
usage:
	@echo "Usage:"
	@echo "  make compile"
	@echo "  make run		# Run using demonstration input."
	@echo "  make help		# or `make usage` for this information."
	@echo "  make clean		# Removes binary files (.class)."

clean:
	rm -f *.class

