package jcc; 

import java.util.HashMap;
import java.io.*;


public class Example implements ExampleParent, ExampleParent1 {
	
	private HashMap<String, String> hashMap;
	private int count;	
	private Example(int x) {int x;}
	
	private int getCount() throws PeccaException {
		return count;
	}
	
	public static void main (String[] args) {
		Example e;
		//Example e1 = new Example();
		//List<Integer> queue;
		int x;
		//Integer y = 10;
		//x = 10;
		
		//if(x <RELOP> 0) {}
		//if(true) {}
	}
	
	
}
