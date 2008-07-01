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
package br.pucrio.telemidia.ginga.ncl.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.dvb.event.EventManager;

import br.org.ginga.ncl.IFormatter;
import br.org.ginga.ncl.IFormatterListener;
import br.org.ncl.INclDocument;
import br.org.ncl.INclDocumentManager;
import br.pucrio.telemidia.converter.ncl.NclDocumentManager;
import br.pucrio.telemidia.ginga.ncl.Formatter;
import br.pucrio.telemidia.ginga.ncl.gui.about.AboutDialog;
import br.pucrio.telemidia.ginga.ncl.gui.remoteControl.RemoteControl;

public class GingaPlayerWindow extends JFrame implements ActionListener,
		IFormatterListener {
	private static final long serialVersionUID = -8347600523961630631L;

	private JPanel frameContentPane = null;

	private JButton pauseJButton = null;

	private JButton stopJButton = null;

	private JButton startJButton = null;

	private IFormatter formatter;

	private JPanel buttonJPanel = null;

	private UserPreferencesDialog userPrefDialog;

	private boolean paused;

	private JFileChooser fileChooser;

	private String currentFile;

	private RemoteControl remoteControl;

	private String iconPath = "gingaNclGuiFiles/images/";

	private INclDocument currentDocument;

	private boolean standAloneApp;

	private WindowHandler windowHandler;

	public GingaPlayerWindow(IFormatter formatter, boolean standAlone) {
		super();

		this.formatter = formatter;
		this.formatter.addFormatterListener(this);
		standAloneApp = standAlone;
		currentFile = null;
		fileChooser = null;

		currentDocument = null;

		initialize();
	}

	private void showAboutDialog() {
		/*
		 * if (aboutDialog == null) aboutDialog = new AboutDialog(this);
		 * aboutDialog.setVisible(true);
		 */
		AboutDialog.showDialog(this);
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource() == getStartJButton()) {
			startPresentation();
		}
		else if (e.getSource() == getStopJButton()) {
			stopPresentation();
		}
		else if (e.getSource() == getPauseJButton()) {
			pausePressed();
		}
		else if (e.getActionCommand() == "Open NCL File...") {
			openNclFile();
		}
		else if (e.getActionCommand() == "Quit") {
			closeFormatterWindow();
		}

		else if (e.getActionCommand() == "Add region...") {
			addRegion();
		}
		else if (e.getActionCommand() == "Remove region...") {
			removeRegion();
		}
		else if (e.getActionCommand() == "Add regionBase...") {
			addRegionBase();
		}
		else if (e.getActionCommand() == "Remove regionBase...") {
			removeRegionBase();
		}

		else if (e.getActionCommand() == "Add rule...") {
			addRule();
		}
		else if (e.getActionCommand() == "Remove rule...") {
			removeRule();
		}
		else if (e.getActionCommand() == "Add ruleBase...") {
			addRuleBase();
		}
		else if (e.getActionCommand() == "Remove ruleBase...") {
			removeRuleBase();
		}

		else if (e.getActionCommand() == "Add transition...") {
			addTransition();
		}
		else if (e.getActionCommand() == "Remove transition...") {
			removeTransition();
		}
		else if (e.getActionCommand() == "Add transitionBase...") {
			addTransitionBase();
		}
		else if (e.getActionCommand() == "Remove transitionBase...") {
			removeTransitionBase();
		}

		else if (e.getActionCommand() == "Add connector...") {
			addConnector();
		}
		else if (e.getActionCommand() == "Remove connector...") {
			removeConnector();
		}
		else if (e.getActionCommand() == "Add connectorBase...") {
			addConnectorBase();
		}
		else if (e.getActionCommand() == "Remove connectorBase...") {
			removeConnectorBase();
		}

		else if (e.getActionCommand() == "Add descriptor...") {
			addDescriptor();
		}
		else if (e.getActionCommand() == "Remove descriptor...") {
			removeDescriptor();
		}
		else if (e.getActionCommand() == "Add descriptorBase...") {
			addDescriptorBase();
		}
		else if (e.getActionCommand() == "Remove descriptorBase...") {
			removeDescriptorBase();
		}

		else if (e.getActionCommand() == "Add importBase...") {
			addImportBase();
		}
		else if (e.getActionCommand() == "Remove importBase...") {
			removeImportBase();
		}

		else if (e.getActionCommand() == "Add importedDocumentBase...") {
			addImportedDocumentBase();
		}
		else if (e.getActionCommand() == "Remove importedDocumentBase...") {
			removeImportedDocumentBase();
		}

		else if (e.getActionCommand() == "Add importNCL...") {
			addImportNCL();
		}
		else if (e.getActionCommand() == "Remove importNCL...") {
			removeImportNCL();
		}

		else if (e.getActionCommand() == "Add node...") {
			addNode();
		}
		else if (e.getActionCommand() == "Remove node...") {
			removeNode();
		}
		else if (e.getActionCommand() == "Add interface...") {
			addInterface();
		}
		else if (e.getActionCommand() == "Remove interface...") {
			removeInterface();
		}
		else if (e.getActionCommand() == "Add link...") {
			addLink();
		}
		else if (e.getActionCommand() == "Remove link...") {
			removeLink();
		}
		else if (e.getActionCommand() == "Set property value...") {
			setPropertyValue();
		}

		else if (e.getActionCommand() == "User preferences...") {
			if (userPrefDialog == null)
				userPrefDialog = new UserPreferencesDialog(formatter);
			userPrefDialog.showDialog();
		}
		else if (e.getActionCommand() == "About Ginga-NCL player...") {
			showAboutDialog();
		}
	}

	private void closeFormatterWindow() {
		stopPresentation();
		formatter.close();
		EventManager.getInstance().close();
		setVisible(false);
		if (standAloneApp)
			System.exit(0);
	}

	private JPanel getButtonJPanel() {
		GridBagConstraints constraints;

		if (buttonJPanel == null) {
			buttonJPanel = new javax.swing.JPanel();
			buttonJPanel.setLayout(new java.awt.GridBagLayout());

			/*
			 * constraints = new java.awt.GridBagConstraints(); constraints.gridx = 0;
			 * constraints.gridy = 0; constraints.insets = new java.awt.Insets(4, 4,
			 * 4, 4); buttonJPanel.add(getRecompileJButton(), constraints);
			 */

			constraints = new java.awt.GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.insets = new java.awt.Insets(4, 4, 4, 4);
			buttonJPanel.add(getStartJButton(), constraints);

			constraints = new java.awt.GridBagConstraints();
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.insets = new java.awt.Insets(4, 4, 4, 4);
			buttonJPanel.add(getStopJButton(), constraints);

			constraints = new java.awt.GridBagConstraints();
			constraints.gridx = 2;
			constraints.gridy = 0;
			constraints.insets = new java.awt.Insets(4, 4, 4, 4);
			buttonJPanel.add(getPauseJButton(), constraints);
		}
		return buttonJPanel;
	}

	private JPanel getJFrameContentPane() {
		if (frameContentPane == null) {
			frameContentPane = new javax.swing.JPanel();
			frameContentPane.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsButtonJPanel = new java.awt.GridBagConstraints();
			constraintsButtonJPanel.gridx = 0;
			constraintsButtonJPanel.gridy = 1;
			constraintsButtonJPanel.gridwidth = 2;
			constraintsButtonJPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsButtonJPanel.weightx = 1.0;
			constraintsButtonJPanel.weighty = 1.0;
			constraintsButtonJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJFrameContentPane().add(getButtonJPanel(), constraintsButtonJPanel);
		}
		return frameContentPane;
	}

	private JButton getPauseJButton() {
		Icon btIcon;

		if (pauseJButton == null) {
			btIcon = new ImageIcon(iconPath + "pause.png");
			pauseJButton = new JButton(btIcon);
			pauseJButton.setToolTipText("pause presentation");
			pauseJButton.setName("PauseJButton");
			// pauseJButton.setText("Pause");
			// pauseJButton.setMinimumSize(new java.awt.Dimension(94, 25));
			// pauseJButton.setMaximumSize(new java.awt.Dimension(94, 25));
			// pauseJButton.setPreferredSize(new java.awt.Dimension(94, 25));
			pauseJButton.setEnabled(false);
		}
		return pauseJButton;
	}

	private JButton getStartJButton() {
		Icon btIcon;

		if (startJButton == null) {
			btIcon = new ImageIcon(iconPath + "play.png");
			startJButton = new JButton(btIcon);
			startJButton.setToolTipText("start presentation");
			startJButton.setName("StartJButton");
			// startJButton.setText("Start");
			// startJButton.setMinimumSize(new java.awt.Dimension(94, 25));
			// startJButton.setMaximumSize(new java.awt.Dimension(94, 25));
			// startJButton.setPreferredSize(new java.awt.Dimension(94, 25));
			startJButton.setEnabled(false);
		}
		return startJButton;
	}

	private JButton getStopJButton() {
		Icon btIcon;

		if (stopJButton == null) {
			btIcon = new ImageIcon(iconPath + "stop.png");
			stopJButton = new JButton(btIcon);
			stopJButton.setToolTipText("stop presentation");
			stopJButton.setName("StopJButton");
			// stopJButton.setText("Stop");
			// stopJButton.setMinimumSize(new java.awt.Dimension(94, 25));
			// stopJButton.setMaximumSize(new java.awt.Dimension(94, 25));
			// stopJButton.setPreferredSize(new java.awt.Dimension(94, 25));
			stopJButton.setEnabled(false);
		}
		return stopJButton;
	}

	private void initConnections() {
		windowHandler = new WindowHandler();
		addWindowListener(windowHandler);
		// getRecompileJButton().addActionListener(this);
		getStartJButton().addActionListener(this);
		getStopJButton().addActionListener(this);
		getPauseJButton().addActionListener(this);
	}

	private void createMenu() {
		JMenuBar mbar;
		JMenu menu;
		JMenuItem menu_item;

		mbar = new JMenuBar();
		menu = new JMenu("Document");
		menu_item = new JMenuItem("Open NCL File...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Quit");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		mbar.add(menu);

		menu = new JMenu("HeaderEntity");
		menu_item = new JMenuItem("Add region...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove region...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add regionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove regionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add rule...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove rule...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add ruleBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove ruleBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add transition...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove transition...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add transitionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove transitionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add connector...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove connector...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add connectorBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove connectorBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add descriptor...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove descriptor...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add descriptorBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove descriptorBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add transition...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove transition...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add transitionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove transitionBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add importBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove importBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu.addSeparator();
		menu_item = new JMenuItem("Add importNCL...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove importNCL...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add importedDocumentBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove importedDocumentBase...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		mbar.add(menu);

		menu = new JMenu("BodyEntity");
		menu_item = new JMenuItem("Add node...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove node...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add interface...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove interface...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Add link...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Remove link...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		menu_item = new JMenuItem("Set property value...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		mbar.add(menu);

		menu = new JMenu("Options");
		menu_item = new JMenuItem("User preferences...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		mbar.add(menu);

		menu = new JMenu("Help");
		menu_item = new JMenuItem("About Ginga-NCL player...");
		menu_item.addActionListener(this);
		menu.add(menu_item);
		mbar.add(menu);

		super.setJMenuBar(mbar);
	}

	private void initialize() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Ginga-NCL Emulator (by TeleMidia Labs) - Version 1.1.1");
		setContentPane(getJFrameContentPane());
		createMenu();
		initConnections();

		remoteControl = new RemoteControl();
		remoteControl.setVisible(true);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		/*setBounds((int)dim.getWidth() - (400 + remoteControl.getWidth()), (int)dim
				.getHeight() - 120 - 20, 400, 120);*/
		setBounds((int)dim.getWidth()-400, 0, 400, 120);
		remoteControl.setBounds(remoteControl.getX(), 120+20,
				remoteControl.getWidth(), remoteControl.getHeight());
	}

	/**
	 * 
	 * 
	 */
	private void openNclFile() {
		int intRet;
		// long time;
		if (fileChooser == null) {
			fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			fileChooser.setDialogTitle("Open NCL Document");
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}

		fileChooser.rescanCurrentDirectory();
		intRet = fileChooser.showOpenDialog(this);
		if (intRet == JFileChooser.APPROVE_OPTION) {
			if (currentDocument != null) {
				formatter.removeDocument(currentDocument.getId());
				getStartJButton().setEnabled(false);
			}
			currentFile = "file:" + fileChooser.getSelectedFile().toString();
			System.out.println(currentFile);
			// time = System.currentTimeMillis();
			formatter.reset();
			currentDocument = formatter.addDocument(currentFile);
			// time = System.currentTimeMillis() - time;

			// System.out.println("compile time: " + time + " ms");

			if (currentDocument != null) {
				getStartJButton().setEnabled(true);
			}
		}
	}

	public void pausePressed() {
		if (paused) {
			formatter.resumeDocument(currentDocument.getId());
			paused = false;
		}
		else {
			formatter.pauseDocument(currentDocument.getId());
			paused = true;
		}
	}

	public void showDialog() {
		setVisible(true);
	}

	public void startPresentation() {
		getStartJButton().setEnabled(false);
		formatter.startDocument(currentDocument.getId(), null);
		getStopJButton().setEnabled(true);
		getPauseJButton().setEnabled(true);
		paused = false;
	}

	public void stopPresentation() {
		EventManager.getInstance().removeAllUserEventListeners();
		if (currentDocument != null) {
			formatter.stopDocument(currentDocument.getId());
		}
		getPauseJButton().setEnabled(false);
		getStopJButton().setEnabled(false);
		getStartJButton().setEnabled(true);
	}

	public void presentationCompleted(String documentId) {
		EventManager.getInstance().removeAllUserEventListeners();
		getPauseJButton().setEnabled(false);
		getStopJButton().setEnabled(false);
		getStartJButton().setEnabled(true);
	}

	private String openNclCommandFile() {
		int intRet;

		if (currentDocument == null) {
			return null;
		}

		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Open NCL Document");
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}

		fileChooser.rescanCurrentDirectory();
		intRet = fileChooser.showOpenDialog(this);
		if (intRet == JFileChooser.APPROVE_OPTION) {
			return "file:" + fileChooser.getSelectedFile().toString();
		}
		return null;
	}

	private void addRegion() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			// TODO choose the parent region
			formatter.addRegion(currentDocument.getId(), null, location);
		}
	}

	private void removeRegion() {
		String id;

		id = JOptionPane.showInputDialog(this, "<region> id:", "Remove <region>",
				JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeRegion(currentDocument.getId(), id);
		}
	}

	private void addRegionBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addRegionBase(currentDocument.getId(), location);
		}
	}

	private void removeRegionBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<regionBase> id:",
				"Remove <regionBase>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeRegionBase(currentDocument.getId(), id);
		}
	}

	private void addRule() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addRule(currentDocument.getId(), location);
		}
	}

	private void removeRule() {
		String id;

		id = JOptionPane.showInputDialog(this, "<rule> or <compositeRule> id:",
				"Remove <rule> or <compositeRule>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeRule(currentDocument.getId(), id);
		}
	}

	private void addRuleBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addRuleBase(currentDocument.getId(), location);
		}
	}

	private void removeRuleBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<ruleBase> id:",
				"Remove <ruleBase>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeRuleBase(currentDocument.getId(), id);
		}
	}

	private void addConnector() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addConnector(currentDocument.getId(), location);
		}
	}

	private void removeConnector() {
		String id;

		id = JOptionPane.showInputDialog(this,
				"<causalConnector> or <constraintConnector> id:",
				"Remove <causalConnector> or <constraintConnector>",
				JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeConnector(currentDocument.getId(), id);
		}
	}

	private void addConnectorBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addConnectorBase(currentDocument.getId(), location);
		}
	}

	private void removeConnectorBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<connectorBase> id:",
				"Remove <connectorBase>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeConnectorBase(currentDocument.getId(), id);
		}
	}

	private void addTransition() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addTransition(currentDocument.getId(), location);
		}
	}

	private void removeTransition() {
		String id;

		id = JOptionPane.showInputDialog(this, "<transition> id:",
				"Remove <transition>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeTransition(currentDocument.getId(), id);
		}
	}

	private void addTransitionBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addTransitionBase(currentDocument.getId(), location);
		}
	}

	private void removeTransitionBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<transition> id:",
				"Remove <transition>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeTransitionBase(currentDocument.getId(), id);
		}
	}

	private void addDescriptor() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addDescriptor(currentDocument.getId(), location);
		}
	}

	private void removeDescriptor() {
		String id;

		id = JOptionPane.showInputDialog(this,
				"<descriptor> or <descriptorSwitch> id:",
				"Remove <descriptor> or <descriptorSwitch>",
				JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeDescriptor(currentDocument.getId(), id);
		}
	}

	private void addDescriptorBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addDescriptorBase(currentDocument.getId(), location);
		}
	}

	private void removeDescriptorBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<descriptorBase> id:",
				"Remove <descriptorBase>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeDescriptorBase(currentDocument.getId(), id);
		}
	}

	private void addImportBase() {
		String location;
		String id;

		location = openNclCommandFile();
		if (location != null) {
			id = JOptionPane.showInputDialog(this, "base id:", "Add <importBase>",
					JOptionPane.QUESTION_MESSAGE);
			if (id != null) {
				formatter.addImportBase(currentDocument.getId(), id, location);
			}
		}
	}

	private void removeImportBase() {
		String id;
		String location;

		id = JOptionPane.showInputDialog(this, "base id:", "Remove <importBase>",
				JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			location = openNclCommandFile();
			if (location != null) {
				formatter.removeImportBase(currentDocument.getId(), id, location);
			}
		}
	}

	private void addImportedDocumentBase() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addImportedDocumentBase(currentDocument.getId(), location);
		}
	}

	private void removeImportedDocumentBase() {
		String id;

		id = JOptionPane.showInputDialog(this, "<importedDocumentBase> id:",
				"Remove <importedDocumentBase>", JOptionPane.QUESTION_MESSAGE);
		if (id != null) {
			formatter.removeImportedDocumentBase(currentDocument.getId(), id);
		}
	}

	private void addImportNCL() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.addImportNCL(currentDocument.getId(), location);
		}
	}

	private void removeImportNCL() {
		String location;

		location = openNclCommandFile();
		if (location != null) {
			formatter.removeImportNCL(currentDocument.getId(), location);
		}
	}

	private void addNode() {
		String location;
		String id;

		location = openNclCommandFile();
		if (location != null) {
			id = JOptionPane.showInputDialog(this, "composition id:", "Add node",
					JOptionPane.QUESTION_MESSAGE);
			if (id != null) {
				formatter.addNode(currentDocument.getId(), id, location);
			}
		}
	}

	private void removeNode() {
		String compositionId, nodeId;

		compositionId = JOptionPane.showInputDialog(this, "composition id:",
				"Remove node", JOptionPane.QUESTION_MESSAGE);
		if (compositionId != null) {
			nodeId = JOptionPane.showInputDialog(this, "node id:", "Remove node",
					JOptionPane.QUESTION_MESSAGE);
			if (nodeId != null) {
				formatter.removeNode(currentDocument.getId(), compositionId, nodeId);
			}
		}
	}

	private void addInterface() {
		String location;
		String id;

		location = openNclCommandFile();
		if (location != null) {
			id = JOptionPane.showInputDialog(this, "node id:", "Add interface",
					JOptionPane.QUESTION_MESSAGE);
			if (id != null) {
				formatter.addInterface(currentDocument.getId(), id, location);
			}
		}
	}

	private void removeInterface() {
		String nodeId, interfaceId;

		nodeId = JOptionPane.showInputDialog(this, "node id:", "Remove interface",
				JOptionPane.QUESTION_MESSAGE);
		if (nodeId != null) {
			interfaceId = JOptionPane.showInputDialog(this, "interface id:",
					"Remove interface", JOptionPane.QUESTION_MESSAGE);
			if (interfaceId != null) {
				formatter.removeInterface(currentDocument.getId(), nodeId, interfaceId);
			}
		}
	}

	private void addLink() {
		String location;
		String id;

		location = openNclCommandFile();
		if (location != null) {
			id = JOptionPane.showInputDialog(this, "composition id:", "Add <link>",
					JOptionPane.QUESTION_MESSAGE);
			if (id != null) {
				formatter.addLink(currentDocument.getId(), id, location);
			}
		}
	}

	private void removeLink() {
		String compositionId, linkId;

		compositionId = JOptionPane.showInputDialog(this, "composition id:",
				"Remove <link>", JOptionPane.QUESTION_MESSAGE);
		if (compositionId != null) {
			linkId = JOptionPane.showInputDialog(this, "<link> id:", "Remove <link>",
					JOptionPane.QUESTION_MESSAGE);
			if (linkId != null) {
				formatter.removeLink(currentDocument.getId(), compositionId, linkId);
			}
		}
	}

	private void setPropertyValue() {
		String nodeId, attributeId, value;

		nodeId = JOptionPane.showInputDialog(this, "node id:",
				"Set Property Value", JOptionPane.QUESTION_MESSAGE);
		if (nodeId != null) {
			attributeId = JOptionPane.showInputDialog(this, "attribute id:",
					"Set Property Value", JOptionPane.QUESTION_MESSAGE);
			if (attributeId != null) {
				value = JOptionPane.showInputDialog(this, "value:",
						"Set Property Value", JOptionPane.QUESTION_MESSAGE);
				if (value != null) {
					formatter.setPropertyValue(currentDocument.getId(), nodeId,
							attributeId, value);
				}
			}
		}
	}

	public class WindowHandler extends WindowAdapter {
		public void windowClosing(WindowEvent evt) {
			closeFormatterWindow();
		}
	}
	
	public static void showJVMError() {
		JOptionPane.showMessageDialog(null,
				"The Java Virtual Machine version 1.4.2 or later must be installed " +
				"before continuing.\n " +
				"You can get this software at http://java.sun.com.\n " +
				"Ginga-NCL player will not work properly without it.", 
				"Runtime Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
	
	/**
	 * Tests if JMF is installed and available in the local machine.
   * @return true if JMF (Java Media Framework) was found in the current JVM 
   * runtime and false otherwise.
   */
  public static void checkJMF() {
  	try {
  		Class.forName("javax.media.Buffer");
  	} catch (ClassNotFoundException e) {
  		JOptionPane.showMessageDialog(null,
  				"The Java Media Framework (JMF) 2.1.1 must be installed before " +
  				"continuing.\n You can get this software at " +
  				"http://java.sun.com/jmf.\n " +
   				"Ginga-NCL Player will not work properly without it.", 
  				"Runtime Error",
  				JOptionPane.ERROR_MESSAGE);
  		System.exit(1);
  	}
  }

	/**
	 * Starts the application. First verifies the JVM version, if it is 1.4.2
	 * or newer.
	 * 
	 * @param args
	 *          an array of command-line arguments
	 */
	public static void main(java.lang.String[] args) {
		String versionString;
		for(int i = 0; i < args.length; i++)
			System.out.println(args[i]);
		
		versionString = System.getProperty("java.version");
		
		if (versionString.lastIndexOf('.') < 0) {
			showJVMError();
		}
		
		String[] params = versionString.split("\\.", -1);
		Integer	version = new Integer(params[0]);
		if (version.intValue() <= 1) {
			if (params.length < 2) {
				showJVMError();
			}
			Integer subVers = new Integer(params[1]);
			if (subVers.intValue() <= 4) {
				if (params.length < 3) {
					showJVMError();
				}
				try {
					Integer release = new Integer(params[2]);
					if (release.intValue() < 2) {
						showJVMError();
					}
				}
				catch (Exception exc) {
					String[] subParams = params[2].split("_", -1);
					Integer release = new Integer(subParams[0]);
					if (release.intValue() < 2) {
						showJVMError();
					}
				}
			}
		}
		
		checkJMF();
		
		INclDocumentManager documentManager = new NclDocumentManager("formatter");
		IFormatter formatter = new Formatter("formatter", documentManager);
		GingaPlayerWindow formatterWin = new GingaPlayerWindow(formatter, true);
		formatterWin.setVisible(true);
		String currentFile = "file:" + args[0];
		System.out.println(currentFile);
		formatter.reset();
		INclDocument currentDocument = formatter.addDocument(currentFile);
		formatter.startDocument(currentDocument.getId(), null);
		// time = System.currentTimeMillis() - time;
	}
}
