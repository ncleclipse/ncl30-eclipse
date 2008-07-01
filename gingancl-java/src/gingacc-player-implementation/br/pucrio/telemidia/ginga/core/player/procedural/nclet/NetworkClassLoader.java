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
package br.pucrio.telemidia.ginga.core.player.procedural.nclet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;

public class NetworkClassLoader extends URLClassLoader {
	public NetworkClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
		// TODO Auto-generated constructor stub
	}

	public NetworkClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		// TODO Auto-generated constructor stub
	}

	public NetworkClassLoader(URL[] urls) {
		super(urls);
		// TODO Auto-generated constructor stub
	}

	public NetworkClassLoader() {
		this(new URL[0]);
	}

	public NetworkClassLoader(ClassLoader parent) {
		this(new URL[0], parent);
	}

	public Class findClass(URL name) {
		byte[] bytecode = loadClassData(name.toString());
		return defineClass(null, bytecode, 0, bytecode.length);
	}

	private byte[] loadClassData(String name) {
		// load the class data from the connection
		URL url;
		URLConnection connection;
		File file;
		DataInputStream inputStream;
		byte[] bytecode;
		long size;

		try {
			url = new URL(name);
			// System.out.println("read url");

			// Read the class bytecodes from the stream
			/*
			 * DataInputStream dataIn = new DataInputStream(url.openStream()); int
			 * avail = dataIn.available(); System.out.println("Available = " + avail);
			 * System.out.println("URLClassLoader: Reading class from stream...");
			 * byte[] classData = new byte[classSize]; dataIn.readFully(classData);
			 */

			connection = url.openConnection();
			size = connection.getContentLength();
			inputStream = new DataInputStream(connection.getInputStream());
			bytecode = new byte[(int)size];
			inputStream.readFully(bytecode);
			return bytecode;
		}
		catch (Exception exc) {
			try {
				// System.out.println("read file");
				file = new File(name);
				size = file.length();
				FileInputStream fis = new FileInputStream(file);
				bytecode = new byte[(int)size];
				fis.read(bytecode);
				return bytecode;
			}
			catch (Exception exc1) {
				System.out.println("[ERR] exception in class loader: " + exc1);
				return null;
			}
		}
	}
}
