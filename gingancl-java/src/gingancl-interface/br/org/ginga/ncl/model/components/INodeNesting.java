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
package br.org.ginga.ncl.model.components;

import java.io.Serializable;
import java.util.List;

import br.org.ncl.components.INode;

/**
 * This class models a sequence of nodes, a perspective. The perspective of a
 * node N is a sequence P = {N0,....,Nm}, with m >= 1, such that Nm = N, Ni is
 * a composite node, Ni+1 is contained in Ni, for i belonging to [0,m-1), and N0
 * is not contained in any other node. Note that it is possible to define
 * several perspectives for the same node N, if this node is contained in more
 * than one composition. The current perspective of a node is the one traversed
 * by the most recent navigation to the node. Given the perspective P =
 * {N0,....,Nm), Nm is called the base node of the perspective (or the anchor
 * node) and N0 is called the head node of the perspective.
 */
public interface INodeNesting extends Serializable {

	/**
	 * This method adds a new sequence in the end of the sequence of this object.
	 * 
	 * @param other_seq
	 *          the perspective to be appended.
	 */
	void append(INodeNesting other_seq);

	void append(List<INode> other_seq);

	List<INode> toList();

	/**
	 * Compares a specific perpective with this one.
	 * 
	 * @param other_seq
	 *          the specific perpective to be compared.
	 * @return 0 with these perpectives are equals, 1 if this perpective is bigger
	 *         than the other and -1 if this perspective is smaller than the
	 *         other.
	 */
	int compareTo(INodeNesting other_seq);

	/**
	 * This method checks if a specific perspective is equal to this one.
	 * 
	 * @param obj
	 *          the perspective to be compared against this one.
	 * @return true if both perspectives are equals and false otherwise.
	 */
	boolean equals(Object obj);

	/**
	 * Given the perspective P = {N0,....,Nm}, Nm is called the base node the
	 * perspective (or the anchor node). This method returns the anchor node.
	 * 
	 * @return the anchor node of this perspective.
	 */
	INode getAnchorNode();

	/**
	 * Given the perspective P = {N0,....,Nm}, N0 is not contained in any other
	 * node and it is called head node. This method returns this node.
	 * 
	 * @return the head node of this perspective.
	 */
	INode getHeadNode();

	/**
	 * Returns a node from the perspective of a specific index.
	 * 
	 * @param index
	 *          specific index where of the desired node.
	 * @return a node from the perspective of a specific index.
	 */
	INode getNode(int index);

	/**
	 * Returns the number of nodes of the perspective.
	 * 
	 * @return number of nodes of the perspective.
	 */
	int getNumNodes();

	/**
	 * Returns a perspective containig the node from specific index
	 * limits.
	 * 
	 * @param begin
	 *          the index in this perpective where the new perspective must begin.
	 * @param end
	 *          the index in this perpective where the new perspective must end.
	 * @return the new perspective.
	 */
	INodeNesting getSubsequence(int begin, int end);

	/**
	 * Inserts a new node after the anchor node of this perspective.
	 * 
	 * @param node
	 *          a new node to be inserted.
	 */
	void insertAnchorNode(INode node);

	/**
	 * Inserts a new node before the head node of this perspective.
	 * 
	 * @param node
	 *          a new node to be inserted.
	 */
	void insertHeadNode(INode node);

	/**
	 * Removes the anchor node from this perspective.
	 * 
	 * @return true if the operation was successful and false otherwise.
	 */
	boolean removeAnchorNode();

	/**
	 * Removes the head node from this perspective.
	 * 
	 * @return true if the operation was successful and false otherwise.
	 */
	boolean removeHeadNode();

	/**
	 * Removes a node of the perspective in a specific index.
	 * Furthermore, all nodes recursively contained in the node are also removed.
	 * 
	 * @param node
	 *          the node to be removed.
	 * @return true if the operation was successful and false otherwise.
	 */
	boolean removeNode(INode node);

	/**
	 * Returns a string containig all node ids, in sequence.
	 * 
	 * @return all node ids, in sequence.
	 */
	String getId();

	/**
	 * Returns a copy of this perspective. This method does not create
	 * a copy of the nodes in the perspective.
	 * 
	 * @return a copy of this perspectiv
	 */
	INodeNesting copy();
}
