package main;

import jcc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaCodeSmellsDetectorVisitorImplementation implements JavaCodeSmellsDetectorVisitor {

	private static final String DELIMITER = "@";
	private static final int LONG_PARAMETERS_LIST_THRESHOLD = 10;
	private static final int LONG_METHOD_THRESHOLD = 80;
	private static final int LARGE_CLASS_THRESHOLD = 300;
	private static final int CYCLOMATIC_COMPLEX_THRESHOLD = 48;
	private static final int CYCLOMATIC_COMPLEX_METHOD_THRESHOLD = 21;
	private static final int ATFD_THRESHOLD = 6;
	private static final int NUM_ACCESS_ATTRIBUTE_METHOD_THRESHOLD = 3;
	private static final int TCC_PERCENTAGE_THRESHOLD = 30; // 30%
	private static final int DUPLICATE_CODE_STMTS_THRESHOLD = 3;
	private static Map<String, List<String>> methodStmtsMap = new HashMap<>();
	private static Report report;
	private static String className;
	private static String currentMethodName;
	private static String currentMethodBodyString;
	private static Integer numberOfMethods = 0;
	private static Integer methodStatementsCount = 0;
	private static Integer classStatementsCount = 0;
	private static Integer methodCyclomaticComplex = 0;
	private static Integer classCyclomaticComplex = 0;
	private static Integer atfdCount = 0;
	private static Integer methodAccessAttributeOtherClass = 0;
	private static Integer tccCount = 0;
	private static Integer methodAccessAttribute = 0;
	private static Integer numberOfClassMessages = 0;
	private static Integer globalVariables = 0;
	private static Integer publicMethods = 0;
	private static Integer privateMethods = 0;

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
		for (Map.Entry<String, List<String>> entry : methodStmtsMap.entrySet()) {
			List<String> methodsNameList = entry.getValue();
			if (methodsNameList.size() > 1) {
				for (String m : methodsNameList) {
					String[] s = m.split(DELIMITER);
					report.appendSmell("Duplicate Code, " + className + ", " + s[0] + ", " + s[1]);
				}
			}
		}
		report.appendMetric("Number of Methods, " + numberOfMethods);
		report.appendMetric("Number of Class's messages, " + numberOfClassMessages);
		report.appendMetric("Number of Global variables, " + globalVariables);
		
		if(publicMethods == 0)
			report.appendMetric("Ratio PrivateMethods/PublicMethods, NaN");
		else
			report.appendMetric("Ratio PrivateMethods/PublicMethods, " + (double) privateMethods/publicMethods);
		
		
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
		currentMethodBodyString = "";
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
			report.appendSmell("Long Method, " + className + ", " + currentMethodName + ", " + startLine);
		
		if(methodCyclomaticComplex >= CYCLOMATIC_COMPLEX_METHOD_THRESHOLD)
			report.appendSmell("Complex Method, " + className + ", " + currentMethodName + ", " + startLine);

		if (methodStatementsCount > DUPLICATE_CODE_STMTS_THRESHOLD) { // because duplicate code must be major than 3 LOC
			List<String> methodsName = methodStmtsMap.get(currentMethodBodyString);
			if (methodsName == null) {
				methodsName = new ArrayList<>();
			}
			methodsName.add(currentMethodName + DELIMITER + startLine);
			methodStmtsMap.put(currentMethodBodyString, methodsName);
		}

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
		globalVariables++;
		classStatementsCount++;
		return null;
	}

	@Override
	public Object visit(ASTmethodDeclaration node, Object data) {
		classStatementsCount++;
		
		Integer numParameterCount = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		ValueNode vn = (ValueNode) node.jjtGetValue();
		Token t = vn.id;
		currentMethodName = t.image;
		countMethodModifiers(vn.modifier);
		
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
		currentMethodBodyString += "ASTvariableInitializer";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTarrayInitializer node, Object data) {
		// TODO Auto-generated method stub
		currentMethodBodyString += "ASTarrayInitializer";
		return null;
	}

	@Override
	public Object visit(ASTconstructorDeclaration node, Object data) {
		classStatementsCount++;
		currentMethodBodyString += "ASTconstructorDeclaration";
		Integer numParameterCount = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		ValueNode vn = (ValueNode) node.jjtGetValue();
		Token t = vn.id;
		currentMethodName = t.image;
		countMethodModifiers(vn.modifier);
		
		if(numParameterCount > LONG_PARAMETERS_LIST_THRESHOLD)
			report.appendSmell("Long Parameters List, " + className + ", " + currentMethodName + ", " + t.beginLine);
		
		int totChild = node.jjtGetNumChildren();
		for (int i = 1; i < totChild; i++) {
			node.jjtGetChild(i).jjtAccept(this, t);
		}
		return null;
	}
	
	private void countMethodModifiers(Token m) {
		if(m != null && m.kind == JavaCodeSmellsDetectorConstants.PUBLIC) {
			publicMethods++;
		} else if(m != null && m.kind == JavaCodeSmellsDetectorConstants.PRIVATE) {
			privateMethods++;
		}
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
		currentMethodBodyString += "ASTthisMethod";
		return null;
	}

	@Override
	public Object visit(ASTsuperMethod node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTsuperMethod";
		return null;
	}

	@Override
	public Object visit(ASTlocalVariableDeclarationStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTlocalVariableDeclarationStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTargumentList node, Object data) {
		currentMethodBodyString += "ASTargumentList";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTargument node, Object data) {
		currentMethodBodyString += "ASTargument";
		return null;
	}

	@Override
	public Object visit(ASTexpression node, Object data) {
		currentMethodBodyString += "ASTexpression";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTfieldAccess node, Object data) {
		currentMethodBodyString += "ASTfieldAccess";
		return null;
	}

	@Override
	public Object visit(ASTand node, Object data) {
		currentMethodBodyString += "ASTand";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTor node, Object data) {
		currentMethodBodyString += "ASTor";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTlogicalOperand node, Object data) {
		currentMethodBodyString += "ASTlogicalOperand";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTaritop_lp node, Object data) {
		currentMethodBodyString += "ASTaritop_lp";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTaritop_hp node, Object data) {
		currentMethodBodyString += "ASTaritop_hp";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTunaryExpression node, Object data) {
		currentMethodBodyString += "ASTunaryExpression";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTpreIncrDecrExpression node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTpreIncrDecrExpression";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTidentifierName node, Object data) {
		currentMethodBodyString += "ASTidentifierName";
		node.childrenAccept(this, node.jjtGetValue());
		return null;
	}

	@Override
	public Object visit(ASTidentifierName1 node, Object data) {
		currentMethodBodyString += "ASTidentifierName1";
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
		currentMethodBodyString += "ASTclassInstanceCreationExpression";
		return null;
	}

	@Override
	public Object visit(ASTstatementNoShortIf node, Object data) {
		currentMethodBodyString += "ASTstatementNoShortIf";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTemptyStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTemptyStatement";
		return null;
	}

	@Override
	public Object visit(ASTlabeledStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTlabeledStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTlabeledStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTlabeledStatementNoShortIf";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTassignmentStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTassignmentStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTmethodInvocation node, Object data) {
		methodStatementsCount++;
		methodAccessAttributeOtherClass--; // to remove false positive due to identifierName
		currentMethodBodyString += "ASTmethodInvocation";
		
		ValueNode vn = (ValueNode) node.jjtGetValue();
		if(vn != null && vn.isAttribute) {
			numberOfClassMessages++;
		}
		
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTpostIncrDecrExpression node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTpostIncrDecrExpression";
		return null;
	}

	@Override
	public Object visit(ASTifThenStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTifThenStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTifBody node, Object data) {
		currentMethodBodyString += "ASTifBody";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTelseStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTelseStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTifThenElseStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTifThenElseStatementNoShortIf";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTswitchStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchBlockStatementGroup node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTswitchBlockStatementGroup";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTswitchLabel node, Object data) {
		currentMethodBodyString += "ASTswitchLabel";
		return null;
	}

	@Override
	public Object visit(ASTwhileStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTwhileStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTwhileCondition node, Object data) {
		currentMethodBodyString += "ASTwhileCondition";
		return null;
	}

	@Override
	public Object visit(ASTwhileBody node, Object data) {
		currentMethodBodyString += "ASTwhileBody";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTwhileStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTwhileStatementNoShortIf";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTdoStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTdoStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforStatement node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTforStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforStatementNoShortIf node, Object data) {
		methodStatementsCount++;
		methodCyclomaticComplex++;
		currentMethodBodyString += "ASTforStatementNoShortIf";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTforInit node, Object data) {
		currentMethodBodyString += "ASTforInit";
		return null;
	}

	@Override
	public Object visit(ASTforUpdate node, Object data) {
		currentMethodBodyString += "ASTforUpdate";
		return null;
	}

	@Override
	public Object visit(ASTbreakStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTbreakStatement";
		return null;
	}

	@Override
	public Object visit(ASTcontinueStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTcontinueStatement";
		return null;
	}

	@Override
	public Object visit(ASTreturnStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTreturnStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTthrowsStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTthrowsStatement";
		return null;
	}

	@Override
	public Object visit(ASTsynchronizedStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTsynchronizedStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTtryStatement node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTtryStatement";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTtryBody node, Object data) {
		currentMethodBodyString += "ASTtryBody";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTcatches node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTcatches";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTcatcheBody node, Object data) {
		currentMethodBodyString += "ASTcatcheBody";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTfinallys node, Object data) {
		methodStatementsCount++;
		currentMethodBodyString += "ASTfinallys";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTconstantExpression node, Object data) {
		currentMethodBodyString += "ASTconstantExpression";
		node.childrenAccept(this, data);
		return null;
	}

	@Override
	public Object visit(ASTerror_skip node, Object data) {
		return null;
	}
}
