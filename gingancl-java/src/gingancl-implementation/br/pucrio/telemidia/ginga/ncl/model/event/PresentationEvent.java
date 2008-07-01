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
package br.pucrio.telemidia.ginga.ncl.model.event;

import br.org.ginga.ncl.model.components.IExecutionObject;
import br.org.ginga.ncl.model.event.IPresentationEvent;
import br.org.ncl.connectors.IEvent;
import br.org.ncl.interfaces.IContentAnchor;
import br.org.ncl.interfaces.IIntervalAnchor;

/**
 * Classe que define um evento de apresentacao no modelo de execucao. Eventos de
 * apresentacao representam a exibicao de um subconjunto das unidades de
 * informacao que compoem o conteudo de um objeto.
 */
public class PresentationEvent extends AnchorEvent implements
		IPresentationEvent {

	private double begin, end;

	private double duration; // duracao do evento

	private long numPresentations; // numero de vezes seguidas que

	// o evento deve ocorrer
	private double repetitionInterval; // intervalo entre as repeticoes

	/**
	 * Construtor da classe. Cria um evento de apresentacao com numero de
	 * repeticoes igual a 1.
	 * 
	 * @param id
	 *          identificador unico para o evento.
	 * @param executionObject
	 *          objeto de execucao ao qual o evento pertence.
	 * @param anchor
	 *          define o subconjunto de unidades de informacao
	 */
	public PresentationEvent(String id, IExecutionObject executionObject,
			IContentAnchor anchor) {

		super(id, executionObject, anchor);
		numPresentations = 1;
		repetitionInterval = 0;

		if (anchor instanceof IIntervalAnchor) {
			begin = ((IIntervalAnchor)anchor).getBegin();
			end = ((IIntervalAnchor)anchor).getEnd();
			duration = (end - begin);
		}
		else {
			begin = IPresentationEvent.UNDEFINED_INSTANT;
			end = IPresentationEvent.UNDEFINED_INSTANT;
			duration = IPresentationEvent.UNDEFINED_INSTANT;
		}
	}

	public boolean stop() {
		if (currentState == IEvent.ST_OCCURRING && numPresentations > 1)
			numPresentations--;
		return super.stop();
	}

	/**
	 * Retorna a duracao do evento.
	 * 
	 * @return duracao do evento.
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Retorna a duracao que deve ser aguardada entre duas ocorrencias sucessivas
	 * do evento, fruto de repeticoes da exibicao.
	 * 
	 * @return duracao entre repeticoes
	 */
	public double getRepetitionInterval() {
		return repetitionInterval;
	}

	/**
	 * Retorna o numero de repeticoes previstas para o evento que ainda faltam.
	 * 
	 * @return numero de repeticoes previstas para o evento.
	 */
	public long getRepetitions() {
		return (numPresentations - 1);
	}

	/**
	 * Permite especificar a duracao do evento.
	 * 
	 * @param dur
	 *          duracao que a ocorrencia do evento deve assumir.
	 */
	public void setDuration(double dur) {
		duration = dur;
	}

	public void setEnd(double e) {
		if (e == IIntervalAnchor.OBJECT_DURATION || e >= begin) {
			end = e;
			if (e == IIntervalAnchor.OBJECT_DURATION) {
				duration = end;
			}
			else {
				duration = (end - begin);
			}
		}
	}

	/**
	 * Permite especificar os parametros para eventuais repeticoes da ocorrencia
	 * do evento. O numero de repeticoes passado como parametro nao deve levar em
	 * conta a primeira ocorrencia do evento. Pro exemplo, se o numero de
	 * repeticoes informado for n, o evento sera' exibido n+1 vezes.
	 * 
	 * @param repetitions
	 *          numero de repeticoes esperadas.
	 * @param repetitionInterval
	 *          intervalo de espera entre as repeticoes.
	 */
	public void setRepetitionSettings(long repetitions, double repetitionInterval) {

		if (repetitions >= 0)
			this.numPresentations = repetitions + 1;
		else
			this.numPresentations = 1;

		this.repetitionInterval = repetitionInterval;
	}

	public double getBegin() {
		return begin;
	}

	public double getEnd() {
		return end;
	}

	public void incrementOccurrences() {
		super.occurrences++;
	}

	public static boolean isUndefinedInstant(double value) {
		return Double.isNaN(value);
	}
}
