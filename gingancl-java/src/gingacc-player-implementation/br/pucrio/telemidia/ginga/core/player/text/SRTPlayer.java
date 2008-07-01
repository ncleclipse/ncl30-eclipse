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

package br.pucrio.telemidia.ginga.core.player.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

public class SRTPlayer extends DefaultPlayerImplementation {
	// private static final long DEFAULT_SLEEP_TIME = 15000;
	private static final String BACKGROUND_PROPERTY="background-color";
	private static final String COLOR_PROPERTY="color";

	private SrtInnerPlayer srtPlayer;

	private List<SrtUnit>srtList;

	private Font subtitleFont;
	
	private Color backgroundColor;
	private Color foregroundColor;

	public SRTPlayer(URL contentURL) {
		super(contentURL);
		backgroundColor = Color.BLACK;
		foregroundColor = Color.YELLOW;

		srtPlayer = null;
		srtList = new ArrayList<SrtUnit>();

		

		// buscar o conteudo caso nao esteja no cache do objeto de dados
		readUrlContent(contentURL);

		this.setSurface(GFXManager.getInstance().createSurface(""));
		subtitleFont = new Font("Tiresias", Font.PLAIN, 20);
		((Container)SRTPlayer.this.getSurface().getSurface()).setBackground(backgroundColor);
	}

	private void readUrlContent(URL url) {
		URLConnection connection;
		BufferedReader buff_reader;
		String line;
		List content;
		String timeParams[];
		double begin, end, lastEnd;
		SrtUnit srtUnit;

		lastEnd = 0;
		try {
			connection = url.openConnection();
			buff_reader = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));

