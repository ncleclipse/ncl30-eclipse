/******************************************************************************
Este arquivo � parte da implementa��o do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laborat�rio TeleM�dia

Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob 
os termos da Licen�a P�blica Geral GNU vers�o 2 conforme publicada pela Free 
Software Foundation.

Este programa � distribu�do na expectativa de que seja �til, por�m, SEM 
NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU 
ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral do 
GNU vers�o 2 para mais detalhes. 

Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do GNU vers�o 2 junto 
com este programa; se n�o, escreva para a Free Software Foundation, Inc., no 
endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informa��es:
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
package br.pucrio.telemidia.ginga.ncl.adapters;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.adapters.IPlayerAdapterManager;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.org.ncl.components.IContent;
import br.org.ncl.components.IContentNode;
import br.org.ncl.components.INodeEntity;
import br.org.ncl.components.IReferenceContent;
import br.org.ncl.descriptor.IDescriptor;
import br.org.ncl.descriptor.IGenericDescriptor;
import br.pucrio.telemidia.ncl.components.ContentTypeManager;

/**
 * Mantem um registro das ferramentas disponiveis para exibicao dos objetos de
 * execucao. Esse registro, na realidade, mantem duas tabelas. A primeira
 * tabela guarda os visualizadores default para cada tipo MIME registrado. A
 * segunda tabela associa visualizadores a classes de controladores
 * implementadas.
 */
public class PlayerAdapterManager implements IPlayerAdapterManager {
	/**
	 * 
	 */
	private static final String mimeFile = "gingaNclConfig/players/mimedefs.ini";

	/**
	 * 
	 */
	private static final String ctrlFile = "gingaNclConfig/players/ctrldefs.ini";

	/**
	 * file extension => player adapter class name
	 */
	private Properties mimeDefaultTable;

	/**
	 * player adapter name => player adapter class name
	 */
	private Properties playerTable;

	/**
	 * execution object id => player adapter instance
	 */
	private Map objectPlayers;

	/**
	 * 
	 */
	public PlayerAdapterManager() {
		super();
		objectPlayers = new Hashtable();
		readConfigFiles();
	}

	/**
	 * @param descriptor
	 * @param dataObject
	 * @return
	 */
	private String getPlayerClass(ICascadingDescriptor descriptor,
			INodeEntity dataObject) {
		String toolName, mime;

		if (dataObject instanceof IContentNode
				&& (((IContentNode)dataObject).getNodeType() == null || ((IContentNode)dataObject)
						.getNodeType().equalsIgnoreCase(IContentNode.SETTING_NODE))) {
			return null;
		}

		if (descriptor == null) {
			toolName = null;
		}
		else {
			toolName = descriptor.getPlayerName();
		}

		if (toolName == null || toolName == "") {
			// there is no player defined, so it should be chose a player based on
			// the node content type
			if (dataObject instanceof IContentNode) {
				mime = ((IContentNode)dataObject).getNodeType();
			}
			else {
				return null;
			}

			if (mime != null) {
				return mimeDefaultTable.getProperty(mime);
			}
			else {
				return null;
			}
		}
		else {
			return playerTable.getProperty(toolName);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adapters.IPlayerAdapterManager#reset()
	 */
	public void reset() {
		objectPlayers.clear();
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adapters.IPlayerAdapterManager#close()
	 */
	public void close() {
		reset();
		objectPlayers = null;
		mimeDefaultTable.clear();
		mimeDefaultTable = null;
		playerTable.clear();
		playerTable = null;
	}

	/**
	 * 
	 */
	private void readConfigFiles() {
		FileInputStream fis;

		try {
			mimeDefaultTable = new Properties();
			fis = new FileInputStream(mimeFile);
			mimeDefaultTable.load(fis);
			fis.close();

			playerTable = new Properties();
			fis = new FileInputStream(ctrlFile);
			playerTable.load(fis);
			fis.close();
		}
		catch (Exception exc) {
			System.err.println("Error: " + exc);
		}
	}

	/**
	 * @param object
	 * @return
	 */
	private IFormatterPlayerAdapter initializePlayer(IExecutionObject object) {
		ICascadingDescriptor descriptor;
		INodeEntity dataObject;
		String playerClassName;
		IFormatterPlayerAdapter player;

		descriptor = object.getDescriptor();
		dataObject = (INodeEntity)object.getDataObject().getDataEntity();
		playerClassName = getPlayerClass(descriptor, dataObject);

		if (playerClassName == null) {
			return null;
		}

		try {
			player = (IFormatterPlayerAdapter)Class.forName(playerClassName)
					.newInstance();
			// System.err.println("PlayerManager::initializePlayer for " + object.getId());
			objectPlayers.put(object.getId(), player);
			return player;
		}
		catch (Exception exc) {
			System.err.println("Error creating player: " + exc);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.ginga.ncl.adapters.IPlayerAdapterManager#getPlayer(br.org.ginga.ncl.model.components.IExecutionObject)
	 */
	public IFormatterPlayerAdapter getPlayer(IExecutionObject execObj) {
		IFormatterPlayerAdapter player;

		player = (IFormatterPlayerAdapter)objectPlayers.get(execObj.getId());
		if (player == null) {
			return initializePlayer(execObj);
		}
		else {
			return player;
		}
	}
	
	public static boolean isProcedural(INodeEntity dataObject){
		String mediaType = "";
		String url = "";
		IGenericDescriptor descriptor;
		IDescriptor simpleDescriptor;
		IContent content;

		//first, descriptor
		descriptor = (IGenericDescriptor)(dataObject.getDescriptor());
		if (descriptor != null) {
			if (descriptor instanceof IDescriptor) {
				simpleDescriptor = (IDescriptor)descriptor;
				mediaType = simpleDescriptor.getPlayerName();
				if (mediaType != null && !mediaType.equals("")) {
					if (mediaType.equals("NCLetPlayerAdapter") ||
							mediaType.equals("LuaPlayerAdapter")) {
						return true;
					}
					return false;
				}
			}
		}

		//second, media type
		if (dataObject instanceof IContentNode) {
			mediaType = ((IContentNode)dataObject).getNodeType();
			if (mediaType != null &&  !mediaType.equals("")) {
				if (mediaType.equals("application/x-ginga-NCLua") ||
						mediaType.equals("application/x-ginga-NCLet")) {

					return true;
				}
				return false;
			}
		}

		//finally, content file extension
		content = dataObject.getContent();
		if (content != null) {
			if (content instanceof IReferenceContent) {
				url = ((IReferenceContent)(content))
					    .getCompleteReferenceUrl().getPath();

				if (url.indexOf(".") != -1) {
					mediaType = ContentTypeManager.getInstance()
							.getMimeType(url.substring(url.lastIndexOf("."))); 
					if (mediaType == "application/x-ginga-NCLua" ||
							mediaType == "application/x-ginga-NCLet") {
						return true;
					}
				}
			}
		}
		return false;
	}
}