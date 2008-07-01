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
package br.pucrio.telemidia.ginga.ncl.converter;

import java.util.Iterator;
import java.util.List;

import br.org.ginga.ncl.converter.ObjectCreationForbiddenException;
import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.components.INodeNesting;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.link.IFormatterCausalLink;
import br.org.ginga.ncl.model.link.ILinkAction;
import br.org.ginga.ncl.model.link.ILinkAssessment;
import br.org.ginga.ncl.model.link.ILinkAssessmentStatement;
import br.org.ginga.ncl.model.link.ILinkAttributeAssessment;
import br.org.ginga.ncl.model.link.ILinkCompoundAction;
import br.org.ginga.ncl.model.link.ILinkCompoundStatement;
import br.org.ginga.ncl.model.link.ILinkCompoundTriggerCondition;
import br.org.ginga.ncl.model.link.ILinkCondition;
import br.org.ginga.ncl.model.link.ILinkRepeatAction;
import br.org.ginga.ncl.model.link.ILinkSimpleAction;
import br.org.ginga.ncl.model.link.ILinkStatement;
import br.org.ginga.ncl.model.link.ILinkTriggerCondition;
import br.org.ncl.IParameter;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.components.INode;
import br.org.ncl.connectors.IAction;
import br.org.ncl.connectors.IAssessmentStatement;
import br.org.ncl.connectors.IAttributeAssessment;
import br.org.ncl.connectors.ICausalConnector;
import br.org.ncl.connectors.ICompoundAction;
import br.org.ncl.connectors.ICompoundStatement;
import br.org.ncl.connectors.ICompoundCondition;
import br.org.ncl.connectors.ICondition;
import br.org.ncl.connectors.IRole;
import br.org.ncl.connectors.ISimpleAction;
import br.org.ncl.connectors.ISimpleCondition;
import br.org.ncl.connectors.IStatementExpression;
import br.org.ncl.connectors.ITriggerExpression;
import br.org.ncl.connectors.IValueAssessment;
import br.org.ncl.interfaces.IInterfacePoint;
import br.org.ncl.link.IBind;
import br.org.ncl.link.ICausalLink;
import br.org.ncl.link.ILink;
import br.pucrio.telemidia.ginga.ncl.model.components.CompositeExecutionObject;
import br.pucrio.telemidia.ginga.ncl.model.components.NodeNesting;
import br.pucrio.telemidia.ginga.ncl.model.link.FormatterCausalLink;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkAndCompoundTriggerCondition;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkAssessmentStatement;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkAssignmentAction;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkAttributeAssessment;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkCompoundAction;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkCompoundStatement;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkCompoundTriggerCondition;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkRepeatAction;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkSimpleAction;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkTransitionTriggerCondition;
import br.pucrio.telemidia.ginga.ncl.model.link.LinkValueAssessment;

public class FormatterLinkConverter {
	private FormatterConverter compiler;

	public FormatterLinkConverter(FormatterConverter compiler) {
		this.compiler = compiler;
	}

	public IFormatterCausalLink createCausalLink(ICausalLink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		ICausalConnector connector;
		ICondition conditionExpression;
		IAction actionExpression;
		ILinkCondition formatterCondition;
		ILinkAction formatterAction;
		IFormatterCausalLink formatterLink;

		if (ncmLink == null) {
			return null;
		}

		/*
		 * System.err.print("FormatterLinkCompiler::createCausalLink "); if
		 * (ncmLink.getId() != null) { System.err.print(ncmLink.getId()); }
		 * System.err.println("");
		 */

		// compile link condition and verify if it is a trigger condition
		connector = (ICausalConnector)ncmLink.getConnector();
		conditionExpression = connector.getConditionExpression();
		formatterCondition = createTriggerExpression(
				(ITriggerExpression)conditionExpression, ncmLink, parentObject,
				depthLevel);
		if (formatterCondition == null
				|| !(formatterCondition instanceof ILinkTriggerCondition))
			return null;

		// compile link action
		actionExpression = connector.getActionExpression();
		formatterAction = createAction(actionExpression, ncmLink, parentObject,
				depthLevel);
		if (formatterAction == null)
			return null;

		// create formatter causal link
		formatterLink = new FormatterCausalLink(
				(ILinkTriggerCondition)formatterCondition, formatterAction, ncmLink,
				(CompositeExecutionObject)parentObject);
		return formatterLink;
	}

