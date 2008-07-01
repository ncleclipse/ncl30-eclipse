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

package br.pucrio.telemidia.ginga.core.io;

import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.havi.ui.event.HRcEvent;

public class CodeMap {
	private static CodeMap _instance = null;

	private Map<String, Integer> keyMap;

	private CodeMap() {
		keyMap = new Hashtable<String, Integer>();

		keyMap.put("NO_CODE", new Integer(-1));

		keyMap.put("0", new Integer(KeyEvent.VK_0));
		keyMap.put("1", new Integer(KeyEvent.VK_1));
		keyMap.put("2", new Integer(KeyEvent.VK_2));
		keyMap.put("3", new Integer(KeyEvent.VK_3));
		keyMap.put("4", new Integer(KeyEvent.VK_4));
		keyMap.put("5", new Integer(KeyEvent.VK_5));
		keyMap.put("6", new Integer(KeyEvent.VK_6));
		keyMap.put("7", new Integer(KeyEvent.VK_7));
		keyMap.put("8", new Integer(KeyEvent.VK_8));
		keyMap.put("9", new Integer(KeyEvent.VK_9));

		keyMap.put("A", new Integer(KeyEvent.VK_A));
		keyMap.put("B", new Integer(KeyEvent.VK_B));
		keyMap.put("C", new Integer(KeyEvent.VK_C));
		keyMap.put("D", new Integer(KeyEvent.VK_D));
		keyMap.put("E", new Integer(KeyEvent.VK_E));
		keyMap.put("F", new Integer(KeyEvent.VK_F));
		keyMap.put("G", new Integer(KeyEvent.VK_G));
		keyMap.put("H", new Integer(KeyEvent.VK_H));
		keyMap.put("I", new Integer(KeyEvent.VK_I));
		keyMap.put("J", new Integer(KeyEvent.VK_J));
		keyMap.put("K", new Integer(KeyEvent.VK_K));
		keyMap.put("L", new Integer(KeyEvent.VK_L));
		keyMap.put("M", new Integer(KeyEvent.VK_M));
		keyMap.put("N", new Integer(KeyEvent.VK_N));
		keyMap.put("O", new Integer(KeyEvent.VK_O));
		keyMap.put("P", new Integer(KeyEvent.VK_P));
		keyMap.put("Q", new Integer(KeyEvent.VK_Q));
		keyMap.put("R", new Integer(KeyEvent.VK_R));
		keyMap.put("S", new Integer(KeyEvent.VK_S));
		keyMap.put("T", new Integer(KeyEvent.VK_T));
		keyMap.put("U", new Integer(KeyEvent.VK_U));
		keyMap.put("V", new Integer(KeyEvent.VK_V));
		keyMap.put("W", new Integer(KeyEvent.VK_W));
		keyMap.put("X", new Integer(KeyEvent.VK_X));
		keyMap.put("Y", new Integer(KeyEvent.VK_Y));
		keyMap.put("Z", new Integer(KeyEvent.VK_Z));

		keyMap.put("*", new Integer(KeyEvent.VK_ASTERISK));
		keyMap.put("#", new Integer(KeyEvent.VK_NUMBER_SIGN));

		// TODO: review
		keyMap.put("GUIDE", new Integer(HRcEvent.VK_GUIDE));
		keyMap.put("HELP", new Integer(HRcEvent.VK_HELP));
		keyMap.put("TV", new Integer(HRcEvent.VK_VIDEO_MODE_NEXT));
		keyMap.put("INFO", new Integer(HRcEvent.VK_INFO));
		
		keyMap.put("PORTAL", new Integer(HRcEvent.VK_BACK_SPACE));
		keyMap.put("EXIT", new Integer(HRcEvent.VK_ESCAPE));
		keyMap.put("MENU", new Integer(HRcEvent.VK_CONTEXT_MENU));
		keyMap.put("BACK", new Integer(HRcEvent.VK_BACK_SPACE));
		keyMap.put("EXIT", new Integer(HRcEvent.VK_ESCAPE));

		keyMap.put("CURSOR_DOWN", new Integer(KeyEvent.VK_DOWN));
		keyMap.put("CURSOR_LEFT", new Integer(KeyEvent.VK_LEFT));
		keyMap.put("CURSOR_RIGHT", new Integer(KeyEvent.VK_RIGHT));
		keyMap.put("CURSOR_UP", new Integer(KeyEvent.VK_UP));

		keyMap.put("CHANNEL_DOWN", new Integer(HRcEvent.VK_CHANNEL_DOWN));
		keyMap.put("CHANNEL_UP", new Integer(HRcEvent.VK_CHANNEL_UP));

		keyMap.put("VOLUME_DOWN", new Integer(HRcEvent.VK_VOLUME_DOWN));
		keyMap.put("VOLUME_UP", new Integer(HRcEvent.VK_VOLUME_UP));

		keyMap.put("ENTER", new Integer(HRcEvent.VK_ENTER));
		keyMap.put("OK", new Integer(HRcEvent.VK_ENTER));

		keyMap.put("RED", new Integer(HRcEvent.VK_COLORED_KEY_0));
		keyMap.put("GREEN", new Integer(HRcEvent.VK_COLORED_KEY_1));
		keyMap.put("YELLOW", new Integer(HRcEvent.VK_COLORED_KEY_2));
		keyMap.put("BLUE", new Integer(HRcEvent.VK_COLORED_KEY_3));

		keyMap.put("POWER", new Integer(HRcEvent.VK_POWER));
		keyMap.put("REWIND", new Integer(HRcEvent.VK_REWIND));
		keyMap.put("STOP", new Integer(HRcEvent.VK_STOP));
		keyMap.put("EJECT", new Integer(HRcEvent.VK_EJECT_TOGGLE));
		keyMap.put("PLAY", new Integer(HRcEvent.VK_PLAY));
		keyMap.put("RECORD", new Integer(HRcEvent.VK_RECORD));

		/*
		 * public static final int VK_FAST_FWD = VK_RECORD + 1; public static final
		 * int VK_PLAY_SPEED_UP = VK_FAST_FWD + 1; public static final int
		 * VK_PLAY_SPEED_DOWN = VK_PLAY_SPEED_UP + 1; public static final int
		 * VK_PLAY_SPEED_RESET = VK_PLAY_SPEED_DOWN + 1; public static final int
		 * VK_RECORD_SPEED_NEXT = VK_PLAY_SPEED_RESET + 1; public static final int
		 * VK_GO_TO_START = VK_RECORD_SPEED_NEXT + 1; public static final int
		 * VK_GO_TO_END = VK_GO_TO_START + 1; public static final int VK_TRACK_PREV =
		 * VK_GO_TO_END + 1; public static final int VK_TRACK_NEXT = VK_TRACK_PREV +
		 * 1; public static final int VK_RANDOM_TOGGLE = VK_TRACK_NEXT + 1; public
		 * static final int VK_CHANNEL_UP = VK_RANDOM_TOGGLE + 1; public static
		 * final int VK_CHANNEL_DOWN = VK_CHANNEL_UP + 1; public static final int
		 * VK_STORE_FAVORITE_0 = VK_CHANNEL_DOWN + 1; public static final int
		 * VK_STORE_FAVORITE_1 = VK_STORE_FAVORITE_0 + 1; public static final int
		 * VK_STORE_FAVORITE_2 = VK_STORE_FAVORITE_1 + 1; public static final int
		 * VK_STORE_FAVORITE_3 = VK_STORE_FAVORITE_2 + 1; public static final int
		 * VK_RECALL_FAVORITE_0 = VK_STORE_FAVORITE_3 + 1; public static final int
		 * VK_RECALL_FAVORITE_1 = VK_RECALL_FAVORITE_0 + 1; public static final int
		 * VK_RECALL_FAVORITE_2 = VK_RECALL_FAVORITE_1 + 1; public static final int
		 * VK_RECALL_FAVORITE_3 = VK_RECALL_FAVORITE_2 + 1; public static final int
		 * VK_CLEAR_FAVORITE_0 = VK_RECALL_FAVORITE_3 + 1; public static final int
		 * VK_CLEAR_FAVORITE_1 = VK_CLEAR_FAVORITE_0 + 1; public static final int
		 * VK_CLEAR_FAVORITE_2 = VK_CLEAR_FAVORITE_1 + 1; public static final int
		 * VK_CLEAR_FAVORITE_3 = VK_CLEAR_FAVORITE_2 + 1; public static final int
		 * VK_SCAN_CHANNELS_TOGGLE = VK_CLEAR_FAVORITE_3 + 1; public static final
		 * int VK_PINP_TOGGLE = VK_SCAN_CHANNELS_TOGGLE + 1; public static final int
		 * VK_SPLIT_SCREEN_TOGGLE = VK_PINP_TOGGLE + 1; public static final int
		 * VK_DISPLAY_SWAP = VK_SPLIT_SCREEN_TOGGLE + 1; public static final int
		 * VK_SCREEN_MODE_NEXT = VK_DISPLAY_SWAP + 1; public static final int
		 * VK_VIDEO_MODE_NEXT = VK_SCREEN_MODE_NEXT + 1; public static final int
		 * VK_VOLUME_UP = VK_VIDEO_MODE_NEXT + 1; public static final int
		 * VK_VOLUME_DOWN = VK_VOLUME_UP + 1; public static final int VK_MUTE =
		 * VK_VOLUME_DOWN + 1; public static final int VK_SURROUND_MODE_NEXT =
		 * VK_MUTE + 1; public static final int VK_BALANCE_RIGHT =
		 * VK_SURROUND_MODE_NEXT + 1; public static final int VK_BALANCE_LEFT =
		 * VK_BALANCE_RIGHT + 1; public static final int VK_FADER_FRONT =
		 * VK_BALANCE_LEFT + 1; public static final int VK_FADER_REAR =
		 * VK_FADER_FRONT + 1; public static final int VK_BASS_BOOST_UP =
		 * VK_FADER_REAR + 1; public static final int VK_BASS_BOOST_DOWN =
		 * VK_BASS_BOOST_UP + 1; public static final int VK_INFO =
		 * VK_BASS_BOOST_DOWN + 1; public static final int VK_GUIDE = VK_INFO + 1;
		 * public static final int VK_TELETEXT = VK_GUIDE + 1; public static final
		 * int VK_SUBTITLE = VK_TELETEXT + 1; public static final int RC_LAST =
		 * VK_SUBTITLE;
		 */
	}

	public static CodeMap getInstance() {
		if (_instance == null) {
			_instance = new CodeMap();
		}
		return _instance;
	}

	public int getCode(String codeStr) {
		Integer code;

		code = (Integer)keyMap.get(codeStr);
		if (code != null) {
			return code.intValue();
		}
		else {
			return -1;
		}
	}
	
	public Set<Entry<String, Integer>> cloneMap(){
		return keyMap.entrySet();
	}
}