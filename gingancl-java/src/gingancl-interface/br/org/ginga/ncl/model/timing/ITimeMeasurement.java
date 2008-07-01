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
package br.org.ginga.ncl.model.timing;

import java.io.Serializable;

/**
 * This class stores two values for a milisecond time measurement:
 * an expected value and an actual value. The actual value is the temporal
 * measurement effectively observed during the document presentation.
 */
public interface ITimeMeasurement extends Serializable {
  /**
   * Returns the expected value.
   * 
   * @return the expected value.
   */
  public double getExpectedValue();

  public double getComputedValue();

  public void setComputedValue(double value);

  /**
   * Returns the actual value for this measurement, after occurring the 
   * presentation.
   * 
   * @return the actual value.
   */
  public double getActualValue();

  /**
   * Sets the expected time measurement.
   * 
   * @param value the expected value for this measurement.
   */
  public void setExpectedValue(double value);

  /**
   * Sets the actual duration.
   * 
   * @param value the actual value for this time measurement.
   */
  public void setActualValue(double value);

  /**
   * Informs whether the expected (or the actual) value is equal or greater 
   * than 0.
   * 
   * @return true if the ideal duration is positive and false otherwise.
   */
  public boolean isPredictable();

  /**
   * Informs whether the optimum duration value was computed.
   * 
   * @return true if the optimum duration was calculated and false otherwise.
   */
  public boolean isComputed();

  public String toString();

  /**
   * Duplicates the time measurement returning a new cloned instance. 
   * 
   * @return the new cloned time measurement.
   */
  public ITimeMeasurement duplicate();

  /**
   * If the actual value is known, return it. Otherwise returns the expected
   * value.
   * 
   * @return the actual value, if already known, or the expected value.
   */
  public double getValue();

  public void fromString(String str);
}