	private ILinkAction createAction(IAction actionExpression,
			ICausalLink ncmLink, ICompositeExecutionObject parentObject,
			int depthLevel) {

		double delay;
		ISimpleAction sae;
		ICompoundAction cae;
		List binds;
		int i, size;
		Object delayObject;
		ILinkSimpleAction simpleAction;
		ILinkCompoundAction compoundAction;

		if (actionExpression instanceof ISimpleAction) {
			sae = (ISimpleAction)actionExpression;
			binds = ncmLink.getRoleBinds(sae);
			size = binds.size();
			if (size == 1) {
				return createSimpleAction(sae, (IBind)binds.get(0), ncmLink,
						parentObject, depthLevel);
			}
			else if (size > 1) {
				compoundAction = new LinkCompoundAction(sae.getQualifier());
				for (i = 0; i < size; i++) {
					simpleAction = createSimpleAction(sae, (IBind)binds.get(i), ncmLink,
							parentObject, depthLevel);
					compoundAction.addAction(simpleAction);
				}
				return compoundAction;
			}
			else {
				return null;
			}
		}

		else { // ICompoundActionExpression)
			delayObject = actionExpression.getDelay();
			delay = compileDelay(ncmLink, delayObject, null);
			cae = (ICompoundAction)actionExpression;
			return createCompoundAction(cae.getOperator(), delay, cae.getActions(),
					ncmLink, parentObject, depthLevel);
		}
	}

	private ILinkCondition createCondition(ICondition ncmExpression,
			ICausalLink ncmLink, ICompositeExecutionObject parentObject,
			int depthLevel) {

		if (ncmExpression instanceof ITriggerExpression) {
			return createTriggerExpression((ITriggerExpression)ncmExpression,
					ncmLink, parentObject, depthLevel);
		}
		else { // IStatementExpression
			return createStatementExpression((IStatementExpression)ncmExpression,
					ncmLink, parentObject, depthLevel);
		}
	}

	private ILinkCompoundTriggerCondition createCompoundTriggerCondition(
			short operator, double delay, Iterator ncmChildConditions,
			ICausalLink ncmLink, ICompositeExecutionObject parentObject,
			int depthLevel) {
		ILinkCompoundTriggerCondition condition;
		ICondition ncmChildCondition;
		ILinkCondition childCondition;

		if (operator == ICompoundCondition.OP_AND) {
			condition = new LinkAndCompoundTriggerCondition();
		}
		else {
			condition = new LinkCompoundTriggerCondition();
		}
		if (delay > 0) {
			condition.setDelay(delay);
		}

		while (ncmChildConditions.hasNext()) {
			ncmChildCondition = (ICondition)ncmChildConditions.next();
			childCondition = createCondition(ncmChildCondition, ncmLink,
					parentObject, depthLevel);
			condition.addCondition(childCondition);
		}

		return condition;
	}

	private ILinkCondition createTriggerExpression(
			ITriggerExpression triggerExpression, ICausalLink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		double delay;
		ISimpleCondition ste;
		ICompoundCondition cte;
		List binds;
		int i, size;
		Object delayObject;
		ILinkCompoundTriggerCondition compoundCondition;
		ILinkTriggerCondition simpleCondition;

		if (triggerExpression instanceof ISimpleCondition) {
			ste = (ISimpleCondition)triggerExpression;
			binds = ncmLink.getRoleBinds(ste);
			size = binds.size();
			if (size == 1) {
				return createSimpleCondition(ste, (IBind)binds.get(0), ncmLink,
						parentObject, depthLevel);
			}
			else if (size > 1) {
				if (ste.getQualifier() == ICompoundCondition.OP_AND) {
					compoundCondition = new LinkAndCompoundTriggerCondition();
				}
				else {
					compoundCondition = new LinkCompoundTriggerCondition();
				}

				for (i = 0; i < size; i++) {
					simpleCondition = createSimpleCondition(ste, (IBind)binds.get(i),
							ncmLink, parentObject, depthLevel);
					compoundCondition.addCondition(simpleCondition);
				}
				return compoundCondition;
			}
			else {
				return null;
			}
		}

		else { // ICompoundTriggerExpression)
			delayObject = triggerExpression.getDelay();
			delay = compileDelay(ncmLink, delayObject, null);
			cte = (ICompoundCondition)triggerExpression;
			return createCompoundTriggerCondition(cte.getOperator(), delay, cte
					.getConditions(), ncmLink, parentObject, depthLevel);
		}
	}

