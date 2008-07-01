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
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.IWindow;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ginga.ncl.model.presentation.IFormatterLayout;
import br.org.ginga.ncl.model.presentation.IFormatterRegion;
import br.org.ncl.animation.IAnimation;
import br.org.ncl.descriptor.DescriptorUtil;
import br.org.ncl.descriptor.IDescriptor;
import br.org.ncl.layout.ILayoutRegion;
import br.org.ncl.transition.ITransition;
import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.ncl.model.presentation.focus.FocusSourceManager;

public class FormatterRegion implements IFormatterRegion {
	private IFormatterLayout layoutManager;

	private ICascadingDescriptor descriptor;

	private String objectId;

	private IWindow outputDisplay;

	private JScrollPane scrollPane;

	private ILayoutRegion ncmRegion;

	private ILayoutRegion originalRegion;

	private ISurface renderedSurface;

	private short focusState;

	private Color focusBorderColor;

	private int borderWidth;

	private Component focusComponent;

	private Color selBorderColor;

	private Component selComponent;

	private Color bgColor;

	private float transparency;

	private short fit;

	private short scroll;

	//private KeyHandler keyHandler;

	private WindowHandler windowHandler;

	public FormatterRegion(String objectId, ICascadingDescriptor descriptor,
			IFormatterLayout layoutManager) {
		super();

		String colorStr;

		this.layoutManager = layoutManager;
		this.objectId = objectId;
		this.outputDisplay = null;
		this.scrollPane = null;
		this.descriptor = descriptor;
		originalRegion = descriptor.getRegion();
		ncmRegion = originalRegion.cloneRegion();

		this.focusState = IFormatterRegion.UNSELECTED;
		this.focusBorderColor = Color.WHITE;
		this.borderWidth = 0;
		this.focusComponent = null;
		this.selBorderColor = Color.RED;
		this.selComponent = null;

		// TODO: look for descriptor parameters overriding region attributes
		if (descriptor.getParameterValue("transparency") != null) {
			transparency = Float.parseFloat(descriptor.getParameterValue(
					"transparency").toString());
			if (transparency < 0.0 || transparency > 1.0) {
				transparency = 1.0f;
			}
		}
		else {
			transparency = 1.0f;
		}

		if (descriptor.getParameterValue("background") != null) {
			colorStr = descriptor.getParameterValue("background").toString();
			if (colorStr.equalsIgnoreCase("transparent")) {
				bgColor = new Color(0, 0, 0, 0);
			}
			else {
				bgColor = DescriptorUtil.getColor(colorStr, transparency);
			}
		}
		else {
			bgColor = new Color(0, 0, 0, 0);
		}

		fit = IDescriptor.FIT_FILL;
		if (descriptor.getParameterValue("fit") != null) {
			String fitStr = descriptor.getParameterValue("fit").toString();
			fit = DescriptorUtil.getFitCode(fitStr);
			if (fit < 0) {
				fit = IDescriptor.FIT_FILL;
			}
		}

		scroll = IDescriptor.SCROLL_NONE;
		if (descriptor.getParameterValue("scroll") != null) {
			String scrollStr = descriptor.getParameterValue("scroll").toString();
			scroll = DescriptorUtil.getScrollCode(scrollStr);
			if (scroll < 0) {
				scroll = IDescriptor.SCROLL_NONE;
			}
		}
	}

	private void meetComponent(int width, int height, int prefWidth,
			int prefHeight, ISurface component) {
		int finalH, finalW;

		if (prefWidth == 0 || prefHeight == 0) {
			return;
		}

		finalH = (prefHeight * width) / prefWidth;
		if (finalH <= height) {
			finalW = width;
		}
		else {
			finalH = height;
			finalW = (prefWidth * height) / prefHeight;
		}
		//component.
		((Component)component).setSize(finalW, finalH);
	}

	private void sliceComponent(int width, int height, int prefWidth,
			int prefHeight, ISurface component) {
		int finalH, finalW;

		if (prefWidth == 0 || prefHeight == 0) {
			return;
		}

		finalH = (prefHeight * width) / prefWidth;
		if (finalH > height) {
			finalW = width;
		}
		else {
			finalH = height;
			finalW = (prefWidth * height) / prefHeight;
		}
		((Component)component).setSize(finalW, finalH);
	}

