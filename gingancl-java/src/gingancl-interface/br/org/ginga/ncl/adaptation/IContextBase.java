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
package br.org.ginga.ncl.adaptation;

import java.util.Iterator;
import java.util.Observer;

public interface IContextBase {
	public final static String SYSTEM_LANGUAGE = "systemLanguage";

	public final static String SYSTEM_CAPTION = "systemCaption";

	public final static String SYSTEM_SUBTITLE = "systemSubtitle";

	public final static String SYSTEM_RETURN_BIT_RATE = "systemReturnBitRate";

	public final static String SYSTEM_SCREEN_SIZE = "systemScreenSize";

	public final static String SYSTEM_SCREEN_GRAPHIC_SIZE = "systemScreenGraphicSize";

	public final static String SYSTEM_AUDIO_TYPE = "systemAudioType";

	public final static String SYSTEM_CPU = "systemCPU";

	public final static String SYSTEM_MEMORY = "systemMemory";

	public final static String SYSTEM_OPERATING_SYSTEM = "systemOperatingSystem";

	public final static String USER_AGE = "userAge";

	public final static String USER_LOCATION = "userLocation";

	public final static String USER_GENRE = "userGenre";

	public final static String DEFAULT_FOCUS_BORDER_COLOR = "defaultFocusBorderColor";

	public final static String DEFAULT_FOCUS_BORDER_WIDTH = "defaultFocusBorderWidth";

	public final static String DEFAULT_FOCUS_BORDER_TRANSPARENCY = "defaultFocusBorderTransparency";

	public final static String DEFAULT_SEL_BORDER_COLOR = "defaultSelBorderColor";

	void setPropertyValue(String name, String value);

	Iterator<String> getPropertyNames();

	String getPropertyValue(String attributeName);

	void addObserver(Observer o);

	void deleteObserver(Observer o);
}
