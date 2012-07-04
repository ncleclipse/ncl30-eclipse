/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.ncl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Position;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class NCLElement {
	private List elementChildren = new ArrayList();
	private Map<String, String> attributes = new HashMap();

	private String tagName;
	private String perspective;
	private String completePerspective;
	private String doc = NCLEditorMessages.getInstance().getString(
			"NCLDoc.Empty");

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		if (doc == null)
			return;
		String beginComment = "@doc";
		int index = doc.indexOf(beginComment);
		if (index != -1)
			this.doc = doc.substring(index + beginComment.length() + 1,
					doc.length()).replace("\t", "");
	}

	private NCLElement parent;
	private Position position; // ver utilidade depois

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	private int lineNumber;

	public NCLElement(String name) {
		super();
		this.tagName = name;
	}

	public NCLElement(String name, String perspective) {
		super();
		this.tagName = name;
		this.perspective = perspective;
	}

	public NCLElement(String name, String perspective, int lineNumber) {
		super();
		this.tagName = name;
		this.perspective = perspective;
		this.lineNumber = lineNumber;
	}

	public List getChildrenDTDElements() {
		return elementChildren;
	}

	public NCLElement addChildElement(NCLElement element) {
		elementChildren.add(element);
		element.setParent(this);
		return this;
	}

	public void setParent(NCLElement element) {
		this.parent = element;
	}

	public NCLElement getParent() {
		return parent;
	}

	public String getTagName() {
		return tagName;
	}

	public void setAttributeValue(String localtagName, String value) {
		attributes.put(localtagName, value);
	}

	public String getAttributeValue(String localtagName) {
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

	public void setCompletePerspective(String completePerspective) {
		this.completePerspective = completePerspective;
	}

	public String getCompletePerspective() {
		return this.completePerspective;
	}

	public void clear() {
		elementChildren.clear();
		attributes.clear();
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
}
