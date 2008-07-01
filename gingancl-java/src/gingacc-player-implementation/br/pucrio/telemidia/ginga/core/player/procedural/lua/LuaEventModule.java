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

package br.pucrio.telemidia.ginga.core.player.procedural.lua;

import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.Map.Entry;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

import br.org.ginga.core.player.IPlayer;
import br.org.ginga.core.player.IPlayerListener;
import br.pucrio.telemidia.ginga.core.io.CodeMap;


public class LuaEventModule {
	private static int LUAEVENT_REFQUEUE       = -3;
	private static int LUAEVENT_REFLISTENERS   = -4;
	private static int LUAEVENT_REFNEWLISTENERS= -5;
	private static int LUAEVENT_REFINPUTEVT    = -6;
	private static int LUAEVENT_REFINPUTMAP    = -7;
	private static int LUAEVENT_REFNCLEVT      = -8;
	private static int LUAEVENT_REFNCLMAP      = -9;
	
	private static final int LUA_ENVIRONINDEX = -10001;
	
	private final long initialTime;
	private Set<Entry<String,Integer>> mappedKeys;
	
	private LuaEventModule(LuaState L) throws LuaException{
		// env = {}
		L.newTable();					// [ env ]
		L.replace(LUA_ENVIRONINDEX);	// [ ]
		
		// env[QUEUE] = {}
		L.newTable();										// [ queue ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFQUEUE);	// [ ]
		
		// env[LISTENERS] = {}
		L.newTable();										// [ listeners ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFLISTENERS);	// [ ]
		
		// env[INPUTEVT] = {}
		L.newTable();										//[ inputevt ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFINPUTEVT);	//[ ]
		
		// env[INPUTMAP] = {}
		//int_DFBInputEventMAP(L);                          // [ inputmap ]
		createInputEventMap(L);								// [ inputmap ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFINPUTMAP);	// [ ]
		
		// env[NCLEVT] = {}
		L.newTable();										//[ nclevt ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFNCLEVT);	//[ ]
		
		// env[NCLMAP] = {
		//     starts  = PL_NOTIFY_START,
		//     stops   = PL_NOTIFY_STOP,
		//     pauses  = PL_NOTIFY_PAUSE,
		//     resumes = PL_NOTIFY_RESUME,
		//     aborts  = PL_NOTIFY_ABORT,
		// }
		L.newTable();                                    // [ nclmap ]
		L.pushNumber(IPlayerListener.PL_NOTIFY_START);   // [ nclmap | START ]
		L.setField(-2, "starts");                        // [ nclmap ]
		L.pushNumber(IPlayerListener.PL_NOTIFY_STOP);    // [ nclmap | STOP ]
		L.setField(-2, "stops");                         // [ nclmap ]
		L.pushNumber(IPlayerListener.PL_NOTIFY_PAUSE);   // [ nclmap | PAUSE ]
		L.setField(-2, "pauses");                        // [ nclmap ]
		L.pushNumber(IPlayerListener.PL_NOTIFY_RESUME);  // [ nclmap | RESUME ]
		L.setField(-2, "resumes");                       // [ nclmap ]
		L.pushNumber(IPlayerListener.PL_NOTIFY_ABORT);   // [ nclmap | ABORT ]
		L.setField(-2, "aborts");                        // [ nclmap ]
		L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFNCLMAP); // [ ]
		
		
		//[]
		L.newTable(); 
		L.pushValue(-1); //[evt]
		L.setGlobal("event");
		
		L.pushString("sleep");  //  [evt | "sleep"]
		L.pushJavaFunction(new L_Sleep(L)); //[evt | "sleep" | func]
		L.setTable(-3);  // [evt]
		
		/*L.pushString("wake");
		L.pushJavaFunction(new L_Wake(L));
		L.setTable(-3);*/
		L.pushString("timer");
		L.pushJavaFunction(new L_Timer(L));
		L.setTable(-3);
		
		L.pushString("post");
		L.pushJavaFunction(new L_Post(L));
		L.setTable(-3);
		
		L.pushString("expired");
		L.pushJavaFunction(new L_Expire(L));
		L.setTable(-3);
		
		initialTime = System.currentTimeMillis();
		L.pushString("uptime");
		L.pushJavaFunction(new L_Uptime(L));
		L.setTable(-3);
		
		L.pushString("register");
		L.pushJavaFunction(new L_Register(L));
		L.setTable(-3);
		
		L.pushString("unregister");
		L.pushJavaFunction(new L_Unregister(L));
		L.setTable(-3);
		
