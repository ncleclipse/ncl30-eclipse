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

import java.util.Vector;

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

public class NCLInformationControl extends AbstractInformationControl implements
		IInformationControlExtension2 {

	private Composite internalComposite;
	private StyledText text;
	private boolean isImage;
	private StackLayout layout;
	private Composite pageImage;
	private Composite pageButton;
	private Composite pageRegion;
	private Composite pageText;
	private Composite pageXml;
	private Composite pageConnector;
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
	private boolean isconnector;

	public NCLInformationControl(Shell parentShell, boolean isResizable) {
		super(parentShell, false);
		fAdditionalTextStyles = isResizable ? SWT.V_SCROLL | SWT.H_SCROLL
				: SWT.NONE;
		create();

	}

	public NCLInformationControl(Shell parent, String statusFieldText,
			IInformationPresenter presenter) {
		super(parent, statusFieldText);
		fAdditionalTextStyles = SWT.NONE;
		create();
	}

	public NCLInformationControl(Shell parent, ToolBarManager toolBarManager,
			IInformationPresenter presenter) {
		super(parent, toolBarManager);
		fAdditionalTextStyles = SWT.V_SCROLL | SWT.H_SCROLL;
		create();
	}

	public NCLInformationControl(Shell parent, int textStyles,
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
				+ "icons" + "/" + "play.png");
		button = new Button(pageButton, SWT.PUSH);
		button.setImage(image);
		// image.dispose();

		pageXml = new Composite(this.internalComposite, SWT.NONE);
		pageXml.setLayout(new FillLayout());
		fBrowser = new Browser(pageXml, SWT.NONE);
		fBrowser.setForeground(internalComposite.getForeground());
		fBrowser.setBackground(internalComposite.getBackground());
		fBrowser.setFont(JFaceResources.getDialogFont());
		Browser.clearSessions();

		pageConnector = new Composite(internalComposite, SWT.NONE);
		pageConnector.setLayout(new FillLayout());
		pageConnector.setBackground(parent.getBackground());
		pageConnector.setForeground(parent.getForeground());

	}

	@Override
	public void setInput(Object input) {

		this.input = input;
		this.isImage = false;
		this.isMedia = false;
		this.isRegion = false;
		this.isHtml = false;
		this.isconnector = false;

		if (input instanceof PreViewImage) {
			layout.topControl = pageImage;
			this.isImage = true;

			PreViewImage imaget = (PreViewImage) input;
			final Image img = new Image(getShell().getDisplay(), imaget
					.toString());

			widthImage = img.getBounds().width;
			heightImage = img.getBounds().height;

			if (widthImage > 200 || heightImage > 200) {
				double proporcao;
				if (heightImage > widthImage) {
					proporcao = (double) widthImage / heightImage;
					heightImage = 200;
					widthImage = (int) Math.floor(heightImage * proporcao);
				} else {
					proporcao = (double) heightImage / widthImage;
					widthImage = 200;
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

		} else if (input instanceof PreViewConnector) {

			isconnector = true;
			PreViewConnector preViewConnector = (PreViewConnector) input;

			final Vector<Attributes> conditionRole = preViewConnector
					.getConditionRole();
			final Vector<Attributes> actionRole = preViewConnector
					.getActionRole();
			final Image conditions[] = new Image[conditionRole.size()];
			final Image actions[] = new Image[actionRole.size()];
			Image compoundCondition = null;
			Image compoundAction = null;
			final String path = this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().toString().substring(5)
					+ "icons/icons/";
			for (int i = 0; i < conditions.length; i++)
				conditions[i] = new Image(pageConnector.getDisplay(), path
						+ "cond" + conditionRole.get(i).getAttribute("role")
						+ ".png");
			for (int i = 0; i < actions.length; i++)
				actions[i] = new Image(pageConnector.getDisplay(), path
						+ "action" + actionRole.get(i).getAttribute("role")
						+ ".png");
			final String compCondition = preViewConnector
					.getCompoundCondition();
			final String compAction = preViewConnector.getCompoundAction();
			if (!compCondition.equals("")) {
				compoundCondition = new Image(pageConnector.getDisplay(), path
						+ "op" + compCondition.toUpperCase() + ".png");
			}
			if (!compAction.equals("")) {
				compoundAction = new Image(pageConnector.getDisplay(), path
						+ "op" + compAction.toUpperCase() + ".png");
			}

			final Image tempCondition = compoundCondition;
			final Image tempAction = compoundAction;
			pageConnector.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					
					for (int i = 0; i < 100; i++){
						e.gc.setBackground(internalComposite.getBackground());
						e.gc.setForeground(internalComposite.getForeground());
						e.gc.fillRectangle(0, 0, 1000, 500);
					}
					
					if (actions.length == 0 || conditions.length == 0)
						return;
					int DEFAULT_WIDTH = 40;
					int DEFAULT_HEIGHT = 40;
					int desX = 0;
					int off = 50;
					int maior = (actions.length > conditions.length ? actions.length
							: conditions.length);
					int Y = off * maior;
					Y += 10;
					if (maior > 5) {
						Y = 220;
						DEFAULT_HEIGHT -= 10;
						DEFAULT_WIDTH -= 10;
					}
					int desY = Y / (conditions.length);
					int x1 = 0, y1 = 0;
					int meio = desY / 2 - DEFAULT_HEIGHT / 2;
					for (int i = 0; i < conditions.length; i++) {
						final Image img = conditions[i];
						e.gc.drawImage(img, 0, 0, img.getBounds().width, img
								.getBounds().height, desX, i * desY + meio,
								DEFAULT_WIDTH, DEFAULT_HEIGHT);
						String max = conditionRole.get(i).getAttribute("max");
						String min = conditionRole.get(i).getAttribute("min");
						
						String toShow = "";
						if (!min.equals("") && !min.equals("1")) toShow += min + "-";
						
						if (!max.equals("") && !max.equals("1")) {
							if (max.equals("unbounded"))
								max = "n";
							toShow += max;
							e.gc.drawText(toShow, desX + DEFAULT_WIDTH, i * desY
									+ meio, true);

						}
						x1 = DEFAULT_WIDTH;
						y1 = i * desY + meio + DEFAULT_HEIGHT / 2;
					}
					int Pmeio = meio;
					desX += 100;
					meio = Y / 2 - DEFAULT_HEIGHT / 2;

					if (!compCondition.equals("")) {

						e.gc.drawImage(tempCondition, 0, 0, tempCondition
								.getBounds().width,
								tempCondition.getBounds().height, desX, meio,
								DEFAULT_WIDTH, DEFAULT_HEIGHT);

						for (int i = 0; i < conditions.length; i++) {
							x1 = DEFAULT_WIDTH;
							y1 = i * desY + Pmeio + DEFAULT_HEIGHT / 2;
							e.gc.drawLine(x1, y1, desX, meio
									+ (int) DEFAULT_HEIGHT / 2);

						}
						x1 = desX + DEFAULT_WIDTH;
						y1 = meio + DEFAULT_HEIGHT / 2;
						desX += 100;

					}

					int x = desX - 30;
					

					for (int i = 0; i < Y; i += 20)
						e.gc.drawLine(x, i, x, i + 10);

				
					if (!compAction.equals("")) {
						e.gc.drawImage(tempAction, 0, 0,
								tempAction.getBounds().width, tempAction
										.getBounds().height, desX, meio,
								DEFAULT_WIDTH, DEFAULT_HEIGHT);
						e.gc.drawLine(x1, y1, desX, meio + (int) DEFAULT_HEIGHT
								/ 2);
						x1 = desX + DEFAULT_WIDTH;
						y1 = meio + DEFAULT_HEIGHT / 2;
						desX += 100;
					}
	
					desY = Y / (actions.length);
					meio = desY / 2 - DEFAULT_HEIGHT / 2;
					for (int i = 0; i < actions.length; i++) {
						final Image img = actions[i];
						e.gc.drawImage(img, 0, 0, img.getBounds().width, img
								.getBounds().height, desX, i * desY + meio,
								DEFAULT_WIDTH, DEFAULT_HEIGHT);
						String max = actionRole.get(i).getAttribute("max");
						String min = actionRole.get(i).getAttribute("min");
						
						String toShow = "";
						if (!min.equals("") && !min.equals("1")) toShow += min + "-";
						
						
						if (!max.equals("") && !max.equals("1")) {
							if (max.equals("unbounded"))
								max = "n";
							toShow += max;
							e.gc.drawText(toShow, desX + DEFAULT_WIDTH, i * desY
									+ meio, true);

							
						}
						int x2 = desX;
						int y2 = i * desY + meio + DEFAULT_HEIGHT / 2;
						e.gc.drawLine(x1, y1, x2, y2);
					}
				}
			});
			layout.topControl = pageConnector;
		}

	}

	@Override
	public boolean hasContents() {
		return true;
	}

	public Point computeSizeHint() {

		if (isconnector) {
			PreViewConnector pre = (PreViewConnector) input;
			if (pre.getActionRole().size() == 0 || pre.getConditionRole().size() == 0)
				return getShell().computeSize(0, 0);
			int x = 60, y;
			if (!pre.getCompoundCondition().equals(""))
				x += 100;
			if (!pre.getCompoundAction().equals(""))
				x += 100;

			x += 110;

			y = 50 * (pre.getActionRole().size() > pre.getConditionRole()
					.size() ? pre.getActionRole().size() : pre
					.getConditionRole().size());
			y += 10;
			return getShell().computeSize(x, y);
		}

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

		int widthHint = SWT.DEFAULT;
		Point constraints = getSizeConstraints();
		if (constraints != null && text.getWordWrap())
			widthHint = constraints.x;

		String toShow = (String) input;

		int heigthHint = 1;

		for (int i = 0; i < toShow.length(); i++)
			if (toShow.charAt(i) == '\n')
				heigthHint++;

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