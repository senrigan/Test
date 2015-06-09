package com.gdc.nms.test.runner;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


import static com.gdc.nms.test.runner.TestManager.tabulator;

public class TestInformed {
	private JUnitCore junit=new JUnitCore();
	private Result resulTest;
	private long ignoreCaseCount;
	private long failuresCaseCount;
	private long testedCaseCount;
	private long getRuntimeCount;
	private List<Failure> failuresTrace;
	private static Logger log;
	private static int numFailClass=0;
	private static int numSuccedClass=0;
	private static int numTestClass=0;
	private static StringBuffer errorLog;
	
	
	public TestInformed(){
		log=Logger.getLogger(TestInformed.class);
	}
	public void runTestClass(Class<?> testClass){
		
		StringBuffer logInformed=getTabulatorEspace(4);
		logInformed.append("iniciando TestClass: ");
		logInformed.append(testClass.getSimpleName());
		resulTest=junit.run(testClass);
		failuresCaseCount=resulTest.getFailureCount();
		ignoreCaseCount=resulTest.getIgnoreCount();
		getRuntimeCount=resulTest.getRunTime();
		testedCaseCount=resulTest.getRunCount();
		failuresTrace=resulTest.getFailures();
		if(!failuresTrace.isEmpty()){
			System.out.println("failures trace"+failuresTrace);
			System.out.println("trace"+failuresTrace.get(0).getTrace());
			System.out.println("taest header"+failuresTrace.get(0).getTestHeader());
			System.out.println("description"+failuresTrace.get(0).getDescription());
		}
		String result=logInformed.toString();
		if(testIsSucessful() && getFailuresCaseCount()==0){
			numSuccedClass++;
//			logInformed.append(getTabulatorEspace(2)+" Passed");
			result=String.format("%-80s%10s",result,"Passed");
			errorLog=null;
		}else{
			numFailClass++;
			if(isErrorLog(errorLog)){
				result=String.format("%-80s%10s",result,"Error");
//				logInformed.append(getTabulatorEspace(2)+" Error");
			}else{
				result=String.format("%-80s%10s",result,"Failed");
//				logInformed.append(getTabulatorEspace(2)+" Failed");

			}

		}
		numTestClass++;
		log.info(result);
		if(errorLog!=null){
			log.error(getTabulatorEspace(5)+""+errorLog);
		}
		
	}
	
	private boolean isErrorLog(StringBuffer error){
		String newError=error.substring(0,error.indexOf(","));
		System.out.println("new Error"+newError);
		System.out.println("error*-*-*"+error);
		if(newError.indexOf("junit")!=-1 ){
			System.out.println("error junit");
			return false;
		}
		return true;
	}
	public static void setErrorLog(StringBuffer errorMessage){
			errorLog=errorMessage;
	}
	public int  getNumofFailedClass(){
		return numFailClass;
	}
	
	public int getNumofSuccedClass(){
		return numSuccedClass;
	}
	
	public int getNumofTestClass(){
		return numTestClass;
	}
	
	private long  numOfTestSucced(){
		return testedCaseCount-failuresCaseCount;
	}
	
	public List<Failure> getFailuresTrace() {
		return failuresTrace;
	}

	public boolean testIsSucessful(){
		return resulTest.wasSuccessful();
	}
	public long getIgnoreCaseCount() {
		return ignoreCaseCount;
	}

	public long getFailuresCaseCount() {
		return failuresCaseCount;
	}

	public long getTestedCaseCount() {
		return testedCaseCount;
	}

	public long getGetRuntimeCount() {
		return getRuntimeCount;
	}

	@Override
	public String toString() {
		return "TestInformed [junit=" + junit + ", resulTest=" + resulTest
				+ ", ignoreCaseCount=" + ignoreCaseCount
				+ ", failuresCaseCount=" + failuresCaseCount
				+ ", TestedCaseCount=" + testedCaseCount + ", getRuntimeCount="
				+ getRuntimeCount + ", failuresTrace=" + failuresTrace + "]";
	}
	
	
	public static StringBuffer getTabulatorEspace(int numTabulator){
		int espaceDefinit=5;
		String numTab="%"+Integer.toString(numTabulator*espaceDefinit)+"s";
		
		StringBuffer tab=new StringBuffer(String.format(numTab,""));
		return tab;
	}
	

	
	
}
