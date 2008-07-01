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

package br.pucrio.telemidia.ginga.core.player.procedural.lua;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import br.org.ginga.core.io.InputEvent;
import br.org.ginga.core.io.InputEventListener;
import br.org.ginga.core.player.IPlayer;
import br.org.ginga.core.player.IPlayerListener;
import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.io.InputEventManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

public class LuaPlayer extends DefaultPlayerImplementation implements InputEventListener{


	private LuaState L;
	String currentScope;
	Map<String, ScopeInfo> scopeMap;
	boolean played;
	boolean running;
	
	private String source;
	
	private static int dipatcherID=-1;
	private Dispatcher dispatcher;
	
	private LuaAWTComponent luaAWTComponent;
	
	private static String LUAPLAYER_PLAYER = "luaplayer.Player";
	private static int LUA_EVENTINDEX = 1;
	
	
	public LuaPlayer(URL contentURL) throws Exception {
		super(contentURL);
		
		dispatcher = new Dispatcher();
		source = contentURL.getPath();
		
		this.setSurface(GFXManager.getInstance().createSurface("LuaSurface"));
		this.currentScope = "";
		this.scopeMap = new HashMap<String, ScopeInfo>();
		
		L = LuaStateFactory.newLuaState();

		L.pushJavaObject(this);         // [ LuaPlayer* ]
		L.setField(LuaState.LUA_REGISTRYINDEX, LUAPLAYER_PLAYER);    // [ ]
		
		L.openLibs();
		luaAWTComponent = new LuaAWTComponent(L);
		this.getSurface().setSurface(luaAWTComponent);
		
	}
	
	@Override
	public boolean hasPresented() {
		return !played;
	}
	
	public static LuaPlayer getPlayer(LuaState L){
		// [ ... ]
		L.getField(LuaState.LUA_REGISTRYINDEX, LUAPLAYER_PLAYER);    // [ ... | LuaPlayer* ]
		LuaPlayer player;
		try {
			player = (LuaPlayer) L.toJavaObject(-1);
		} catch (LuaException e) {
			e.printStackTrace();
			return null;
		}
		L.pop(1);                                           // [ ... ]
		return player;
	}
	
	public void setScope(String scopeId, double begin, double end) {
		addScope(scope, begin, end);
	}

	public void addScope(String scopeId, double begin, double end) {
		if(!scopeMap.containsKey(scopeId)){
			ScopeInfo newScope = new ScopeInfo();
			newScope.scopeId = scopeId;
			newScope.initTime = begin;
			newScope.endTime = end;
			scopeMap.put(scopeId, newScope);
		}
	}
	public void setCurrentScope(String scopeId) {
		this.currentScope = scopeId;
	}
	
	public void play(){
		super.play();

		try {
			L.pushJavaFunction(new LuaOpen_Event(L));
		} catch (LuaException e) {
			System.err.println("[ERR] Unnable to use Canvas library:" + e.getMessage());
		}
		L.call(0,1);
		
		
		luaAWTComponent.repaint();
		try {
			L.pushObjectValue(luaAWTComponent.getCanvas());
		} catch (LuaException e) {
			e.printStackTrace();
			return;
		}
		L.setGlobal("mainCanvas");
		int err = 0;
	    err = L.LloadFile("./lib/ext/canvas.lua");
	    if (err == 0)
	      err = L.pcall(0, 0, 0);
	    if(err != 0)
	    {
	      String error = L.toString(-1);
	      L.pop(1);
	      System.err.println("Erro carregando script inicial: "+ error);
	    }
	    int slash = source.lastIndexOf("/");
	    if(slash==-1)
	    	slash= source.lastIndexOf("\\");
	    if(slash!=-1){
	    	int res = L.LdoString("package.path=package.path..\";"+source.substring(0, slash+1)+"?.lua\"");
	    	if(res != 0 ){
				System.err.println("[ERR] #"+res+L.toString(-1));
			}
	    }
		
	    synchronized (L) {
	    	L.getField(LUA_EVENTINDEX, "post");    // [ event.post ]
			L.pushString("in");                    // [ event.post | "in" ]
			LuaEventModule.nclEventToTable(this.L, "ncl", "presentation",
			                "start", currentScope);   // [ event.post | "in" | evt ]
			L.call(2, 0);                          // [ ]
		}
		while(((Component)this.getSurface()).getParent()==null); //HACK: As vezes o scrip comeca
																 //e asurface ainda nao tem pai,
																 //ou sejan, ainda nao foi 
																 //redimensionada.
	    if(this.currentScope==""){
	    	synchronized (L) {
				int res = L.LdoFile(source);
				if(res != 0 ){
					this.notifyListeners(IPlayerListener.PL_NOTIFY_ABORT, "");
					System.err.println("[ERR] #"+res+L.toString(-1));
					return;
				}
	    	}
			this.dispatcher.start();
	    }
	    InputEventManager.getInstance().addInputEventListener(this, new HashSet<Integer>());
	}
	
