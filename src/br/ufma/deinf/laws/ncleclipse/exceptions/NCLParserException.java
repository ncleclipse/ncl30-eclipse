package br.ufma.deinf.laws.ncleclipse.exceptions;

import org.w3c.dom.Element;

public class NCLParserException extends Exception{
	String msg = null;
	Element element = null;
	String id = null;
	public NCLParserException(String id, String msg, Element root) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.msg = msg;
		this.element = root;
	}
	
	public String toString(){
		return this.msg;
	}
}
