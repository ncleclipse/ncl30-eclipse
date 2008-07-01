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
package br.pucrio.telemidia.ginga.ncl.focus;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.org.ginga.core.io.InputEvent;
import br.org.ginga.core.io.InputEventListener;
import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.adapters.IPlayerAdapterManager;
import br.org.ginga.ncl.focus.IFormatterFocusManager;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.ISelectionEvent;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.pucrio.telemidia.ginga.core.io.CodeMap;
import br.pucrio.telemidia.ginga.core.io.InputEventManager;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.PresentationContext;

public class FormatterFocusManager implements IFormatterFocusManager,
		/*UserEventListener,*/InputEventListener {
	private Map<String,IExecutionObject> focusTable;

	private List<String> focusSequence;

	private String currentFocus;

	private IExecutionObject selectedObject;

	//private UserEventRepository repository;

	private Color defaultFocusBorderColor;

	private int defaultFocusBorderWidth;

	private Color defaultSelBorderColor;

	private IPlayerAdapterManager playerManager;

	public FormatterFocusManager(IPlayerAdapterManager playerManager) {
		focusTable = new Hashtable<String, IExecutionObject>();
		focusSequence = new ArrayList<String>();
		currentFocus = null;
		selectedObject = null;

		defaultFocusBorderColor = Color.BLUE;
		defaultFocusBorderWidth = -3;
		defaultSelBorderColor = Color.GREEN;

		/*repository = new UserEventRepository("focusManager");
		repository.addAllArrowKeys();
		repository.addKey(KeyEvent.VK_ENTER);*/

		this.playerManager = playerManager;
	}

	public void setKeyMaster(String focusIndex) {
		IExecutionObject nextObject, currentObject;
		ICascadingDescriptor currentDescriptor = null;
		IFormatterPlayerAdapter player;

		nextObject = (IExecutionObject)focusTable.get(focusIndex);
		if (nextObject == null ||
				!nextObject.getDescriptor().getFormatterRegion().isVisible()) {

			return;
		}

		currentObject = (IExecutionObject)focusTable.get(currentFocus);
		if (currentObject != null) {
			currentDescriptor = currentObject.getDescriptor();
			if (currentObject != selectedObject) {
				currentDescriptor.getFormatterRegion().setFocus(false);
			}
		}

		currentFocus = focusIndex;
		PresentationContext.getInstance().setPropertyValue(
				"currentKeyMaster", currentFocus);

		if (currentDescriptor != null &&
				currentDescriptor.getFormatterRegion().setSelection(true)) {

			if (selectedObject != null) {
				selectedObject.getDescriptor().getFormatterRegion().setSelection(
						false);
			}

			selectedObject = currentObject;
			player = playerManager.getPlayer(currentObject);

			enterSelection(player);
			currentObject.select(
					ISelectionEvent.NO_CODE, player.getMediaTime());
		}
	}

	public void setFocus(String focusIndex) {
		IExecutionObject nextObject, currentObject;
		ICascadingDescriptor currentDescriptor;

		nextObject = (IExecutionObject)focusTable.get(focusIndex);
		if (nextObject == null ||
				!nextObject.getDescriptor().getFormatterRegion().isVisible()) {

			return;
		}

		currentObject = (IExecutionObject)focusTable.get(currentFocus);
		if (currentObject != null) {
			currentDescriptor = currentObject.getDescriptor();
			if (currentObject != selectedObject) {
				currentDescriptor.getFormatterRegion().setFocus(false);
			}
		}

		currentFocus = focusIndex;
		PresentationContext.getInstance().setPropertyValue(
				"currentFocus", currentFocus);

		if (nextObject != selectedObject) {
			setFocus(nextObject.getDescriptor());
		}
	}

	private void setFocus(ICascadingDescriptor descriptor) {
		Float borderAlfa;
		Color focusColor, selColor;
		Integer borderWidth;
		int width;
		IFormatterRegion region;

		focusColor = descriptor.getFocusBorderColor();
		if (focusColor == null) {
			focusColor = defaultFocusBorderColor;
		}

		borderAlfa = descriptor.getFocusBorderTransparency();
		if (borderAlfa != null) {
			focusColor = new Color(focusColor.getRed(), focusColor.getGreen(),
					focusColor.getBlue(), (int)(borderAlfa.floatValue() * 255));
		}

		borderWidth = descriptor.getFocusBorderWidth();
		if (borderWidth == null) {
			width = defaultFocusBorderWidth;
		}
		else {
			width = borderWidth.intValue();
		}

		selColor = descriptor.getSelBorderColor();
		if (selColor == null) {
			selColor = defaultSelBorderColor;
		}

		if (borderAlfa != null) {
			selColor = new Color(selColor.getRed(), selColor.getGreen(), selColor
					.getBlue(), (int)(borderAlfa.floatValue() * 255));
		}

		region = descriptor.getFormatterRegion();
		region.setFocusInfo(focusColor, width, descriptor.getFocusSrc(), selColor,
				descriptor.getSelectionSrc());
		region.setFocus(true);
	}

	public synchronized void showObject(IExecutionObject object) {
		ICascadingDescriptor descriptor;
		String focusIndex, auxIndex;
		int i, size;
		Object paramValue;
		IExecutionObject currentObject;
		IFormatterPlayerAdapter player;

		if (object == null || object.getDescriptor() == null) {
			return;
		}

		descriptor = object.getDescriptor();

		focusIndex = descriptor.getFocusIndex();
		if (focusIndex == null) {
			return;
		}
		
		focusTable.put(focusIndex, object);

		// put new index in alphabetical order
		size = focusSequence.size();
		for (i = 0; i < size; i++) {
			auxIndex = (String)focusSequence.get(i);
			if (focusIndex.compareTo(auxIndex) <= 0) {
				break;
			}
		}
		focusSequence.add(i, focusIndex);

		if (currentFocus == null) {
			//EventManager.getInstance().addUserEventListener(this, repository);
			Set<Integer> evs = new HashSet<Integer>();
			evs.add(InputEvent.CURSOR_DOWN_CODE);
			evs.add(InputEvent.CURSOR_LEFT_CODE);
			evs.add(InputEvent.CURSOR_RIGHT_CODE);
			evs.add(InputEvent.CURSOR_UP_CODE);
			
			evs.add(InputEvent.ENTER_CODE);
			evs.add(InputEvent.OK_CODE);
			
			evs.add(InputEvent.BACK_CODE);
			evs.add(InputEvent.EXIT_CODE);
			
			InputEventManager.getInstance().addInputEventListener(this, evs);
			currentFocus = focusIndex;

			paramValue = PresentationContext.getInstance().getPropertyValue(
					"currentKeyMaster");

			if (paramValue != null && (paramValue.toString() == currentFocus)) {
				setKeyMaster(currentFocus);

			} else {
				setFocus(descriptor);
			}
		}
		else {
			paramValue = PresentationContext.getInstance().getPropertyValue(
					"currentFocus");

			if (paramValue != null &&
					paramValue.toString().equalsIgnoreCase(focusIndex) &&
					descriptor.getFormatterRegion().isVisible()) {

				currentObject = (IExecutionObject)focusTable.get(currentFocus);
				if (currentObject != null) {
					currentObject.getDescriptor().getFormatterRegion().
						setFocus(false);
				}
				currentFocus = focusIndex;
				setFocus(descriptor);
			}

			paramValue = PresentationContext.getInstance().getPropertyValue(
					"currentKeyMaster");

			if (paramValue != null &&
					paramValue.toString().equalsIgnoreCase(focusIndex) &&
					descriptor.getFormatterRegion().isVisible()) {

				// first set as current focus
				currentObject = (IExecutionObject)focusTable.get(currentFocus);
				if (currentObject != null) {
					currentObject.getDescriptor().getFormatterRegion().setFocus(false);
				}
				currentFocus = focusIndex;
				PresentationContext.getInstance().setPropertyValue(
						"currentFocus", currentFocus);

				setFocus(descriptor);
				
				// then set as selected
				if (descriptor.getFormatterRegion().setSelection(true)) {
					// unselect the previous selected object, if exists
					if (selectedObject != null) {
						selectedObject.getDescriptor().getFormatterRegion().setSelection(
								false);
					}

					selectedObject = object;

					player = playerManager.getPlayer(selectedObject);
					enterSelection(player);
				}
			}
		}
	}

	public synchronized void hideObject(IExecutionObject object) {
		String focusIndex;
		IFormatterRegion region;
		IExecutionObject newFocusObject;
		IFormatterPlayerAdapter player;

		if (object == null || object.getDescriptor() == null
				|| object.getDescriptor().getFormatterRegion() == null) {
			return;
		}

		region = object.getDescriptor().getFormatterRegion();
		focusIndex = object.getDescriptor().getFocusIndex();
		if (focusIndex != null) {
			focusTable.remove(focusIndex);
			focusSequence.remove(focusIndex);

			if (region.getFocusState() == IFormatterRegion.SELECTED) {
				player = playerManager.getPlayer(selectedObject);
				exitSelection(player);
				region.setSelection(false);
				selectedObject = null;
			}

			if (currentFocus.equals(focusIndex)) {
				region.setFocus(false);

				if (focusSequence.isEmpty()) {
					currentFocus = null;
					/*EventManager.getInstance().removeUserEventListener(this);*/
					InputEventManager.getInstance()
						.removeInputEventListener(this);
				}
				else {
					currentFocus = (String)focusSequence.get(0);
					newFocusObject = (IExecutionObject)focusTable.get(currentFocus);
					setFocus(newFocusObject.getDescriptor());
				}
			}
		}
	}

	public void clear() {
		focusTable.clear();
		focusSequence.clear();
		currentFocus = null;
	}

	private void enterSelection(IFormatterPlayerAdapter player) {
		InputEventManager.getInstance().removeInputEventListener(this);

		Set<Integer> evs = new HashSet<Integer>();
		evs.add(InputEvent.ESCAPE_CODE);
		evs.add(InputEvent.EXIT_CODE);

		InputEventManager.getInstance().addInputEventListener(this, evs);
		player.setFocusHandler(true);
	}

	private void exitSelection(IFormatterPlayerAdapter player) {
		InputEventManager.getInstance().removeInputEventListener(this);
		player.setFocusHandler(false);

		Set<Integer> evs = new HashSet<Integer>();
		evs.add(InputEvent.CURSOR_DOWN_CODE);
		evs.add(InputEvent.CURSOR_LEFT_CODE);
		evs.add(InputEvent.CURSOR_RIGHT_CODE);
		evs.add(InputEvent.CURSOR_UP_CODE);

		evs.add(InputEvent.ENTER_CODE);
		evs.add(InputEvent.OK_CODE);
		InputEventManager.getInstance().addInputEventListener(this, evs);
	}

	public void setDefaultFocusBorderColor(Color color) {
		defaultFocusBorderColor = color;
	}

	public void setDefaultFocusBorderWidth(int width) {
		defaultFocusBorderWidth = width;
	}

	public void setDefaultSelBorderColor(Color color) {
		defaultSelBorderColor = color;
	}

/*	public void userEventReceived(UserEvent userEvent) {
		IExecutionObject currentObject;
		ICascadingDescriptor currentDescriptor;
		String nextIndex;
		IFormatterPlayerAdapter player;

		currentObject = (IExecutionObject)focusTable.get(currentFocus);
		if (currentObject == null) {
			return;
		}

		currentDescriptor = currentObject.getDescriptor();
		nextIndex = null;
		switch (userEvent.getCode()) {
		case KeyEvent.VK_UP:
			nextIndex = currentDescriptor.getMoveUp();
			break;
		case KeyEvent.VK_DOWN:
			nextIndex = currentDescriptor.getMoveDown();
			break;
		case KeyEvent.VK_LEFT:
			nextIndex = currentDescriptor.getMoveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			nextIndex = currentDescriptor.getMoveRight();
			break;

		case KeyEvent.VK_ENTER:
			if (currentDescriptor.getFormatterRegion().setSelection(true)) {
				if (selectedObject != null) {
					selectedObject.getDescriptor().getFormatterRegion().setSelection(
							false);
				}

				selectedObject = currentObject;

				if (currentDescriptor.getGrabFocus() != null
						&& currentDescriptor.getGrabFocus().booleanValue()) {
					enterSelection();
				}

				player = playerManager.getPlayer(currentObject);
				currentObject.select(ISelectionEvent.NO_CODE, player
						.getMediaTime());
			}
			return;

		case KeyEvent.VK_ESCAPE:
			currentDescriptor.getFormatterRegion().setSelection(false);
			currentDescriptor.getFormatterRegion().setFocus(true);
			exitSelection();
			selectedObject = null;
			return;

		default:
			break;
		}

		if (nextIndex != null) {
			setFocus(nextIndex);
		}
	}
*/
	public void userEventReceived(InputEvent keyEvent) {
		IExecutionObject currentObject;
		ICascadingDescriptor currentDescriptor;
		String nextIndex;
		IFormatterPlayerAdapter player;

		currentObject = (IExecutionObject)focusTable.get(currentFocus);
		if (currentObject == null) {
			return;
		}

		currentDescriptor = currentObject.getDescriptor();
		nextIndex = null;
		if(keyEvent instanceof KeyEvent)
		switch (((KeyEvent)keyEvent).getKeyCode()) {
			case InputEvent.CURSOR_UP_CODE:
				nextIndex = currentDescriptor.getMoveUp();
				break;
			case InputEvent.CURSOR_DOWN_CODE:
				nextIndex = currentDescriptor.getMoveDown();
				break;
			case InputEvent.CURSOR_LEFT_CODE:
				nextIndex = currentDescriptor.getMoveLeft();
				break;
			case InputEvent.CURSOR_RIGHT_CODE:
				nextIndex = currentDescriptor.getMoveRight();
				break;
	
			case InputEvent.ENTER_CODE:
				if (currentDescriptor.getFormatterRegion().setSelection(true)) {
					if (selectedObject != null) {
						selectedObject.getDescriptor().getFormatterRegion().setSelection(
								false);
					}
	
					selectedObject = currentObject;
					player = playerManager.getPlayer(currentObject);

					enterSelection(player);
					currentObject.select(
							ISelectionEvent.NO_CODE, player.getMediaTime());
				}
				return;
	
			case InputEvent.ESCAPE_CODE:
				currentDescriptor.getFormatterRegion().setSelection(false);
				currentDescriptor.getFormatterRegion().setFocus(true);
				player = playerManager.getPlayer(currentObject);
				exitSelection(player);
				selectedObject = null;
				return;
	
			default:
				break;
		}

		if (nextIndex != null) {
			setFocus(nextIndex);
		}
	}
}