		L.pushString("dispatch");
		L.pushJavaFunction(new L_Dispatch(L));
		L.setTable(-3);
	}
	
	public static int nclEventToTable(LuaState L, String clazz, String type, String param1, String param2){
		// [ ... ]
		L.newTable();                       // [ ... | evt ]
		L.pushString(clazz);              // [ ... | evt | clazz ]
		L.setField(-2, "class");          // [ ... | evt ]

		// NCL event
		if(clazz.equals("ncl"))
		{
			L.pushString(type);           // [ ... | evt | type ]
			L.setField(-2, "type");       // [ ... | evt ]

			// PRESENTATION event
			if (type.equals("presentation"))
			{
				L.pushString(param1);     // [ ... | evt | action]
				L.setField(-2, "action"); // [ ... | evt ]
				L.pushString(param2);     // [ ... | evt | area ]
				L.setField(-2, "area");   // [ ... | evt ]
				return 1;
			}
		}
		return L.LargError(1, "Invalid event.");//luaL_error(L, "invalid event");
	}
	
	private int createInputEventMap(LuaState L){
		/*// [ ... ]
		L.createTable(255, 0);			// [ ... | map ]
		L.pushString("VK_ESCAPE");
		L.rawSetI(-2, KeyEvent.VK_ESCAPE);
		L.pushString("VK_SPACE");
		L.rawSetI(-2, KeyEvent.VK_SPACE);
		L.pushString("VK_PLUS");
		L.rawSetI(-2, KeyEvent.VK_PLUS);
		L.pushString("VK_LEFT");
		L.rawSetI(-2, KeyEvent.VK_LEFT);
		L.pushString("VK_RIGHT");
		L.rawSetI(-2, KeyEvent.VK_RIGHT);
		L.pushString("VK_UP");
		L.rawSetI(-2, KeyEvent.VK_UP);
		L.pushString("VK_DOWN");
		L.rawSetI(-2, KeyEvent.VK_DOWN);*/
		
		// [ ... ]
		L.newTable();            // [ ... | map ]

		Set<Entry<String,Integer>> entries = CodeMap.getInstance().cloneMap();
		
		for(Entry<String, Integer> entry : entries){
			// t[code] = 'VK_*'
			L.pushString(entry.getKey());				// [ ... | map | 'VK_*' ]
			L.rawSetI(-2, entry.getValue().intValue());	// [ ... | map ]
		}
/*
		// TODO: hardcoded
		// F1=RED, F2=GREEN, F3=YELLOW, F4=BLUE
		lua_pushstring(L, "RED");                   // [ ... | map | 'RED' ]
		lua_rawseti(L, -2, 61697);                  // [ ... | map ]
		lua_pushstring(L, "GREEN");                 // [ ... | map | 'GREEN' ]
		lua_rawseti(L, -2, 61698);                  // [ ... | map ]
		lua_pushstring(L, "YELLOW");                // [ ... | map | 'YELLOW' ]
		lua_rawseti(L, -2, 61699);                  // [ ... | map ]
		lua_pushstring(L, "BLUE");                  // [ ... | map | 'BLUE' ]
		lua_rawseti(L, -2, 61670);                  // [ ... | map ]
*/
		// [ ... | map ]
		return 1;
	}
	
	private int inputEventTotable (LuaState L, KeyEvent evt)
	{
		// [ ... ]
		L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFINPUTEVT); // [ ... | evt ]

		// evt.class = 'key'
		L.pushString("key");                       // [ ... | evt | class ]
		L.setField(-2, "class");                   // [ ... | evt ]

		// evt.type = 'press' or 'release'
		// TEMP: optimize
		//evt.
		if(evt.getID() == KeyEvent.KEY_RELEASED)
			L.pushString("release");               // [ ... | evt | 'release' ]
		else if (evt.getID() == KeyEvent.KEY_PRESSED || 
				evt.getID() == KeyEvent.KEY_TYPED)
			L.pushString("press");                 // [ ... | evt | 'press' ]
		else
			L.pushString("press");                 // [ ... | evt | 'press' ]
		L.setField(-2, "type");                    // [ ... | evt ]

		// evt.key = 'VK_*'
		/*L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFINPUTMAP); // [ ... | evt | map ]
		L.rawGetI(-1,evt.getKeyCode());            // [ ... | evt | map | "key" ]
		L.setField(-3, "key");                     // [ ... | evt | map ]
		L.pop(1);                                  // [ ... | evt ]*/
		if(mappedKeys == null){
			mappedKeys = CodeMap.getInstance().cloneMap();
		}
		for(Entry<String,Integer> entry : mappedKeys){
			int keyCode = evt.getKeyCode();
			if(entry.getValue().intValue() == keyCode){
				L.pushString(entry.getKey());
				L.setField(-2, "key");
				break;
			}
		}

		return 1;
	}
	
	public static int open(LuaState L){
		try {
			LuaEventModule envetAPI = new LuaEventModule(L);
		} catch (LuaException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	private class L_Sleep extends JavaFunction{

		public L_Sleep(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			return super.L.yield(0);
		}
	}
	
	private class L_Post extends JavaFunction{

		public L_Post(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			// [ dst | evt ]
			//System.err.println("CHICAO");
			String dst = L.LcheckString(2);

			// dst == "in"
			if ( dst.equals("in") )
			{
				// [ dst | evt ]
				if(super.L.type(-1) == LuaState.LUA_TLIGHTUSERDATA || super.L.type(-1) == LuaState.LUA_TUSERDATA){
					KeyEvent event = (KeyEvent)super.L.toJavaObject(-1);
					super.L.pop(-1);                       // [ dst ]
					inputEventTotable(super.L, event);     // [ dst | evt ]
				}

				// QUEUE[#QUEUE+1] = evt
				super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFQUEUE);  // [ dst | evt | queue ]
				super.L.pushValue(-2);                         // [ dst | evt | queue | evt ]
				super.L.rawSetI(-2, L.objLen(-2)+1);       // [ dst | evt | queue ]

				// ?  ?
				//LuaPlayer.getPlayer(super.L).unlockConditionSatisfied();
				//GETPLAYER(L)->unlockConditionSatisfied();?
			}

			// dst == "out"
			else if ( dst.equals("out") )
			{
				super.L.LcheckType(3, LuaState.LUA_TTABLE);
				super.L.getField(3, "class");              // [ dst | evt | class ]
				String clazz = super.L.toString(-1);

				// NCL event
				if ( clazz.equals("ncl") )
				{
					super.L.getField(3, "type");           // [ dst | evt | class | type ]
					String type = super.L.toString(-1);
					if ( type.equals("presentation") )
					{
						super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFNCLMAP);  // [ dst | evt | class | type | transitions ]
						super.L.getField(3, "transition"); // [ dst | evt | class | type | transitions | transition ]
						super.L.getTable(-2);              // [ dst | evt | class | type | transitions | TRANSITION ]
						super.L.getField(3, "area");       // [ dst | evt | class | type | transitions | TRANSITION | area ]
						if (super.L.isNil(-1)) {
							super.L.pop(1);                // [ dst | evt | class | type | transitions | TRANSITION ]
							super.L.pushString("");        // [ dst | evt | class | type | transitions | TRANSITION | "" ]
						}
						// ?  ?
						LuaPlayer.getPlayer(super.L).notifyListeners((short)L.toInteger(-2),
								L.toString(-1));
						/*short a = (short)L.toInteger(-2);
						String s = L.toString(-1);
						IPlayer p = LuaPlayer.getPlayer(super.L);
						p.notifyListeners(a, s);*/
						//GETPLAYER(L)->notifyListeners(lua_tointeger(L, -2), lua_tostring(L, -1));
					}
				}
				else
					return super.L.error();//luaL_error(L, "invalid event class");
			}

			else
				return super.L.LargError(1, "possible values are: 'in', 'out'");

			return 0;
		}
		
	}
	
	private class L_Uptime extends JavaFunction{

		public L_Uptime(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			long uptime = System.currentTimeMillis() - initialTime;
			super.L.pushNumber(uptime);
			super.L.pushNumber(LuaPlayer.getPlayer(super.L).getMediaTime()); // [ msec ]
			return 1;
		}
	}
	
	private class L_Register extends JavaFunction{

		public L_Register(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			/*for(int i=1;i<=L.getTop();i++){
				System.err.println("i("+i+" -> " + L.type(i) );
			}
			return 0;*/
			// [ J | func ]
			super.L.LcheckType(2, LuaState.LUA_TFUNCTION);

			// listeners[#listeners+1] = func
			super.L.rawGetI(LUA_ENVIRONINDEX,LUAEVENT_REFLISTENERS);  // [ J | func | listeners ]
			super.L.pushValue(2);                              // [ J | func | listeners | func ]
			super.L.rawSetI(-2, super.L.objLen(-2)+1);           // [ J | func | listeners ]

			// listeners[#listeners+1] = func
			super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFNEWLISTENERS);  // [ func | newlisteners ]
			if (!super.L.isNil(-1)) {
				super.L.pushValue(2);                             // [ func | listeners | func ]
				super.L.rawSetI(-2, super.L.objLen(-2)+1);          // [ func | listeners ]
			}
			else
				super.L.pop(1);

			return 0;
		}
	}
	
	private class L_Expire extends JavaFunction{

		public L_Expire(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {	
			// [ ref ]
			int ref = L.LcheckInteger(2);
			super.L.rawGetI(LUA_ENVIRONINDEX, ref);  // [ ref | func ]
			super.L.LunRef(LUA_ENVIRONINDEX, ref);
			super.L.call(0, 0);
			return 0;
		}
	}
	
	private class Timer{
		LuaPlayer player;
		int ref;
		int time;
	}
	
	private class SleepFunction implements Runnable{
		private Timer timer;
		
		public SleepFunction(Timer timer){
			this.timer = timer;
		}
		public void run() {
			try {
				Thread.sleep(timer.time);
				timer.player.timerExpired(timer.ref);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			//timer.player.timerExpired(timer.ref);			
		}
	}
	
	private class L_Timer extends JavaFunction{

		public L_Timer(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {	
			// [ msec, func ]
			Timer timer = new Timer();

			// saves a reference to the function for later calling
			timer.time = super.L.LcheckInteger(2);
			super.L.LcheckType(3, LuaState.LUA_TFUNCTION);
			timer.ref = super.L.Lref(LUA_ENVIRONINDEX);          // [ msec ]
			timer.player = LuaPlayer.getPlayer(L);
			
			Thread t = new Thread(new SleepFunction(timer));
			t.start();
			return 0;
		}
	}
	
	private class L_Unregister extends JavaFunction{

		public L_Unregister(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			
			// [ func ]
			super.L.LcheckType(2, LuaState.LUA_TFUNCTION);

			super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFLISTENERS);  // [ func | listeners ]
			super.L.newTable();                                  // [ func | listeners | newtable ]

			int ret = 0;
			int top = 0;
			int len = super.L.objLen(-2);
			for (int i=1; i<=len; i++) {
				super.L.rawGetI(-2, i);                        // [ func | listeners | newtable | v[i] ]
				if (super.L.equal(1, -1) == 0) {
					super.L.rawSetI(-2, ++top);                // [ func | listeners | newtable ]
					ret = 1;
				}
				else
					super.L.pop(1);                            // [ func | listeners | newtable ]
			}

			super.L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFNEWLISTENERS); // [ func | listeners ]
			super.L.pushBoolean(ret==0?false:true);                          // [ func | listeners | ret ]
			return 1;
		}
	}
	
	private class L_Dispatch extends JavaFunction{

		public L_Dispatch(LuaState arg0) {
			super(arg0);
		}

		public int execute() throws LuaException {
			// [ ]
			
			// push old QUEUE and get its size
			super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFQUEUE);             // [ queue ]
			int evts = super.L.objLen(-1);

			// new QUEUE = {}
			super.L.newTable();                                         // [ queue | newqueue ]
			super.L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFQUEUE);             // [ queue ]

			// handle events in old QUEUE
			for (int evt=1; evt<=evts; evt++)
			{
				// iterate over all listeners and call each of them
				super.L.rawGetI(-1, evt);                             // [ queue | evt ]
				super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFLISTENERS);     // [ queue | evt | listeners ]
				int len = super.L.objLen(-1);
				for (int i=1; i<=len; i++) {
					super.L.rawGetI(-1, i);                           // [ queue | evt | listeners | func ]
					super.L.pushValue(-3);                            // [ queue | evt | listeners | func | evt ]
					super.L.call(1, 0);                               // [ queue | evt | listeners ]
				}
				super.L.pop(2);                                       // [ queue ]

				// check if listeners has been changed
				super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFNEWLISTENERS);  // [ queue | newlisteners ]
				if (!super.L.isNil(-1)) {
					super.L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFLISTENERS); // [ queue ]
					super.L.pushNil();                                  // [ queue | nil ]
					super.L.rawSetI(LUA_ENVIRONINDEX, LUAEVENT_REFNEWLISTENERS); // [ queue ]
				} else
					super.L.pop(1);                                   // [ queue ]
			}
			super.L.pop(1);                                           // [ ]

			// check for more in new QUEUE
			super.L.rawGetI(LUA_ENVIRONINDEX, LUAEVENT_REFQUEUE);             // [ newqueue ]
			super.L.pushBoolean(super.L.objLen(-1)==0?false:true);                    // [ newqueue | more ]
			super.L.remove(-2);                                       // [ more ]
			return 1;
		}
	}
}
