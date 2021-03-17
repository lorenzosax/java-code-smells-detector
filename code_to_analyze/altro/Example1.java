package gerolora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import base.CommitterBadCodeSmell;
import base.BadCodeSmell;
import base.Change;
import base.Commit;
import base.Committer;
import base.DeadCode;
import base.DuplicatedCode;
import base.LargeClass;
import base.LongMethod;
import base.LongParameterList;
import base.Range;
import main.Report;

public class Example1 {

	private final String DELIMITER = "@";
	private final int LONG_PARAMETERS_LIST_THRESHOLD = 10;
	private final int LONG_METHOD_THRESHOLD = 100;
	private final int LARGE_CLASS_THRESHOLD = 1000;
	private final int CYCLOMATIC_COMPLEX_THRESHOLD = 48;
	private final int CYCLOMATIC_COMPLEX_METHOD_THRESHOLD = 21;
	private final int ATFD_THRESHOLD = 6;
	private final int NUM_ACCESS_ATTRIBUTE_METHOD_THRESHOLD = 3;
	private final int TCC_PERCENTAGE_THRESHOLD = 30; // 30%
	private final int DUPLICATE_CODE_STMTS_THRESHOLD = 3;
	private Report report;
	private String className;
	private String currentMethodName;
	private String currentMethodBodyString;
	private Integer numberOfMethods = 0;
	private Integer methodStatementsCount = 0;
	private Integer classStatementsCount = 0;
	private Integer methodCyclomaticComplex = 0;
	private Integer classCyclomaticComplex = 0;
	private Integer atfdCount = 0;
	private Integer methodAccessAttributeOtherClass = 0;
	private Integer tccCount = 0;
	private Integer methodAccessAttribute = 0;
	private Integer numberOfClassMessages = 0;
	private Integer globalVariables = 0;
	private Integer publicMethods = 0;
	private Integer privateMethods = 0;
	
	public Example1(Report report, String className, String currentMethodName,
			String currentMethodBodyString, Integer numberOfMethods, Integer methodStatementsCount,
			Integer classStatementsCount, Integer methodCyclomaticComplex, Integer classCyclomaticComplex,
			Integer atfdCount, Integer methodAccessAttributeOtherClass, Integer tccCount, Integer methodAccessAttribute,
			Integer numberOfClassMessages, Integer globalVariables, Integer publicMethods, Integer privateMethods) {
		super();

		this.report = report;
		this.className = className;
		this.currentMethodName = currentMethodName;
		this.currentMethodBodyString = currentMethodBodyString;
		this.numberOfMethods = numberOfMethods;
		this.methodStatementsCount = methodStatementsCount;
		this.classStatementsCount = classStatementsCount;
		this.methodCyclomaticComplex = methodCyclomaticComplex;
		this.classCyclomaticComplex = classCyclomaticComplex;
		this.atfdCount = atfdCount;
		this.methodAccessAttributeOtherClass = methodAccessAttributeOtherClass;
		this.tccCount = tccCount;
		this.methodAccessAttribute = methodAccessAttribute;
		this.numberOfClassMessages = numberOfClassMessages;
		this.globalVariables = globalVariables;
		this.publicMethods = publicMethods;
		this.privateMethods = privateMethods;
	}

	public void ExampleMethod(Report report, String className, String currentMethodName,
			String currentMethodBodyString, Integer numberOfMethods, Integer methodStatementsCount,
			Integer classStatementsCount, Integer methodCyclomaticComplex, Integer classCyclomaticComplex,
			Integer atfdCount, Integer methodAccessAttributeOtherClass, Integer tccCount, Integer methodAccessAttribute,
			Integer numberOfClassMessages, Integer globalVariables, Integer publicMethods, Integer privateMethods) {
		this.report = report;
		this.className = className;
		this.currentMethodName = currentMethodName;
		this.currentMethodBodyString = currentMethodBodyString;
		this.numberOfMethods = numberOfMethods;
		this.methodStatementsCount = methodStatementsCount;
		this.classStatementsCount = classStatementsCount;
		this.methodCyclomaticComplex = methodCyclomaticComplex;
		this.classCyclomaticComplex = classCyclomaticComplex;
		this.atfdCount = atfdCount;
		this.methodAccessAttributeOtherClass = methodAccessAttributeOtherClass;
		this.tccCount = tccCount;
		this.methodAccessAttribute = methodAccessAttribute;
		this.numberOfClassMessages = numberOfClassMessages;
		this.globalVariables = globalVariables;
		this.publicMethods = publicMethods;
		this.privateMethods = privateMethods;
	}
	
	private String getTypeBadCodeSmell(BadCodeSmell badCodeSmell) {

		if (badCodeSmell instanceof DeadCode)
			return badCodeSmell.getType();
		else if (badCodeSmell instanceof DuplicatedCode)
			return "Duplicated Code";
		else if (badCodeSmell instanceof LargeClass)
			return "Large Class";
		else if (badCodeSmell instanceof LongMethod)
			return "Long Method";
		else if (badCodeSmell instanceof LongParameterList)
			return "Long Parameter List";
		else
			return "Undefined";
	}
	
	private String TypeBad(BadCodeSmell badCodeSmell) {

		if (badCodeSmell instanceof DeadCode)
			return badCodeSmell.getType();
		else if (badCodeSmell instanceof DuplicatedCode)
			return "Duplicated Code";
		else if (badCodeSmell instanceof LargeClass)
			return "Large Class";
		else if (badCodeSmell instanceof LongMethod)
			return "Long Method";
		else if (badCodeSmell instanceof LongParameterList)
			return "Long Parameter List";
		else
			return "Undefined";
	}
	
	public void visualizeSystem(String name) {

		try {
			new ProgressFrame(name);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void System(String name) {

		try {
			new Frame(name);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}