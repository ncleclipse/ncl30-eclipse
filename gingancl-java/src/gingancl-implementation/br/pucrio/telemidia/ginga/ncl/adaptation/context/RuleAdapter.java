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
package br.pucrio.telemidia.ginga.ncl.adaptation.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import br.org.ginga.ncl.model.components.ICompositeExecutionObject;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.switches.IExecutionObjectSwitch;
import br.org.ncl.components.INode;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.switches.ICompositeRule;
import br.org.ncl.switches.IDescriptorSwitch;
import br.org.ncl.switches.IRule;
import br.org.ncl.switches.ISimpleRule;
import br.org.ncl.switches.ISwitchNode;
import br.org.ncl.util.Comparator;

public class RuleAdapter implements Observer {
	/**
	 * 
	 */
	private Map ruleListenMap;

	/**
	 * 
	 */
	private Map entityListenMap;

	/**
	 * 
	 */
	public RuleAdapter() {
		PresentationContext.getInstance().addObserver(this);
		ruleListenMap = new HashMap();
		entityListenMap = new HashMap();
	}

	/**
	 * 
	 */
	public void reset() {
		ruleListenMap.clear();
		entityListenMap.clear();
	}

	/**
	 * Adapt all switches of execution objects and descriptors found within an 
	 * composite execution object.
	 * 
	 * @param compositeObject
	 *          group of nodes (execution objects) and links
	 * @param force
	 *          when force is false and an already evaluated switch is found no
	 *          adaptation is performed. When force is true, rules are always
	 *          evaluated independent on had already been evaluated.
	 */
	public void adapt(ICompositeExecutionObject compositeObject, boolean force) {
		Iterator objects;
		IExecutionObject object;

		objects = compositeObject.getExecutionObjects();
		while (objects.hasNext()) {
			object = (IExecutionObject)objects.next();
			if (object instanceof IExecutionObjectSwitch) {
				initializeRuleObjectRelation((IExecutionObjectSwitch)object);
				adapt((IExecutionObjectSwitch)object, force);
				object = ((IExecutionObjectSwitch)object).getSelectedObject();
			}
			adaptDescriptor(object);

			if (object instanceof ICompositeExecutionObject) {
				adapt((ICompositeExecutionObject)object, force);
			}
		}
	}

	/**
	 * @param topRule
	 * @param rule
	 */
	private void initializeAttributeRuleRelation(IRule topRule, IRule rule) {
		HashSet ruleSet;
		Iterator rules;

		if (rule instanceof ISimpleRule) {
			ruleSet = (HashSet)ruleListenMap.get(((ISimpleRule)rule).getAttribute());
			if (ruleSet == null) {
				ruleSet = new HashSet();
				ruleListenMap.put(((ISimpleRule)rule).getAttribute(), ruleSet);
			}
			ruleSet.add(topRule);
		}
		else {
			rules = ((ICompositeRule)rule).getRules();
			while (rules.hasNext()) {
				initializeAttributeRuleRelation(topRule, (IRule)rules.next());
			}
		}
	}

	/**
	 * Adapt a switch of execution objects. The method traverses all the switch
	 * presentation rules and select the node for the first rule evaluated as
	 * true. If no rule is satified, the default node (if present) is selected.
	 * 
	 * @param objectAlternatives
	 *          the switch of nodes that should be evaluated.
	 * @param force
	 *          when force is false and an already the switch has already been
	 *          evaluated, no adaptation is performed. When force is true, rules
	 *          are always evaluated independent on had already been evaluated
	 *          previously.
	 */
	public void adapt(IExecutionObjectSwitch objectAlternatives, boolean force) {
		/*
		 * int i, size; IRule rule; IExecutionObject object; boolean selected,
		 * result; Iterator events; IFormatterEvent event;
		 * 
		 * if (!force && objectAlternatives.getSelectedObject() != null) { return; } //
		 * if any event is running the node should no be adapted object =
		 * objectAlternatives.getSelectedObject(); if (object != null) { events =
		 * object.getEvents(); while (events.hasNext()) { event =
		 * (IFormatterEvent)events.next(); if (event.getCurrentState() ==
		 * IEvent.ST_OCCURRING) { return; } } }
		 * 
		 * selected = false; size = objectAlternatives.getNumRules(); for (i = 0; i <
		 * size && !selected; i++) { rule = objectAlternatives.getRule(i); result =
		 * evaluateRule(rule); if (result && !selected) { selected = true;
		 * objectAlternatives.select(objectAlternatives.getExecutionObject(i)); } }
		 * if (!selected) { objectAlternatives.selectDefault(); }
		 * 
		 * object = objectAlternatives.getSelectedObject(); if (object != null &&
		 * object instanceof IExecutionObjectSwitch) {
		 * adapt((IExecutionObjectSwitch)object, force); }
		 */
	}

