/*
 * Created on Oct 10, 2004
 */
package br.ufma.deinf.laws.ncleclipse.xml;



/**
 * 
 * @author Phil Zoio
 */
public class XMLValidationError
	{
	    private String errorMessage;
	    private int lineNumber;
	    private int columnNumber;

	    public String getErrorMessage()
	    {
	        return errorMessage;
	    }

	    public void setErrorMessage(String errorMessage)
	    {
	        this.errorMessage = errorMessage;
	    }

	    public int getLineNumber()
	    {
	        return lineNumber;
	    }

	    public void setLineNumber(int lineNumber)
	    {
	        this.lineNumber = lineNumber;
	    }

	    public int getColumnNumber()
	    {
	        return columnNumber;
	    }

	    public void setColumnNumber(int columnNumber)
	    {
	        this.columnNumber = columnNumber;
	    }

	    public String toString()
	    {
	        StringBuffer buf = new StringBuffer();
	        buf.append("Error on ")
	            .append(" line ")
	            .append(lineNumber)
	            .append(", column ")
	            .append(columnNumber)
	            .append(": ")
	            .append(errorMessage);
	        return buf.toString();
	    }
	}

