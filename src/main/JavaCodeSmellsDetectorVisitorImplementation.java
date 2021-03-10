package main;

import jcc.*;

public class JavaCodeSmellsDetectorVisitorImplementation implements JavaCodeSmellsDetectorVisitor {

	private static final int LONG_PARAMETERS_LIST_THRESHOLD = 10;
	private static final int LONG_METHOD_THRESHOLD = 100;
	private static final int LARGE_CLASS_THRESHOLD = 1000;
	private static final int CYCLOMATIC_COMPLEX_THRESHOLD = 2;
	private static final int CYCLOMATIC_COMPLEX_METHOD_THRESHOLD = 20;
	private static final int ATFD_THRESHOLD = 2;
	private static final int NUM_ACCESS_ATTRIBUTE_METHOD_THRESHOLD = 3;
	private static final int TCC_PERCENTAGE_THRESHOLD = 30; // 30%
	private static Report report;
	private static String className;
	private static String currentMethodName;
	private static Integer numberOfMethods = 0;
	private static Integer methodStatementsCount = 0;
	private static Integer classStatementsCount = 0;
	private static Integer methodCyclomaticComplex = 0;
	private static Integer classCyclomaticComplex = 0;
	private static Integer atfdCount = 0;
	private static Integer methodAccessAttributeOtherClass = 0;
	private static Integer tccCount = 0;
	private static Integer methodAccessAttribute = 0;

