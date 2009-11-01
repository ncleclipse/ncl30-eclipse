
/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.hover;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Browser-based implementation of
 * {@@link org.eclipse.jface.text.IInformationControl}.
 * <p>
 * Displays HTML in a {@@link org.eclipse.swt.browser.Browser} widget.
 */
public class NCLHoverInformationControl extends AbstractInformationControl
		implements IInformationControlExtension2 {

	/**
	 * A wrapper used to deliver content to the hover control, either as
	 * marked-up text or as a URL.
	 */
	public interface IHTMLHoverInfo {
		/**
		 * @@return true if the String returned by getHTMLString() represents a
		 *         URL; false if the String contains marked-up text.
		 */

		public boolean isURL();

		/**
		 * @@return The input string to be displayed in the Browser widget
		 *         (either as marked-up text, or as a URL.)
		 */
		public String getHTMLString();
	}

	private Browser fBrowser;
	boolean fIsURL;
	boolean fShowInDefaultInformationControl;
	private Color fBackgroundColor;
	private Object result;
	private Composite composite;
	private Shell shell;
	private String image;
	private Image img;

	/**
	 * The width size constraint.
	 * 
	 * @@since 3.2
	 */
	private int fMaxWidth = SWT.DEFAULT;

	/**
	 * The height size constraint.
	 * 
	 * @@since 3.2
	 */
	private int fMaxHeight = SWT.DEFAULT;

	/**
	 * Creates a JavaHoverInformationControl with the given shell as parent.
	 * 
	 * @@param parent
	 *            the parent shell
	 * @@param b 
	 */
	public NCLHoverInformationControl(Shell parent) {
		
		super(parent, false);

		create();
	}

	/*
	 * @@see
	 * org.eclipse.jface.text.AbstractInformationControl#createContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected void createContent(Composite parent) { // 1
		// super.createContent(parent);
		try {
			
			composite = getShell();
			initializeColors();
			composite.setForeground(composite.getDisplay().getSystemColor(
					SWT.COLOR_INFO_FOREGROUND));
			composite.setBackground(fBackgroundColor);
			shell = (Shell) composite;

			fBrowser = new Browser(parent, SWT.BORDER);
			fBrowser.setBackground(fBackgroundColor);
			fBrowser.setForeground(composite.getDisplay().getSystemColor(
					SWT.COLOR_INFO_FOREGROUND));

		} catch (SWTError e) {
			e.printStackTrace();
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR
					| SWT.OK);
			messageBox.setMessage("Browser cannot be initialized."); //$NON-NLS-1$
			messageBox.setText("Error"); //$NON-NLS-1$
			messageBox.open();
		}

	}

	private void initializeColors() {
		fBackgroundColor = getShell().getDisplay().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND);
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			
				//Point currentSize= getShell().getSize();
				getShell().pack(true);
				Point newSize=computeSizeHint();
				//if (newSize.x > currentSize.x || newSize.y > currentSize.y)
					setSize(newSize.x, newSize.y); 
					// restore previous size
			
		}
		
		super.setVisible(visible);
	}
	/*
	 * @@see IInformationControl#setInformation(String)
	 */
	public void setInformation(String content) { // 3
		
		String tmp [] = content.split ("'");
		image = tmp[9];
		fBrowser.setBounds(getShell().getClientArea());

		if (fIsURL) {
			fBrowser.setUrl(content);
		} else {
			
			
			fBrowser.setText(content);
		}
	}

	/*
	 * @@see IInformationControl#computeSizeHint()
	 */
	
	public Point computeSizeHint() { // 4

		Image img = null;
		try {
			img = ImageIO.read((new File (image)));
			this.img=img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x = 20, y = 20;
		if (result instanceof RegionTest){
			x += 20;
			y += 20;
		}
		return new Point (img.getWidth(null)+20, img.getHeight(null)+20);

	}

	/*
	 * @@see IInformationControlExtension#hasContents()
	 */
	public boolean hasContents() {
		return fBrowser.getText().length() > 0;
	}

	/*
	 * @@seeorg.eclipse.jface.text.IInformationControlExtension5#
	 * getInformationPresenterControlCreator()
	 * 
	 * @@since 3.4
	 */
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new IInformationControlCreator() {
			/*
			 * @@seeorg.eclipse.jface.text.IInformationControlCreator#
			 * createInformationControl(org.eclipse.swt.widgets.Shell)
			 */
			public IInformationControl createInformationControl(Shell parent) {
				return new NCLHoverInformationControl(parent);
			}
		};
	}

	/*
	 * @@see org.eclipse.jface.text#setInput() The input object may be a String,
	 * an instance of IHTMLHoverInfo, or any object that returns a displayable
	 * String from its toString() implementation.
	 * 
	 * @@since 3.4
	 */
	public void setInput(Object input) { // 2
		// Assume that the input is marked-up text, not a URL
		fIsURL = false;
		final String inputString;
		result = input;
		if (input instanceof IHTMLHoverInfo) {
			IHTMLHoverInfo inputInfo = (IHTMLHoverInfo) input;
			inputString = inputInfo.getHTMLString();
			fIsURL = inputInfo.isURL();
		} else if (input instanceof String) {
			String tmp = (String) input;
			inputString = "<html><body bgcolor='fffacd' border='0' top ='0' style='font-size:12; font-face=Courier New;'>"
					+ tmp.replace("\n", "<br/>") + "</body>";
			
		} else
			inputString = input.toString();

		setInformation(inputString);

	}
	
	/* (non-Javadoc)
	 * @@see org.eclipse.jface.text.AbstractInformationControl#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		File tmp = new File (this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().toString().substring(5)
				+ "icons" + File.separatorChar + "tmp.png");
		if (tmp.isFile()) tmp.delete();
	}

}


