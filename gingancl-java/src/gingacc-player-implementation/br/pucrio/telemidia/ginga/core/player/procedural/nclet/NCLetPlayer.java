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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.tv.xlet.Xlet;
import javax.tv.xlet.XletStateChangeException;

import br.org.ginga.core.player.procedural.nclet.INCLet;
import br.pucrio.telemidia.ginga.core.io.GFXManager;
import br.pucrio.telemidia.ginga.core.player.DefaultPlayerImplementation;

public class NCLetPlayer extends DefaultPlayerImplementation {
	public static String CLASSPATH_DESCRIPTOR_PARAM = "x-classpath";
	public static String ARGUMENTS_DESCRIPTOR_PARAM = "x-args";
	public static String MAINCLASS_DESCRIPTOR_PARAM = "x-mainXlet";
	
	private String classpath;
	private String arguments;
	private String mainXlet;
	
	private NetworkClassLoader jarClassLoader;
	
	private Object nclet;
	private NCLetContext ncletContext;
	private String documentPath;
	boolean played;

	public NCLetPlayer(URL contentURL, String documentPath) {
		super(contentURL);
		this.documentPath = documentPath;
		this.setSurface(GFXManager.getInstance().createSurface(""));
	}

	public void eventStateChanged(String id, short type, short transition,
			int code) {
		if(nclet != null && nclet instanceof INCLet){
			//((INCLet)nclet).eventStateChanged(id, type, transition);
		}
	}

	public String getPropertyValue(String name) {
		if(name.equals(CLASSPATH_DESCRIPTOR_PARAM))
			return classpath;
		else if(name.equals(ARGUMENTS_DESCRIPTOR_PARAM))
			return arguments;
		else if(name.equals(MAINCLASS_DESCRIPTOR_PARAM))
			return mainXlet;
		return null;
	}

	public void setPropertyValue(String name, String value) {
		if(name.equals(CLASSPATH_DESCRIPTOR_PARAM)){
			classpath=value;
			return;
		}else if(name.equals(ARGUMENTS_DESCRIPTOR_PARAM)){
			arguments=value;
			return;
		}else if(name.equals(MAINCLASS_DESCRIPTOR_PARAM)){
			mainXlet=value;
			return;
		}
		
		Class ncletClass;
		Method ncletMethod;
		Field ncletField;
		String parameters[];
		int i;
		Class parameterTypes[];
		
		if(nclet == null){
			return;
		}
		ncletClass = nclet.getClass();
		
		try {
			// first look for a method
			if (value == null || value.toString().equals("")) {
				ncletMethod = ncletClass.getMethod(name, null);
				if (ncletMethod != null) {
					ncletMethod.invoke(nclet, null);
				}
			}
			else {
				parameters = value.toString().split(",", -1);
				parameterTypes = new Class[parameters.length];
				for (i = 0; i < parameters.length; i++) {
					parameters[i] = parameters[i].trim();
					parameterTypes[i] = parameters[i].getClass();
				}
				ncletMethod = ncletClass.getMethod(name, parameterTypes);
				if (ncletMethod != null) {
					ncletMethod.invoke(nclet, parameters);
				}
			}
		}
		catch (Exception exc1) {
			// System.err.println("NCLetPlayer::setAttributeValue " + exc1);
			try {
				// then look for a field
				ncletField = ncletClass.getField(name);
				if (ncletField != null) {
					ncletField.set(nclet, value);
				}
			}
			catch (Exception exc2) {
				System.err.println("[WARN] NCLetPlayer::setAttributeValue " + exc2);
			}
		}
	}
	
	private void loadJarFiles() {
		
		String[] jarFiles;
		String urlStr = documentPath;
		//urlStr=urlStr.substring(0, urlStr.lastIndexOf(File.separator)+1);
		URL[] jarUrls;
		int i;

		try {

			if (classpath != null && !classpath.equals("")) {
				jarFiles = ((String)classpath).split(";", -1);
				if (jarFiles.length > 0) {
					jarUrls = new URL[jarFiles.length];
					for (i = 0; i < jarFiles.length; i++) {
						// process jar name as URL
						jarFiles[i] = jarFiles[i].trim();
						//if (jarFiles[i].endsWith(".jar") || jarFiles[i].endsWith(".zip")
						//		|| jarFiles[i].endsWith(".class")) {
						jarUrls[i] = new URL(urlStr + jarFiles[i]);
						System.out.println("[INFO] NCLetPlayerObject::loadJarFiles "
								+ jarUrls[i]);
						//}
					}
					jarClassLoader = new NetworkClassLoader(jarUrls);
				}
			}
		}
		catch (Exception exc) {
			System.err.println("[ERR] NCLetPlayerObject::loadJarFiles " + exc);
			exc.printStackTrace();
		}
	}

