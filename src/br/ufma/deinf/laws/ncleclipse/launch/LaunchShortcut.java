/*******************************************************************************
 * Este arquivo é parte da implementação do ambiente de autoria em Nested 
 * Context Language - NCL Eclipse.
 * Direitos Autorais Reservados (c) 2007-2010 UFMA/LAWS (Laboratório de Sistemas 
 * Avançados da Web)
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob
 * os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
 * Software Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM 
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU
 * ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do
 * GNU versão 2 para mais detalhes. Você deve ter recebido uma cópia da Licença
 * Pública Geral do GNU versão 2 junto com este programa; se não, escreva para a
 * Free Software Foundation, Inc., no endereço 59 Temple Street, Suite 330,
 * Boston, MA 02111-1307 USA.
 *
 * Para maiores informações:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 *******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * Copyright: 2007-2010 UFMA/LAWS (Laboratory of Advanced Web Systems), All
 * Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.launch;

import java.io.IOException;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;
import br.ufma.deinf.laws.ncleclipse.launch.util.GingaVMRemoteUtility;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class LaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		IFile file = ((IFile) ((TreeSelection) selection).getFirstElement());

		run(file);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();

		IFile file = null;

		if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}

		run(file);
	}

	public void run(IFile activeFile) {
		final IFile file = activeFile;

		Thread runThread = new Thread() {
			public void run() {
				// Getting default values
				String hostName = NCLEditorPlugin.getDefault()
						.getPreferenceStore().getString(
								PreferenceConstants.P_SSH_RUN_IP);

				String userName = NCLEditorPlugin.getDefault()
						.getPreferenceStore().getString(
								PreferenceConstants.P_SSH_RUN_USER);

				String userPassword = NCLEditorPlugin.getDefault()
						.getPreferenceStore().getString(
								PreferenceConstants.P_SSH_RUN_PASSW);

				String remoteLauncher = NCLEditorPlugin.getDefault()
						.getPreferenceStore().getString(
								PreferenceConstants.P_SSH_RUN_SCRIPT);

				String remoteWorkspace = NCLEditorPlugin.getDefault()
						.getPreferenceStore().getString(
								PreferenceConstants.P_SSH_RUN_WORKSPACE);

				// Getting workspace path
				String workspace = ResourcesPlugin.getWorkspace().getRoot()
						.getLocation().toString();

				// Creating console
				MessageConsole console = new MessageConsole(
						"Ginga-NCL VM Player - " + userName + "@" + hostName,
						null);

				console.activate();
				console.clearConsole();

				IConsole[] consoles = (IConsole[]) new IConsole[] { (IConsole) console };
				ConsolePlugin.getDefault().getConsoleManager().addConsoles(
						consoles);

				MessageConsoleStream consoleStream = console.newMessageStream();

				// Validating values
				consoleStream.println("Validating values...");

				if (file != null) {
					consoleStream.println("Done!");
				} else {
					consoleStream.println("Fail!");
					try {
						sleep(5);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					return;
				}

				// Setting values
				GingaVMRemoteUtility remoteUtility = new GingaVMRemoteUtility(
						hostName, userName, userPassword, consoleStream,
						remoteLauncher, remoteWorkspace);

				remoteUtility.setVerboseMode(true);

				String workspaceProject = file.getProject().getFullPath()
						.toString();
				String workspaceProjectFile = file.getFullPath().toString();

				// Connecting to server
				consoleStream.println("Connecting to server...");
				try {
					remoteUtility.connect();
					consoleStream.println("Done!");
				} catch (IOException e) {
					consoleStream.println("Fail!");
					try {
						sleep(2);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					return;
				}
				
				//Synchronizing clocks
				consoleStream.println(
					"Synchronizing clocks...");
				
				long ctime = System.currentTimeMillis();
				
				try {
					remoteUtility.exec("date --rfc-3339=\""+ctime+"\"");
					consoleStream.println("Done!");
				} catch (IOException e) {
					consoleStream.println("Fail!");
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					return;
				}
				
				// Copying files to server
				consoleStream.println("Copying files to server...");
				try {
					remoteUtility.commit(workspace + workspaceProject);
					consoleStream.println("Done!");
				} catch (IOException e) {
					consoleStream.println("Fail!");
					try {
						sleep(2);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					return;
				}

				// Play application
				consoleStream.println("Playing application on server...");
				try {
					remoteUtility.play(remoteWorkspace + workspaceProjectFile);
					try {
						sleep(2);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					consoleStream.println("Done!");
				} catch (IOException e) {
					consoleStream.println("Fail!");
					try {
						sleep(2);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ConsolePlugin.getDefault().getConsoleManager()
							.removeConsoles(consoles);
					console.destroy();
					return;
				}
			}
		};

		runThread.start();
	}
}
