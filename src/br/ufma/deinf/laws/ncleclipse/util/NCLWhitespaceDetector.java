package br.ufma.deinf.laws.ncleclipse.util;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class NCLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
