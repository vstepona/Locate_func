/* testClass1.java
 * A simple class file for testing LocateFunc.
 * 2014-03-02
*/

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.Comparator;
import java.util.ListIterator;

//----------------------------------------------------------------------
class TestClassTwo
{

//----------------------------------------
public static void main(String[] args)
{
	bar("bar", 2);
}


//----------------------------------------
public static void bar(String str, Integer i)
{
	TestClassOne.foo(str, i);
}

}

