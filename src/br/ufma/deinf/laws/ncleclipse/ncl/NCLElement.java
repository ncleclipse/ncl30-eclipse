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