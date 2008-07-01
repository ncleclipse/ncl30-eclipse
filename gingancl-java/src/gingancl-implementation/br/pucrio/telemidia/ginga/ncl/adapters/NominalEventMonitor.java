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

package br.pucrio.telemidia.ginga.ncl.adapters;

import br.org.ginga.core.player.IPlayer;
import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.transition.IEventTransition;

public class NominalEventMonitor {
	private final static double DEFAULT_SLEEP_TIME = 5000.0;

	private final static double DEFAULT_ERROR = 50.0;

	private double sleepTime;

	private IPlayer player;

	private IExecutionObject executionObject;

	private boolean running;

	private boolean paused;

	private boolean stopped;
	
	public NominalEventMonitor(IExecutionObject executionObject, IPlayer player){
		this(executionObject,player, DEFAULT_SLEEP_TIME);
	}
	
	public NominalEventMonitor(IExecutionObject executionObject, IPlayer player, double sleepTime){
		this.player = player;
		this.executionObject = executionObject;
		if (sleepTime >= 0) {
			this.sleepTime = sleepTime;
		}
		else {
			this.sleepTime = DEFAULT_SLEEP_TIME;
		}
	}

	public void startMonitor() {
		NominalEventMonitorThread monitorThread;

		if (!running) {
			try {
				// start monitor only if there is predictable events
				running = true;
				paused = false;
				stopped = false;
				monitorThread = new NominalEventMonitorThread();
				monitorThread.start();
			}
			catch (Exception exc) {
			}
		}
	}

	public void pauseMonitor() {
		paused = true;
	}

	public void resumeMonitor() {
		paused = false;
	}

	public void stopMonitor() {
		// System.err.println("NominalEventMonitor::stopMonitor (" +
		// executionObject.getId() + ")");
		stopped = true;
		running = false;
	}

	private class NominalEventMonitorThread extends Thread {
		public void run() {
			IEventTransition nextTransition;
			double mediaTime; // tempo da mídia em milisegundos
			double nextEntryTime;
			double expectedSleepTime;

			// System.err.println("NominalEventMonitorThread::run monitor ativado " +
			// executionObject.getId());

			// enquanto o player estiver tocando e ainda houver eventos a serem
			// reportados e
			// não for solicitado o seu término
			while (running) {
				if (paused) {
					try {
						sleep((long)Math.min(sleepTime, 1000));
					}
					catch (InterruptedException e) {
						// Esta exceção é utilizada para acordar o monitor quando a
						// apresentação
						// é interrompida.
					}
				}
				else {
					nextTransition = executionObject.getNextTransition();
					if (nextTransition == null) {
						running = false;
					}
					else {
						nextEntryTime = nextTransition.getTime();
						// System.err.println("NominalEventMonitorThread::run
						// nextTransitionTime=" + nextEntryTime);
						// System.err.println("NominalEventMonitorThread::run " +
						// "nextEntryTime=" + nextEntryTime);
						mediaTime = player.getMediaTime() * 1000;
						expectedSleepTime = (long)(nextEntryTime - mediaTime);

						try {
							if (expectedSleepTime > 0) {
								sleep((long)Math.min(expectedSleepTime, sleepTime));
								if (running) {
									mediaTime = player.getMediaTime() * 1000;
								}
							}
						}
						catch (InterruptedException e) {
							// Esta exceção é utilizada para acordar o monitor quando a
							// apresentação
							// é interrompida.
						}

						if (running) {
							// System.err.println("NominalEventMonitorThread::run " +
							// "mediaTime=" + mediaTime);
							if (!paused && ((nextEntryTime - mediaTime) <= DEFAULT_ERROR)) {
								// efetua a transicao no estado do evento
								executionObject
										.updateTransitionTable(mediaTime + DEFAULT_ERROR);
							}
						}
					}
				}
			}

			if (!stopped) {
				player.forceNaturalEnd();
			}
			// System.err.println("NominalEventMonitorThread::run monitor desativado "
			// + executionObject.getId());
		}
	}
}