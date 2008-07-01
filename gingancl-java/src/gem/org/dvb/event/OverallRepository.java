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

/**
 * 
 * 
 * @author Martin Sveden
 * @statuscode 4
 */
public class OverallRepository extends UserEventRepository {
    
    public OverallRepository(){
        this("OverallRepository"); 
    }
    
    public OverallRepository(String name){
        super(name); 
        this.addAllArrowKeys();
        this.addAllColourKeys();
        this.addAllNumericKeys();
    }
}