	private void updateCurrentComponentSize(IAnimation animation) {
		int prefWidth, prefHeight, width, height;

		sizeRegion(animation);

		switch (fit) {
		case IDescriptor.FIT_HIDDEN:
			((Component)renderedSurface).setSize((int)((Component)renderedSurface).getPreferredSize()
					.getWidth(), (int)((Component)renderedSurface).getPreferredSize().getHeight());
			break;

		case IDescriptor.FIT_MEET:
			prefWidth = (int)((Component)renderedSurface).getPreferredSize().getWidth();
			prefHeight = (int)((Component)renderedSurface).getPreferredSize().getHeight();
			width = outputDisplay.getW();
			height = outputDisplay.getH();
			meetComponent(width, height, prefWidth, prefHeight, renderedSurface);
			break;

		case IDescriptor.FIT_MEETBEST:
			prefWidth = (int)((Component)renderedSurface).getPreferredSize().getWidth();
			prefHeight = (int)((Component)renderedSurface).getPreferredSize().getHeight();
			width = outputDisplay.getW();
			height = outputDisplay.getH();

			// the scale factor must not overtake 100% (2 times)
			if ((2 * prefWidth) >= width && (2 * prefHeight) >= height) {
				meetComponent(width, height, prefWidth, prefHeight, renderedSurface);
			}
			break;

		case IDescriptor.FIT_SLICE:
			prefWidth = (int)((Component)renderedSurface).getPreferredSize().getWidth();
			prefHeight = (int)((Component)renderedSurface).getPreferredSize().getHeight();
			width = outputDisplay.getW();
			height = outputDisplay.getH();
			sliceComponent(width, height, prefWidth, prefHeight, renderedSurface);
			break;

		case IDescriptor.FIT_FILL:
		default:
			((Component)renderedSurface).setSize(outputDisplay.getW(), 
					outputDisplay.getH());
			break;
		}
	}

	private void updateCurrentComponentLocation() {
		((Component)renderedSurface).setLocation(0, 0);
	}

	public void updateRegionBounds(IAnimation animation) {
		// sizeRegion();
		if (outputDisplay != null && renderedSurface != null) {
			updateCurrentComponentSize(animation);
			updateCurrentComponentLocation();

			// TODO improve the visual effect to avoid image blinks
			if (focusState == IFormatterRegion.UNSELECTED) {
				outputDisplay.validate();
			}
			else {
				setFocus(true);
				if (focusState == IFormatterRegion.SELECTED) {
					setSelection(true);
				}
			}

			((java.awt.Frame)outputDisplay).toFront();
			// ((javax.swing.JFrame)outputDisplay).toFront();
		}
	}

	private void sizeRegion(IAnimation animation) {
		int left, top, width, height;
		double dur = 0.0,by = 0.0;

		left = ncmRegion.getAbsoluteLeft();
		top = ncmRegion.getAbsoluteTop();
		width = ncmRegion.getWidthInPixels();
		height = ncmRegion.getHeightInPixels();
		
		if (animation != null) {
			if(animation.getDuration() != null)
				dur = Double.parseDouble(animation.getDuration().toString());
			else
				dur = 0;
			if(animation.getBy() != null)
				by = Double.parseDouble(animation.getBy().toString());
			else
				by = 0;
		}
		
		if (left < 0)
			left = 0;

		if (top < 0)
			top = 0;

		if (width <= 0)
			width = 1;

		if (height <= 0)
			height = 1;
		
		if (outputDisplay != null) {
			if(animation != null)
				outputDisplay.setBounds((int)left, (int)top, (int)width, (int)height, dur, by);
			else
				outputDisplay.setBounds((int)left, (int)top, (int)width, (int)height);
		}
	}

	public ILayoutRegion getLayoutRegion() {
		return ncmRegion;
	}

	public ILayoutRegion getOriginalRegion() {
		return originalRegion;
	}

	private Container getODContentPane() {
		return (Container)outputDisplay.getWidgetSurface();
		//return ((javax.swing.JFrame)outputDisplay).getContentPane();
		// return outputDisplay;
	}

