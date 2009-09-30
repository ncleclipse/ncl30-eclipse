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

import java.awt.Image;
import java.awt.MouseInfo;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.eclipse.swt.program.Program;
import org.eclipse.ui.PlatformUI;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class MediaTest extends JComponent{
	private Image img;
	private final int WIDTH=108;
	private final int HEIGHT=108;
	private JFrame j;
	JButton jbutton;
	String string;
	String type;
	File file;
	Program p;
	
	
	public MediaTest (String file,String type) {
		
		this.string = file;
		 this.type = type;
		this.file = new File(file);
		this.p = Program.findProgram(type);
		
		
		/*j = new JFrame();*/
		/*j.addMouseListener(new Mouseout());*/
		
		
		j = new JFrame();
		
		jbutton =new JButton (new ImageIcon (this.getClass().getResource("play.png")));
		jbutton.addMouseListener(new Mouseout());
		
		
		j.add(jbutton);
		j.setUndecorated(true);
		j.setSize(WIDTH, HEIGHT);
		java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
		j.setLocation(p.x - 5, p.y - 5);
		j.setVisible(true);
			
		
		
		
		
	
	}
	public class Mouseout implements java.awt.event.MouseListener{
		/*Program p = Program.findProgram(type);*/
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			
			PlatformUI.getWorkbench().getDisplay().syncExec(
		    	      new Runnable() {
		    	        public void run(){
		    	        	//Program.launch(string) ;
		    	        	p = Program.findProgram(type);
		    	        	if(p!=null){
		    	    				try{
		    	    					p.execute(string);
		    	    				}catch (Exception ev) {
		    	    					ev.printStackTrace();
		    	    				}
		    	    		}
		    	        }
		    	      });


			j.dispose();
			
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
			
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
			j.dispose();
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			
		}

			

			
		
		}
	
	

	public String toString (){
		
		j.add(this);
		
		
		return "";
	}
}
