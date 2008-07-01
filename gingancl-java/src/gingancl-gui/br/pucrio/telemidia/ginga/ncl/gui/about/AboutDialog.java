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
package br.pucrio.telemidia.ginga.ncl.gui.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * This is a simple "About" dialog. It will show info about the development of
 * the program.
 * <p>
 */
public class AboutDialog extends JDialog {

	/**
	 * This will be the label that will represent the TeleMídia Labs Link.
	 * <p>
	 */
	JLabel teleMidiaLink = new MyLabel(Messages.getString("TM_LABS")); //$NON-NLS-1$

	/**
	 * This will be the label that will represent the PUC-Rio Link.
	 * <p>
	 */
	JLabel pucLink = new MyLabel(Messages.getString("PUC_RIO")); //$NON-NLS-1$

	/**
	 * This will be the default title of the about dialog
	 * <p>
	 */
	private static final String DEFAULT_TITLE = Messages.getString("ABOUT_HP"); //$NON-NLS-1$

	/**
	 * Creates a non-modal dialog without a title with the specified Frame as its
	 * owner. If owner is null, a shared, hidden frame will be set as the owner of
	 * the dialog.
	 * <p>
	 * 
	 * @param owner
	 *          the <code>Frame</code> from which the dialog is displayed
	 * @throws HeadlessException
	 *           if GraphicsEnvironment.isHeadless() returns true.
	 */
	public AboutDialog(Frame owner) throws HeadlessException {

		super(owner);

		// make it modal and non-resizable
		setUndecorated(true);
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setTitle(DEFAULT_TITLE);

		// build it
		buildUI();

	} // end AboutDialog()

	/**
	 * Alternate (static) way to show an About dialog.
	 * <p>
	 * 
	 * @param owner
	 *          the <code>Frame</code> from which the dialog is displayed.
	 *          <p>
	 */
	public static void showDialog(Frame owner) {
		AboutDialog ad = new AboutDialog(owner);
		/*
		 * ad.setLocation( (owner.getWidth() - ad.getWidth()) / 2 + owner.getX(),
		 * (owner.getHeight() - ad.getHeight()) / 2 + owner.getY());
		 */
		ad.setLocation(0, 0);
		ad.setVisible(true);

	} // end showDialog()

