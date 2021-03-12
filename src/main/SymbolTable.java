package main;

import java.util.HashMap;
import java.util.Map;
import jcc.Token;


public class SymbolTable {

	private Map<String, SymbolTableEntry> st;
	
	public SymbolTable() {
		this.st = new HashMap<>();
	}

	public SymbolTable(Map<String, SymbolTableEntry> st) {
		this.st = st;
	}

	public SymbolTableEntry get(String key) {
		return this.st.get(key);
	}
	
	public void install(SymbolTableEntry symbolTableEntry) throws Exception {
		if (this.st.containsKey(symbolTableEntry.getId().image)) {
			throw new Exception();	
		} else {
			this.st.put(symbolTableEntry.getId().image, symbolTableEntry);
		}
	}
}