	public void prepareOutputDisplay(ISurface surface) {
		//Container contentPane;

		if (outputDisplay == null) {
			outputDisplay = GFXManager.getInstance().createWindow(ncmRegion.getTitle());
			// outputDisplay = new java.awt.Frame();

			if (((javax.swing.JFrame)outputDisplay).getTitle() != null
					&& !((javax.swing.JFrame)outputDisplay).getTitle().equals("")) {
				((javax.swing.JFrame)outputDisplay).setTitle(ncmRegion.getTitle());
			}
			else {
				((javax.swing.JFrame)outputDisplay).setTitle(objectId);
			}
			int left = 0;
			int top = 0;
			int width = 0;
			int height = 0;
			if(ncmRegion!=null){
				left = ncmRegion.getAbsoluteLeft();
				top = ncmRegion.getAbsoluteTop();
				width = ncmRegion.getWidthInPixels();
				height = ncmRegion.getHeightInPixels();
			}
			if (left < 0)
				left = 0;

			if (top < 0)
				top = 0;

			if (width <= 0)
				width = 1;

			if (height <= 0)
				height = 1;
			outputDisplay.setBounds(left, top, width, height);
			
			if (bgColor != null) {
				outputDisplay.setBackgroundColor(
					    bgColor.getRed(),
					    bgColor.getGreen(),
					    bgColor.getBlue());
			}
			/*
			 * if (((java.awt.Frame)outputDisplay).getTitle() != null &&
			 * ((java.awt.Frame)outputDisplay).getTitle() != "") {
			 * ((java.awt.Frame)outputDisplay).setTitle(ncmRegion.getTitle()); } else {
			 * ((java.awt.Frame)outputDisplay).setTitle(objectId); }
			 */

			if (ncmRegion.isDecorated() == null
					|| ncmRegion.isDecorated().booleanValue() == false) {
				((javax.swing.JFrame)outputDisplay).setUndecorated(true);
				// ((java.awt.Frame)outputDisplay).setUndecorated(true);
			}

			//contentPane = getODContentPane();

			
			//outputDisplay.addKeyListener(keyHandler);

			windowHandler = new WindowHandler();
			((javax.swing.JFrame)outputDisplay).addWindowFocusListener(windowHandler);
			// ((java.awt.Frame)outputDisplay).addWindowFocusListener(windowHandler);

			/*if (scroll == IDescriptor.SCROLL_NONE) {
				contentPane.setLayout(null);
				contentPane.setBackground(bgColor);
			}
			else {
				int vertPolicy, horzPolicy;
				switch (scroll) {
				case IDescriptor.SCROLL_HORIZONTAL:
					vertPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
					horzPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
					break;

				case IDescriptor.SCROLL_VERTICAL:
					vertPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
					horzPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
					break;

				case IDescriptor.SCROLL_BOTH:
					vertPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
					horzPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
					break;

				case IDescriptor.SCROLL_AUTOMATIC:
					vertPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
					horzPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
					break;

				default:
					vertPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
					horzPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
					break;
				}

				scrollPane = new JScrollPane(vertPolicy, horzPolicy);
				scrollPane.getViewport().setBackground(bgColor);
				contentPane.add(scrollPane);
			}*/

			// sizeRegion();
		}

		if (surface != null) {
			renderedSurface = surface;
			//test
			updateCurrentComponentSize(null);
			updateCurrentComponentLocation();
		}
		renderSurface(surface);
	}
	
	private void renderSurface(ISurface surface){
		if (renderedSurface != null && outputDisplay != null) {
			if (renderedSurface.setParent(outputDisplay)) {
				outputDisplay.renderFrom(renderedSurface);
			}
		}
	}

