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

import java.net.URL;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Browser-based implementation of
 * {@link org.eclipse.jface.text.IInformationControl}.
 * <p>
 * Displays HTML in a {@link org.eclipse.swt.browser.Browser} widget.
 */
public class NCLHoverInformationControl extends DefaultInformationControl
		implements IInformationControlExtension2 {

	/**
	 * A wrapper used to deliver content to the hover control, either as
	 * marked-up text or as a URL.
	 */
	public interface IHTMLHoverInfo {
		/**
		 * @return true if the String returned by getHTMLString() represents a
		 *         URL; false if the String contains marked-up text.
		 */

		public boolean isURL();

		/**
		 * @return The input string to be displayed in the Browser widget
		 *         (either as marked-up text, or as a URL.)
		 */
		public String getHTMLString();
	}

	private Browser fBrowser;
	private Composite comp;
	private boolean fIsURL;
	private boolean isImage;
	private boolean isRegion;
	private boolean isMedia;
	private boolean isString;
	boolean fShowInDefaultInformationControl;
	private Canvas canvas;
	/**
	 * Creates a JavaHoverInformationControl with the given shell as parent.
	 * 
	 * @param parent
	 *            the parent shell
	 */
	public NCLHoverInformationControl(Shell parent) {
		super(parent, (String) null);
		getShell().setLayout(new FillLayout());
		create();
	}

	/*
	 * @see
	 * org.eclipse.jface.text.AbstractInformationControl#createContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected void createContent(Composite parent) {
			super.createContent(parent);
			
			try {
				
				//canvas = new Canvas(getShell(),SWT.NONE);
				//comp = new Composite(getShell(),SWT.NONE);
				//comp.setForeground(parent.getForeground());
				//comp.setBackground(parent.getBackground());
				//comp.setFont(JFaceResources.getDialogFont());
				/*fBrowser = new Browser(getShell(), SWT.NONE);
				fBrowser.setForeground(parent.getForeground());
				fBrowser.setBackground(parent.getBackground());
				fBrowser.setFont(JFaceResources.getDialogFont());*/
			} catch (SWTError e) {
				MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR
						| SWT.OK);
				messageBox.setMessage("Browser cannot be initialized."); //$NON-NLS-1$
				messageBox.setText("Error"); //$NON-NLS-1$
				messageBox.open();
			}
	}

	/*
	 * @see IInformationControl#setInformation(String)
	 */
	public void setInformation(String content) {
		
		if(isMedia){
			
		}else if(isImage){
			/*comp.setLayout(new FillLayout(SWT.NO));*/
			System.out.println("isimage");
			//System.out.println(getShell().getChildren());
			//getShell().setFullScreen(true);
			//getShell().setLayout(new FillLayout());
			//fBrowser.setBounds(getShell().getClientArea());
			final Image image = new Image(getShell().getDisplay(),"/home/thiago/Imagens/ken_1440x900.jpg");
			
		getShell().getParent().addPaintListener(new PaintListener(){

				@Override
				public void paintControl(PaintEvent arg0) {
					arg0.gc.drawImage(image,0,0,image.getBounds().width,image.getBounds().height,5,5,300,300);
					
					
				}
				
			});
			
			
		}else if(isRegion){
			
		}else if(fIsURL){
			
		}else if(isString){
			super.setInformation(content);
		}
		
	}

	/*
	 * @see IInformationControl#computeSizeHint()
	 */
	public Point computeSizeHint() {
		if(fShowInDefaultInformationControl) return super.computeSizeHint();
		else {
			
			return getShell().computeSize(1000, 1000,true);
		}
	}

	/*
	 * @see IInformationControlExtension#hasContents()
	 */
	public boolean hasContents() {
		if(fShowInDefaultInformationControl) return super.hasContents();
		return true;
	}

	/*
	 * @seeorg.eclipse.jface.text.IInformationControlExtension5#
	 * getInformationPresenterControlCreator()
	 * 
	 * @since 3.4
	 */
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if(fShowInDefaultInformationControl) return super.getInformationPresenterControlCreator();
		return new IInformationControlCreator() {
			/*
			 * @seeorg.eclipse.jface.text.IInformationControlCreator#
			 * createInformationControl(org.eclipse.swt.widgets.Shell)
			 */
			public IInformationControl createInformationControl(Shell parent) {
				return new NCLHoverInformationControl(parent);
			}
		};
	}

	/*
	 * @see org.eclipse.jface.text#setInput() The input object may be a String,
	 * an instance of IHTMLHoverInfo, or any object that returns a displayable
	 * String from its toString() implementation.
	 * 
	 * @since 3.4
	 */
	public void setInput(Object input) {
		// Assume that the input is marked-up text, not a URL
		fIsURL = false;
		isImage=false;
		isRegion=false;
		isMedia=false;
		isString=false;
		
		String inputString=null;

		if (input instanceof IHTMLHoverInfo) {
			// Get the input string, then see whether it's a URL
			IHTMLHoverInfo inputInfo = (IHTMLHoverInfo) input;
			inputString = inputInfo.getHTMLString();
			fIsURL = inputInfo.isURL();
		} else if (input instanceof String) {
			// Treat the String as marked-up text to be displayed.
			isString = true;
			inputString = (String) input;
		}else if(input instanceof ImageTest){
			System.out.println("isimage1");
			ImageTest test= (ImageTest)input;
			isImage=true;
			inputString = input.toString();
		}else if(input instanceof RegionTest) {
			System.out.println("isregion");
			RegionTest regontest= (RegionTest)input;
			inputString = input.toString();
		}else if(input instanceof MediaTest){
			System.out.println("ismedia");
			RegionTest regontest= (RegionTest)input;
			inputString = input.toString();
		}
		setInformation(inputString);
	}

}