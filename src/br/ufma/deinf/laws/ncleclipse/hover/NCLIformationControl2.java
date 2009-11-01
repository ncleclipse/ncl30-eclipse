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
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.laws.ncleclipse.hover.NCLHoverInformationControl.IHTMLHoverInfo;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class NCLIformationControl2 extends AbstractInformationControl
implements IInformationControlExtension2 {
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
	
	static int pageNum = 1;
	;
	private Composite internalComposite;
	private Browser fBrowser;
	boolean fisImage;
	private static Image image;
	private StackLayout layout;
	private Composite pageImage;
	private Composite page1;
	private Composite pageButton;
	private Composite pageRegion;
	
	private boolean TinyImage;
	private boolean fisMedia;
	public boolean fisString;
	public boolean fisRegion;
	
	private Button button;
	private Color colorb;
	private Color colorf;

	private Program p;
	/**
	 * @param parentShell
	 * @param isResizable
	 */
	public NCLIformationControl2(Shell parentShell, boolean isResizable) {
		super(parentShell, isResizable);
		create();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.AbstractInformationControl#createContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createContent(Composite parent) {
		
		internalComposite = new Composite(parent,SWT.BORDER);
		internalComposite.setForeground(parent.getForeground());
		internalComposite.setBackground(parent.getBackground());
		internalComposite.setFont(JFaceResources.getDialogFont());
		
		//System.out.println("createcontent");
		
		 layout = new StackLayout ();
		this.internalComposite.setLayout(layout);
		
		pageImage = new Composite (this.internalComposite, SWT.NONE);
		pageImage.setLayout (new FillLayout());
		
		
		page1 = new Composite (this.internalComposite, SWT.NONE);
		page1.setLayout (new FillLayout());
		fBrowser = new Browser(page1,SWT.NONE);
		
		fBrowser.pack ();
		
		pageButton = new Composite (this.internalComposite, SWT.NONE);
		pageButton.setLayout (new FillLayout());
		Image image = new Image(pageButton.getDisplay(),this.getClass().getProtectionDomain().getCodeSource()
				.getLocation().toString().substring(5)
				+ "icons" + File.separatorChar + "play.png");
		button = new Button(pageButton, SWT.PUSH);
		button.setImage(image);
		image.dispose();
		
		pageRegion = new Composite (this.internalComposite, SWT.NONE);
		pageRegion.setLayout (new FillLayout());
		
		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IInformationControlExtension2#setInput(java.lang.Object)
	 */
	/*private class PaintR implements PaintListener{

		 (non-Javadoc)
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 
		@Override
		public void paintControl(PaintEvent e) {
			Drawiamge(e);
			
		}
		
	}
	public void Drawiamge(PaintEvent e){
		
	}*/
	@Override
	public void setInput(Object input) {
		
		//System.out.println("setinput");
		this.fisImage =false;
		this.TinyImage=false;
		this.fisMedia=false;
		this.fisString=false;
		this.fisRegion=false;
		System.out.println(input.getClass());
		
		if(input instanceof ImageTest){
			layout.topControl=pageImage;
			this.fisImage=true;
			
			ImageTest imaget= (ImageTest)input;
			System.out.println(imaget.toString());
				final Image img= new Image(getShell().getDisplay(),imaget.toString());
				
			if(img.getBounds().width<300 && img.getBounds().height<300){
				
				this.TinyImage=true;
			}
			this.image=img;
			if(!this.TinyImage){
			pageImage.addPaintListener(new PaintListener(){
				
				@Override
				public void paintControl(PaintEvent e) {
					e.gc.fillRectangle(0, 0, 300, 300);
					e.gc.drawImage(img, 0,0,img.getBounds().width,img.getBounds().height,0,0,300,300);
					
				}
				
			});
			}else if(this.TinyImage){
				pageImage.addPaintListener(new PaintListener(){
					
					@Override
					public void paintControl(PaintEvent e) {
						e.gc.fillRectangle(0, 0, 300, 300);
						e.gc.drawImage(img, 0,0,img.getBounds().width,img.getBounds().height,0,0,img.getBounds().width,img.getBounds().height);
						
					}
					
				});
			}
			
		}else if(input instanceof PreHtml){
			
			layout.topControl=page1;
			PreHtml html=(PreHtml)input;
			
			
			
			fBrowser.setText(html.toString());
			fBrowser.setSize(400,400);
			
			
		}else if(input instanceof RegionTest){
			this.fisRegion=true;
			layout.topControl=pageRegion;
			final Color cf= pageRegion.getForeground();
			final Color cb= pageRegion.getBackground();
			RegionTest region = (RegionTest)input;
			pageRegion.addPaintListener(new PaintListener(){
				
				@Override
				public void paintControl(PaintEvent e) {
					for(int i=0;i<100;i++){
					e.gc.setBackground(cb);
				e.gc.setForeground(cf);
				e.gc.setAlpha(100);
					e.gc.fillRectangle(0, 0, 300, 300);
					
					}
				}
				
			});
			region.paintregions(pageRegion);
			
		}else if(input instanceof MediaTest){
			this.fisMedia=true;
			layout.topControl=pageButton;
			final MediaTest med= (MediaTest)input;
			
			this.button.addMouseListener( new MouseListener(){

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					PlatformUI.getWorkbench().getDisplay().syncExec(
				    	      new Runnable() {
				    	        public void run(){
				    	        	//Program.launch(string) ;
				    	        	p = Program.findProgram(med.getType());
				    	        	if(p!=null){
				    	    				try{
				    	    					p.execute(med.toString());
				    	    				}catch (Exception ev) {
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
					
				}});
			
			

		}else if(input instanceof String){
			this.fisString=true;
			
			//super.setInformation((String)input);
			
			System.out.println((String)input);
		}
		
		
		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.IInformationControlExtension#hasContents()
	 */
	@Override
	public boolean hasContents() {
		//System.out.println("hascontent");
		return true;
	}
	
	/*public Rectangle computeTrim() {
		return Geometry.add(super.computeTrim(), internalComposite.computeTrim(0, 0, 0, 0));
	}*/
	public void setVisible(boolean visible) {
		//System.out.println("setvisible");
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
	
	public Point computeSizeHint() {
		//System.out.println("computesizeint");
		// see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=117602
		if(fisImage && !TinyImage){
			return getShell().computeSize(300,300, true);
		}if(fisImage && TinyImage){
			return getShell().computeSize(this.image.getBounds().width,this.image.getBounds().height, true);
		}if(fisRegion){
			return getShell().computeSize(300, 300);
		}
			
		
		if(fisMedia){
			return getShell().computeSize(80,80);
		}
		/*int widthHint= SWT.DEFAULT;
		Point constraints= getSizeConstraints();
		if (constraints != null)
			widthHint= constraints.x;*/
		
		return getShell().computeSize(400,400, true);
	}
	
	
	
	public void dispose() {
		// TODO Auto-generated method stub
	//	System.out.println("dispose");
		super.dispose();
		
		
			
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */

	
}
