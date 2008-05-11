package br.ufma.deinf.laws.ncleclipse.ncl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import br.ufma.deinf.laws.util.MultiHashMap;

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
	
	public void addElement(String tagname, String id) {
		// TODO Auto-generated method stub
		if(alias != null && !alias.equals(""))
				elements.put(tagname, alias + "#" +id);		
		else elements.put(tagname, id);
	}
	
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
}
