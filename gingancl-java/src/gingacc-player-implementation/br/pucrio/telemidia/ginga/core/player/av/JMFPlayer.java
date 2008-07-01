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

package br.pucrio.telemidia.ginga.core.player.av;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;

import javax.media.CachingControlEvent;
import javax.media.CannotRealizeException;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataStarvedEvent;
import javax.media.DurationUpdateEvent;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaEvent;
import javax.media.MediaTimeSetEvent;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RestartingEvent;
import javax.media.StartEvent;
import javax.media.StopAtTimeEvent;
import javax.media.StopByRequestEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;

import br.org.ginga.core.player.IPlayerListener;
import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

public class JMFPlayer extends DefaultPlayerImplementation implements ControllerListener {
	public static final short AUDIO_PLAYER_TYPE = 0;
	public static final short VIDEO_PLAYER_TYPE = 1;
	
	public static final String SOUNDLEVEL_PROPERTY = "soundLevel";
	public static final String RATE_PROPERTY = "rate";
	public static final String DURATION_PROPERTY = "explicitDur";
	
	
	private Player player;
	
	public JMFPlayer(URL contentURL, short type) {
		super(contentURL);
		this.setSurface(GFXManager.getInstance().createSurface(""));
		
		try {
			player = Manager.createRealizedPlayer(contentURL);
		} catch (NoPlayerException e) {
			System.err.println("[ERR] Unable to handle content of \"" + contentURL 
					+"\"." + e.getMessage());
		} catch (IOException e) {
			System.err.println("[ERR] Error readinf content \"" + contentURL 
					+"\"." + e.getMessage());
		} catch (CannotRealizeException e) {
			System.err.println("[ERR] Error realizing player \"" + contentURL 
					+"\"." + e.getMessage());
		}
		player.addControllerListener(this);
		player.prefetch();
		if(type ==  VIDEO_PLAYER_TYPE)
			player.getVisualComponent().addComponentListener(new ComponentHandler());
	}

	public void eventStateChanged(String id, short type, short transition,
			int code) {
	}

	public String getPropertyValue(String name) {
		if(name.equals(SOUNDLEVEL_PROPERTY)){
			if(player != null)
				return Float.toString(player.getGainControl().getLevel());
		}else if(name.equals(RATE_PROPERTY)){
			return Float.toString(player.getRate());
		}else if(name.equals(DURATION_PROPERTY)){
			try{
				double duration = player.getDuration().getSeconds() * 1000;
				return Double.toString(duration);
			} catch (Exception exc) {
				// System.out.println("Error: " + exc);
				return null;
			}
		}
		return null;
	}

	public void setPropertyValue(String name, String value) {
		if(name.equals(SOUNDLEVEL_PROPERTY)){
			if(player != null)
				try{
					player.getGainControl().setLevel(Float.parseFloat(value));
				}catch(NumberFormatException ex){
					System.err.println("[ERR] Could not set sound level: " + ex.getMessage());
				}
		}else if(name.equals(RATE_PROPERTY)){
			try{
				player.setRate(Float.parseFloat(value));
			}catch(NumberFormatException ex){
				System.err.println("[ERR] Could not set playing rate: " + ex.getMessage());
			}
		}
	}

	@Override
	public double getMediaTime() {
		if(player != null)
			return player.getMediaTime().getSeconds();
		else
			return -1;
	}

	@Override
	public void pause() {
		player.stop();
		super.pause();
	}

	@Override
	public void play() {
		this.getSurface().setSurface(player.getVisualComponent());
		player.start();
		super.play();
	}

	@Override
	public void resume() {
		player.start();
		super.resume();
	}

	@Override
	public void stop() {
		/*running = false;
		elapsedTime = 0;
		elapsedPause = 0;*/
		player.stop();
		this.getSurface().clear();
		super.stop();
	}
	
	public void close() {
		//player.removeControllerListener(this);
		player.deallocate();
		player.close();
		//super.close();
	}

