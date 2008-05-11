/*
 * Created on Oct 10, 2004
 */
package br.ufma.deinf.laws.ncleclipse.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Phil Zoio
 */
public class EscapedCharRule implements IRule
{

	IToken fToken;
	StringBuffer buffer = new StringBuffer();

	public EscapedCharRule(IToken token)
	{
		super();
		this.fToken = token;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner)
	{

		buffer.setLength(0);

		int c = read(scanner);
		if (c == '&')
		{
		    
		    int i = 0;
			do
			{
				c = read(scanner);
				i++;
				
				if (c == '<' || c == ']')
				{
				    System.out.println("Char " + (char)c);
					for (int j= i-1; j > 0; j--)
						scanner.unread();
					return Token.UNDEFINED;
				}
			}
			while (c != ';');
			return fToken;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}
	

	private int read(ICharacterScanner scanner)
	{
		int c = scanner.read();
		buffer.append((char) c);
		return c;
	}

	
}