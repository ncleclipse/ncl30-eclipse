package br.org.ginga.ncl.adapters;

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


import br.org.ncl.animation.IAnimation;
import br.org.ginga.core.io.ISurface;
import br.org.ginga.core.player.IPlayer;
import br.org.ginga.core.player.IPlayerListener;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IAttributeValueMaintainer;
import br.org.ginga.ncl.model.event.IAttributionEvent;
import br.org.ginga.ncl.model.event.IFormatterEvent;
import br.org.ginga.ncl.model.event.IPresentationEvent;

/**
 * Every media player integrated to the formatter must have an adapter that
 * implement this interface. A formatter player adapter can simultaneously 
 * control more than one execution object presentation. The same instance of a 
 * formatter player adapter can also be reused to control different execution 
 * object presentations.
 */
public interface IFormatterPlayerAdapter extends IPlayerListener, IAttributeValueMaintainer {
	/**
	 * Aborts the execution object presentation that was previously started. The
	 * abort action must be applied over all object events, whose occurrence
	 * attribute should not be incremented. New programmed presentation
	 * repetitions should be ignored.
	 * 
	 * @return true if the object presentation can be correctly aborted and false
	 *         otherwise.
	 */
	boolean abort();

	/**
	 * Frees all resources previously allocatted for this player adapter. After 
	 * closing a player adapter, the player adapter cannot be invoked anymore for 
	 * controlling object presentations.
	 */
	//void close();

	/**
	 * Informs if the object and respective presentation event were previously
	 * prepared in this player adapter.
	 * 
	 * @return true if the object was previously prepared and false otherwise.
	 */
	boolean hasPrepared();
	
	void naturalEnd();
	
	/**
	 * The player object is the class that actually encapsulates the media player
	 * controlling the execution object content presentation. As a consequence,
	 * for each execution object, there is a player object wrapping its content
	 * rendering.
	 * @return the player object that wraps the player object. Return null
	 * if the execution object is not being controlled by the player adapter.
	 */
	IPlayer getPlayer();
	
	//IExecutionObject getObject(IPlayer player);

	/**
	 * Returns the GUI component where the object is presented. This is the
	 * rendering surface for the object content.
	 * 
	 * @return the GUI component created to present the object content. The method
	 *         returns null if the object has no graphical interface defined, as
	 *         for some audio players.
	 */
	ISurface getObjectDisplay();

	void setFocusHandler(boolean isHandler);
	/**
	 * Pauses an execution object presentation that was previously started. The
	 * pause action must be applied over all object events, whose state is
	 * occurring.
	 * 
	 * @param object
	 *          the object that should have its presentation paused.
	 * @return true if the object presentation can be correctly paused and false
	 *         otherwise.
	 */
	boolean pause();

	/**
	 * Prepares the presentation of an execution object. The mainEvent, as the name
	 * suggests, is the object main presentation event. When presenting the object
	 * as a whole, this is the whole content presentation event. However, object
	 * internal fragments can be selected to be presented. Despite of the kind of
	 * mainEvent, firing the transitions to prepare the internal events associated
	 * to the main event presentation is a player adapter responsability. An
	 * implementation may opt to automatically prepare all execution object events
	 * related to the presentation.
	 * 
	 * @param object
	 *          the execution object that should be controlled.
	 * @param mainEvent
	 *          the execution object main presentation event. Every object event
	 *          related to the object rendering must be contained in the main
	 *          event.
	 * @return true if the object preparation can be correctly fired. It is
	 *         important noticing that one may choose to implement the player 
	 *         adapter methods asynchronously, i.e., when returning true, the 
	 *         object preparation may not yet be concluded. The method returns 
	 *         false if there is any problem for preparing the object. Problem 
	 *         examples are: object content cannot be found; the object is already
	 *         registred to be controlled.
	 */
	boolean prepare(IExecutionObject object, IFormatterEvent mainEvent);

	/**
	 * Resumes an execution object presentation that was previously paused. The
	 * resume action must be applied over all object events, whose state is paused
	 * turning them back to the occurring state. However, if an event was already
	 * paused when the pause method has been called, this event must not be
	 * resumed. To be resumed the event needs to explicitly resumed, not the
	 * object. 
	 * 
	 * @return true if the object presentation can be correctly resumed and false
	 *         otherwise.
	 */
	boolean resume();

	/**
	 * Assigns a value to an attribution event of an execution object controlled 
	 * by this player adapter. When modifying the attribute value of the 
	 * attribution event, the player adapter should change the event state to 
	 * occurring. After the modification the event state should be put back in the 
	 * prepared state.
	 * 
	 * @param object
	 *          the execution object whose attribute should be modified.
	 * @param event
	 *          the event that wrapps the object attribute.
	 * @param value
	 *          the attribute new value.
	 * @return true if the value can be assigned to the attribute identified by 
	 * the event argument.
	 */
	boolean setPropertyValue(IAttributionEvent event, Object value, 
				IAnimation animation);
	
	//boolean runAction(IExecutionObject object, IFormatterEvent event, short action);

	/**
	 * Starts the presentation of an execution object. If the
	 * object main event is sleeping, the prepare method should be called before
	 * starting the objectpresentation. If the object main event is preparing,
	 * the preparation procedure should finish before starting the presentation.
	 * 
	 * @return true if the object can be correctly started. The method returns
	 *         false if there is any problem for presenting the object. Problem
	 *         examples are: the object was already being presented (the mainEvent
	 *         was in the occurring or paused state etc.).
	 */
	boolean start();

	/**
	 * Stops the presentation of an execution object that was previously started.
	 * The stop action should be repassed to all object events that should
	 * increment their occurrence attributes but should ignore new previously
	 * programmed repetitions (new repetitions should occur only when the object
	 * presentation naturaly ends and its freeze attribute is not true). It is 
	 * important to mention that it is a player adapter responsability to fire the 
	 * presentation repetition (when programmed) - both the content presentation 
	 * as the state machine transition.
	 * 
	 * @return true if the object can be correctly stopped and false otherwise.
	 */
	boolean stop();

	/**
	 * This method should be called when an object should not be anymore
	 * controlled by this player. The object events must receive the following
	 * action commands: stop (if they are occurring or paused) and afterwards
	 * unprepare. The player should clear the resources allocated for this object.
	 * 
	 * @return true if the object can be unprepared and false otherwise.
	 */
	boolean unprepare();

	/**
	 * Sets the implicit duration for all presentation events of this object. An
	 * implicit duration is set only for events that have no explicit duration
	 * defined.
	 */
	void updateObjectExpectedDuration();
	double getObjectExpectedDuration();

	/**
	 * Returns the time instant in seconds that the object presentation is. This
	 * time is measured based on the object content duration and not on an
	 * absolute clock. So, if the object is a video stream and delays have been 
	 * introduced by a network, the method should return the instant of content
	 * being presented as if a deterministic network was avalilable. Pause
	 * intervals should not interfere in the getMediaTime return value too.
	 * 
	 * @return the current content time in seconds.
	 */
	double getMediaTime();
	

	/**
	 * One execution object can have its time control given by another execution
	 * object. As an example, a text subtitle execution object may have its time
	 * base given by a video execution object. This method allows establishing
	 * this kind of relationship.
	 * @param timeBasePlayer
	 *          the player adapter controlling the time base object.
	 */
	void setTimeBasePlayer(IFormatterPlayerAdapter timeBasePlayerAdapter);
	
}
