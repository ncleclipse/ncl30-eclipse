/******************************************************************************
Este arquivo é parte da implementação do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratório de Sistemas Avançados da Web) 

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

******************************************************************************
This file is part of the authoring environment in Nested Context Language -
NCL Eclipse.

Copyright: 2007-2008 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

*******************************************************************************/

package br.ufma.deinf.laws.ncleclipse.ncl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Position;

public class NCLElement
{
	private List elementChildren = new ArrayList();
	private Map <String, String> attributes = new HashMap();

	private String tagName;
	private String perspective;
	
	private NCLElement parent;
	private Position position; // ver utilidade depois

	public NCLElement(String name)
	{
		super();
		this.tagName = name;
	}
	
	public NCLElement(String name, String perspective)
	{
		super();
		this.tagName = name;
		this.perspective = perspective;
	}	

	public List getChildrenDTDElements()
	{
		return elementChildren;
	}

	public NCLElement addChildElement(NCLElement element)
	{
		elementChildren.add(element);
		element.setParent(this);
		return this;
	}

	public void setParent(NCLElement element)
	{
		this.parent = element;
	}

	public NCLElement getParent()
	{
		return parent;
	}
	

	public String getTagName()
	{
		return tagName;
	}
	
	public void setAttributeValue(String localtagName, String value){
		attributes.put(localtagName, value);
	}
	
	public String getAttributeValue(String localtagName)
	{
		return attributes.get(localtagName);
	}

	public List getElementChildren() {
		return elementChildren;
	}

	public void setElementChildren(List elementChildren) {
		this.elementChildren = elementChildren;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getPerspective() {
		return perspective;
	}

	public void setPerspective(String perspective) {
		this.perspective = perspective;
	}

	public void clear()
	{
		elementChildren.clear();
		attributes.clear();
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