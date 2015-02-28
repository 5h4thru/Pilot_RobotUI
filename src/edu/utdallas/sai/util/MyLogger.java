package edu.utdallas.sai.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A simple logger class that will be used to log any exceptions that occur in the application
 * The log file will be present in /logs folder
 * NetID: dxp141030
 * Date: 19th February, 2015
 * @author Durga Sai Preetham Palagummi
 *
 */
public class MyLogger {

	public static void writeToLog(String className, String exception) {
		Logger logger = Logger.getLogger(className);
		logger.setUseParentHandlers(false);
		MyFormatter formatter= new MyFormatter();
		FileHandler handler = null;
		try {
			handler = new FileHandler("./logs/LogFile%g%u.txt");
			handler.setFormatter(formatter);
			logger.addHandler(handler);
			logger.info("Class Name: "+className);
			logger.info("Exception: "+exception);
		} catch (SecurityException e) {
			logger.info("Error in logger class "+e.getMessage());
		} catch (IOException e) {
			logger.info("Error in logger class "+e.getMessage());
		} finally {
			if(handler!=null) handler.close();
		}
	}
}

class MyFormatter extends Formatter {
	//
	// Create a DateFormat to format the timestamp.
	//
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append(DATE_FORMATTER.format(new Date(record.getMillis()))).append(" - ");
		builder.append("[").append(record.getSourceClassName()).append(".");
		builder.append(record.getSourceMethodName()).append("] - ");
		builder.append("[").append(record.getLevel()).append("] - ");
		builder.append(formatMessage(record));
		builder.append("\n");
		return builder.toString();
	}

	public String getHead(Handler h) {
		return super.getHead(h);
	}

	public String getTail(Handler h) {
		return super.getTail(h);
	}
}
