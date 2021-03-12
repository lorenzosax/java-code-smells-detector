package main;

import jcc.Token;

public class SymbolTableEntry {

	private Token type;
	private Token id;
	private boolean isClassMember;

	public SymbolTableEntry(Token type, Token id, boolean isClassMember) {
		this.type = type;
		this.id = id;
		this.isClassMember = isClassMember;
	}

	public Token getType() {
		return type;
	}

	public Token getId() {
		return id;
	}

	public boolean isClassMember() {
		return isClassMember;
	}
}
