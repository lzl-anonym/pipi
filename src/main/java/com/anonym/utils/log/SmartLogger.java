package com.anonym.utils.log;

public interface SmartLogger {

	void error(String string, Throwable t);
	
	void error(String string);

	void warn(String string);

}
