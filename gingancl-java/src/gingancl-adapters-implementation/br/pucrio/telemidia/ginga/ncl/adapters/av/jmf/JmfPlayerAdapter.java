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
package br.pucrio.telemidia.ginga.ncl.adapters.av.jmf;

import br.org.ginga.ncl.model.presentation.ICascadingDescriptor;
import br.pucrio.telemidia.ginga.core.player.av.JMFPlayer;
import br.pucrio.telemidia.ginga.ncl.adapters.DefaultFormatterPlayerAdapter;

public abstract class JmfPlayerAdapter extends DefaultFormatterPlayerAdapter {

	@Override
	public double getObjectExpectedDuration() {
		String strDuration = player.getPropertyValue(JMFPlayer.DURATION_PROPERTY);
		if(strDuration == null){
			return super.getObjectExpectedDuration();
		}
		return Double.parseDouble(strDuration);
	}

	protected boolean hasVisual;

	protected boolean hasPaused, hasResumed, hasAborted;

	//private ComponentHandler componentHandler;

	public JmfPlayerAdapter(boolean visual) {
		super();
		hasVisual = visual;
	}
	
	public JmfPlayerAdapter() {
		this(true);
	}

	/*protected boolean abort(IExecutionObject object, IPlayerObject playerObj) {
		Player player;

		player = ((JmfPlayerObject)playerObj).getPlayer();

		if (hasVisual && super.display != null) {
			player.getVisualComponent().removeComponentListener(componentHandler);
			((Container)super.display).removeAll();
		}

		hasAborted = true;
		player.stop();
		return true;
	}

	protected boolean pause(IPlayerObject playerObj) {
		hasPaused = true;
		if (super.pause(playerObj)) {
			return true;
		}
		else {
			hasPaused = false;
			return false;
		}
	}

	protected boolean resume(IPlayerObject playerObj) {
		hasResumed = true;
		if (super.resume(playerObj)) {
			return true;
		}
		else {
			hasResumed = false;
			return false;
		}
	}*/

	/*public void updateStatus(IPlayerObject playerObj, IExecutionObject object,
			int transition, Object notificationObject) {

		ControllerEvent jmfEvent;

		if (!(notificationObject instanceof ControllerEvent)) {
			super.updateStatus(playerObj, object, transition, notificationObject);
		}

		jmfEvent = (ControllerEvent)notificationObject;
		if (jmfEvent instanceof PrefetchCompleteEvent) {
			// prefetch concluded
			super.prepare(object, playerObj);
		}

		else if (jmfEvent instanceof StartEvent) {
			// presentation started
			if (hasResumed) {
				hasResumed = false;
			}
			else {
				super.start(object, playerObj);
			}
		}

		else if (jmfEvent instanceof EndOfMediaEvent
				|| jmfEvent instanceof StopAtTimeEvent) {
			// media reached the natural (or pre-configured) end
			// System.out.println("End of media or Stop At Time!");
			super.naturalEnd(object);
			// super.unprepare(object);
		}

		// StopByRequestEvent - Ocorre como consequencia de uma acao JMF stop.
		// Isso pode ser consequencia de uma acao NCM termina ou suspende, ou uma
		// interaï¿½ï¿½o do usuï¿½rio
		else if (jmfEvent instanceof StopByRequestEvent) {
			// ocorre como consequencia de uma acao JMF stop.
			// isso pode ser consequencia de uma acao NCM termina ou suspende, ou uma
			// interacao do usuario
			// System.out.println("Stop by request!");
			//if (hasPaused) {
			//	hasPaused = false;
			//}
			//else if (hasAborted) {
			//	super.abort(object, playerObj);
			//}
			//else {
			//	super.stop(object, playerObj);
				// super.unprepare(object);
			//}
		}

		else if (jmfEvent instanceof RestartingEvent) {
			// Restarting Event - Ocorre quando o usuario altera o ponto de exibicao
			// atraves de interacao com a barra de tempo do controle
		}

		else if (jmfEvent instanceof RateChangeEvent) {
			// RateChangeEvent - Ocorre quando a taxa de exibiï¿½ï¿½o da mï¿½dia ï¿½
			// alterada

			// Acorda o monitor de ï¿½ncoras quando a taxa de exibiï¿½ï¿½o ï¿½
			// alterada,
			// para que ele recalcule os parï¿½metros granularity e sleep factor.
			
			 // if (nominalMonitor != null && nominalMonitor.isAlive()) { //
			 // monitor.setUpdateParamsFlag(); // nominal_monitor.interrupt(); }
			 
		}

		else if (jmfEvent instanceof ControllerErrorEvent) {
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
			playerObj.changeCurrentTime();
		}else if (jmfEvent instanceof CachingControlEvent) {
			// System.out.println("EVENT: Caching Control");
		}else if (jmfEvent instanceof DataStarvedEvent) {
			// System.out.println("EVENT: Data Starved");
		}else if (jmfEvent instanceof StopTimeChangeEvent) {
			// System.out.println("EVENT: Stop Time Change");
		}else {
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
*/
	/*protected void prepare(IExecutionObject object, IPlayerObject playerObj) {
		Player jmfPlayer;

		jmfPlayer = ((JmfPlayerObject)playerObj).getPlayer();
		jmfPlayer.prefetch();
	}*/

