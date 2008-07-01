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

package br.org.ginga.core.player;
import java.awt.AWTEvent;

import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.io.InputEventListener;


public interface IPlayer {
	static final short NONE = 0;
	static final short PLAY = 1;
	static final short PAUSE = 2;
	static final short STOP = 3;
	
	void addListener(IPlayerListener listener);
	void removeListener(IPlayerListener listener);
	void notifyListeners(short code, String paremeter);

	void setSurface(ISurface surface);
	ISurface getSurface();
	double getMediaTime();
	void setFocusHandler(boolean isHandler);
	void setScope(String scope);
	void setScope(String scope, double begin);
	void setScope(String scope, double begin, double end);
	void play();
	void stop();
	void pause();
	void resume();

	void eventStateChanged(
			String id, short type, short transition, int code);

	String getPropertyValue(String name);
	void setPropertyValue(String name, String value);
	//void setReferenceTimePlayer(IPlayer player);

	void addTimeReferPlayer(IPlayer referPlayer);
	void removeTimeReferPlayer(IPlayer referPlayer);
	void notifyReferPlayers(int transition);
	void timebaseObjectTransitionCallback(int transition);
	void setTimeBasePlayer(IPlayer timeBasePlayer);
	boolean hasPresented();
	void setPresented(boolean presented);
	boolean isVisible();
	void setVisible(boolean visible);
	boolean immediatelyStart();
	void setImmediatelyStart(boolean immediatelyStartVal);
	void forceNaturalEnd();
	boolean isForcedNaturalEnd();
}
