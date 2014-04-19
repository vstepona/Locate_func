/* locate_func.java
 *
 * 2014-03-02
*/

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.Map;

// Java parsing library
import japa.parser.*;
import japa.parser.ast.*;
import japa.parser.ast.visitor.*;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;

//----------------------------------------------------------------------
class LocateFunc
{

//----------------------------------------
// Requires a path to source files.
public static void main(String[] args)
{
	parseFiles(args);
}


//----------------------------------------
private static void parseFiles(String[] fileNames)
{
	for (String file : fileNames){
		ThreadDemo T1 = new ThreadDemo(file);
		T1.start();
	}
}


//----------------------------------------
// Parse a source file to find function calls.
//private static Map<String, List<Integer>> findFuncCalls(String fileName)
public static void findFuncCalls(String fileName)
{
	try {
		FileInputStream inFile = new FileInputStream(fileName);

		CompilationUnit cu = new CompilationUnit();
		try {
			// parse the file
			cu = JavaParser.parse(inFile);
		} catch (Exception e){
		} finally {
			inFile.close();
		}

		//System.out.println(cu.toString());
		// visit and print the methods names
		new MethodVisitor().visit(cu, fileName);
		new MethodCallVisitor().visit(cu, fileName);

	} catch (IOException ioe){
		System.out.println("Input/Output error.");
		System.exit(1);
	}
}

// Simple visitor implementation for visiting MethodDeclaration nodes. 
private static class MethodVisitor extends VoidVisitorAdapter {
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		// here you can access the attributes of the method.
		// this method will be called for all methods in this 
		// CompilationUnit, including inner class methods
		System.out.println("Declaration: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
	}
}
private static class MethodCallVisitor extends VoidVisitorAdapter {
	@Override
	public void visit(MethodCallExpr n, Object arg) {
		// here you can access the attributes of the method.
		// this method will be called for all methods in this 
		// CompilationUnit, including inner class methods
		System.out.println("Call: " + n.getName() + " " + n.getBeginLine() + " " + arg.toString());
	}
}

}


//----------------------------------------
class ThreadDemo extends Thread {
   private Thread t;
   private String threadName;
   
   ThreadDemo( String name){
       threadName = name;
       System.out.println("Creating " +  threadName );
   }
   public void run() {
      System.out.println("Running " +  threadName );
		LocateFunc.findFuncCalls(threadName);
		  /*
      try {
			LocateFunc.findFuncCalls(threadName);
         for(int i = 4; i > 0; i--) {
            System.out.println("Thread: " + threadName + ", " + i);
            // Let the thread sleep for a while.
            Thread.sleep(50);
         }
     } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
     }
		 */
     System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start ()
   {
      System.out.println("Starting " +  threadName );
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}

/*
public class TestThread {
   public static void main(String args[]) {
   
      ThreadDemo T1 = new ThreadDemo( "Thread-1");
      T1.start();
      
      ThreadDemo T2 = new ThreadDemo( "Thread-2");
      T2.start();
   }   
}
*/