	private ILinkAssessmentStatement createAssessmentStatement(
			IAssessmentStatement assessmentStatement, IBind bind, ILink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		ILinkAttributeAssessment mainAssessment;
		ILinkAssessment otherAssessment;
		IAttributeAssessment aa;
		ILinkAssessmentStatement statement;
		IValueAssessment valueAssessment;
		Object paramValue;
		IParameter connParam, param;
		List otherBinds;

		mainAssessment = createAttributeAssessment(assessmentStatement
				.getMainAssessment(), bind, ncmLink, parentObject, depthLevel);
		if (assessmentStatement.getOtherAssessment() instanceof IValueAssessment) {
			valueAssessment = (IValueAssessment)assessmentStatement
					.getOtherAssessment();
			paramValue = valueAssessment.getValue();
			if (paramValue instanceof IParameter) {
				connParam = (IParameter)paramValue;
				param = bind.getParameter(connParam.getName());
				if (param == null) {
					param = ncmLink.getParameter(connParam.getName());
				}
				if (param == null) {
					return null;
				}
				paramValue = param.getValue();
			}

			if (paramValue == null) {
				return null;
			}
			otherAssessment = new LinkValueAssessment((Comparable)paramValue);
		}
		else {
			aa = (IAttributeAssessment)assessmentStatement.getOtherAssessment();
			otherBinds = ncmLink.getRoleBinds(aa);
			otherAssessment = createAttributeAssessment(aa, (IBind)otherBinds.get(0),
					ncmLink, parentObject, depthLevel);
		}
		statement = new LinkAssessmentStatement(
				assessmentStatement.getComparator(), mainAssessment, otherAssessment);

		return statement;
	}

	private ILinkStatement createStatementExpression(
			IStatementExpression statementExpression, ILink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		IAssessmentStatement as;
		ICompoundStatement cs;
		List binds;
		int size;
		ILinkStatement statement, childStatement;
		Iterator statements;
		IStatementExpression ncmChildStatement;

		if (statementExpression instanceof IAssessmentStatement) {
			as = (IAssessmentStatement)statementExpression;
			binds = ncmLink.getRoleBinds(as.getMainAssessment());
			size = binds.size();
			if (size == 1) {
				statement = createAssessmentStatement(as, (IBind)binds.get(0), ncmLink,
						parentObject, depthLevel);
			}
			else {
				return null;
			}
		}

		else { // ICompoundStatement)
			cs = (ICompoundStatement)statementExpression;
			statement = new LinkCompoundStatement(cs.getOperator());
			((ILinkCompoundStatement)statement).setNegated(cs.isNegated());
			statements = cs.getStatements();
			while (statements.hasNext()) {
				ncmChildStatement = (IStatementExpression)statements.next();
				childStatement = createStatementExpression(ncmChildStatement, ncmLink,
						parentObject, depthLevel);
				((ILinkCompoundStatement)statement).addStatement(childStatement);
			}

		}

		return statement;
	}

	private ILinkAttributeAssessment createAttributeAssessment(
			IAttributeAssessment attributeAssessment, IBind bind, ILink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		IFormatterEvent event;

		event = createEvent(bind, ncmLink, parentObject, depthLevel);
		return new LinkAttributeAssessment(event, attributeAssessment
				.getAttributeType());
	}

