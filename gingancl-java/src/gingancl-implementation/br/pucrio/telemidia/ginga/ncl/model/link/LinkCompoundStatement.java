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
package br.pucrio.telemidia.ginga.ncl.model.link;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import br.org.ginga.ncl.model.link.ILinkCompoundStatement;
import br.org.ginga.ncl.model.link.ILinkStatement;
import br.org.ncl.connectors.ICompoundStatement;

public class LinkCompoundStatement implements ILinkCompoundStatement {
	protected List statements;

	protected boolean isNegated;

	protected short operator;

	public LinkCompoundStatement(short operator) {
		super();
		statements = new Vector();
		this.operator = operator;
	}

	public short getOperator() {
		return operator;
	}

	public void addStatement(ILinkStatement statement) {
		statements.add(statement);
	}

	public Iterator getStatements() {
		return statements.iterator();
	}

	public boolean isNegated() {
		return isNegated;
	}

	public void setNegated(boolean neg) {
		isNegated = neg;
	}

	protected boolean returnEvaluationResult(boolean result) {
		return isNegated ^ result;
	}

	public List getEvents() {
		List events;
		int i, size;
		ILinkStatement statement;

		events = new Vector();
		size = statements.size();
		for (i = 0; i < size; i++) {
			statement = (ILinkStatement)statements.get(i);
			events.addAll(statement.getEvents());
		}
		return events;
	}

	public void destroy() {
		int i, size;
		ILinkStatement statement;

		size = statements.size();
		for (i = 0; i < size; i++) {
			statement = (ILinkStatement)statements.get(i);
			statement.destroy();
		}
		statements.clear();
		statements = null;
	}

	public boolean evaluate() {
		int i, size;
		ILinkStatement childStatement;

		size = statements.size();

		if (operator == ICompoundStatement.OP_OR) {
			for (i = 0; i < size; i++) {
				childStatement = (ILinkStatement)statements.get(i);
				if (childStatement.evaluate())
					return returnEvaluationResult(true);
			}
			return returnEvaluationResult(false);
		}
		else {
			for (i = 0; i < size; i++) {
				childStatement = (ILinkStatement)statements.get(i);
				if (!childStatement.evaluate())
					return returnEvaluationResult(false);
			}
			return returnEvaluationResult(true);
		}
	}
}