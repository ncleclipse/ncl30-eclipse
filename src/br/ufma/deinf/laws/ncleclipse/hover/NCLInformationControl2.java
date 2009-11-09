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

import java.io.File;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


public class NCLInformationControl2 extends AbstractInformationControl implements
		IInformationControlExtension2 {
	public interface IHTMLHoverInfo {
		/**
		 * @@return true if the String returned by getHTMLString() represents a
		 *          URL; false if the String contains marked-up text.
		 */

		public boolean isURL();

		/**
		 * @@return The input string to be displayed in the Browser widget
		 *          (either as marked-up text, or as a URL.)
		 */
		public String getHTMLString();
	}

	static int pageNum = 1;
	private Shell internalShell;
	private Composite internalComposite;
	private Browser fBrowser;
	private StyledText text;
	boolean fisImage;
	private static Image image;
	private StackLayout layout;
	private Composite pageImage;
	private Composite page1;
	private Composite pageButton;
	private Composite pageRegion;
	private Composite pageText;
	private boolean TinyImage;
	private boolean fisMedia;
	private Button button;
	private File file;
	private Program p;
	private Point regionSize;
	private boolean fisRegion;
	private Color cb;
	private Color cf;
	private int widthImage;
	private int heightImage;
	private boolean fisHtml;

	/**
	 * @param parentShell
	 * @param isResizable
	 */
	public NCLInformationControl2(Shell parentShell, boolean isResizable) {
		super(parentShell, isResizable);
		create();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.AbstractInformationControl#createContent(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void createContent(Composite parent) {
		internalComposite = new Composite(parent, SWT.BORDER);
		internalComposite.setForeground(parent.getForeground());
		internalComposite.setBackground(parent.getBackground());
		internalComposite.setFont(JFaceResources.getDialogFont());

		// System.out.println("createcontent");

		layout = new StackLayout();
		this.internalComposite.setLayout(layout);

		pageImage = new Composite(this.internalComposite, SWT.NONE);
		pageImage.setLayout(new FillLayout());
		
		pageRegion = new Composite(this.internalComposite, SWT.NONE);
		pageRegion.setLayout(new FillLayout());
		
		pageText = new Composite(this.internalComposite, SWT.NONE);
		pageText.setLayout(new FillLayout());
		text = new StyledText (pageText, SWT.V_SCROLL | SWT.H_SCROLL);
		text.setForeground(parent.getForeground());
		text.setBackground(parent.getBackground());
		text.setFont(JFaceResources.getDialogFont());
		
		
		/*page1 = new Composite(this.internalComposite, SWT.NONE);
		page1.setLayout(new FillLayout());*/
		/*fBrowser = new Browser(page1, SWT.NONE);
		fBrowser.setForeground(internalComposite.getForeground());
		fBrowser.setBackground(internalComposite.getBackground());
		internalComposite.setFont(JFaceResources.getDialogFont());*/
		//fBrowser.pack();

		pageButton = new Composite(this.internalComposite, SWT.NONE);
		pageButton.setLayout(new FillLayout());
		Image image = new Image(pageButton.getDisplay(), this.getClass()
				.getProtectionDomain().getCodeSource().getLocation().toString()
				.substring(5)
				+ "icons" + File.separatorChar + "play.png");
		button = new Button(pageButton, SWT.PUSH);
		button.setImage(image);
		image.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IInformationControlExtension2#setInput(java.lang
	 * .Object)
	 */
	/*
	 * private class PaintR implements PaintListener{
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events
	 * .PaintEvent)
	 * 
	 * @Override public void paintControl(PaintEvent e) { Drawiamge(e);
	 * 
	 * }
	 * 
	 * } public void Drawiamge(PaintEvent e){
	 * 
	 * }
	 */
	@Override
	public void setInput(Object input) {

		// System.out.println("setinput");
		this.fisImage = false;
		this.TinyImage = false;
		this.fisMedia = false;
		this.fisRegion = false;
		this.fisHtml = false;
		// System.out.println(input.getClass());

		if (input instanceof ImageTest) {
			layout.topControl = pageImage;
			this.fisImage = true;

			ImageTest imaget = (ImageTest) input;
			final Image img = new Image(getShell().getDisplay(), imaget
					.toString());
			
			widthImage = img.getBounds().width;
			heightImage = img.getBounds().height;
			
			if (widthImage > 300 || heightImage > 300){
				double proporcao;
				if (heightImage > widthImage){
					proporcao = (double)widthImage/heightImage;
					heightImage = 300;
					widthImage = (int) Math.floor(heightImage * proporcao);
				}
				else{
					proporcao = (double)heightImage/widthImage;
					widthImage = 300;
					heightImage = (int) Math.floor(widthImage * proporcao);
				}
			}
			
			pageImage.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					for(int i=0;i<100;i++){
					e.gc.fillRectangle(0, 0, widthImage, heightImage);
					}
				}

			});
			pageImage.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {

					e.gc.drawImage(img, 0, 0, img.getBounds().width, img
							.getBounds().height, 0, 0, widthImage, heightImage);

				}

			});

		} else if (input instanceof PreHtml) {
			fisHtml = true;
			PreHtml html = (PreHtml) input;
			Composite page11 = new Composite(this.internalComposite, SWT.NONE);
			page11.setLayout(new FillLayout());
			Browser fBrowser1 = new Browser(page11, SWT.NONE);
			fBrowser1.setForeground(internalComposite.getForeground());
			fBrowser1.setBackground(internalComposite.getBackground());
			fBrowser1.setFont(JFaceResources.getDialogFont());
				
			fBrowser1.setUrl (html.getUrl());
			
			layout.topControl = page11;
		} else if (input instanceof RegionTest) {
			this.fisRegion = true;
			layout.topControl = pageRegion;
			RegionTest region = (RegionTest) input;
			this.cb = pageRegion.getBackground();
			this.cf = pageRegion.getForeground();
			pageRegion.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					for (int i = 0; i < 100; i++) {
						e.gc.setBackground(cb);
						e.gc.setForeground(cf);
						e.gc.setAlpha(100);
						e.gc.fillRectangle(0, 0, 300, 300);

					}
				}

			});
			regionSize = region.paintRegions(pageRegion);
		} else if (input instanceof MediaTest) {
			this.fisMedia = true;
			layout.topControl = pageButton;
			final MediaTest med = (MediaTest) input;

			this.button.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					PlatformUI.getWorkbench().getDisplay().syncExec(
							new Runnable() {
								public void run() {
									// Program.launch(string) ;
									p = Program.findProgram(med.getType());
									if (p != null) {
										try {
											p.execute(med.toString());
										} catch (Exception ev) {
											ev.printStackTrace();
										}
									}
								}
							});
					dispose();
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

		} else if (input instanceof String) {
			text.setText((String) input);
			
			layout.topControl = pageText;
			//fBrowser.setText((String) input);
			//super.setInformation((String) input);
			
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.IInformationControlExtension#hasContents()
	 */
	@Override
	public boolean hasContents() {
		return true;
	}

	/*
	 * public Rectangle computeTrim() { return Geometry.add(super.computeTrim(),
	 * internalComposite.computeTrim(0, 0, 0, 0)); }
	 */
	public void setVisible(boolean visible) {
		if (visible) {

			// Point currentSize= getShell().getSize();
			getShell().pack(true);
			Point newSize = computeSizeHint();
			// if (newSize.x > currentSize.x || newSize.y > currentSize.y)
			setSize(newSize.x, newSize.y);
			// restore previous size

		}

		super.setVisible(visible);
	}

	public Point computeSizeHint() {
		// see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=117602
		if (fisImage) {
			return new Point (widthImage, heightImage);
		}
		if (fisMedia) {
			return getShell().computeSize(80, 80);
		}
		if (fisRegion)
			return regionSize;
		
		if (fisHtml) return new Point (230, 180);

		return getShell().computeSize(300, 180, true);
	}

	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new IInformationControlCreator() {
			/*
			 * @seeorg.eclipse.jface.text.IInformationControlCreator#
			 * createInformationControl(org.eclipse.swt.widgets.Shell)
			 */
			public IInformationControl createInformationControl(Shell parent) {
				return new NCLInformationControl2(parent, true);
			}
		};
	}

	public void dispose() {
		// TODO Auto-generated method stub

		super.dispose();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events
	 * .PaintEvent)
	 */

}