	/**
	 * @param executionObject
	 */
	public boolean adaptDescriptor(IExecutionObject executionObject) {
		ICascadingDescriptor cascadingDescriptor;
		IGenericDescriptor currentDescriptor;
		IGenericDescriptor unsolvedDescriptor;
		IDescriptorSwitch descAlternatives;
		int i, size;
		IRule rule;
		boolean selected, result, adapted;

		cascadingDescriptor = executionObject.getDescriptor();
		if (cascadingDescriptor == null) {
			return false;
		}

		adapted = false;
		unsolvedDescriptor = cascadingDescriptor.getFirstUnsolvedDescriptor();
		while (unsolvedDescriptor != null) {
			if (unsolvedDescriptor instanceof IDescriptorSwitch) {
				descAlternatives = (IDescriptorSwitch)unsolvedDescriptor;
				currentDescriptor = descAlternatives.getSelectedDescriptor();
				selected = false;
				size = descAlternatives.getNumRules();
				for (i = 0; i < size; i++) {
					rule = descAlternatives.getRule(i);
					result = evaluateRule(rule);
					if (result && !selected) {
						selected = true;
						descAlternatives.select(descAlternatives.getDescriptor(i));
					}
					HashSet objectSet = (HashSet)entityListenMap.get(rule);
					if (objectSet == null) {
						objectSet = new HashSet();
						entityListenMap.put(rule, objectSet);
					}
					objectSet.add(descAlternatives);
				}
				if (!selected) {
					descAlternatives.selectDefault();
				}

				if (currentDescriptor !=
						descAlternatives.getSelectedDescriptor()) {

					adapted = true;
				}
			}
			cascadingDescriptor.cascadeUnsolvedDescriptor();
			unsolvedDescriptor = cascadingDescriptor.getFirstUnsolvedDescriptor();
		}
		// TODO: rever a logica de observacao das regras para nao ter problema de
		// concorrencia
		// TODO: ao cascatear pode ser necessário recalcular os tempos dos eventos
		return adapted;
	}

	/**
	 * @param switchNode
	 * @return
	 */
	public INode adaptSwitch(ISwitchNode switchNode) {
		int i, size;
		IRule rule;
		INode selectedNode;

		selectedNode = null;
		size = switchNode.getNumRules();
		for (i = 0; i < size; i++) {
			rule = switchNode.getRule(i);
			if (evaluateRule(rule)) {
				selectedNode = switchNode.getNode(i);
			}
		}
		if (selectedNode == null) {
			selectedNode = switchNode.getDefaultNode();
		}

		return selectedNode;
	}

	/**
	 * @param rule
	 * @return
	 */
	public boolean evaluateRule(IRule rule) {
		if (rule instanceof ISimpleRule) {
			return evaluateSimpleRule((ISimpleRule)rule);
		}
		else if (rule instanceof ICompositeRule) {
			return evaluateCompositeRule((ICompositeRule)rule);
		}
		else {
			return false;
		}
	}

	/**
	 * @param rule
	 * @return
	 */
	private boolean evaluateCompositeRule(ICompositeRule rule) {
		Iterator rules;
		IRule childRule;

		rules = rule.getRules();

		switch (rule.getOperator()) {
		case ICompositeRule.OP_OR:
			while (rules.hasNext()) {
				childRule = (IRule)rules.next();
				if (evaluateRule(childRule))
					return true;
			}
			return false;

		case ICompositeRule.OP_AND:
		default:
			while (rules.hasNext()) {
				childRule = (IRule)rules.next();
				if (!evaluateRule(childRule))
					return false;
			}
			return true;
		}
	}

	/**
	 * @param rule
	 * @return
	 */
	private boolean evaluateSimpleRule(ISimpleRule rule) {
		Object attribute;
		short operator;
		Comparable ruleValue;
		Comparable attributeValue;

		attribute = rule.getAttribute();
		attributeValue = PresentationContext.getInstance().getPropertyValue(
				attribute.toString());
		ruleValue = rule.getValue();

		if (attributeValue == null) {
			return false;
		}

		operator = rule.getOperator();
		switch (operator) {
		case Comparator.CMP_EQ:
			if (attributeValue == null && ruleValue == null) {
				return true;
			}
			else if (attributeValue == null) {
				return false;
			}
			else {
				return Comparator.evaluate(attributeValue, ruleValue, operator);
			}

		case Comparator.CMP_NE:
			if (attributeValue == null && ruleValue == null) {
				return false;
			}
			else if (attributeValue == null) {
				return true;
			}
			else {
				return Comparator.evaluate(attributeValue, ruleValue, operator);
			}

		default:
			return Comparator.evaluate(attributeValue, ruleValue, operator);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		HashSet ruleSet, objectSet;
		IRule rule;
		Iterator rules, objects;
		Object object;

		ruleSet = (HashSet)ruleListenMap.get(arg1);

		if (ruleSet == null) {
			return;
		}

		rules = ruleSet.iterator();
		while (rules.hasNext()) {
			rule = (IRule)rules.next();
			objectSet = (HashSet)entityListenMap.get(rule);
			if (objectSet != null) {
				objects = objectSet.iterator();
				while (objects.hasNext()) {
					object = objects.next();
					if (object instanceof IExecutionObjectSwitch) {
						adapt((IExecutionObjectSwitch)object, true);
					}
					else {
						// TODO precisa pensar melhor como adaptar descritores
						// dinamicamente.
					}
				}
			}
		}
	}

	/**
	 * @param objectAlternatives
	 */
	public void initializeRuleObjectRelation(
			IExecutionObjectSwitch objectAlternatives) {
		/*
		 * int i, size; IRule rule; HashSet objectSet; IExecutionObject object;
		 * 
		 * size = objectAlternatives.getNumRules(); for (i = 0; i < size; i++) {
		 * rule = objectAlternatives.getRule(i);
		 * initializeAttributeRuleRelation(rule, rule); // the switch will pertain
		 * to a set of objects that depend on this rule objectSet =
		 * (HashSet)entityListenMap.get(rule); if (objectSet == null) { objectSet =
		 * new HashSet(); entityListenMap.put(rule, objectSet); } if
		 * (!objectSet.contains(objectAlternatives)) {
		 * objectSet.add(objectAlternatives); }
		 * 
		 * object = objectAlternatives.getExecutionObject(i); if (object instanceof
		 * IExecutionObjectSwitch) {
		 * initializeRuleObjectRelation((IExecutionObjectSwitch)object); } }
		 */
	}
}