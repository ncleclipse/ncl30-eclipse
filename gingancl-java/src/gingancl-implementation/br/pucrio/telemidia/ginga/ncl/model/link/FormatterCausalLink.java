/******************************************************************************
Este arquivo � parte da implementa��o do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laborat�rio TeleM�dia

Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob 
os termos da Licen�a P�blica Geral GNU vers�o 2 conforme publicada pela Free 
Software Foundation.

Este programa � distribu�do na expectativa de que seja �til, por�m, SEM 
NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU 
ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral do 
GNU vers�o 2 para mais detalhes. 

Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do GNU vers�o 2 junto 
com este programa; se n�o, escreva para a Free Software Foundation, Inc., no 
endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informa��es:
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

import br.org.ginga.ncl.model.link.IFormatterCausalLink;
import br.org.ginga.ncl.model.link.ILinkAction;
import br.org.ginga.ncl.model.link.ILinkActionProgressionListener;
import br.org.ginga.ncl.model.link.ILinkTriggerCondition;
import br.org.ginga.ncl.model.link.ILinkTriggerListener;
import br.org.ncl.link.ILink;
import br.pucrio.telemidia.ginga.ncl.model.components.CompositeExecutionObject;

public class FormatterCausalLink extends FormatterLink implements
		IFormatterCausalLink, ILinkTriggerListener, ILinkActionProgressionListener {
	private ILinkTriggerCondition condition;

	private ILinkAction action;

	public FormatterCausalLink(ILinkTriggerCondition condition,
			ILinkAction action, ILink ncmLink, CompositeExecutionObject parentObject) {
		super(ncmLink, parentObject);
		this.condition = condition;
		this.action = action;

		if (this.condition != null) {
			this.condition.setTriggerListener(this);
		}

		if (this.action != null) {
			this.action.addActionProgressionListener(this);
		}
	}

	public ILinkAction getAction() {
		return action;
	}

	public ILinkTriggerCondition getTriggerCondition() {
		return condition;
	}

	public void conditionSatisfied(ILinkTriggerCondition condition) {
		Thread actionThread;

		// System.err.println("FormatterCausalLink::conditionSatisfied " +
		// ncmLink.getId());
		actionThread = new Thread(action);
		actionThread.start();
	}

	public Iterator getEvents() {
		List events;

		events = condition.getEvents();
		events.addAll(action.getEvents());
		return events.iterator();
	}

	public void destroy() {
		this.condition.destroy();
		this.condition = null;
		this.action.destroy();
		this.action = null;
	}

	public void evaluationStarted() {
		// System.err.println("FormatterCausalLink::evaluationStarted " +
		// ncmLink.getId());
		super.parentObject.linkEvaluationStarted(this);
	}

	public void evaluationEnded() {
		// System.err.println("FormatterCausalLink::evaluationFinished " +
		// ncmLink.getId());
		super.parentObject.linkEvaluationFinished(this, false);
	}

	public void actionProcessed(boolean start) {
		// System.err.println("FormatterCausalLink::actionProcessed " +
		// ncmLink.getId());
		super.parentObject.linkEvaluationFinished(this, start);
	}
}