	@Override
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTcompilationUnit node, Object data) {
		report = (Report) data;
		report.appendMetric("Type, Value");
		report.appendSmell("CodeSmell, ClassName, MethodName, StartLine");
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTeof node, Object data) {
		System.err.println("LOC: " + classStatementsCount);
		System.err.println("NUM METHOD: " + numberOfMethods);
		System.err.println("TCC_C: " + tccCount);
		System.err.println("ATFD: " + atfdCount);
		return null;
	}
	
	@Override
	public Object visit(ASTpackageDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTimportDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTclassDeclaration node, Object data) {
		classStatementsCount++;
		className = ((Token) node.jjtGetValue()).image;
		node.childrenAccept(this, data);
		
		if(classStatementsCount >= LARGE_CLASS_THRESHOLD)
			report.appendSmell("Large Class, " + className + ", " + "-" + ", " + "-");
	
		if(classCyclomaticComplex >= CYCLOMATIC_COMPLEX_THRESHOLD 
				&& atfdCount >= ATFD_THRESHOLD
				&& ((double)tccCount/(double)numberOfMethods*100) <= TCC_PERCENTAGE_THRESHOLD)
			report.appendSmell("God Class, " + className + ", " + "-" + ", " + "-");
	
		return null;
	}

	@Override
	public Object visit(ASTsuperConstruct node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTimplementsList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTinterfaceDeclaration node, Object data) {
		classStatementsCount++;
		className = ((Token) node.jjtGetValue()).image;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTinterfaceBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTattributeDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTabstractMethodDeclaration node, Object data) {
		Integer numParameterCount = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Token t = (Token) node.jjtGetValue();
		currentMethodName = t.image;
		
		if(numParameterCount >= LONG_PARAMETERS_LIST_THRESHOLD)
			report.appendSmell("Long Parameters List, " + className + ", " + currentMethodName + ", " + t.beginLine);
	
		return null;
	}

	@Override
	public Object visit(ASTmethodBody node, Object data) {
		numberOfMethods++;
		methodStatementsCount = 0;
		methodCyclomaticComplex = 0;
		methodAccessAttributeOtherClass = 0;
		methodAccessAttribute = 0;
		node.childrenAccept(this, data);
		classStatementsCount += methodStatementsCount;
		classCyclomaticComplex += methodCyclomaticComplex;
		atfdCount += (methodAccessAttributeOtherClass >= NUM_ACCESS_ATTRIBUTE_METHOD_THRESHOLD ? 1 : 0);
		tccCount += (methodAccessAttribute > 0 ? 1 : 0);
		
		int startLine = ((Token) data).beginLine;
		
		if(methodStatementsCount >= LONG_METHOD_THRESHOLD)
			report.appendSmell("Long Method, " + className + ", " + currentMethodName + ", " + Integer.toString(startLine));
		
		if(methodCyclomaticComplex >= CYCLOMATIC_COMPLEX_METHOD_THRESHOLD)
			report.appendSmell("Complex Method, " + className + ", " + currentMethodName + ", " + Integer.toString(startLine));
	
		return null;
	}

	@Override
	public Object visit(ASTextendsList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTclassBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTfieldDeclaration node, Object data) {
		classStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTmethodDeclaration node, Object data) {
		classStatementsCount++;
		Integer numParameterCount = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Token t = (Token) node.jjtGetValue();
		currentMethodName = t.image;
		
		if(numParameterCount > LONG_PARAMETERS_LIST_THRESHOLD)
			report.appendSmell("Long Parameters List, " + className + ", " + currentMethodName + ", " + t.beginLine);

		int totChild = node.jjtGetNumChildren();
		for (int i = 1; i < totChild; i++) {
			node.jjtGetChild(i).jjtAccept(this, t);
		}

		return null;
	}

	@Override
	public Object visit(ASTvariableInitializer node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTarrayInitializer node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTconstructorDeclaration node, Object data) {
		classStatementsCount++;
		Integer numParameterCount = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Token t = (Token) node.jjtGetValue();
		currentMethodName = t.image;
		
		if(numParameterCount > LONG_PARAMETERS_LIST_THRESHOLD)
			report.appendSmell("Long Parameters List, " + className + ", " + currentMethodName + ", " + t.beginLine);
		
		node.jjtGetChild(1).jjtAccept(this, t);
		return null;
	}

	@Override
	public Object visit(ASTformalParameterList node, Object data) {
		return node.jjtGetNumChildren();
	}

	@Override
	public Object visit(ASTformalParameter node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTthrowsConstruct node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTconstructorBody node, Object data) {
		numberOfMethods++;
		methodStatementsCount = 0;
		methodCyclomaticComplex = 0;
		methodAccessAttributeOtherClass = 0;
		methodAccessAttribute = 0;
		node.childrenAccept(this, data);
		classStatementsCount += methodStatementsCount;
		classCyclomaticComplex += methodCyclomaticComplex;
		atfdCount += (methodAccessAttributeOtherClass >= NUM_ACCESS_ATTRIBUTE_METHOD_THRESHOLD ? 1 : 0);
		tccCount += (methodAccessAttribute > 0 ? 1 : 0);
		
		int startLine = ((Token) data).beginLine;
		
		if(methodStatementsCount >= LONG_METHOD_THRESHOLD)
			report.appendSmell("Long Method, " + className + ", " + currentMethodName + ", " + Integer.toString(startLine));
		
		if(methodCyclomaticComplex >= CYCLOMATIC_COMPLEX_METHOD_THRESHOLD)
			report.appendSmell("Complex Method, " + className + ", " + currentMethodName + ", " + Integer.toString(startLine));
	
		return null;
	}

	@Override
	public Object visit(ASTthisMethod node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTsuperMethod node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTlocalVariableDeclarationStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTexpression node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTfieldAccess node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTunaryExpression node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTunaryExpression1 node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTpreIncrDecrExpression node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTidentifierName node, Object data) {
		node.childrenAccept(this, node.jjtGetValue());
		return null;
	}

	@Override
	public Object visit(ASTidentifierName1 node, Object data) {
		ValueNode vn = (ValueNode) data;
		if (vn != null && vn.isClassMember) {
			methodAccessAttribute++;
			if (vn.isAttribute)
				methodAccessAttributeOtherClass++;
		} else if(vn != null && vn.isAttribute) {
			methodAccessAttributeOtherClass++;
		}
		return null;
	}

	@Override
	public Object visit(ASTclassInstanceCreationExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTstatementNoShortIf node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTemptyStatement node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTlabeledStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTlabeledStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTassignmentStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTmethodInvocation node, Object data) {
		methodStatementsCount++;
		methodAccessAttributeOtherClass--; // to remove false positive due to identifierName
		return null;
	}

	@Override
	public Object visit(ASTpostIncrDecrExpression node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTifThenStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTifBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTelseStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTifThenElseStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchBlockStatementGroup node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchLabel node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTwhileStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTwhileCondition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTwhileBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTwhileStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTdoStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforInit node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTforUpdate node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTbreakStatement node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTcontinueStatement node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTreturnStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTthrowsStatement node, Object data) {
		methodStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTsynchronizedStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTtryStatement node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTtryBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTcatches node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTcatcheBody node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTfinallys node, Object data) {
		methodStatementsCount++;
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTconstantExpression node, Object data) {
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTerror_skip node, Object data) {
		return null;
	}
}