	private ILinkSimpleAction createSimpleAction(ISimpleAction sae, IBind bind,
			ILink ncmLink, ICompositeExecutionObject parentObject, int depthLevel) {

		IFormatterEvent event;
		short actionType;
		ILinkSimpleAction action;
		IParameter connParam, param;
		Object paramValue, repeatObject, delayObject, animObject;
		IAnimation animation;

		long repeat;
		double delay;

		action = null;
		event = createEvent(bind, ncmLink, parentObject, depthLevel);
		actionType = sae.getActionType();
		switch (actionType) {
		case ISimpleAction.ACT_START:
			action = new LinkRepeatAction(event, actionType);

			repeatObject = sae.getRepeat();
			if (repeatObject == null) {
				repeat = 0;
			}
			else if (repeatObject instanceof IParameter) {
				connParam = (IParameter)repeatObject;
				param = bind.getParameter(connParam.getName());
				if (param == null) {
					param = ncmLink.getParameter(connParam.getName());
				}
				if (param == null) {
					repeat = 0;
				}
				else {
					repeat = Long.parseLong(param.getValue().toString());
				}
			}
			else {
				repeat = ((Long)repeatObject).longValue();
			}

			((ILinkRepeatAction)action).setRepetitions(repeat);

			delayObject = sae.getRepeatDelay();
			delay = compileDelay(ncmLink, delayObject, bind);
			((ILinkRepeatAction)action).setRepetitionInterval(delay);
			break;

		case ISimpleAction.ACT_STOP:
		case ISimpleAction.ACT_PAUSE:
		case ISimpleAction.ACT_RESUME:
		case ISimpleAction.ACT_ABORT:
			action = new LinkSimpleAction(event, actionType);
			break;

		case ISimpleAction.ACT_SET:
			paramValue = sae.getValue();
			if (paramValue instanceof IParameter) {
				connParam = (IParameter)paramValue;
				param = bind.getParameter(connParam.getName());
				if (param == null) {
					param = ncmLink.getParameter(connParam.getName());
				}
				if (param != null) {
					paramValue = param.getValue();
				}
				else {
					paramValue = null;
				}
			}

			action = new LinkAssignmentAction(event, actionType, paramValue);

			animation = sae.getAnimation();
			if (animation != null) {
				animObject = animation.getDuration();
				if (animObject instanceof IParameter) {
					param = bind.getParameter(((IParameter)animObject).getName());
					if (param == null) {
						param = ncmLink.getParameter(
								((IParameter)animObject).getName());
					}

					if (param != null && !param.getValue().toString().equals("")) {
						String attValue = param.getValue().toString();
						if(attValue.endsWith("s"))
							animation.setDuration(
									new Double(Double.parseDouble(attValue.substring(
											0, attValue.length() - 1))));
						else
							animation.setDuration(
									new Double(Double.parseDouble(attValue)));

					} else {
						animation.setDuration(0);
					}
				}

				animObject = animation.getBy();
				if (animObject instanceof IParameter) {
					param = bind.getParameter(((IParameter)animObject).getName());
					if (param == null) {
						param = ncmLink.getParameter(
								((IParameter)animObject).getName());
					}

					if (param != null && !param.getValue().toString().equals("")) {
						String attValue = param.getValue().toString();
						if(attValue.endsWith("s"))
							animation.setBy(
									new Double(Double.parseDouble(attValue.substring(
											0, attValue.length() - 1))));
						else
							animation.setBy(
									new Double(Double.parseDouble(attValue)));

					} else {
						animation.setBy(0);
					}
				}

				((LinkAssignmentAction)action).setAnimation(animation);
			}
			break;

		default:
			action = null;
			break;
		}

		if (action != null) {
			delayObject = sae.getDelay();
			delay = compileDelay(ncmLink, delayObject, bind);
			action.setWaitDelay(delay);
		}

		return action;
	}