	public void showContent() {
		String value;
		value = (String) ((ICascadingDescriptor)descriptor).getParameterValue(
			    "visible");
		if(value==null)
			value="true";
		
		if (value.equals("") || !value.toUpperCase().equals("FALSE")) {
			//List<Transition*>* transitions;
			List<ITransition> transitions;
			transitions = ((ICascadingDescriptor)descriptor)
				    .getInputTransitions();

			int transitionType;
			if(transitions!=null){
				for(ITransition transition : transitions){
					transitionType = transition.getType();
					if((transitionType == ITransition.TYPE_FADE ||
							    transitionType == ITransition.TYPE_BARWIPE) &&
							    outputDisplay != null) {
						outputDisplay.show(transitionType, transition.getSubtype(), transition.getDur(), 
								transition.getStartProgress(), transition.getEndProgress(), transition.getDirection(), transition.getFadeColor());
						return;
					}
				}
			}else{
				this.setRegionVisibility(true);
				//this.windowHandler.
				//windowGainedFocus();
			}
		}
		this.setRegionVisibility(true);
		//this.windowHandler.
		//windowGainedFocus();
			
		/*Container contentPane;

		contentPane = getODContentPane();
		if (currentComponent != null) {
			updateCurrentComponentSize();
			updateCurrentComponentLocation();
		}
		if (scrollPane != null) {
			scrollPane.setViewportView(((SwingSurface)currentComponent));
		}
		else {
			contentPane.add(((SwingSurface)currentComponent));
		}
		outputDisplay.validate();
		if (((SwingSurface)currentComponent).isVisible()) {
			//outputDisplay.setVisible(true);
			outputDisplay.show();
			((SwingSurface)currentComponent).validate();
			((SwingSurface)currentComponent).repaint();
		}*/
	}

	public void hideContent() {
		if (outputDisplay != null) {
			//outputDisplay.setVisible(false);
			outputDisplay.hide();
			disposeOutputDisplay();
		}

		/*
		 * Container contentPane;
		 * 
		 * if (outputDisplay != null) { outputDisplay.setVisible(false);
		 * 
		 * contentPane = getODContentPane(); contentPane.removeAll();
		 * contentPane.validate(); outputDisplay.validate();
		 * 
		 * dispose(); }
		 */
	}

	public void setRegionVisibility(boolean isVisible) {
		if(isVisible)
			outputDisplay.show();
		else
			outputDisplay.hide();
		//outputDisplay.setVisible(isVisible);
		//((Component)renderedSurface).setVisible(isVisible);
		if (isVisible) {
			//outputDisplay.repaint();
			outputDisplay.draw();
			//((Component)renderedSurface).repaint();
		}
	}

	public void disposeOutputDisplay() {
		if (outputDisplay != null) {
			//outputDisplay.removeKeyListener(keyHandler);
			((javax.swing.JFrame)outputDisplay)
					.removeWindowFocusListener(windowHandler);
			// ((java.awt.Frame)outputDisplay).removeWindowFocusListener(windowHandler);

			// dispose & removeAll sometimes crashes the presentation, thus the comment
			/*
			try {
				outputDisplay.removeAll();
				
				((javax.swing.JFrame)outputDisplay).dispose();
				// ((java.awt.Frame)outputDisplay).dispose();
			}
			catch (Exception exc) {
				System.out.println(exc);
			}
			*/

			//keyHandler = null;
			windowHandler = null;
			scrollPane = null;
			outputDisplay = null;
		}
	}

	public void toFront() {
		if (outputDisplay != null) {
			outputDisplay.raiseToTop();
			//((javax.swing.JFrame)outputDisplay).toFront();
			// ((java.awt.Frame)outputDisplay).toFront();
		}
	}

	public boolean isVisible() {
		if (outputDisplay != null) {
			return outputDisplay.isVisible();
		}
		else {
			return false;
		}
	}

	public short getFocusState() {
		return focusState;
	}

	public boolean setSelection(boolean selOn) {
		Container contentPane;
		MouseListener listeners[];
		int i;
		
		if ((selOn && focusState == IFormatterRegion.SELECTED) ||
				outputDisplay == null || !isVisible()) {
			return false;
		}

		contentPane = getODContentPane();
		if (selOn) {
			focusState = IFormatterRegion.SELECTED;

			contentPane.setBackground(selBorderColor);

			if (selComponent != null) {
				selComponent.setBounds(((Component)renderedSurface).getBounds());
				if (focusComponent != null) {
					contentPane.remove(focusComponent);
				}
				else {
					contentPane.remove(((Component)renderedSurface));
				}
				contentPane.add(selComponent);

				listeners = ((Component)renderedSurface).getMouseListeners();
				for (i = 0; i < listeners.length; i++) {
					selComponent.addMouseListener(listeners[i]);
				}

				contentPane.validate();
			}
		}
		else {
			unselect();
		}
		outputDisplay.validate();
		//outputDisplay.repaint();
		outputDisplay.draw();
		return selOn;
	}

