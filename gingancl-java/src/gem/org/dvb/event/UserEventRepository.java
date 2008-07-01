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

import java.util.*;
import java.awt.event.*;
import org.havi.ui.event.*;

/**
 * 
 * 
 * @author Martin Sveden
 * @statuscode 4
 */
public class UserEventRepository //extends RepositoryDescriptor {
{

    
    private Vector userEvents;

    public UserEventRepository (String name) {
        // super(name, null);
        userEvents = new Vector();
    }
    
    /*
     *  Events are considered to be already in the repository if an event with 
     *  the same triplet of family, type and code is already in the repository. 
     *  But since all events have the same family(UserEvent.UEF_KEY_EVENT)
     *  and type(KeyEvent.KeyPressed) we only check the code.
     */    
    public void addUserEvent(UserEvent event) {
        //Debug.write(this, "event code = " + event.getCode());
        for(int i = 0; i < userEvents.size(); i++){
            UserEvent ue = (UserEvent) userEvents.get(i);
            if(ue.getCode() == event.getCode()){
                // it already exist so we don't want to add it
                return;
            }            
        }
        
        // it didn't exist so we add it
        userEvents.add(event);
    }
    
    public UserEvent[] getUserEvent() { 
        
        UserEvent[] events = new UserEvent[userEvents.size()];
        for(int i = 0; i < userEvents.size(); i++){
            Object o = userEvents.get(i);
            events[i] = (UserEvent) o;  
        }
        return events;
    }
    
    public boolean containsKey(int keyCode) {
    	int i, size;
    	UserEvent userEvent;
    	
    	size = userEvents.size();
    	for (i = 0; i < size; i++) {
    		userEvent = (UserEvent)userEvents.get(i);
    		if (userEvent.getCode() == keyCode) {
    			return true;
    		}
    	}
    	return false;
    }

    public void removeUserEvent (UserEvent event) {
        userEvents.remove(event);
    }
    
    public void addKey (int keyCode){        
        addKeyCode(keyCode);        
    }
   
    public void removeKey (int keyCode) {
        for(int i = 0; i < userEvents.size(); i++){
            UserEvent ue = (UserEvent) userEvents.get(i);
            if(ue.getCode() == keyCode){
                // it already exist so we remove it
                userEvents.remove(ue);
            }            
        }
    }

    public void addAllNumericKeys() {
        addKeyCode(KeyEvent.VK_0);
        addKeyCode(KeyEvent.VK_1);
        addKeyCode(KeyEvent.VK_2);
        addKeyCode(KeyEvent.VK_3);
        addKeyCode(KeyEvent.VK_4);
        addKeyCode(KeyEvent.VK_5);
        addKeyCode(KeyEvent.VK_6);
        addKeyCode(KeyEvent.VK_7);
        addKeyCode(KeyEvent.VK_8);
        addKeyCode(KeyEvent.VK_9);
    }

    public void addAllColourKeys(){
        addKeyCode(HRcEvent.VK_COLORED_KEY_0);
        addKeyCode(HRcEvent.VK_COLORED_KEY_1);
        addKeyCode(HRcEvent.VK_COLORED_KEY_2);
        addKeyCode(HRcEvent.VK_COLORED_KEY_3);
    }

    public void addAllArrowKeys(){
        addKeyCode(KeyEvent.VK_UP);
        addKeyCode(KeyEvent.VK_DOWN);
        addKeyCode(KeyEvent.VK_LEFT);
        addKeyCode(KeyEvent.VK_RIGHT);        
    }

    public void removeAllNumericKeys(){
        removeKey(KeyEvent.VK_0);
        removeKey(KeyEvent.VK_1);
        removeKey(KeyEvent.VK_2);
        removeKey(KeyEvent.VK_3);
        removeKey(KeyEvent.VK_4);
        removeKey(KeyEvent.VK_5);
        removeKey(KeyEvent.VK_6);
        removeKey(KeyEvent.VK_7);
        removeKey(KeyEvent.VK_8);
        removeKey(KeyEvent.VK_9);
    }

    public void removeAllColourKeys(){
        removeKey(HRcEvent.VK_COLORED_KEY_0);
        removeKey(HRcEvent.VK_COLORED_KEY_1);
        removeKey(HRcEvent.VK_COLORED_KEY_2);
        removeKey(HRcEvent.VK_COLORED_KEY_3);
    }

    public void removeAllArrowKeys(){
        removeKey(KeyEvent.VK_UP);
        removeKey(KeyEvent.VK_DOWN);
        removeKey(KeyEvent.VK_LEFT);
        removeKey(KeyEvent.VK_RIGHT);
    }

    /* for making it simpler*/
    private void addKeyCode(int keyCode){
        //Debug.write(this, "addKeyCode = " + keyCode);
        UserEvent ue = new UserEvent(this, UserEvent.UEF_KEY_EVENT, KeyEvent.KEY_PRESSED, keyCode, KeyEvent.CHAR_UNDEFINED, -1);
        userEvents.add(ue);
    }

}