			line = buff_reader.readLine();
			while (line != null) {
				// jump the id and read the time info
				line = buff_reader.readLine();

				// read the time info
				timeParams = line.split("-->", -1);
				if (timeParams.length != 2) {
					return;
				}

				begin = readTimeParam(timeParams[0].trim());
				end = readTimeParam(timeParams[1].trim());

				// read the text info
				content = new Vector();
				line = buff_reader.readLine();
				while (line != null && line.length() > 0 && line.charAt(0) != '\n') {
					content.add(line);
					line = buff_reader.readLine();
				}

				if (begin <= end && begin >= lastEnd) {
					srtUnit = new SrtUnit(content, begin, end);
					srtList.add(srtUnit);
					lastEnd = end;
				}

				line = buff_reader.readLine();
			}
		}
		catch (Exception exc) {
			System.err.println("exc: " + exc);
		}
	}

	private double readTimeParam(String timeStr) {
		String timeParams[];
		double seconds;

		timeParams = timeStr.split(":");
		if (timeParams.length != 3) {
			return 0;
		}

		seconds = Double.parseDouble(timeParams[0].trim()) * 3600;
		seconds += Double.parseDouble(timeParams[1].trim()) * 60;
		timeParams[2] = timeParams[2].replace(',', '.');
		seconds += Double.parseDouble(timeParams[2].trim());
		return seconds;
	}

	public double getMediaTime() {
		if (super.timeBasePlayer == null) {
			return super.getMediaTime();
		}
		return super.timeBasePlayer.getMediaTime();
	}

	public void play() {
		
			srtPlayer = new SrtInnerPlayer();
			srtPlayer.start();
			super.play();
	}

	public void stop() {
		if (srtPlayer != null) {
			srtPlayer.stopPlayer();
		}
		super.stop();
	}

	/*public void timebaseObjectTransitionCallback(int transition) {
		if (transition == IEvent.TR_STOPS) {
			// player.setReferenceTimePlayer(null);
			stop();
		}
	}*/

	/*public Container getTextPanel() {
		return textPane;
	}*/

	private Component getSubtitleLine(String line) {
		Label subtitleLine;

		// subtitleLine = new HStaticText();
		subtitleLine = new Label(line);
		subtitleLine.setFont(subtitleFont);
		subtitleLine.setBackground(backgroundColor);
		subtitleLine.setForeground(foregroundColor);
		// subtitleLine.setBackgroundMode(HVisible.NO_BACKGROUND_FILL);
		// subtitleLine.setBordersEnabled(false);
		// subtitleLine.setTextContent(line, HState.ALL_STATES);

		return subtitleLine;
	}

	private class SrtUnit {
		private List text;

		private double begin;

		private double end;

		public SrtUnit(List text, double begin, double end) {
			this.text = text;
			this.begin = begin * 1000;
			this.end = end * 1000;
		}

		public List getText() {
			return text;
		}

		public double getBegin() {
			return begin;
		}

		public double getEnd() {
			return end;
		}
	}

	private class SrtInnerPlayer extends Thread {
		private boolean running;

		private int getNextSrt(double time, int curSrtPos) {
			SrtUnit currUnit, prevUnit;
			int pos, beg, end;
			currUnit = null;
			if (curSrtPos < srtList.size()) {
				currUnit = (SrtUnit)srtList.get(curSrtPos);
			}

			prevUnit = null;
			if (curSrtPos > 0) {
				prevUnit = (SrtUnit)srtList.get(curSrtPos - 1);
			}

			if (currUnit == null && prevUnit != null) {
				// maybe reached the last unit
				if (prevUnit.getEnd() < time) {
					// no more units to be presented
					return curSrtPos;
				}
			}
			else if (prevUnit == null && currUnit != null) {
				// maybe it is still in the beginning
				if (time < currUnit.getEnd()) {
					// first unit to be presented or being presented
					return curSrtPos;
				}
			}
			else {
				if (time > prevUnit.getEnd() && time < currUnit.getEnd()) {
					return curSrtPos;
				}
			}

			// look for the position again using binary search
			beg = 0;
			end = srtList.size() - 1;
			while (beg <= end) {
				pos = (beg + end) / 2;
				currUnit = (SrtUnit)srtList.get(pos);
				if (currUnit.getBegin() <= time && time <= currUnit.getEnd()) {
					return pos;
				}
				else if (currUnit.getBegin() > time) {
					end = pos - 1;
				}
				else {
					beg = pos + 1;
				}
			}

			return beg;
		}

		public void run() {
			double time;
			int pos;
			SrtUnit srtUnit;
			long sleepTime;
			List subtitleText;
			Container textPane = (Container)SRTPlayer.this.getSurface().getSurface();
			textPane.setBackground(backgroundColor);
			if (srtList.isEmpty()) {
				return;
			}

			running = true;
			pos = 0;
			while (running) {
				
				time = getMediaTime()*1000;
				pos = getNextSrt(time, pos);
				if (pos == srtList.size()) {
					/*
					 * try { sleep(DEFAULT_SLEEP_TIME); } catch (Exception exc1) {}
					 */
					running = false;
					SRTPlayer.this.stop();
				}
				else {
					srtUnit = (SrtUnit)srtList.get(pos);
					sleepTime = (long)(srtUnit.getBegin() - (time));
					if (sleepTime > 0) {
						try {
							sleep(sleepTime);
							if(!running)
								return;
						}
						catch (Exception exc1) {
						}
					}

					subtitleText = srtUnit.getText();

					GridBagLayout layout = new GridBagLayout();

					GridBagConstraints c = new GridBagConstraints();
					c.fill = GridBagConstraints.BOTH;
					c.gridx = 0;
					c.insets = new java.awt.Insets(4, 4, 4, 4);

					textPane.setLayout(layout);

					for (int i = 0; i < subtitleText.size(); i++) {
						Component subtitleComponent = getSubtitleLine((String)subtitleText
								.get(i));
						c.gridy = i;
						textPane.add(subtitleComponent, c);
					}
					textPane.validate();

					//System.err.println("-->(IN) " + srtUnit.getText());
					
					time = getMediaTime()*1000;
					sleepTime = (long)(srtUnit.getEnd() - (time));
					if (sleepTime > 0) {
						try {
							sleep(sleepTime);
							if(!running)
								return;
						}
						catch (Exception exc1) {
						}
					}

					//System.err.println("<--(OUT) " + srtUnit.getText());
					textPane.removeAll();
					textPane.validate();
				}
			}
		}

		public void stopPlayer() {
			running = false;
		}
	}

	public void eventStateChanged(String id, short type, short transition,
			int code) {
		// TODO Auto-generated method stub
		
	}

	public String getPropertyValue(String name) {
		if(name.equals(COLOR_PROPERTY))
			return "#"+foregroundColor.getRGB();
		if(name.equals(BACKGROUND_PROPERTY))
			return "#"+backgroundColor.getRGB();
		return null;
	}
	
	public void setBackgroundColor(Color color){
		this.backgroundColor = color;
		Container surfaceContainer = (Container)this.getSurface().getSurface();
		surfaceContainer.setBackground(backgroundColor);
		for(Component comp : surfaceContainer.getComponents()){
			if(comp instanceof Label){
				comp.setBackground(backgroundColor);
			}
		}
		surfaceContainer.validate();
	}
	
	public void setForegroundColor(Color color){
		this.foregroundColor = color;
		Container surfaceContainer = (Container)this.getSurface().getSurface();
		surfaceContainer.setForeground(foregroundColor);
		for(Component comp : surfaceContainer.getComponents()){
			if(comp instanceof Label){
				comp.setBackground(foregroundColor);
			}
		}
		surfaceContainer.validate();
	}

	public void setPropertyValue(String name, String value) {
		if(name.equals(COLOR_PROPERTY)){
			try{
				this.setForegroundColor(new Color(Integer.parseInt(value)));
			}catch(NumberFormatException ex){
				System.err.println("Could not set Color: "+ex.getLocalizedMessage());
			}
		}
		if(name.equals(BACKGROUND_PROPERTY)){
			try{
				this.setBackgroundColor(new Color(Integer.parseInt(value)));
			}catch(NumberFormatException ex){
				System.err.println("Could not set Baclground: "+ex.getLocalizedMessage());
			}
		}
	}
}