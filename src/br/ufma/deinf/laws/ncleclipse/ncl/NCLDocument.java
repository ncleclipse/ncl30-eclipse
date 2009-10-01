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
package br.ufma.deinf.laws.ncleclipse.ncl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import br.ufma.deinf.laws.util.MultiHashMap;
/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class NCLDocument{
	protected MultiHashMap elements;
	protected Element root;
	protected String id;
	protected URI parentURI;
	protected String fileName;
	protected String alias;
	
	public NCLDocument(){
		elements = new MultiHashMap();
	}

	public MultiHashMap getElements() {
		return elements;
	}

	public void setElements(MultiHashMap elements) {
		this.elements = elements;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	//tem q voltar a ser Assim
	//Problema: Não consigo instanciar Attributes logo sempre o valor é o do último
	public void addElement(String tagname, Attributes atts) {
		// TODO Auto-generated method stub
		elements.put(tagname, atts);
	}
	
	/*public void addElement(String tagname, String id) {
		// TODO Auto-generated method stub
		if(alias != null && !alias.equals(""))
				elements.put(tagname, alias + "#" +id);		
		else elements.put(tagname, id);
	}*/
	
	public void addElement(NCLElement element, String id) {
		// TODO Auto-generated method stub
		if(alias != null && !alias.equals("")){
				element.setAttributeValue("id", alias+"#"+element.getAttributeValue("id"));
				elements.put(element.getTagName(), element);
		}
		else elements.put(element.getTagName(), element);
	}	

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public URI getParentURI() {
		return parentURI;
	}

	public void setParentURI(URI parentURI) {
		this.parentURI = parentURI;
	}
	
	public Collection getElementsFromPerspective(String tagname, String perspective){
		Collection elementsFromPerspective = new ArrayList();
		Collection elements = getElements().get(tagname);
		if (elements == null) return null;
		Iterator it = elements.iterator();
		while(it.hasNext()){
			NCLElement nclElement = (NCLElement)it.next();
			if(nclElement.getPerspective().equals(perspective)){
				elementsFromPerspective.add(nclElement);
			}
			
		}
		return elementsFromPerspective;
	}
	
	public NCLElement getElementById(String id){
		Set keySet = getElements().keySet();
		if (elements == null) return null;
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			Collection elements = getElements().get((String)it.next());
			if (elements == null) return null;
			Iterator it2 = elements.iterator();
			while(it2.hasNext()){
				NCLElement nclElement = (NCLElement)it2.next();
				String idElement = nclElement.getAttributes().get("id");
				if( idElement != null && idElement.equals(id)){
					return nclElement;
				}
			}
		}
		return null;
	}
	
	public Vector<NCLElement> getElements(String tag, String attribute){
		Set keySet = getElements().keySet();
		if (elements == null) return null;
		Iterator it = keySet.iterator();
		Vector <NCLElement> r = new Vector <NCLElement> ();
		while(it.hasNext()){
			Collection elements = getElements().get((String)it.next());
			if (elements == null) return null;
			Iterator it2 = elements.iterator();
			while(it2.hasNext()){
				NCLElement nclElement = (NCLElement)it2.next();
				String idElement = nclElement.getAttributes().get(tag);
				if( idElement != null && idElement.equals(attribute)){
						r.add (nclElement);
				}
			}
		}
		
		return r.size() == 0 ? null : r;
	}
	
	public NCLElement getElementByAlias(String alias){
		Set keySet = getElements().keySet();
		if (elements == null) return null;
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			Collection elements = getElements().get((String)it.next());
			if (elements == null) return null;
			Iterator it2 = elements.iterator();
			while(it2.hasNext()){
				NCLElement nclElement = (NCLElement)it2.next();
				String aliasElement = nclElement.getAttributes().get("alias");
				if( aliasElement != null && aliasElement.equals(alias)){
					return nclElement;
				}
			}
		}
		return null;
	}
}