	public void setFocus(boolean focusOn) {
		Container contentPane;
		MouseListener listeners[];
		int i;

		contentPane = getODContentPane();
		if (focusOn) {
			focusState = IFormatterRegion.FOCUSED;
			
			contentPane.setBackground(focusBorderColor);

			if (borderWidth > 0) {
				outputDisplay.setBounds(outputDisplay.getX() - borderWidth,
						outputDisplay.getY() - borderWidth, outputDisplay.getW()
								+ (2 * borderWidth), outputDisplay.getH()
								+ (2 * borderWidth));

				((Component)renderedSurface).setLocation(((Component)renderedSurface).getX() + borderWidth,
						((Component)renderedSurface).getY() + borderWidth);
			}
			else if (borderWidth < 0) {
				((Component)renderedSurface).setSize(renderedSurface.getW()
						+ (2 * borderWidth), renderedSurface.getH()
						+ (2 * borderWidth));

				((Component)renderedSurface).setLocation(((Component)renderedSurface).getX() - borderWidth,
						((Component)renderedSurface).getY() - borderWidth);
			}

			if (focusComponent != null) {
				focusComponent.setBounds(((Component)renderedSurface).getBounds());
				contentPane.remove(((Component)renderedSurface));

				contentPane.add(focusComponent,null);

				listeners = ((Component)renderedSurface).getMouseListeners();
				for (i = 0; i < listeners.length; i++) {
					focusComponent.addMouseListener(listeners[i]);
				}
				contentPane.validate();
				contentPane.repaint();
			}
		}
		else {
			unselect();
		}
		outputDisplay.validate();
		//outputDisplay.repaint();
		//((JFrame)outputDisplay).repaint();
		outputDisplay.draw();
	}

	private void unselect() {
		Container contentPane;

		contentPane = getODContentPane();
		focusState = IFormatterRegion.UNSELECTED;
		contentPane.setBackground(bgColor);

		if (borderWidth > 0) {
			outputDisplay.setBounds(outputDisplay.getX() + borderWidth, outputDisplay
					.getY()
					+ borderWidth, outputDisplay.getW() - (2 * borderWidth),
					outputDisplay.getH() - (2 * borderWidth));

			((Component)renderedSurface).setLocation(((Component)renderedSurface).getX() - borderWidth,
					((Component)renderedSurface).getY() - borderWidth);
		}
		else if (borderWidth < 0) {
			((Component)renderedSurface).setLocation(((Component)renderedSurface).getX() + borderWidth,
					((Component)renderedSurface).getY() + borderWidth);

			((Component)renderedSurface).setSize(((Component)renderedSurface).getWidth() - (2 * borderWidth),
					((Component)renderedSurface).getHeight() - (2 * borderWidth));
		}

		if (focusComponent != null) {
			focusComponent.setBounds(((Component)renderedSurface).getBounds());

			while (focusComponent.getMouseListeners().length > 0) {
				focusComponent
						.removeMouseListener(focusComponent.getMouseListeners()[0]);
			}

			contentPane.remove(focusComponent);
		}

		if (selComponent != null) {
			selComponent.setBounds(((Component)renderedSurface).getBounds());

			while (selComponent.getMouseListeners().length > 0) {
				selComponent.removeMouseListener(selComponent.getMouseListeners()[0]);
			}

			contentPane.remove(selComponent);
		}

		if (selComponent != null || focusComponent != null) {
			contentPane.add(((Component)renderedSurface));
			contentPane.validate();
		}
	}

	public void setFocusInfo(Color focusBorderColor, int borderWidth,
			String focusSrc, Color selBorderColor, String selSrc) {
		this.focusBorderColor = focusBorderColor;
		this.borderWidth = borderWidth;
		this.selBorderColor = selBorderColor;
		this.focusComponent = FocusSourceManager.getComponent(focusSrc);
		this.selComponent = FocusSourceManager.getComponent(selSrc);
	}

