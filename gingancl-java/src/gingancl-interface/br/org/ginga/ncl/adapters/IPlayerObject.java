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
package br.org.ginga.ncl.adapters;

import java.awt.event.MouseAdapter;

import br.org.ginga.ncl.model.components.IExecutionObject;

public interface IPlayerObject {
	boolean immediatelyStart();

	void setImmediatelyStart(boolean value);

	boolean isVisible();

	void close();

	boolean hasPresented();

	void setPresented();

	boolean start();

	boolean stop();

	boolean abort();

	boolean pause();

	boolean resume();

	void forceNaturalEnd();

	void setVisible(boolean isVisible);

	void changeCurrentTime();

	double getMediaTime();

	IExecutionObject getExecutionObject();

	boolean startEvent(String anchorId);

	boolean stopEvent(String anchorId);

	boolean abortEvent(String anchorId);

	boolean pauseEvent(String anchorId);

	boolean resumeEvent(String anchorId);

	MouseAdapter getMouseHandler();

	void addPlayerObjectListener(IPlayerObjectListener listener);

	void removePlayerObjectListener(IPlayerObjectListener listener);

	void setTimeBasePlayerObject(IPlayerObject timeBasePlayerObj);

	void addTimeReferPlayerObject(IPlayerObject referPlayer);

	void removeTimeReferPlayerObject(IPlayerObject referPlayer);

	void notifyReferPlayerObjects(int transition);

	void timebaseObjectTransitionCallback(int transition);
}