	/**
	 * Assembles all components within the interface.
	 * <p>
	 */
	private void buildUI() {

		Container c = getContentPane();

		final int PANELS_QTY = 10;
		int i;

		JPanel[] panel = new JPanel[PANELS_QTY];

		JLabel icon = new MyLabel(new ImageIcon(Messages.getString("LOG_PATH"))); //$NON-NLS-1$
		JLabel hyperPropLabel = new MyLabel(Messages.getString("HP_CLIENT")); //$NON-NLS-1$
		JLabel versionLabel = new MyLabel(Messages.getString("VERSION")); //$NON-NLS-1$
		JLabel crLabel = new MyLabel(Messages.getString("COPYRIGHT")); //$NON-NLS-1$
		JLabel rightsLabel = new MyLabel(Messages.getString("ALL_RIGHTS_RESERVED")); //$NON-NLS-1$
		JLabel pucLabel = new MyLabel(Messages.getString("PUC_RIO")); //$NON-NLS-1$
		JLabel visitLabel1 = new MyLabel(Messages.getString("Visit") + " "); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel visitLabel2 = new MyLabel(Messages.getString("Visit") + " "); //$NON-NLS-1$ //$NON-NLS-2$
		JLabel siteLabel1 = new MyLabel(" " + Messages.getString("site") + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		JLabel siteLabel2 = new MyLabel(" " + Messages.getString("site") + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JButton closeButton = new JButton(Messages.getString("CLOSE")); //$NON-NLS-1$

		LinkMouseListener linkMouseListener = new LinkMouseListener();

		KeyActionCommand closeAction = new KeyActionCommand();

		KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

		// versionLabel
		for (i = 0; i < panel.length; i++) {
			panel[i] = new JPanel(new BorderLayout());
			panel[i].setBackground(Color.BLACK);
		} // end for

		// create a nice border for the icon
		panel[1].add(icon, BorderLayout.CENTER);

		panel[2].setLayout(new GridLayout(8, 1, -1, -1));
		panel[2].add(hyperPropLabel);
		// TODO change the version number
		panel[2].add(versionLabel);
		panel[2].add(crLabel);
		panel[2].add(rightsLabel);
		// TODO how will i mention PUC
		panel[2].add(pucLabel);

		panel[3].setLayout(new FlowLayout());
		panel[3].add(visitLabel1);
		panel[3].add(teleMidiaLink);
		teleMidiaLink.addMouseListener(linkMouseListener);
		teleMidiaLink.setForeground(Color.BLUE);
		panel[3].add(siteLabel1);
		panel[2].add(panel[3]);

		panel[4].setLayout(new FlowLayout());
		panel[4].add(visitLabel2);
		panel[4].add(pucLink);
		pucLink.addMouseListener(linkMouseListener);
		pucLink.setForeground(Color.BLUE);
		panel[4].add(siteLabel2);
		panel[2].add(panel[4]);
		panel[2].add(panel[5]);

		panel[5].setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel[5].add(closeButton);

		((BorderLayout)panel[0].getLayout()).setHgap(10);
		((BorderLayout)panel[0].getLayout()).setVgap(10);
		panel[0].add(panel[1], BorderLayout.WEST);
		panel[0].add(panel[2], BorderLayout.EAST);
		panel[0].add(panel[5], BorderLayout.SOUTH);
		panel[0].setBorder(BorderFactory.createEtchedBorder());
		c.setLayout(new BorderLayout());
		c.add(panel[0], BorderLayout.CENTER);
		panel[6].setLayout(new FlowLayout());
		panel[7].setLayout(new FlowLayout());
		panel[8].setLayout(new FlowLayout());
		panel[9].setLayout(new FlowLayout());
		c.add(panel[6], BorderLayout.SOUTH);
		c.add(panel[7], BorderLayout.EAST);
		c.add(panel[8], BorderLayout.WEST);
		c.add(panel[9], BorderLayout.NORTH);
		getRootPane().setBorder(
				BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));

		closeButton.setMnemonic('C');
		closeButton.addActionListener(closeAction);
		closeButton.getActionMap().put(enterKeyStroke.toString(), closeAction);
		closeButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				enterKeyStroke, enterKeyStroke.toString());
		closeButton.getActionMap().put(escKeyStroke.toString(), closeAction);
		closeButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				escKeyStroke, escKeyStroke.toString());

