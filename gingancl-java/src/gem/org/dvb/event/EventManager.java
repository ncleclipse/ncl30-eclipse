/*
This file is based on XleTView implementation 

Copyright (C) 2003 Martin Svedén
 
This is free software, and you are welcome to redistribute it under certain 
conditions;

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

Last modified by PUC-Rio/TeleMidia Lab
*/
package org.dvb.event;

import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 
 * 
 * @author Martin Sveden
 * @statuscode 4
 */
public class EventManager {

  private static EventManager THE_INSTANCE;

  private Map listeners;
  private boolean notifying;
  private Map toBeAdded;
  private List toBeRemoved;
  
  
  private EventManager() {
  	notifying = false;
  	toBeAdded = new Hashtable();
  	toBeRemoved = new Vector();
  	
    listeners = new Hashtable();
    
    /*
    try {
      fileWriter = new FileWriter("interaction.log");
      out = new PrintWriter(new BufferedWriter(fileWriter));
      reference = System.currentTimeMillis();
    }
    catch (Exception exc) {
    }
    */
  }

  public static EventManager getInstance() {
    if (THE_INSTANCE == null) {
      THE_INSTANCE = new EventManager();
    }
    return THE_INSTANCE;
  }

  public synchronized void addUserEventListener(UserEventListener listener, 
  		UserEventRepository userEvents) {
  	if (!listeners.containsKey(listener) && !toBeAdded.containsKey(listener)) {
  		if (notifying) {
  			toBeAdded.put(listener, userEvents);
  		}
  		else { 
  			listeners.put(listener, userEvents);
  		}
  	}
  }

  public synchronized void removeUserEventListener(UserEventListener listener) {
  	if (listeners.containsKey(listener) && !toBeRemoved.contains(listener)) {
  		if (notifying) {
  			toBeRemoved.add(listener);
  		}
  		else {
  			listeners.remove(listener);
  		}
  	}
  }
  
  public void removeAllUserEventListeners() {
    listeners.clear();
  }

  /**
   * All events are passed to this method that notify registered listeners 
   * @param source
   * @param keyEvent
   */
  public void fireKeyEvent(Object source, KeyEvent keyEvent) {
  	Iterator keys;
    UserEventListener listener;
    UserEventRepository repository;
    UserEvent ue;
    int keyCode;
    
    notifying = true;
    keyCode = keyEvent.getKeyCode();
    keys = listeners.keySet().iterator();
    while (keys.hasNext()) {
    	try {
    		listener = (UserEventListener)keys.next();
    		repository = (UserEventRepository)listeners.get(listener);
    		// test the repository may not be necessary
    		if (repository != null &&
    				(repository instanceof OverallRepository || 
    						repository.containsKey(keyEvent.getKeyCode()))) {
    			ue = new UserEvent(source, UserEvent.UEF_KEY_EVENT, keyEvent.getID(), 
    					keyCode, -1, System.currentTimeMillis());
    			listener.userEventReceived(ue);
    		}
    	}
    	catch (Exception exc) {
    		System.err.println("Exception: " + exc);
    	}
    }
    
    updateListeners();
    
    /*
    try {
      long time = System.currentTimeMillis() - reference;
      String line;
      line = time + ";" + (time + 500) + ";" + keyEvent.getKeyCode() + "\n";
      out.write(line);
    }
    catch (Exception exc) {}
    */
  }
  
  private synchronized void updateListeners() {
  	Iterator keys;
    UserEventListener listener;
    int i, size;
    
  	notifying = false;
    keys = toBeAdded.keySet().iterator();
    while (keys.hasNext()) {
    	listener = (UserEventListener)keys.next();
    	listeners.put(listener, toBeAdded.get(listener));
    }
    
    size = toBeRemoved.size();
    for (i = 0; i < size; i++) {
    	listeners.remove(toBeRemoved.get(i));
    }
    
    toBeAdded.clear();
    toBeRemoved.clear();
  }
  
  public void close() {
  	removeAllUserEventListeners();
  	
    /*
    try {
      out.close();
      fileWriter.close();
    }
    catch (Exception exc) {}
    */
  }
}

