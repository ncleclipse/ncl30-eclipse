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
package br.pucrio.telemidia.ginga.ncl.model.presentation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterLayout;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.org.ncl.IParameter;
import br.org.ncl.descriptor.IDescriptor;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.org.ncl.layout.ILayoutRegion;
import br.org.ncl.navigation.IFocusDecoration;
import br.org.ncl.navigation.IKeyNavigation;
import br.org.ncl.switches.IDescriptorSwitch;
import br.org.ncl.transition.ITransition;

public class CascadingDescriptor implements ICascadingDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 679044573567065713L;

	private String id;

	private List<IGenericDescriptor> descriptors;

	private List<IGenericDescriptor> unsolvedDescriptors;

	private Double explicitDuration;

	private String playerName;

	private long repetitions;

	private Boolean freeze;

	private ILayoutRegion region;

	private String focusIndex, moveUp, moveDown, moveLeft, moveRight;

	private String focusSrc, selectionSrc;

	private Color focusBorderColor, selBorderColor;

	private Integer focusBorderWidth;

	private Float focusBorderTransparency;

	private Boolean grabFocus;

	private IFormatterRegion formatterRegion;

	private List<ITransition> inputTransitions, outputTransitions;

	private Map<String,Object> parameters;

	public CascadingDescriptor(IGenericDescriptor firstDescriptor) {
		id = null;
		explicitDuration = null;
		playerName = null;
		repetitions = 0;
		freeze = null;
		region = null;

		focusIndex = null;
		moveUp = null;
		moveDown = null;
		moveLeft = null;
		moveRight = null;

		focusSrc = null;
		selectionSrc = null;
		focusBorderColor = null;
		selBorderColor = null;
		focusBorderWidth = null;
		focusBorderTransparency = null;

		inputTransitions = null;
		outputTransitions = null;

		formatterRegion = null;
		parameters = new Hashtable<String, Object>();

		descriptors = new ArrayList<IGenericDescriptor>();
		unsolvedDescriptors = new ArrayList<IGenericDescriptor>();

		if (firstDescriptor != null) {
			cascade(firstDescriptor);
		}
	}

	public CascadingDescriptor(ICascadingDescriptor descriptor) {
		int i, size;

		id = null;
		explicitDuration = null;
		playerName = null;
		repetitions = 0;
		freeze = null;
		region = null;

		focusIndex = null;
		moveUp = null;
		moveDown = null;
		moveLeft = null;
		moveRight = null;

		focusSrc = null;
		selectionSrc = null;
		focusBorderColor = null;
		selBorderColor = null;
		focusBorderWidth = null;
		focusBorderTransparency = null;
		grabFocus = null;

		inputTransitions = null;
		outputTransitions = null;

		formatterRegion = null;
		parameters = new Hashtable<String, Object>();

		descriptors = new ArrayList<IGenericDescriptor>();
		unsolvedDescriptors = new ArrayList<IGenericDescriptor>();

		if (descriptor != null) {
			size = ((CascadingDescriptor)descriptor).descriptors.size();
			for (i = 0; i < size; i++) {
				cascade((IGenericDescriptor)((CascadingDescriptor)descriptor).descriptors
						.get(i));
			}
			size = ((CascadingDescriptor)descriptor).unsolvedDescriptors.size();
			for (i = 0; i < size; i++) {
				cascade((IGenericDescriptor)((CascadingDescriptor)descriptor).unsolvedDescriptors
						.get(i));
			}
		}
	}

	public String getId() {
		return id;
	}

	private void cascadeDescriptor(IDescriptor descriptor) {
		IKeyNavigation keyNavigation;
		IFocusDecoration focusDecoration;
		Iterator<ITransition> transitions;
		int i;

		if (descriptor.getPlayerName() != null)
			playerName = descriptor.getPlayerName();

		if (descriptor.getRegion() != null)
			region = descriptor.getRegion();

		if (descriptor.getExplicitDuration() != null)
			explicitDuration = descriptor.getExplicitDuration();

		if (descriptor.isFreeze() != null) {
			freeze = descriptor.isFreeze();
		}

		keyNavigation = descriptor.getKeyNavigation();
		if (keyNavigation.getFocusIndex() != null) {
			focusIndex = keyNavigation.getFocusIndex();
		}
		if (keyNavigation.getMoveUp() != null) {
			moveUp = keyNavigation.getMoveUp();
		}
		if (keyNavigation.getMoveDown() != null) {
			moveDown = keyNavigation.getMoveDown();
		}
		if (keyNavigation.getMoveLeft() != null) {
			moveLeft = keyNavigation.getMoveLeft();
		}
		if (keyNavigation.getMoveRight() != null) {
			moveRight = keyNavigation.getMoveRight();
		}

		focusDecoration = descriptor.getFocusDecoration();
		if (focusDecoration.getFocusBorderColor() != null) {
			focusBorderColor = focusDecoration.getFocusBorderColor();
		}
		if (focusDecoration.getSelBorderColor() != null) {
			selBorderColor = focusDecoration.getSelBorderColor();
		}
		if (focusDecoration.getFocusBorderWidth() != null) {
			focusBorderWidth = focusDecoration.getFocusBorderWidth();
		}
		if (focusDecoration.getFocusBorderTransparency() != null) {
			focusBorderTransparency = focusDecoration.getFocusBorderTransparency();
		}
		if (focusDecoration.getFocusSrc() != null) {
			focusSrc = focusDecoration.getFocusSrc();
		}
		if (focusDecoration.getGrabFocus() != null) {
			grabFocus = focusDecoration.getGrabFocus();
		}
		if (focusDecoration.getFocusSelSrc() != null) {
			selectionSrc = focusDecoration.getFocusSelSrc();
		}

		transitions = descriptor.getInputTransitions();
		if (transitions.hasNext()) {
			if (inputTransitions == null) {
				inputTransitions = new ArrayList<ITransition>();
			}
			for (i = 0; transitions.hasNext(); i++) {
				inputTransitions.add(i, transitions.next());
			}
		}

		transitions = descriptor.getOutputTransitions();
		if (transitions.hasNext()) {
			if (outputTransitions == null) {
				outputTransitions = new ArrayList<ITransition>();
			}
			for (i = 0; transitions.hasNext(); i++) {
				outputTransitions.add(i, transitions.next());
			}
		}

		Iterator<IParameter> params = descriptor.getParameters();
		while (params.hasNext()) {
			IParameter param = (IParameter)params.next();
			parameters.put(param.getName(), param.getValue());
		}
	}

	public boolean isLastDescriptor(IGenericDescriptor descriptor) {
		if (descriptors.size() > 0
				&& descriptor.getId() == ((IGenericDescriptor)descriptors
						.get(descriptors.size() - 1)).getId())
			return true;
		else
			return false;
	}

	public void cascade(IGenericDescriptor descriptor) {
		IGenericDescriptor preferredDescriptor;

		preferredDescriptor = (IGenericDescriptor)descriptor.getDataEntity();

		if (preferredDescriptor == null
				|| preferredDescriptor instanceof CascadingDescriptor)
			return;

		// verifica se o descritor a ser cascateado nao e' identico ao
		// ultimo descritor ja' cascateado
		if (isLastDescriptor(preferredDescriptor))
			return;

		descriptors.add(preferredDescriptor);
		if (id == null)
			id = (String) preferredDescriptor.getId();
		else
			id = id + "+" + preferredDescriptor.getId();
		if (preferredDescriptor instanceof IDescriptor
				&& unsolvedDescriptors.size() == 0) {
			cascadeDescriptor((IDescriptor)preferredDescriptor);
		}
		else {
			// verificar se o switch de descritor foi resolvido
			unsolvedDescriptors.add(preferredDescriptor);
		}
	}

	public IGenericDescriptor getFirstUnsolvedDescriptor() {
		if (unsolvedDescriptors.size() > 0)
			return (IGenericDescriptor)unsolvedDescriptors.get(0);
		else
			return null;
	}

	public Iterator<IGenericDescriptor> getUnsolvedDescriptors() {
		return unsolvedDescriptors.iterator();
	}

	public void cascadeUnsolvedDescriptor() {
		IGenericDescriptor genericDescriptor, descriptor;
		IDescriptorSwitch descAlternatives;
		IGenericDescriptor auxDescriptor;

		genericDescriptor = (IGenericDescriptor)unsolvedDescriptors.get(0);
		if (genericDescriptor instanceof IDescriptorSwitch) {
			descAlternatives = (IDescriptorSwitch)genericDescriptor;
			auxDescriptor = descAlternatives.getSelectedDescriptor();
			descriptor = (IGenericDescriptor)auxDescriptor.getDataEntity();
		}
		else {
			descriptor = (IDescriptor)genericDescriptor;
		}
		unsolvedDescriptors.remove(0);

		// verifica se o descritor a ser cascateado nao e' identico ao
		// ultimo descritor ja' cascateado
		if (isLastDescriptor(descriptor))
			return;

		cascadeDescriptor((IDescriptor)descriptor);
	}

	public Double getExplicitDuration() {
		return explicitDuration;
	}

	public Boolean getFreeze() {
		return freeze;
	}

	public String getPlayerName() {
		return playerName;
	}

	public ILayoutRegion getRegion() {
		return region;
	}

	public IFormatterRegion getFormatterRegion() {
		return formatterRegion;
	}

	public void setFormatterRegion(IFormatterLayout formatterLayout) {
		if (region != null) {
			formatterRegion = new FormatterRegion(id.toString(), this,
					formatterLayout);
		}
	}

	public long getRepetitions() {
		return repetitions;
	}

	public Iterator<Object> getParameters() {
		return parameters.values().iterator();
	}

	public Object getParameterValue(String paramName) {
		return parameters.get(paramName);
	}

	public List<IGenericDescriptor> getNcmDescriptors() {
		return this.descriptors;
	}

	public Color getFocusBorderColor() {
		return focusBorderColor;
	}

	public Float getFocusBorderTransparency() {
		return focusBorderTransparency;
	}

	public Integer getFocusBorderWidth() {
		return focusBorderWidth;
	}

	public String getFocusIndex() {
		return focusIndex;
	}

	public String getFocusSrc() {
		return focusSrc;
	}

	public String getSelectionSrc() {
		return selectionSrc;
	}

	public Boolean getGrabFocus() {
		return grabFocus;
	}

	public String getMoveDown() {
		return moveDown;
	}

	public String getMoveLeft() {
		return moveLeft;
	}

	public String getMoveRight() {
		return moveRight;
	}

	public String getMoveUp() {
		return moveUp;
	}

	public Color getSelBorderColor() {
		return selBorderColor;
	}

	public List<ITransition> getInputTransitions() {
		return inputTransitions;
	}

	public List<ITransition> getOutputTransitions() {
		return outputTransitions;
	}
}