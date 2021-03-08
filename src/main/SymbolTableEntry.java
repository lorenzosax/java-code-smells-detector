package main;

import jcc.Token;

public class SymbolTableEntry {

	private Token type;
	private Token id;
	private boolean isConst;

	public SymbolTableEntry(Token type, Token id) {
		this.type = type;
		this.id = id;
	}

	public Token getType() {
		return type;
	}

	public Token getId() {
		return id;
	}

	public boolean isConst() {
		return isConst;
	}

	public void setConst(boolean aConst) {
		isConst = aConst;
	}
}
