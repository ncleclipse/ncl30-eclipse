/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class XMLTree
{

	private XMLElement rootElement;
	private List allElements = new ArrayList();
	private List allAttributes = new ArrayList();

	public XMLTree()
	{

		super();
		rootElement = new XMLElement("world");
		XMLElement continent = newDTDElement("continent");
		rootElement.addChildElement(continent);

		continent.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
		addAttribute("name");
		addAttribute("population");

		XMLElement continentDescription = newDTDElement("description");
		continent.addChildElement(continentDescription);

		XMLElement country = newDTDElement("country");
		country.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
		continent.addChildElement(country);

		XMLElement countryDescription = newDTDElement("description");
		country.addChildElement(countryDescription);
		XMLElement countryAttraction = newDTDElement("attraction");
		country.addChildElement(countryAttraction);
		countryAttraction.addChildAttribute(new XMLAttribute("name"));

		XMLElement city = newDTDElement("city");
		city.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
		country.addChildElement(city);

		XMLElement cityDescription = newDTDElement("description");
		city.addChildElement(cityDescription);
		XMLElement cityAttraction = newDTDElement("attraction");
		cityAttraction.addChildAttribute(new XMLAttribute("name"));
		cityAttraction.addChildAttribute(new XMLAttribute("cost"));
		city.addChildElement(cityAttraction);
		addAttribute("cost");

		XMLElement ocean = newDTDElement("ocean");
		continent.addChildElement(ocean);
		ocean.addChildAttribute(new XMLAttribute("name"));
		ocean.addChildAttribute(new XMLAttribute("depth"));
		addAttribute("depth");

	}

	private XMLElement newDTDElement(String elementName)
	{
		XMLElement element = new XMLElement(elementName);
		allElements.add(element);
		return element;
	}

	private void addAttribute(String attributeName)
	{
		if (!allAttributes.contains(attributeName))
		{
			allAttributes.add(attributeName);
		}
	}

	public List getAllElements()
	{
		return allElements;
	}

	public List getAllAttributes()
	{
		return allAttributes;
	}

	public XMLElement getRootElement()
	{
		return rootElement;
	}

	public void setRootElement(XMLElement rootElement)
	{
		this.rootElement = rootElement;
	}
}