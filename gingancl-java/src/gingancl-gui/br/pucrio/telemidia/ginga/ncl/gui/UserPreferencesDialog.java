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
package br.pucrio.telemidia.ginga.ncl.gui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.org.ginga.ncl.IFormatter;
import br.pucrio.telemidia.ginga.ncl.adaptation.context.PresentationContext;

public class UserPreferencesDialog extends JDialog implements ActionListener,
		FocusListener, ItemListener {
	private JComboBox attrNameCombo = null;

	private JTextField attrValueJTField = null;

	private ButtonBar ivjButtonBar = null;

	private JPanel ivjContentPane = null;

	private DefaultComboBoxModel attrList;

	public UserPreferencesDialog(IFormatter formatter) {
		super();
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getButtonBar().getOkJButton()) {
			configurePreferences();
			setVisible(false);
		}

		else if (e.getSource() == getButtonBar().getCancelJButton()) {
			setVisible(false);
			resetAttributeCombo();
		}

		else if (e.getSource() == getButtonBar().getHelpJButton())
			System.out.println("Help");
	}

	public void configurePreferences() {
		int i, size;
		ContextAttribute attribute;

		size = attrList.getSize();
		for (i = 0; i < size; i++) {
			attribute = (ContextAttribute)attrList.getElementAt(i);
			PresentationContext.getInstance().setPropertyValue(attribute.name,
					(String)attribute.value);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		ContextAttribute attribute;

		attribute = (ContextAttribute)attrNameCombo.getSelectedItem();
		attribute.value = attrValueJTField.getText();
	}

	private ButtonBar getButtonBar() {
		if (ivjButtonBar == null) {
			ivjButtonBar = new ButtonBar();
		}
		return ivjButtonBar;
	}

	private void initConnections() {
		getButtonBar().setActionListener(this);
	}

	private void resetAttributeCombo() {
		Iterator attrNames;
		String attrName;
		ContextAttribute attribute;

		attrList.removeAllElements();
		attrNames = PresentationContext.getInstance().getPropertyNames();
		while (attrNames.hasNext()) {
			attrName = (String)attrNames.next();
			attribute = new ContextAttribute(attrName, PresentationContext
					.getInstance().getPropertyValue(attrName));
			attrList.addElement(attribute);
		}
		attrNameCombo.setModel(attrList);
	}

	private void initialize() {
		GridBagConstraints constraints;

		setDefaultCloseOperation(2);
		setBounds(new java.awt.Rectangle(0, 0, 311, 213));
		setSize(419, 213);
		setModal(false);
		setTitle("User Preference Options");

		ivjContentPane = new javax.swing.JPanel();
		ivjContentPane.setLayout(new java.awt.GridBagLayout());

		attrNameCombo = new JComboBox();
		attrList = new DefaultComboBoxModel();
		resetAttributeCombo();
		constraints = new java.awt.GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new java.awt.Insets(4, 4, 4, 4);
		ivjContentPane.add(attrNameCombo, constraints);
		attrNameCombo.addItemListener(this);

		attrValueJTField = new JTextField();
		constraints = new java.awt.GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraints.anchor = java.awt.GridBagConstraints.WEST;
		constraints.insets = new java.awt.Insets(4, 4, 4, 4);
		ivjContentPane.add(attrValueJTField, constraints);
		attrValueJTField.addFocusListener(this);

		constraints = new java.awt.GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = java.awt.GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = new java.awt.Insets(4, 4, 4, 4);
		ivjContentPane.add(getButtonBar(), constraints);

		setContentPane(ivjContentPane);

		initConnections();
	}

	public void itemStateChanged(ItemEvent e) {
		ContextAttribute attribute;

		if (e.getStateChange() == ItemEvent.SELECTED) {
			attribute = (ContextAttribute)e.getItem();
			attrValueJTField.setText(attribute.value.toString());
		}
	}

	public void showDialog() {
		ContextAttribute attribute;

		attribute = (ContextAttribute)attrNameCombo.getSelectedItem();
		attrValueJTField.setText(attribute.value.toString());
		setVisible(true);
	}

	private class ContextAttribute {
		private String name;

		private Comparable value;

		public ContextAttribute(String name, Comparable value) {
			this.name = name;
			this.value = value;
		}

		public String toString() {
			return name;
		}
	}
}
