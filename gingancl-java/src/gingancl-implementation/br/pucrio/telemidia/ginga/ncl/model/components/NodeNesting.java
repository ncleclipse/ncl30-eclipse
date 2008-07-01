/******************************************************************************
Este arquivo é parte da implementação do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laboratório TeleMídia

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
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
******************************************************************************
This file is part of the declarative environment of middleware Ginga (Ginga-NCL)

Copyright: 1989-2007 PUC-RIO/LABORATORIO TELEMIDIA, All Rights Reserved.

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
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
*******************************************************************************/
package br.pucrio.telemidia.ginga.ncl.model.components;

import java.util.ArrayList;
import java.util.List;

import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ncl.components.INode;

public class NodeNesting implements INodeNesting {
	private static final long serialVersionUID = -2781801487585648979L;

	/**
	 * A string containig all node ids, in sequence.
	 */
	private String id;

	/**
	 * Node list. The first node is the most nesting.
	 */
	private List<INode> nodes;

	/**
	 * Class constructor.
	 */
	public NodeNesting() {
		this.nodes = new ArrayList<INode>();
		id = "";
	}

	/**
	 * Class constructor.
	 * 
	 * @param node
	 *          the first node of this node nesting.
	 */
	public NodeNesting(INode node) {
		this();
		insertAnchorNode(node);
	}

	/**
	 * Class constructor.
	 * 
	 * @param seq
	 *          a node sequence.
	 */
	public NodeNesting(INodeNesting seq) {
		this();
		append(seq);
	}

	/**
	 * Class constructor.
	 * 
	 * @param seq
	 *          a node sequence.
	 */
	public NodeNesting(List<INode> seq) {
		this();
		append(seq);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#append(br.org.ginga.ncl.model.components.INodeNesting)
	 */
	public void append(INodeNesting otherSeq) {
		int i, size;

		size = otherSeq.getNumNodes();
		for (i = 0; i < size; i++) {
			insertAnchorNode(otherSeq.getNode(i));
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#append(java.util.List)
	 */
	public void append(List<INode> otherSeq) {
		int i, size;

		size = otherSeq.size();
		for (i = 0; i < size; i++) {
			insertAnchorNode((INode)otherSeq.get(i));
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#toList()
	 */
	public List<INode> toList() {
		return new ArrayList<INode>(nodes);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#compareTo(br.org.ginga.ncl.model.components.INodeNesting)
	 */
	public int compareTo(INodeNesting seq) {
		return id.compareTo(((NodeNesting)seq).id);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other_persp) {
		if (!(other_persp instanceof NodeNesting))
			return false;

		if (compareTo((NodeNesting)other_persp) == 0)
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getAnchorNode()
	 */
	public INode getAnchorNode() {
		if (nodes.size() <= 0)
			return null;
		else
			return (INode)nodes.get(nodes.size() - 1);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getHeadNode()
	 */
	public INode getHeadNode() {
		if (nodes.size() <= 0)
			return null;
		else
			return (INode)nodes.get(0);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getNode(int)
	 */
	public INode getNode(int index) {
		if (index < 0 || index >= nodes.size())
			return null;

		return (INode)nodes.get(index);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getNumNodes()
	 */
	public int getNumNodes() {
		return nodes.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		// return toString().hashCode();
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#insertAnchorNode(br.org.ncl.components.INode)
	 */
	public void insertAnchorNode(INode node) {
		if (nodes.size() > 0)
			id = id + "/" + node.getId();
		else
			id = node.getId().toString();
		nodes.add(node);
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#insertHeadNode(br.org.ncl.components.INode)
	 */
	public void insertHeadNode(INode node) {
		if (nodes.size() > 0)
			id = node.getId() + "/" + id;
		else
			id = node.getId().toString();
		nodes.add(0, node);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		int i, size;
		String text;

		text = "";

		size = nodes.size();
		for (i = 0; i < size; i++) {
			text = text + "/" + nodes.get(i).toString();
		}
		return text;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#removeAnchorNode()
	 */
	public boolean removeAnchorNode() {
		if (nodes.size() <= 0)
			return false;

		nodes.remove(nodes.size() - 1);
		id = id.substring(0, id.lastIndexOf("/"));
		return true;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#removeHeadNode()
	 */
	public boolean removeHeadNode() {
		if (nodes.size() <= 0)
			return false;

		nodes.remove(0);
		id = id.substring(id.indexOf("/") + 1);
		return true;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#removeNode(br.org.ncl.components.INode)
	 */
	public boolean removeNode(INode node) {
		int i;

		i = nodes.indexOf(node);

		if (i == -1)
			return false;

		while (nodes.size() > i) {
			removeAnchorNode();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#getSubsequence(int, int)
	 */
	public INodeNesting getSubsequence(int begin, int end) {
		INodeNesting new_sequence;
		int i;

		if (begin < 0 || begin >= nodes.size() || end < begin
				|| end >= nodes.size())
			return null;

		new_sequence = new NodeNesting((INode)nodes.get(begin));
		for (i = begin + 1; i <= end; i++) {
			new_sequence.insertAnchorNode((INode)nodes.get(i));
		}

		return new_sequence;
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.model.components.INodeNesting#copy()
	 */
	public INodeNesting copy() {
		return new NodeNesting(this);
	}
}
