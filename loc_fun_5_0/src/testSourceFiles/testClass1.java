/* testClass1.java
 * A simple class file for testing LocateFunc.
 * 2014-03-02
*/

import java.io.*;

//----------------------------------------------------------------------
class TestClassOne
{

//----------------------------------------
public static void main(String[] args)
{
	TestClassTwo.bar("Test", 1);
}


//----------------------------------------
public static void foo(String str, Integer i){
	System.out.println(str + i.toString() + " called.");
}

}

