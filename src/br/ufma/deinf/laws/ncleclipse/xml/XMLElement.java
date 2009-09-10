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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.Position;
/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class XMLElement
{

	private List elementChildren = new ArrayList();
	private List attributeChildren = new ArrayList();

	private String name;
	private XMLElement parent;
	private Position position;

	public XMLElement(String name)
	{
		super();
		this.name = name;
	}

	public List getChildrenDTDElements()
	{
		return elementChildren;
	}

	public XMLElement addChildElement(XMLElement element)
	{
		elementChildren.add(element);
		element.setParent(this);
		return this;
	}

	public void setParent(XMLElement element)
	{
		this.parent = element;
	}

	public XMLElement getParent()
	{
		return parent;
	}

	public XMLElement addChildAttribute(XMLAttribute attribute)
	{
		attributeChildren.add(attribute);
		return this;
	}

	public String getName()
	{
		return name;
	}
	
	public String getAttributeValue(String localName)
	{
		for (Iterator iter = attributeChildren.iterator(); iter.hasNext();)
		{
			XMLAttribute attribute = (XMLAttribute) iter.next();
			if (attribute.getName().equals(localName)) return attribute.getValue();
		}
		return null;
	}

	public void clear()
	{
		elementChildren.clear();
		attributeChildren.clear();
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}

	public Position getPosition()
	{
		return position;
	}
}