	private Class<?> loadXletMainClassFromJar(URL jarUrl) {
		URL[] urls;
		URLClassLoader urlClassLoader;
		//ICascadingDescriptor descriptor;
		//Object parameter;
		InputStream inputStream;
		Properties properties;
		String mainClassName;
		Class<?> mainXletClass;

		urls = new URL[1];
		urls[0] = jarUrl;
		if (jarClassLoader == null) {
			urlClassLoader = new URLClassLoader(urls);
		}
		else {
			urlClassLoader = new URLClassLoader(urls, jarClassLoader);
		}

		// look for descriptor information

		if (mainXlet != null && !mainXlet.equals("")) {
			try {
				mainXletClass = urlClassLoader.loadClass(mainXlet);
				return mainXletClass;
			}
			catch (Exception exc) {
				System.err.print("[ERR] NCLetPlayerObject::loadMainClassFromJar " + exc.getMessage());
			}
		}

		// look for a manifest resource META-INF/MANIFEST.MF
		inputStream = urlClassLoader.getResourceAsStream("META-INF/MANIFEST.MF");
		if (inputStream != null) {
			// MainClass:CLASS-NAME
			properties = new Properties();
			try {
				properties.load(inputStream);
				mainClassName = properties.getProperty("MainClass");
				if (mainClassName != null) {
					mainXletClass = urlClassLoader.loadClass(mainClassName);
					return mainXletClass;
				}
			}
			catch (Exception exc) {
				System.err.print("[ERR] NCLetPlayerObject::loadMainClassFromJar " + exc.getMessage());
			}
		}

		return null;
	}

	@Override
	public void pause() {
		if(nclet instanceof Xlet){
			((Xlet)nclet).pauseXlet();
		}
		super.pause();
	}

	@Override
	public void play() {
		Class<?> ncletClass;
		try{
			jarClassLoader = null;
			loadJarFiles();

			ncletClass = null;
			if (this.getContentURL().getPath().endsWith(".class")) {
				if (jarClassLoader == null) {
					jarClassLoader = new NetworkClassLoader();
				}
				if (this.getContentURL().getProtocol().equals("file")) {
					ncletClass = jarClassLoader.findClass(this.getContentURL());
				}
				else {
					ncletClass = jarClassLoader.findClass(this.getContentURL());
				}
			}
			else if (this.getContentURL().getPath().endsWith(".jar")) {
				ncletClass = loadXletMainClassFromJar(this.getContentURL());
			}

			nclet = null;
			if (ncletClass != null) {
				// Class ncletClass = classLoader.findClass(url.toString());
				nclet = ncletClass.newInstance();
			}
		}
		catch (NullPointerException exc) {
			System.err.println("[ERR] NCLet class loader exception: " + exc.getMessage());
			return;
		}
		catch(NoClassDefFoundError err){
			System.err.println("[ERR] NCLet class loader errot: " + err.getLocalizedMessage());
			return;
		}
		catch (Exception exc) {
			System.err.println("[ERR] NCLet class loader exception: " + exc.getMessage());
			return;
		}
		if(nclet != null && nclet instanceof Xlet){
			try {
				if(arguments != null)
					ncletContext = new NCLetContext(this, arguments);
				else
					ncletContext = new NCLetContext(this);

				if (nclet instanceof Xlet) {
					((Xlet)nclet).initXlet(ncletContext);
					((Xlet)nclet).startXlet();
				}				
			} catch (XletStateChangeException e) {
				System.err.println("[ERROR] Could no start Xlet:" +
						e.getMessage());
			}
		}
		super.play();
	}

	@Override
	public void resume() {
		try {
			((Xlet)nclet).startXlet();
		} catch (XletStateChangeException e) {
			System.err.println("[ERROR] Could no start Xlet:" +
					e.getMessage());
		}
		super.resume();
	}

	@Override
	public void stop() {
		if (nclet instanceof Xlet) {
			try {
				((Xlet)nclet).destroyXlet(true);
			} catch (XletStateChangeException e) {
				System.err.println("[ERROR] Could not destoy Xlet:" +
						e.getMessage());
				//e.printStackTrace();
			}
		}
		super.stop();
	}
	
	@Override
	public boolean hasPresented() {
		return !played;
	}

}
