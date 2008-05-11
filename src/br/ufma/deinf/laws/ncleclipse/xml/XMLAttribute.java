
package br.ufma.deinf.laws.ncleclipse.xml;

public class XMLAttribute
{

	private String name;
	private String value;

	public XMLAttribute(String name)
	{
		super();
		this.name = name;
	}

	public XMLAttribute(String name, String value)
	{
		super();
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}
}