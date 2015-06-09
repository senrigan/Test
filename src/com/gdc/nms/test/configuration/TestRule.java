package com.gdc.nms.test.configuration;

import java.util.Arrays;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.gdc.nms.test.runner.TestInformed;


public class TestRule extends TestWatcher{
	/**
	 * this method is called when one test method is failed 
	 */
	@Override
	protected void failed(Throwable e , Description description){
		System.out.println(e.getClass());
		System.out.println("ocurrio un fallo en el test");
		System.out.println("decription"+description);
		System.out.println("thorwabe"+e.getMessage());
		System.out.println("starcktrece"+Arrays.toString(e.getStackTrace()));
		TestInformed.setErrorLog(appendErrorMessage(e));
			
	}
	/**
	 * This method converts fail to display information in a log
	 * @param e
	 * @return
	 */
	private StringBuffer appendErrorMessage(Throwable e){
		String errorMessage="";
		if(e.getMessage()!=null){
			errorMessage+=e.getMessage();
		}
		if(e.getCause()!=null){
			errorMessage+=e.getCause();
		}
		StringBuffer errorInfo=TestInformed.getTabulatorEspace(5);
		errorInfo.append(" ");
		errorInfo.append(errorMessage);
		errorInfo.append(" ");
		errorInfo.append(Arrays.toString(e.getStackTrace()));
		return errorInfo;
	}
	
	/**
	 * this method is invoked when one test method is call and this have succeeded
	 */
	@Override
	 protected void succeeded(Description description) {
		System.out.println("paso el test"+description);
		System.out.println("method"+description.getMethodName());
		System.out.println("class name"+description.getClassName());
		System.out.println("display name"+description.getDisplayName());
	}

	
	
	
}