	public void stop(){
		InputEventManager.getInstance().removeInputEventListener(this);
		dispatcher.stop();
		this.getSurface().clear();
	}
	
	public void pause(){
		
	}
	
	public void resume(){
		
	}
	
	public String getPropertyValue(String name) {
		return null;
	}
	
	public void setPropertyValue(String name, String value) {
		synchronized (L) {
			L.getGlobal(name);
			L.pushString(value);
			// is a function, call it
			if( L.type(-2) == LuaState.LUA_TFUNCTION )
				L.call(1, 0);                  // [ ]
			// otherwise, sets as global
			else {
				L.setGlobal(name);     // [ var ]
				L.pop(1);                      // [ ]
			}
		}
	}
	

	public void eventStateChanged(String id, short type, short transition,
			int code) {
		// TODO Auto-generated method stub
		
	}
	
	private class LuaOpen_Event extends JavaFunction {

		public LuaOpen_Event(LuaState arg0) {
			super(arg0);
		}

		@Override
		public int execute() throws LuaException {
			//luaCanvas = new LuaCanvasModule(super.L);
			LuaEventModule.open(L);
			return 1;
		}
		
	}

	public void userEventReceived(InputEvent ev) {
		if(ev instanceof KeyEvent){
			if(L!=null)
				synchronized (L) {
					L.getField(LUA_EVENTINDEX, "post");    // [ event.post ]
					L.pushString("in");                    // [ event.post | "in" ]
					L.pushJavaObject(ev);              // [ event.post | "in" | evt* ]
					L.call(2, 0);                          // [ ]
				}
		}
	}
	
	public class Dispatcher implements Runnable{
		private boolean running = false;
		private boolean canRun = false;
		
		private Thread thread;
		public void start(){
			this.canRun=true;
			thread = new Thread(this,"Dispatcher-"+(++LuaPlayer.dipatcherID));
			thread.start();
		}
		
		public void stop(){
			this.canRun = false;
			this.thread = null;
		}
		
		public boolean isRunning(){
			return running;
		}

		public synchronized void run(){
			running = true;
			try{
				while (this.canRun){
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(L!=null)
						synchronized (L) {
							L.getField(LUA_EVENTINDEX, "dispatch");    // [ dispatch ]
							L.pushValue(-1);                       // [ dispatch | dispatch ]
							L.call(0, 1);                          // [ dispatch | more ]
							boolean ret = L.toBoolean(-1);             // more events?
							L.pop(1);                              // [ dispatch ]
	
							if (!ret && this.running) {
								// TODO: se eu descomento a linha abaixo dá problema de concorrencia
								// cout << "[LUA] waiting " << ret << endl;
								//this->waitForUnlockCondition();
							}
						}
				}
				// [ event.dispatch | ret ]
				running=false;
				//cout << "[LUA] stopping " << this->mrl.c_str() << endl;
				if(L!=null)
					synchronized (L) {
						if(LuaPlayer.this.status == IPlayer.STOP){
							L.close();
							L=null;
						}
					}
				if (this.running)
					LuaPlayer.this.notifyListeners(IPlayerListener.PL_NOTIFY_STOP, "");
			}catch(Exception ex){
				ex.printStackTrace();
			}catch (Error e) {
				e.printStackTrace();
			}
			running=false;
		}
	}
	
	public String getRelativePath(){
		int slash = source.lastIndexOf("/");
	    if(slash==-1)
	    	slash= source.lastIndexOf("\\");
	    if(slash!=-1){
	    	return source.substring(0, slash);
	    }else
	    	return ".";
	}
	
	public void timerExpired (int ref) {
		if(L!=null)
			synchronized (L){
				L.getField(LUA_EVENTINDEX, "expired"); // [ event.expired ]
				L.pushNumber(ref);                     // [ event.expired | ref ]
				L.call(1, 0);                                // [ ]
			}
	}
	
	private class ScopeInfo{
		String scopeId;
		double initTime;
		double endTime;
		LuaState L;
	}
}
