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

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonBar extends JPanel {
	private static final long serialVersionUID = 1724153738286453781L;

	private JButton ivjOkJButton = null;

	private JButton ivjCancelJButton = null;

	private JButton ivjHelpJButton = null;

	public ButtonBar() {
		super();
		initialize();
	}

	public ButtonBar(ActionListener listener) {
		super();
		initialize();
		setActionListener(listener);
	}

	public javax.swing.JButton getCancelJButton() {
		if (ivjCancelJButton == null) {
			ivjCancelJButton = new javax.swing.JButton();
			ivjCancelJButton.setName("CancelJButton");
			ivjCancelJButton.setText("Cancel");
		}
		return ivjCancelJButton;
	}

	public javax.swing.JButton getHelpJButton() {
		if (ivjHelpJButton == null) {
			ivjHelpJButton = new javax.swing.JButton();
			ivjHelpJButton.setName("HelpJButton");
			ivjHelpJButton.setText("Help");
			ivjHelpJButton.setContentAreaFilled(true);
		}
		return ivjHelpJButton;
	}

	public javax.swing.JButton getOkJButton() {
		if (ivjOkJButton == null) {
			ivjOkJButton = new javax.swing.JButton();
			ivjOkJButton.setName("OkJButton");
			ivjOkJButton.setText("Ok");
			ivjOkJButton.setContentAreaFilled(true);
		}
		return ivjOkJButton;
	}

	private void initialize() {
		setName("ButtonBar");
		setLayout(new java.awt.GridBagLayout());
		setSize(351, 36);

		java.awt.GridBagConstraints constraintsOkJButton = new java.awt.GridBagConstraints();
		constraintsOkJButton.gridx = 0;
		constraintsOkJButton.gridy = 0;
		constraintsOkJButton.weightx = 0.33;
		constraintsOkJButton.ipadx = 46;
		constraintsOkJButton.ipady = 4;
		constraintsOkJButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getOkJButton(), constraintsOkJButton);

		java.awt.GridBagConstraints constraintsCancelJButton = new java.awt.GridBagConstraints();
		constraintsCancelJButton.gridx = 1;
		constraintsCancelJButton.gridy = 0;
		constraintsCancelJButton.weightx = 0.33;
		constraintsCancelJButton.ipadx = 24;
		constraintsCancelJButton.ipady = 4;
		constraintsCancelJButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getCancelJButton(), constraintsCancelJButton);

		java.awt.GridBagConstraints constraintsHelpJButton = new java.awt.GridBagConstraints();
		constraintsHelpJButton.gridx = 2;
		constraintsHelpJButton.gridy = 0;
		constraintsHelpJButton.weightx = 0.33;
		constraintsHelpJButton.ipadx = 38;
		constraintsHelpJButton.ipady = 4;
		constraintsHelpJButton.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getHelpJButton(), constraintsHelpJButton);
	}

	public void setActionListener(ActionListener listener) {
		ivjOkJButton.addActionListener(listener);
		ivjCancelJButton.addActionListener(listener);
		ivjHelpJButton.addActionListener(listener);
	}
}
