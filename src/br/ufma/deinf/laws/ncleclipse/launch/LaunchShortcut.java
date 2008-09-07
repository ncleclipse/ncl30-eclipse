/******************************************************************************
Este arquivo é parte da implementação do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratório de Sistemas Avançados da Web) 

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
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

******************************************************************************
This file is part of the authoring environment in Nested Context Language -
NCL Eclipse.

Copyright: 2007-2008 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.

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
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

*******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.launch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


public class LaunchShortcut implements ILaunchShortcut {
	private String platformPath = Platform.getInstallLocation().getURL().getPath().substring(1);
	@Override
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		String file = ((File)((TreeSelection)selection).getFirstElement()).getLocation().toString();
		run(file);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub
		String file = ((IFileEditorInput) editor.getEditorInput()).getFile().getLocation().toString();
		run(file);
	}
	
	public void run(String file){
		final MessageConsole console = new MessageConsole("Ginga-NCL Player", null);
		final IConsole[] consoles = (IConsole[]) new IConsole[]{ (IConsole) console };
		console.activate();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(consoles);
		final MessageConsoleStream stream = console.newMessageStream();
		
		String gingaNcl = platformPath+"/plugins/ncl_eclipse_1.0.0/gingancl-java";
		System.out.println(Platform.getOS());
		String cmd [];
		if(Platform.getOS().equals("win32")){ //Windows
			cmd = new String[2];
			gingaNcl = platformPath+"plugins/ncl_eclipse_1.0.0/gingancl-java";
			cmd[0] = "\""+gingaNcl+"/gingancl.bat\"";
			cmd[1] = "\""+file+"\"";
			System.out.println(cmd[0]+" "+cmd[1]);
		}
		else { //Linux
			cmd = new String[2];
			gingaNcl = "/"+platformPath+"plugins/ncl_eclipse_1.0.0/gingancl-java/";
			cmd[0] = gingaNcl+"gingancl.sh";
			cmd[1] = file;
			System.out.println(cmd[0] + " "+ cmd[1]);
		}
		try {
			final Process process = DebugPlugin.exec(cmd, new java.io.File(gingaNcl));
			final InputStream is = process.getInputStream();
			final InputStream es = process.getErrorStream();
			
			//IProcess p = DebugPlugin.newProcess(null, process, "Ginga NCL Emulator");
			Thread isThread = new Thread() {
				public void run() {
					BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
					String isString;
					// 	You don't care that the readLine will block
					// 	because it is running in a different thread.
					try {
						while ((isString = isReader.readLine()) != null) {
							// This may need to be executed on the Display
							// Thread. If so, you need to wrap the
							// ocons.println call in a Display.asyncExec() call.
							stream.println(isString);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			isThread.start();

			Thread esThread = new Thread() {
				    public void run() {
				        byte[] buffer = new byte[100];
				        try {
							while (es.read(buffer) != -1) {
								stream.write(buffer);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			};
			esThread.start();
			
			Thread controlPlugin = new Thread(){
				public void run() {
					while(true){
						try{
							int exit = process.exitValue();
							System.out.println("destruindo console");
							ConsolePlugin.getDefault().getConsoleManager().removeConsoles(consoles);
							return;
						}
						catch(IllegalThreadStateException e){
						}
					}
				}
			};
			controlPlugin.start();
			//TODO: Aparecer mensagens no console
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}