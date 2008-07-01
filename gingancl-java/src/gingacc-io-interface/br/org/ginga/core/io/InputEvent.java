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

package br.org.ginga.core.io;

import org.havi.ui.event.HRcEvent;

public interface InputEvent {
	public static short KEYEVENT_TYPE = 0;
	public static short CLICKEVENT_TYPE = 0;
	
	public static int NO_CODE = -1;
	
	public static final int ENTER_CODE          = '\n';
    public static final int BACK_SPACE_CODE     = '\b';
    public static final int TAB_CODE            = '\t';
    public static final int CANCEL_CODE         = 0x03;
    public static final int CLEAR_CODE          = 0x0C;
    public static final int SHIFT_CODE          = 0x10;
    public static final int CONTROL_CODE        = 0x11;
    public static final int ALT_CODE            = 0x12;
    public static final int PAUSE_CODE          = 0x13;
    public static final int CAPS_LOCK_CODE      = 0x14;
    public static final int ESCAPE_CODE         = 0x1B;
    public static final int SPACE_CODE          = 0x20;
    public static final int PAGE_UP_CODE        = 0x21;
    public static final int PAGE_DOWN_CODE      = 0x22;
    public static final int END_CODE          = 0x23;
    public static final int HOME_CODE           = 0x24;

    /**
     * Constant for the non-numpad <b>left</b> arrow key.
     * @see #KP_LEFT
     */
    public static final int LEFT_CODE           = 0x25;

    /**
     * Constant for the non-numpad <b>up</b> arrow key.
     * @see #KP_UP
     */
    public static final int UP_CODE             = 0x26;

    /**
     * Constant for the non-numpad <b>right</b> arrow key.
     * @see #KP_RIGHT
     */
    public static final int RIGHT_CODE          = 0x27;

    /**
     * Constant for the non-numpad <b>down</b> arrow key.
     * @see #KP_DOWN
     */
    public static final int DOWN_CODE           = 0x28;

    /**
     * Constant for the comma key, ","
     */
    public static final int COMMA_CODE          = 0x2C;

    /**
     * Constant for the minus key, "-"
     * @since 1.2
     */
    public static final int MINUS_CODE          = 0x2D;

    /**
     * Constant for the period key, "."
     */
    public static final int PERIOD_CODE         = 0x2E;

    /**
     * Constant for the forward slash key, "/"
     */
    public static final int SLASH_CODE          = 0x2F;

    /** 0 thru 9 are the same as ASCII '0' thru '9' (0x30 - 0x39) */
    public static final int NUM_0_CODE              = 0x30;
    public static final int NUM_1_CODE              = 0x31;
    public static final int NUM_2_CODE              = 0x32;
    public static final int NUM_3_CODE              = 0x33;
    public static final int NUM_4_CODE              = 0x34;
    public static final int NUM_5_CODE              = 0x35;
    public static final int NUM_6_CODE              = 0x36;
    public static final int NUM_7_CODE              = 0x37;
    public static final int NUM_8_CODE              = 0x38;
    public static final int NUM_9_CODE              = 0x39;

    /**
     * Constant for the semicolon key, ";"
     */
    public static final int SEMICOLON_CODE      = 0x3B;

    /**
     * Constant for the equals key, "="
     */
    public static final int EQUALS         = 0x3D;

    /** A thru Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A) */
    public static final int A_CODE              = 0x41;
    public static final int B_CODE              = 0x42;
    public static final int C_CODE              = 0x43;
    public static final int D_CODE              = 0x44;
    public static final int E_CODE              = 0x45;
    public static final int F_CODE              = 0x46;
    public static final int G_CODE              = 0x47;
    public static final int H_CODE              = 0x48;
    public static final int I_CODE              = 0x49;
    public static final int J_CODE              = 0x4A;
    public static final int K_CODE              = 0x4B;
    public static final int L_CODE              = 0x4C;
    public static final int M_CODE              = 0x4D;
    public static final int N_CODE              = 0x4E;
    public static final int O_CODE              = 0x4F;
    public static final int P_CODE              = 0x50;
    public static final int Q_CODE              = 0x51;
    public static final int R_CODE              = 0x52;
    public static final int S_CODE              = 0x53;
    public static final int T_CODE              = 0x54;
    public static final int U_CODE              = 0x55;
    public static final int V_CODE              = 0x56;
    public static final int W_CODE              = 0x57;
    public static final int X_CODE              = 0x58;
    public static final int Y_CODE              = 0x59;
    public static final int Z_CODE              = 0x5A;

