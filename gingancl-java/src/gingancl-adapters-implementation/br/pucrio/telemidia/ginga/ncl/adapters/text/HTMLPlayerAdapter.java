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
package br.pucrio.telemidia.ginga.ncl.adapters.text;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import br.org.ginga.core.player.IPlayer;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ncl.components.IContent;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.components.IReferenceContent;
import br.pucrio.telemidia.ginga.core.player.text.CobraHTMLPlayer;
import br.pucrio.telemidia.ginga.core.player.text.HTMLPlayer;
import br.pucrio.telemidia.ginga.ncl.adapters.DefaultFormatterPlayerAdapter;

/**
 * @author Rafael Ferreira Rodrigues
 *
 */
public class HTMLPlayerAdapter extends
		DefaultFormatterPlayerAdapter {
	
	private Map<String,String> rawContentCache;

	/**
	 * 
	 */
	public HTMLPlayerAdapter() {
		super();
		rawContentCache = new Hashtable<String,String>();
	}

	/* (non-Javadoc)
	 * @see br.pucrio.telemidia.ginga.ncl.adapters2.DefaultFormatterPlayerAdapterImplementation#createPlayer(br.org.ginga.ncl.model.event.IPresentationEvent, double)
	 */
	@Override
	protected void createPlayer() {
		INodeEntity dataObject;
		IContent content;
		URL url;
		String cacheUrl;
		String rawContent;
		IPlayer newPlayer;

		dataObject = (INodeEntity)object.getDataObject()
				.getDataEntity();
		content = dataObject.getContent();

		if (content instanceof IReferenceContent) {
			url = ((IReferenceContent)content).getCompleteReferenceUrl();
		}
		else {
			//TODO: esse tratamento precisa ser melhorado
			try {
				url = new URL(content.toString());
			}
			catch (Exception exc) {
				System.out.println("Exception: " + exc);
			}
		}
		url = this.getMRL();
		cacheUrl = url.getProtocol() + ":";
		if (url.getHost().length() > 0) {
			cacheUrl += "//" + url.getHost();
			if (url.getPort() >= 0) {
				cacheUrl += ":" + url.getPort();
			}
			cacheUrl += "/";
		}
		cacheUrl += url.getFile();

		if (rawContentCache.containsKey(cacheUrl)) {
			rawContent = (String)rawContentCache.get(cacheUrl);
		}else {
			// buscar o conteudo caso nao esteja no cache do objeto de dados
			try {
				rawContent = getNodeContent(new URL(cacheUrl));
				rawContentCache.put(cacheUrl, rawContent);
			}
			catch (Exception exc) {
				rawContent = "Error READING FILE";
				System.out.println("Exception: " + exc);
			}
		}
		player = new HTMLPlayer(url,rawContent);
		//player = new CobraHTMLPlayer(url);
		ICascadingDescriptor descriptor = object.getDescriptor();
		if(descriptor != null){
			String paramValue = (String)descriptor.getParameterValue(HTMLPlayer.BORDER_PROPERTY);
			if(paramValue!=null && !paramValue.equals("")){
				player.setPropertyValue(HTMLPlayer.BORDER_PROPERTY, paramValue);
			}
		}
		super.createPlayer();
	}
	
	private String getNodeContent(URL url) {
		URLConnection connection;
		BufferedReader buff_reader;
		String line, content;

		try {
			connection = url.openConnection();
			buff_reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));

			content = "";
			line = buff_reader.readLine();
			while (line != null) {
				content += line + '\n';
				line = buff_reader.readLine();
			}

			// para mostrar arquivo txt
			if (!content.startsWith("<html>") && !content.endsWith("</html>")) {
				content = content.replaceAll("<", "&lt;");
				content = content.replaceAll(">", "&gt;");
				content = "<html><head></head><body>" + content + "</body></html>";
			}

			// para mostrar links nas falas do eths
			content = content.replaceAll("<div ", "<span ");
			content = content.replaceAll("</div>", "</span>");

			return content;
		}
		catch (Exception exc) {
			System.out.println("Error: " + exc);
			return "<html><head></head><body><h1>Content not found</h1></body></html>";
		}
	}

}
