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

import br.org.ginga.ncl.model.timing.IFlexibleTimeMeasurement;
import br.org.ginga.ncl.model.timing.ITimeMeasurement;

/**
 * This class defines a flexible duration to be used when computing
 * elastic time presentations. The class adds to an ordinary duration lower
 * and upper bound limits for computing the duration. Besides the ideal
 * duration interval, the class allows to store a feasible interval for
 * the duration. The class also defines an attribute to store the duration
 * optimum value, calculated by some elastic time algorithm.
 * 
 */
public class FlexibleTimeMeasurement extends TimeMeasurement implements
    IFlexibleTimeMeasurement {
  protected double computedValue;
  protected double minimumValue; // duration lower bound
  protected double maximumValue; // duration upper bound
  protected double minFeasibleValue;
  protected double maxFeasibleValue;

  /**
   * Class constructor. Receives the interval information, besides the ideal
   * duration. The constructor initializes the feasible interval and the
   * optimum value as "not computed".
   * 
   * @param idealValue ideal duration.
   * @param minValue minimum duration.
   * @param maxValue maximum duration.
   */
  public FlexibleTimeMeasurement(double expectedValue, double minValue,
      double maxValue) {
    super(expectedValue);
    computedValue = Double.NaN;
    minFeasibleValue = Double.NaN;
    maxFeasibleValue = Double.NaN;
    setBoundaryValues(minValue, maxValue);
  }

  public double getComputedValue() {
    return computedValue;
  }

  /**
   * Returns the duration optimum value.
   * 
   * @return the duration optimum value.
   */
  public double getOptimumValue() {
    return getComputedValue();
  }

  public void setComputedValue(double value) {
    computedValue = value;
  }

  /**
   * Sets the duration optimum value.
   * 
   * @param optValue the duration optimum value
   */
  public void setOptimumValue(double optValue) {
    setComputedValue(optValue);
  }

  /**
   * Returns the duration maximum value.
   * 
   * @return the duration maximum value.
   */
  public double getMaximumValue() {
    return maximumValue;
  }

  /**
   * Returns the duration maximum feasible value.
   * 
   * @return the duration maximum feasible value.
   */
  public double getMaximumFeasibleValue() {
    return maxFeasibleValue;
  }

  /**
   * Returns the duration minimum feasible value.
   * 
   * @return the duration minimum feasible value.
   */
  public double getMinimumFeasibleValue() {
    return minFeasibleValue;
  }

  /**
   * Returns the duration maximum value.
   * 
   * @return the duration maximum value.
   */
  public double getMinimumValue() {
    return minimumValue;
  }

  /**
   * Informs whether the expected (or the actual) value is equal or greater 
   * than 0.
   * 
   * @return true if the ideal duration is positive and false otherwise.
   */
  public boolean isPredictable() {
    if (super.isPredictable() || computedValue >= 0)
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
    if (computedValue != Double.NaN)
      return true;
    else
      return false;
  }

  /**
   * Informa se a duracao possui um valor maximo definido.
   * 
   * @return true se a duracao tiver um valor maximo especificado.
   */
  public boolean isUpperBounded() {
    if (maximumValue >= 0)
      return true;
    else
      return false;
  }

  /**
   * Sets the interval limits as valid values if they are not configures as so.
   * 
   * @param minValue duration lower limit.
   * @param maxValue duration upper limit.
   */
  private void setBoundaryValues(double minValue, double maxValue) {
    if (minValue > maxValue)
      minValue = maxValue;

    if (minValue < 0)
      minimumValue = 0;
    else if (super.expectedValue != Double.NaN
        && minValue > super.expectedValue)
      minimumValue = expectedValue;
    else
      minimumValue = minValue;

    if (maxValue < 0) {
      maximumValue = Double.POSITIVE_INFINITY;
    }
    else if (super.expectedValue != Double.NaN
        && maxValue < super.expectedValue)
      maximumValue = expectedValue;
    else
      maximumValue = maxValue;
  }

  /**
   * Overwrites all the object attribute values using the values passed as
   * parameter.
   * 
   * @param dur the duration whose values will be used to overwrite the
   * attribute values of the current Duration object
   */
  protected void overwrite(FlexibleTimeMeasurement dur) {
    super.overwrite(dur);
    minimumValue = dur.getMinimumValue();
    maximumValue = dur.getMaximumValue();
    minFeasibleValue = dur.getMinimumFeasibleValue();
    maxFeasibleValue = dur.getMaximumFeasibleValue();
    computedValue = dur.getComputedValue();
  }

  /**
   * Duplicates the duration returning a new cloned instance. 
   * 
   * @return the new cloned duration.
   */
  public ITimeMeasurement duplicate() {
    FlexibleTimeMeasurement newDuration;

    newDuration = new FlexibleTimeMeasurement(super.expectedValue,
        minimumValue, maximumValue);
    newDuration.overwrite(this);
    return newDuration;
  }

  public String toString() {
    return (super.toString()
        + "; minimum value: "
        + minimumValue
        + "; maximum value : "
        + (maximumValue == Double.POSITIVE_INFINITY ? "INFINITY" : ""
            + maximumValue) + "; computed value: " + (computedValue == Double.NaN ? "UNDEFINED"
        : "" + computedValue));
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
    else if (computedValue != Double.NaN)
      return computedValue;
    else
      return expectedValue;
  }

  public void setMinimumValue(double value) {
    minimumValue = value;
  }

  public void setMaximumValue(double value) {
    maximumValue = value;
  }

  public void setMinimumFeasibleValue(double value) {
    minFeasibleValue = value;
  }

  public void setMaximumFeasibleValue(double value) {
    maxFeasibleValue = value;
  }
}