	public String getFocusIndex() {
		return descriptor.getFocusIndex();
	}

	public Color getBackgroundColor() {
		return bgColor;
	}
	
	public void changeCurrentComponent(ISurface newComponent) {
		if (((Component)newComponent) != null && renderedSurface != null &&
				outputDisplay != null) {
			((Component)newComponent).setBounds(((Component)renderedSurface).getBounds());
			if(outputDisplay instanceof Component){
				((Container)outputDisplay).remove(((Component)renderedSurface));
				((Container)outputDisplay).add(((Component)newComponent));
			}
			renderedSurface = newComponent;
			outputDisplay.validate();
		}
	}

	

	private class WindowHandler extends WindowAdapter {
		private synchronized void bringChildrenToFront(ILayoutRegion parentRegion) {
			Iterator<ILayoutRegion> regions;
			Iterator<IFormatterRegion> formRegions;
			ILayoutRegion layoutRegion;
			IFormatterRegion region;

			regions = parentRegion.getRegionsSortedByZIndex();
			while (regions.hasNext()) {
				layoutRegion = (ILayoutRegion)regions.next();
				bringChildrenToFront(layoutRegion);

				try {
					formRegions = layoutManager
							.getFormatterRegionsFromNcmRegion(layoutRegion.getId().toString());
					while (formRegions.hasNext()) {
						region = (IFormatterRegion)formRegions.next();
						region.toFront();
					}
				}
				catch (Exception exc) {
					System.err.println("FormatterRegion::bringChildrenToFront " + exc);
				}
			}
		}

		private void traverseFormatterRegions(ILayoutRegion region,
				Rectangle baseRect, ILayoutRegion baseRegion) {
			Iterator<IFormatterRegion> formRegions;
			IFormatterRegion formRegion;
			Rectangle regionRect;

			formRegions = layoutManager.getFormatterRegionsFromNcmRegion(region
					.getId().toString());
			if (formRegions.hasNext()) {
				while (formRegions.hasNext()) {
					formRegion = (IFormatterRegion)formRegions.next();
					regionRect = formRegion.getLayoutRegion().getRectangle();
					// region(2) is redrawn only if it overlaps the current region(1)
					if (baseRect.intersects(regionRect)) {
						// region overlaps this region
						formRegion.toFront();
					}
				}
			}
			else {
				bringHideWindowToFront(baseRect, baseRegion, region);
			}
		}

		private void bringHideWindowToFront(Rectangle baseRect,
				ILayoutRegion baseRegion, ILayoutRegion hideRegion) {

			Rectangle regionRect;
			Iterator<ILayoutRegion> regions;
			ILayoutRegion region;

			regionRect = hideRegion.getRectangle();
			if (baseRect.intersects(regionRect)) {
				regions = hideRegion.getRegions();
				while (regions.hasNext()) {
					region = (ILayoutRegion)regions.next();
					traverseFormatterRegions(region, baseRect, baseRegion);
				}
			}
		}

		private void bringSiblingToFront(IFormatterRegion region) {
			Rectangle regionRect;
			ILayoutRegion layoutRegion, parentRegion, baseRegion, siblingRegion;
			Iterator<ILayoutRegion> regions;

			// bring sibling regions with zIndex smaller to front
			regionRect = region.getLayoutRegion().getRectangle();
			layoutRegion = region.getOriginalRegion();
			parentRegion = layoutRegion.getParent();
			baseRegion = layoutRegion;
			while (parentRegion != null) {
				regions = parentRegion.getRegionsOverRegion(baseRegion);
				while (regions.hasNext()) {
					siblingRegion = (ILayoutRegion)regions.next();
					traverseFormatterRegions(siblingRegion, regionRect, layoutRegion);
				}
				baseRegion = parentRegion;
				parentRegion = parentRegion.getParent();
			}
		}

		public void windowGainedFocus(WindowEvent event) {
			bringChildrenToFront(ncmRegion);
			bringSiblingToFront(FormatterRegion.this);
		}
	}
}
