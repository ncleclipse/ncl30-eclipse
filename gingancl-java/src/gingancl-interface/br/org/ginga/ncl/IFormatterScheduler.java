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
package br.org.ginga.ncl;

import java.util.List;

import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.presentation.IFormatterLayout;

/**
 * The scheduler interface. The scheduler is the main component in the 
 * presentation engine runtime control.
 */
public interface IFormatterScheduler {
	/**
	 * Starts executing a formatter event.
	 * @param event the event to be put in the occurring state.
	 */
	void startEvent(IFormatterEvent event);

	/**
	 * Stops executing a formatter event.
	 * @param event the event to be put in the sleeping state.
	 */
	void stopEvent(IFormatterEvent event);

	/**
	 * Pauses executing a formatter event.
	 * @param event the event to be put in the paused state.
	 */
	void pauseEvent(IFormatterEvent event);

	/**
	 * Resumes executing a formatter event.
	 * @param event the event to be put in the occurring state again.
	 */
	void resumeEvent(IFormatterEvent event);

	void startDocument(IFormatterEvent documentEvent, List entryEvents);

	void stopDocument(IFormatterEvent documentEvent);

	void pauseDocument(IFormatterEvent documentEvent);

	void resumeDocument(IFormatterEvent documentEvent);

	void stopAllDocuments();

	void pauseAllDocuments();

	void resumeAllDocuments();

	void addSchedulerListener(IFormatterSchedulerListener listener);

	void removeSchedulerListener(IFormatterSchedulerListener listener);

	void reset();

	IFormatterLayout getLayoutManager();
}