	public void controllerUpdate(ControllerEvent jmfEvent) {
		if(jmfEvent instanceof EndOfMediaEvent
				|| jmfEvent instanceof StopAtTimeEvent) {
			this.notifyListeners(IPlayerListener.PL_NOTIFY_STOP, "");
			//this.forceNaturalEnd();
		}else if (jmfEvent instanceof PrefetchCompleteEvent) {
			//this.notifyListeners(IPlayerListener., parameter)
		}else if (jmfEvent instanceof StartEvent) {
			// presentation started
			/*if (hasResumed) {
				hasResumed = false;
			}
			else {
				super.start(object, playerObj);
			}*/
		}else if (jmfEvent instanceof StopByRequestEvent) {
			// ocorre como consequencia de uma acao JMF stop.
			// isso pode ser consequencia de uma acao NCM termina ou suspende, ou uma
			// interacao do usuario
			// System.out.println("Stop by request!");
			/*if (hasPaused) {
				hasPaused = false;
			}
			else if (hasAborted) {
				super.abort(object, playerObj);
			}
			else {
				super.stop(object, playerObj);
				// super.unprepare(object);
			}*/
		}else if (jmfEvent instanceof RestartingEvent) {
			// Restarting Event - Ocorre quando o usuario altera o ponto de exibicao
			// atraves de interacao com a barra de tempo do controle
		}else if (jmfEvent instanceof RateChangeEvent) {
			// RateChangeEvent - Ocorre quando a taxa de exibiï¿½ï¿½o da mï¿½dia ï¿½
			// alterada

			// Acorda o monitor de ï¿½ncoras quando a taxa de exibiï¿½ï¿½o ï¿½
			// alterada,
			// para que ele recalcule os parï¿½metros granularity e sleep factor.
			/*
			 * if (nominalMonitor != null && nominalMonitor.isAlive()) { //
			 * monitor.setUpdateParamsFlag(); // nominal_monitor.interrupt(); }
			 */
		}else if (jmfEvent instanceof ControllerErrorEvent) {
			// ControllerErrorEvent - Ocorre quando o player encontra um erro em algum
			// ponto (possivelmente no stream de dados) e nao pode se recuperar do
			// erro.
		}else if (jmfEvent instanceof DurationUpdateEvent) {
			// DurationUpdateEvent - Ocorre quando a duraï¿½ï¿½o do player ï¿½
			// alterada
			// Time t = ((DurationUpdateEvent)jmfEvent).getDuration();
		}

		else if (jmfEvent instanceof MediaTimeSetEvent) {
			// Usuario modificou o instante corrente de apresentacao do objeto
			// System.out.println("EVENT: Media Time Set");
			/*thi
			playerObj.changeCurrentTime();*/
		}

		// Caching control.
		else if (jmfEvent instanceof CachingControlEvent) {
			// System.out.println("EVENT: Caching Control");
		}
		else if (jmfEvent instanceof DataStarvedEvent) {
			// System.out.println("EVENT: Data Starved");
		}
		else if (jmfEvent instanceof StopTimeChangeEvent) {
			// System.out.println("EVENT: Stop Time Change");
		} else {
			// Catch implementation specific events here...

			// UnsupportedFormatEvent is not a part of the JMF 1.0 spec.
			// It is generates when the media contains something that is not
			// supported by the underlying framework. For example, a QuickTime
			// movie with a VR track, an unsupported codec, etc.
			try {
				Class ufEvent = Class.forName("com.sun.media.UnsupportedFormatEvent");
				if (ufEvent.isInstance(jmfEvent)) {
					System.out.println("EVENT: UnsupportedFormat");
					System.err.println("HF_Viewer: UnsupportedFormatEvent ");
					System.err.println("   Reason: " + ((MediaEvent)jmfEvent).toString());
				}
			}
			catch (ClassNotFoundException e) {
			}

			// SizeChangeEvent is not a part of the JMF 1.0 spec. It is
			// generated when the size of the video changes or right at the
			// beginning of a video clip, to inform listeners about the
			// dimensions of the video
			try {
				Class scEvent = Class.forName("com.sun.media.SizeChangeEvent");
				if (scEvent.isInstance(jmfEvent)) {
					// System.out.println("EVENT: Size Change");
				}
			}
			catch (ClassNotFoundException e) {
			}
		}
	}
	
	private class ComponentHandler extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			Component source;

			if (e.getSource() instanceof Component) {
				source = (Component)e.getSource();
				source.removeComponentListener(this);
				source.setBounds(0, 0, 
						((Component)JMFPlayer.this.getSurface().getSurface()).getWidth(), 
						((Component)JMFPlayer.this.getSurface().getSurface()).getHeight());
				source.addComponentListener(this);
			}
		}
	}

	@Override
	public void setScope(String scope, double begin, double end) {
		super.setScope(scope, begin, end);
			if (scopeInitTime > 0) {
				player.setMediaTime(new Time(scopeInitTime));
			}

			if (scopeEndTime > 0) {
				player.setStopTime(new Time(scopeEndTime));
			}
	}
}