	/*protected boolean start(IExecutionObject object, IPlayerObject playerObj) {
		Player jmfPlayer;
		Component visualComponent;

		hasPaused = false;
		hasResumed = false;
		hasAborted = false;
		jmfPlayer = ((JmfPlayerObject)playerObj).getPlayer();

		// se esse codigo for necessario deve estar no start

		// chama o doResize sempre que o tamanho do framePanel se alterar
		
		//  if (super.display != null) { framePanel.addComponentListener(new
		//  ComponentAdapter() { public void componentResized(ComponentEvent ce) {
		//  doResize(); } }); }
		 

		
		  // altera o tamanho do frame_panel sempre que o tamanho do JFrame se
		  //alterar super.display.addComponentListener( new ComponentAdapter() {
		  //public void componentResized(ComponentEvent ce) {
		  //System.out.println("component resized"); Insets insets =
		  //frame_panel.getInsets(); Dimension dim = frame_panel.getSize();
		  //control_comp.setSize(dim.width - insets.left - insets.right, dim.height -
		 // insets.top - insets.bottom);
		 

		if (hasVisual) {
			visualComponent = jmfPlayer.getVisualComponent();
			visualComponent.addComponentListener(componentHandler);
			((Container)super.display).add(visualComponent);
		}

		jmfPlayer.start();
		return true;
	}*/

	/*protected boolean stop(IExecutionObject object, IPlayerObject playerObj) {
		Player jmfPlayer;

		jmfPlayer = ((JmfPlayerObject)playerObj).getPlayer();

		if (hasVisual && super.display != null) {
			jmfPlayer.getVisualComponent().removeComponentListener(componentHandler);
			((Container)super.display).removeAll();
		}

		jmfPlayer.stop();
		return true;
	}*/

	protected void createPlayer() {
		ICascadingDescriptor descriptor;
		descriptor = object.getDescriptor();
		if(descriptor != null){
			String parameterValue = (String)descriptor.getParameterValue(JMFPlayer.SOUNDLEVEL_PROPERTY);
			if(parameterValue != null && !parameterValue.equals(""))
				player.setPropertyValue(JMFPlayer.SOUNDLEVEL_PROPERTY, parameterValue);
			else
				player.setPropertyValue(JMFPlayer.SOUNDLEVEL_PROPERTY, "1.0");
			parameterValue = (String)descriptor.getParameterValue(JMFPlayer.RATE_PROPERTY);
			if(parameterValue != null && !parameterValue.equals(""))
				player.setPropertyValue(JMFPlayer.RATE_PROPERTY, parameterValue);
		}
		super.createPlayer();
	}

	/*public double getObjectExpectedDuration(IExecutionObject object) {
		IContent content;
		Player player;
		MediaLocator mrl = null;
		double duration;

		content = ((INodeEntity)object.getDataObject().getDataEntity())
				.getContent();
		try {
			if (content instanceof IReferenceContent) {
				// cria uma instancia de player JMF
				player = Manager.createRealizedPlayer(((IReferenceContent)content)
						.getCompleteReferenceUrl());
			}
			else {
				mrl = new MediaLocator(content.toString());
				if (mrl == null) {
					System.out.println("Error: Can't build URL for "
							+ ((IReferenceContent)content).getCompleteReferenceUrl()
									.toString());
					return super.getObjectExpectedDuration(object);
				}

				// cria uma instancia de player JMF
				player = Manager.createRealizedPlayer(mrl);
			}
			duration = player.getDuration().getSeconds() * 1000;
			player.deallocate();
			return duration;
		}
		catch (Exception exc) {
			// System.out.println("Error: " + exc);
			return super.getObjectExpectedDuration(object);
		}
	}*/

	/*protected void setAttributeValue(IExecutionObject object,
			IAttributionEvent event, 
			Object value, IAnimation animation, IPlayerObject playerObj) {

		String attName;
		Player player;
		GainControl gainControl;

		if (value == null) {
			return;
		}

		super.setAttributeValue(object, event, value, animation, playerObj);

		attName = event.getAnchor().getPropertyName();
		if (attName.equals("soundLevel")) {
			player = ((JmfPlayerObject)playerObj).getPlayer();
			gainControl = player.getGainControl();
			if (gainControl != null) {
				gainControl.setLevel(Float.parseFloat(value.toString()));
			}
		}
	}*/

	// o metodo e' reimplementado para forcar sempre a criacao de um novo player
	/*public boolean hasPrepared(IExecutionObject object, IPresentationEvent event) {
		IPlayerObject playerObj;

		playerObj = super.getPlayerObject(object);
		if (playerObj != null
				&& playerObj.getExecutionObject() == object
				&& (object.getMainEvent().getCurrentState() == IEvent.ST_OCCURRING || object
						.getMainEvent().getCurrentState() == IEvent.ST_PAUSED)) {
			return true;
		}
		else {
			return false;
		}
	}*/

	/*private class ComponentHandler extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			Component source;

			if (e.getSource() instanceof Component) {
				source = (Component)e.getSource();
				source.removeComponentListener(this);
				source.setBounds(0, 0, display.getWidth(), display.getHeight());
				source.addComponentListener(this);
			}
		}
	}*/
}