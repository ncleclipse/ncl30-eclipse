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

import java.awt.MouseInfo;

import javax.xml.soap.Text;
import javax.xml.stream.EventFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;


import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;


import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import sun.java2d.pipe.TextPipe;
import sun.security.action.GetBooleanAction;


/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class PreHtml {
	static int pageNum = 1;
	IWorkbench wb = PlatformUI.getWorkbench();
	private Display display = wb.getDisplay();;
	private Shell shell=new Shell(display,SWT.CLOSE) ;
	private Browser browser;
	private StyledText styletext;
	
	private final int X = MouseInfo.getPointerInfo().getLocation().x;
	private final int Y = MouseInfo.getPointerInfo().getLocation().y;
	
	
	
	public PreHtml(int width,int height,final String result,final String url){
		
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		shell.setSize(width,height);
		
		shell.setLocation(X, Y);
				
		
		final Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		final MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		if(pageNum == 0){
			fileItem.setText("HTML");
		}else{
			fileItem.setText("Source");
		}
		
		
		final Composite contentPanel = new Composite (shell, SWT.BORDER);
		final StackLayout layout = new StackLayout ();
		contentPanel.setLayout (layout);
		
		
		final Composite page0 = new Composite (contentPanel, SWT.NONE);
		page0.setLayout (new FillLayout());
		styletext = new StyledText(page0,SWT.V_SCROLL|SWT.H_SCROLL);
		styletext.setText (result);
		styletext.setEditable(false);
		styletext.pack ();
		
		final Composite page1 = new Composite (contentPanel, SWT.NONE);
		page1.setLayout (new FillLayout());
		browser = new Browser(page1,SWT.H_SCROLL);
		browser.setUrl(url);
		browser.pack ();
		layout.topControl = pageNum == 0 ? page0 : page1;
		
		

		fileItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				pageNum = ++pageNum % 2;
				
				layout.topControl = pageNum == 0 ? page0 : page1;
				if(pageNum == 0){
					fileItem.setText("HTML");
				}else{
					fileItem.setText("Source");
				}
				contentPanel.layout ();
			}
		});
		shell.open();
		browser.addListener(SWT.MouseMove, new Listener(){
			

			@Override
			public void handleEvent(Event event) {
				if(shell.getBounds().contains(event.x,event.y)){
					System.out.println("sdaas");
				}
				
			}
			
		});
		
		
			
	}
}
	
	
	
	


	
	
		

	


