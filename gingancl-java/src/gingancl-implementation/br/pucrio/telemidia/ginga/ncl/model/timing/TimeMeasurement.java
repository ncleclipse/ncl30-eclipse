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
package br.pucrio.telemidia.ginga.ncl.model.timing;

import br.org.ginga.ncl.model.timing.ITimeMeasurement;

/**
 * This class stores two values for a milisecond time measurement:
 * an expected value and an actual value. The actual value is the temporal
 * measurement effectively observed during the document presentation.
 */
public class TimeMeasurement implements ITimeMeasurement {
	protected double expectedValue;
	protected double actualValue;

  /**
   * Class constructor where the expected value is specified and the actual 
   * value is not yet known.
   * 
   * @param value the time expected to measured.
   */
  public TimeMeasurement(double value) {
    expectedValue = value;
    actualValue = Double.NaN;
  }

  /**
   * Returns the expected value.
   * 
   * @return the expected value.
   */
  public double getExpectedValue() {
    return expectedValue;
  }
  
  public double getComputedValue() {
  	return expectedValue;
  }
  
  public void setComputedValue(double value) {
  	// ignores the command. To be dealed by subclasses.
  }

  /**
   * Returns the actual value for this measurement, after occurring the 
   * presentation.
   * 
   * @return the actual value.
   */
  public double getActualValue() {
    return actualValue;
  }

  /**
   * Sets the expected time measurement.
   * 
   * @param value the expected value for this measurement.
   */
  public void setExpectedValue(double value) {
    expectedValue = value;
  }
  
  /**
   * Sets the actual duration.
   * 
   * @param value the actual value for this time measurement.
   */
  public void setActualValue(double value) {
    actualValue = value;
  }

  /**
   * Informs whether the expected (or the actual) value is equal or greater 
   * than 0.
   * 
   * @return true if the ideal duration is positive and false otherwise.
   */
  public boolean isPredictable() {
    if (expectedValue >= 0 || actualValue >= 0)
      return true;
    else
      return false;
  }
  
	/**
	 * Informs whether the optimum duration value was computed.
	 * 
	 * @return true if the optimum duration was calculated and false otherwise.
	 */
	public boolean isComputed() {
		return true;
	}

  public String toString() {
    return (
      "expected value: "
      + expectedValue
      + "; actual value: "
      + (actualValue==Double.NaN ? "UNDEFINED" : ""+actualValue));
  }

  /**
   * Overwrites all the object attribute values using the object passed as
   * parameter.
   * 
   * @param time the measurement whose values will be used to overwrite the
   * attribute values of the current time measurement object
   */
  protected void overwrite(TimeMeasurement time) {
    expectedValue = time.getExpectedValue();
    actualValue = time.getActualValue();
  }

  /**
   * Duplicates the time measurement returning a new cloned instance. 
   * 
   * @return the new cloned time measurement.
   */
  public ITimeMeasurement duplicate() {
  	TimeMeasurement newMeas;
  	
  	newMeas = new TimeMeasurement(expectedValue);
  	newMeas.overwrite(this);
  	return newMeas;
  }
  
  /**
   * If the actual value is known, return it. Otherwise returns the expected
   * value.
   * 
   * @return the actual value, if already known, or the expected value.
   */
  public double getValue() {
  	if (actualValue != Double.NaN)
  	  return actualValue;
  	else
  	  return expectedValue;
  }
  
  public void fromString(String str) {
  	// TODO
  }
}
