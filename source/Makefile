# Makefile
# 
# Kevin Whalen, Vidmantas Steponavicius
# Due: 2014-02-24

compile: LocateFunc.java
	javac -cp ".:javaparser.jar" LocateFunc.java
	javac -cp ".:javaparser.jar" Test.java
	cp LocateFunc.java ../loc_fun_5_0/src/.

run: compile
	java -cp ".:javaparser.jar" Test ./testSourceFiles/testClass1.java ./testSourceFiles/testClass2.java

runGUI:
	@mkdir -p classes
	cp LocateFunc.java ../loc_fun_5_0/src/.
	javac -cp ".:javaparser.jar" -d ./classes ../loc_fun_5_0/src/LocateFunc.java
	javac -cp ".:javaparser.jar" -d ./classes ../loc_fun_5_0/src/My_Editor.java
	java -cp ".:javaparser.jar:./classes" My_Editor

#test: clean compile
#	java -cp ".:javaparser.jar" LocateFunc testSourceFiles/testClass1.java testSourceFiles/testClass2.java

help: usage
usage:
	@echo "Usage:"
	@echo "  make compile"
	@echo "  make run		# Run using demonstration input."
	@echo "  make help		# or `make usage` for this information."
	@echo "  make clean		# Removes binary files (.class)."

clean:
	rm -f *.class