    /**
     * Constant for the open bracket key, "["
     */
    public static final int OPEN_BRACKET_CODE   = 0x5B;

    /**
     * Constant for the back slash key, "\"
     */
    public static final int BACK_SLASH_CODE     = 0x5C;

    /**
     * Constant for the close bracket key, "]"
     */
    public static final int CLOSE_BRACKET_CODE  = 0x5D;

    /*public static final int NUMPAD0        = 0x60;
    public static final int NUMPAD1        = 0x61;
    public static final int NUMPAD2        = 0x62;
    public static final int NUMPAD3        = 0x63;
    public static final int NUMPAD4        = 0x64;
    public static final int NUMPAD5        = 0x65;
    public static final int NUMPAD6        = 0x66;
    public static final int NUMPAD7        = 0x67;
    public static final int NUMPAD8        = 0x68;
    public static final int NUMPAD9        = 0x69;*/
    public static final int MULTIPLY_CODE       = 0x6A;
    public static final int ADD_CODE            = 0x6B;

    /** 
     * This constant is obsolete, and is included only for backwards
     * compatibility.
     * @see #SEPARATOR
     */
    public static final int SEPARATER_CODE      = 0x6C;

    /** 
     * Constant for the Numpad Separator key. 
     * @since 1.4
     */
    public static final int SEPARATOR_CODE      = SEPARATER_CODE;

    public static final int SUBTRACT_CODE       = 0x6D;
    public static final int DECIMAL_CODE        = 0x6E;
    public static final int DIVIDE_CODE         = 0x6F;
    public static final int DELETE_CODE         = 0x7F; /* ASCII DEL */
	
	/**
	 * Constants for GINGA REMOTE CONTROL.
	 */
    public static final int GUIDE_CODE         = HRcEvent.VK_GUIDE; 
    public static final int HELP_CODE          = HRcEvent.VK_HELP;
    public static final int TV_CODE            = HRcEvent.VK_VIDEO_MODE_NEXT;
    public static final int INFO_CODE          = HRcEvent.VK_INFO;
	
    public static final int PORTAL_CODE        = HRcEvent.VK_BACK_SPACE;
    public static final int EXIT_CODE          = HRcEvent.VK_ESCAPE;
    public static final int MENU_CODE          = HRcEvent.VK_CONTEXT_MENU;
    public static final int BACK_CODE          = HRcEvent.VK_BACK_SPACE;
    
    public static final int CURSOR_DOWN_CODE   = HRcEvent.VK_DOWN;
    public static final int CURSOR_LEFT_CODE   = HRcEvent.VK_LEFT;
    public static final int CURSOR_RIGHT_CODE  = HRcEvent.VK_RIGHT;
    public static final int CURSOR_UP_CODE     = HRcEvent.VK_UP;
    
    public static final int CHANNEL_DOWN_CODE  = HRcEvent.VK_CHANNEL_DOWN;
    public static final int CHANNEL_UP_CODE    = HRcEvent.VK_CHANNEL_UP;

    public static final int VOLUME_DOWN_CODE   = HRcEvent.VK_VOLUME_DOWN;
    public static final int VOLUME_UP_CODE     = HRcEvent.VK_VOLUME_UP;
    
    public static final int OK_CODE            = END_CODE;

    public static final int RED_CODE           = HRcEvent.VK_COLORED_KEY_0;
    public static final int GREEN_CODE         = HRcEvent.VK_COLORED_KEY_1;
    public static final int YELLOW_CODE        = HRcEvent.VK_COLORED_KEY_2;
    public static final int BLUE_CODE          = HRcEvent.VK_COLORED_KEY_3;
    
    public static final int POWER_CODE         = HRcEvent.VK_POWER;
    public static final int REWIND_CODE        = HRcEvent.VK_REWIND;
    public static final int STOP_CODE          = HRcEvent.VK_STOP;
    public static final int EJECT_CODE         = HRcEvent.VK_EJECT_TOGGLE;
    public static final int PLAY_CODE          = HRcEvent.VK_PLAY;
    public static final int RECORD_CODE        = HRcEvent.VK_RECORD;
	
	public short getType();
	public int getCode();
	public Object getSource();
}
