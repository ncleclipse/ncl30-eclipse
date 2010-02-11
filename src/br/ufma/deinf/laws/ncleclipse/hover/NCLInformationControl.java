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

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.DefaultInformationControl.IInformationPresenter;
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

public class NCLInformationControl extends AbstractInformationControl
		implements IInformationControlExtension2 {
	
	private Composite internalComposite;
	private StyledText text;
	private boolean isImage;
	private StackLayout layout;
	private Composite pageImage;
	private Composite pageButton;
	private Composite pageRegion;
	private Composite pageText;
	private Composite pageXml;
	private boolean isMedia;
	private Button button;
	private Program p;
	private Point regionSize;
	private boolean isRegion;
	private Color cb;
	private Color cf;
	private int widthImage;
	private int heightImage;
	private boolean isHtml;
	private final int fAdditionalTextStyles;
	private Object input;
	private Browser fBrowser;

	public NCLInformationControl(Shell parentShell, boolean isResizable) {
		super(parentShell, isResizable);
		fAdditionalTextStyles = isResizable ? SWT.V_SCROLL | SWT.H_SCROLL
				: SWT.NONE;
		create();

	}
	
	public NCLInformationControl (Shell parent, String statusFieldText,
			IInformationPresenter presenter) {
		super(parent, statusFieldText);
		fAdditionalTextStyles = SWT.NONE;
		create();
	}
	
	public NCLInformationControl(Shell parent,
			ToolBarManager toolBarManager, IInformationPresenter presenter) {
		super(parent, toolBarManager);
		fAdditionalTextStyles = SWT.V_SCROLL | SWT.H_SCROLL;
		create();
	}
	
	
	public NCLInformationControl (Shell parent, int textStyles,
			IInformationPresenter presenter, String statusFieldText) {
		super(parent, statusFieldText);
		fAdditionalTextStyles = textStyles;
		create();
	}
	
	@Override
	protected void createContent(Composite parent) {
		internalComposite = new Composite(parent, SWT.BORDER_DASH);
		
		
		internalComposite.setForeground(parent.getForeground());
		internalComposite.setBackground(parent.getBackground());
		internalComposite.setFont(JFaceResources.getDialogFont());


		layout = new StackLayout();
		this.internalComposite.setLayout(layout);

		pageImage = new Composite(this.internalComposite, SWT.NONE);
		pageImage.setLayout(new FillLayout());

		pageRegion = new Composite(this.internalComposite, SWT.NONE);
		pageRegion.setLayout(new FillLayout());

		pageText = new Composite(this.internalComposite, SWT.NONE);
		pageText.setLayout(new FillLayout());
		
		text = new StyledText(pageText, SWT.READ_ONLY | fAdditionalTextStyles);
		text.setForeground(parent.getForeground());
		text.setBackground(parent.getBackground());
		text.setFont(JFaceResources.getDialogFont());

		

		pageButton = new Composite(this.internalComposite, SWT.NONE);
		pageButton.setLayout(new FillLayout());
		Image image = new Image(pageButton.getDisplay(), this.getClass()
				.getProtectionDomain().getCodeSource().getLocation().toString()
				.substring(5)
				+ "icons" + File.separatorChar + "play.png");
		button = new Button(pageButton, SWT.PUSH);
		button.setImage(image);
		//image.dispose();
		
		pageXml = new Composite(this.internalComposite, SWT.NONE);
		pageXml.setLayout(new FillLayout());
		fBrowser = new Browser(pageXml, SWT.NONE);
		fBrowser.setForeground(internalComposite.getForeground());
		fBrowser.setBackground(internalComposite.getBackground());
		fBrowser.setFont(JFaceResources.getDialogFont());
		Browser.clearSessions();

	
	}

	@Override
	public void setInput(Object input) {
		
		this.input = input;
		this.isImage = false;
		this.isMedia = false;
		this.isRegion = false;
		this.isHtml = false;

		if (input instanceof PreViewImage) {
			layout.topControl = pageImage;
			this.isImage = true;

			PreViewImage imaget = (PreViewImage) input;
			final Image img = new Image(getShell().getDisplay(), imaget
					.toString());

			widthImage = img.getBounds().width;
			heightImage = img.getBounds().height;

			if (widthImage > 300 || heightImage > 300) {
				double proporcao;
				if (heightImage > widthImage) {
					proporcao = (double) widthImage / heightImage;
					heightImage = 300;
					widthImage = (int) Math.floor(heightImage * proporcao);
				} else {
					proporcao = (double) heightImage / widthImage;
					widthImage = 300;
					heightImage = (int) Math.floor(widthImage * proporcao);
				}
			}

			pageImage.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					for (int i = 0; i < 100; i++) {
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

		} else if (input instanceof PreViewXML) {
			isHtml = true;
			PreViewXML html = (PreViewXML) input;
			fBrowser.setUrl(html.getUrl());	
			layout.topControl = pageXml;
			
			
		} else if (input instanceof PreViewRegion) {
			this.isRegion = true;
			layout.topControl = pageRegion;
			PreViewRegion region = (PreViewRegion) input;
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
		} else if (input instanceof PreViewMedia) {
			this.isMedia = true;
			layout.topControl = pageButton;
			final PreViewMedia med = (PreViewMedia) input;

			this.button.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					PlatformUI.getWorkbench().getDisplay().syncExec(
							new Runnable() {
								public void run() {

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

				}

				@Override
				public void mouseUp(MouseEvent e) {

				}
			});

		} else if (input instanceof String) {
			text.setText((String) input);

			layout.topControl = pageText;

		}

	}

	@Override
	public boolean hasContents() {
		return true;
	}


	public Point computeSizeHint() {

		if (isImage) {
			return new Point(widthImage, heightImage);
		}
		if (isMedia) {
			return getShell().computeSize(80, 80);
		}
		if (isRegion)
			return regionSize;

		if (isHtml)
			return new Point(230, 180);
		
		int widthHint= SWT.DEFAULT;
		Point constraints= getSizeConstraints();
		if (constraints != null && text.getWordWrap())
			widthHint= constraints.x;
		
		String toShow = (String) input;
		
		int heigthHint = 1;
		
		for (int i=0; i < toShow.length(); i++) if (toShow.charAt(i) == '\n') heigthHint++;
			
		
		
		
		return getShell().computeSize(widthHint, heigthHint * 18, true);

	}

	public IInformationControlCreator getInformationPresenterControlCreator() {
		return new IInformationControlCreator() {

			public IInformationControl createInformationControl(Shell parent) {
				return new NCLInformationControl(parent, true);
			}
		};
	}

	public void dispose() {
		super.dispose();

	}
	
	
}