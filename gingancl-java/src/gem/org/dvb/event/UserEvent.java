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

import java.util.EventObject;
import java.awt.event.*;

/**
 * 
 * 
 * @author Martin Sveden
 * @statuscode 4
 */
public class UserEvent extends EventObject {
    
    public static final int UEF_KEY_EVENT = 1 ;
    private int family;
    private int type;
    private int code;
    private int modifiers;
    private long when;
    private char keyChar;

    public UserEvent(Object source, int family, int type, int code, int modifiers, long when) {
        super(source);      
        this.family = family;
        this.type = type;
        this.code = code;
        this.modifiers = modifiers;
        this.when = when;
    }  
    
    public UserEvent(Object source, int family, char keyChar, long when) {  
        super(source);
        this.family = family;
        this.keyChar = keyChar;
        this.when = when;
    }  

    public int getFamily(){
        return family;
    }

    public int getType(){
        return type;
    }

    public int getCode(){
        return code;
    }    

    public char getKeyChar(){ 
        return keyChar;
    }

    public int getModifiers(){ 
        return modifiers;
    }

    public boolean isShiftDown() { 
        boolean is = (KeyEvent.SHIFT_DOWN_MASK == modifiers)? true:false;
        return is;
    }

    public boolean isControlDown(){
        boolean is = (KeyEvent.CTRL_DOWN_MASK == modifiers)? true:false;
        return is;
    }

    public boolean isMetaDown(){
        boolean is = (KeyEvent.META_DOWN_MASK == modifiers)? true:false;
        return is;
    }

    public boolean isAltDown(){
        boolean is = (KeyEvent.ALT_DOWN_MASK == modifiers)? true:false;
        return is;
    }

    public long getWhen(){
        return when;
    }

}
