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

package br.pucrio.telemidia.ginga.core.player.text;

import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.EmptyBorder;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlBlockPanel;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

public class MozillaHTMLPlayer extends DefaultPlayerImplementation {
	private HtmlPanel panel;
	private boolean border;
	
	public static final String BORDER_PROPERTY = "border";
	
	public static final String NO_BORDER_PROPERTY_VAUE = "none";
	public MozillaHTMLPlayer(URL contentURL) {
		super(contentURL);
		Logger.global.setLevel(Level.OFF);
		Logger.getLogger(HtmlBlockPanel.class.getName()).setLevel(Level.OFF);
		//Logger.this.setLevel(Level.OFF);
		this.setSurface(GFXManager.getInstance().createSurface("HTML"));
		this.border=false;
		
		URLConnection connection;
		InputStream in;
		try {
			connection = contentURL.openConnection();
			in = connection.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// A Reader should be created with the correct charset,
		// which may be obtained from the Content-Type header
		// of an HTTP response.
		Reader reader = new InputStreamReader(in);

		panel= new HtmlPanel();
		LocalHtmlRendererContext rcontext = new LocalHtmlRendererContext(panel);
		//SimpleHtmlRendererContext rcontext = new SimpleHtmlRendererContext(panel);
		UserAgentContext ucontext = rcontext.getUserAgentContext();
		// Note that document builder should receive both contexts.
		DocumentBuilderImpl dbi = new DocumentBuilderImpl(ucontext, rcontext);
		// A documentURI should be provided to resolve relative URIs.
		Document document;
		try {
			document = dbi.parse(new InputSourceImpl(reader, contentURL.getFile()));
			// Now set document in panel. This is what causes the document to render.
			panel.setAutoscrolls(false);
			panel.setDocument(document, rcontext);
			in.close();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eventStateChanged(String id, short type, short transition,
			int code) {
		// TODO Auto-generated method stub
		
	}

	public String getPropertyValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPropertyValue(String name, String value) {
		if(name.equals(BORDER_PROPERTY)){
			if(value.equals(NO_BORDER_PROPERTY_VAUE))
				border = false;
			else
				border = true;
		}
	}

	@Override
	public void play() {
		if(border)
			panel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		
		this.getSurface().setSurface(panel);
		super.play();
	}

	@Override
	public void stop() {
		this.getSurface().clear();
		super.stop();
	}
	
	private static class LocalHtmlRendererContext extends SimpleHtmlRendererContext {
		// Override methods here to implement browser functionality
		public LocalHtmlRendererContext(HtmlPanel contextComponent) {
			super(contextComponent);
		}
	}

}