	private ILinkCompoundAction createCompoundAction(short operator,
			double delay, Iterator ncmChildActions, ICausalLink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {
		ILinkCompoundAction action;
		IAction ncmChildAction;
		ILinkAction childAction;

		action = new LinkCompoundAction(operator);
		if (delay > 0) {
			action.setWaitDelay(delay);
		}

		while (ncmChildActions.hasNext()) {
			ncmChildAction = (IAction)ncmChildActions.next();
			childAction = createAction(ncmChildAction, ncmLink, parentObject,
					depthLevel);
			action.addAction(childAction);
		}

		return action;
	}

	private ILinkTriggerCondition createSimpleCondition(
			ISimpleCondition triggerExpression, IBind bind, ILink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		IFormatterEvent event;
		double delay;
		Object delayObject;
		ILinkTriggerCondition condition;

		event = createEvent(bind, ncmLink, parentObject, depthLevel);
		condition = new LinkTransitionTriggerCondition(event, triggerExpression
				.getTransition());

		delayObject = triggerExpression.getDelay();
		delay = compileDelay(ncmLink, delayObject, bind);
		if (delay > 0) {
			condition.setDelay(delay);
		}
		return condition;
	}

	private IFormatterEvent createEvent(IBind bind, ILink ncmLink,
			ICompositeExecutionObject parentObject, int depthLevel) {

		INodeNesting endPointNodeSequence, endPointPerspective;
		INode parentNode;
		IExecutionObject executionObject;
		IInterfacePoint interfacePoint;
		String key;
		IFormatterEvent event;

		endPointPerspective = parentObject.getNodePerspective();

		// parent object may be a refer
		parentNode = endPointPerspective.getAnchorNode();

		// teste para verificar se ponto terminal eh o proprio contexto ou
		// refer para o proprio contexto
		endPointNodeSequence = new NodeNesting(bind.getNodeNesting());
		if (endPointNodeSequence.getAnchorNode() != endPointPerspective
				.getAnchorNode()
				&& endPointNodeSequence.getAnchorNode() != parentNode.getDataEntity()) {
			endPointPerspective.append(endPointNodeSequence);
		}

		try {
			executionObject = compiler.getExecutionObject(endPointPerspective, bind
					.getDescriptor(), depthLevel);
		}
		catch (ObjectCreationForbiddenException exc) {
			return null;
		}

		interfacePoint = bind.getEndPointInterface();
		if (interfacePoint == null) {
			// TODO: This is an error, the formatter then return the main event
			return executionObject.getWholeContentPresentationEvent();
		}

		key = getBindKey(ncmLink, bind);
		event = compiler.getEvent(executionObject, interfacePoint, bind.getRole()
				.getEventType(), key);

		return event;
	}

	private double getDelayParameter(ILink ncmLink, IParameter connParam,
			IBind ncmBind) {
		IParameter parameter;

		parameter = null;
		if (ncmBind != null) {
			parameter = ncmBind.getParameter(connParam.getName());
		}
		if (parameter == null) {
			parameter = ncmLink.getParameter(connParam.getName());
		}
		if (parameter == null) {
			return 0.0;
		}
		else {
			try {
				return Double.parseDouble(parameter.getValue().toString().substring(0,
						parameter.getValue().toString().length() - 1)) * 1000;
			}
			catch (Exception exc) {
				return 0.0;
			}
		}
	}

	private String getBindKey(ILink ncmLink, IBind ncmBind) {
		IRole role;
		Object keyValue;
		IParameter param, auxParam;
		String key;

		role = ncmBind.getRole();
		if (role instanceof ISimpleCondition) {
			keyValue = ((ISimpleCondition)role).getKey();
		}
		else if (role instanceof IAttributeAssessment) {
			keyValue = ((IAttributeAssessment)role).getKey();
		}
		else {
			return null;
		}

		if (keyValue == null) {
			key = null;
		}
		else if (keyValue instanceof IParameter) {
			param = (IParameter)keyValue;
			auxParam = ncmBind.getParameter(param.getName());
			if (auxParam == null) {
				auxParam = ncmLink.getParameter(param.getName());
			}

			if (auxParam != null) {
				key = auxParam.getValue().toString();
			}
			else {
				key = null;
			}
		}
		else {
			key = keyValue.toString();
		}

		return key;
	}

	private double compileDelay(ILink ncmLink, Object delayObject, IBind bind) {
		double delay;
		IParameter param;

		if (delayObject == null) {
			delay = 0;
		}
		else if (delayObject instanceof IParameter) {
			param = (IParameter)delayObject;
			delay = getDelayParameter(ncmLink, param, bind);
		}
		else {
			delay = ((Double)delayObject).doubleValue();
		}
		return delay;
	}
}