		pack();

	} // end buildUI()

	/**
	 * This will get the current instance of AboutDialog class.
	 * <p>
	 * 
	 * @return the current instance of AboutDialog class.
	 *         <p>
	 */
	public AboutDialog getThis() {
		return this;
	} // end getThis()

	/**
	 * This class will allow mouse and label change their appearance when the
	 * mouse enters and exits their area.
	 * <p>
	 * 
	 */
	private class LinkMouseListener extends MouseAdapter {

		/**
		 * This method will allow the mouse cursor changes to a hand when it enters
		 * the area of the associated label. So will the label change its font style
		 * to bold.
		 * <p>
		 * 
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {

			super.mouseEntered(e);

			MyLabel enteredLabel = (MyLabel)e.getSource();
			enteredLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			// enteredLabel.setFont(enteredLabel.getFont().deriveFont(Font.BOLD));
			enteredLabel.setUnderlined(true);

		} // end mouseEntered()

		/**
		 * This method will allow the mouse cursor changes to its default format
		 * when it exits the area of the associated label. So will the label change
		 * its font style to plain.
		 * <p>
		 * 
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {

			super.mouseExited(e);

			MyLabel exitedLabel = (MyLabel)e.getSource();
			exitedLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			exitedLabel.setUnderlined(false);

		} // end mouseExited()

		/**
		 * This method will try open a web page on the default web browser.
		 * <p>
		 * 
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			String url = ""; //$NON-NLS-1$
			super.mouseReleased(e);

			if (e.getSource() == pucLink)
				url = Messages.getString("PUC_SITE"); //$NON-NLS-1$
			else if (e.getSource() == teleMidiaLink)
				url = Messages.getString("TM_SITE"); //$NON-NLS-1$

			// try to open the URL
			try {
				BrowserLauncher.openURL(url);
			} // end try
			catch (IOException e1) {
				JOptionPane.showMessageDialog(getThis(), Messages
						.getString("ERROR_OPENING_BROWSER") + e1.getMessage() + ")", //$NON-NLS-1$ //$NON-NLS-2$
						Messages.getString("ERROR"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
			} // end catch

		} // end mouseReleased()

	} // end LinkMouseListener

	/**
	 * This class extends AbstractAction to implement the command associatedt with
	 * the close button.
	 * <p>
	 * 
	 */
	private class KeyActionCommand extends AbstractAction {

		/**
		 * This method will be called whenever the close button is hit (or
		 * implicitly called with ESC, ENTER, ALT+C or SPACE when it has the focus).
		 * <p>
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			dispose();
		} // end actionPerformed()

	} // end KeyActionCommand

} // end AboutDialog

/**
 * This class was created to change the default format of the JLabel's Font. The
 * default is bold. The desired is plain format.
 * <p>
 * 
 */
class MyLabel extends JLabel {

	private boolean underlined = false;

	/**
	 * Creates a <code>MyLabel</code> instance with no image and with an empty
	 * string for the title. The label is centered vertically in its display area.
	 * The label's contents, once set, will be displayed on the leading edge of
	 * the label's display area.
	 */
	public MyLabel() {
		super();
		commonSettings();
	} // end MyLabel()

	private void commonSettings() {
		setForeground(Color.WHITE);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(getFont().deriveFont(Font.BOLD));
	}

	/**
	 * Creates a <code>MyLabel</code> instance with the specified text. The
	 * label is aligned against the leading edge of its display area, and centered
	 * vertically.
	 * 
	 * @param label
	 *          The text to be displayed by the label.
	 */
	public MyLabel(String label) {
		super(label);
		commonSettings();
	} // end MyLabel()

	/**
	 * Creates a <code>MyLabel</code> instance with the specified image. The
	 * label is centered vertically and horizontally in its display area.
	 * 
	 * @param icon
	 *          The image to be displayed by the label.
	 */
	public MyLabel(ImageIcon icon) {
		super(icon);
		commonSettings();
	} // end MyLabel()

	public void setUnderlined(boolean underline) {

		if (getText() != null) {
			// if it is underlined and the user is not trying to underline it again
			if (underlined && !underline)
				setText(getText().substring(getText().indexOf("<u>") + 3,
						getText().indexOf("</u>")));

			// if the text is not underlined and the user is trying to underline it
			else if (!underlined && underline)
				setText("<html><u>" + getText() + "</u></html>");

			underlined = underline;
		} // end if

	} // end setUnderlined()

} // end MyLabel

/**
 * This class will check the strings on AboutDialog.properties and associate
 * them whenever getString is called.
 * <p>
 */
class Messages {

	/**
	 * The path of the AboutDialog properties file
	 */
	private static final String BUNDLE_NAME = "br.pucrio.telemidia.ginga.ncl.gui.about.AboutDialog"; //$NON-NLS-1$

	/**
	 * The String resource that will be instatiated for the current BUNDLE_NAME
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * Try to find <code>key</code> on the current resourceBundle. If it is not
	 * found, "!key!" is returned.
	 * <p>
	 * 
	 * @param key
	 *          The string to be searched on the resource bundle.
	 *          <p>
	 * @return the corresponding string to the key. If it is not found, "!key!" is
	 *         returned.
	 *         <p>
	 */
	public static String getString(String key) {

		try {
			return RESOURCE_BUNDLE.getString(key);
		} // end try
		catch (MissingResourceException e) {
			return '!' + key + '!';
		} // end catch

	} // end getString()

} // end Messages
