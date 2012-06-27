/*******************************************************************************
 * Este arquivo Ã© parte da implementaÃ§Ã£o do ambiente de autoria em Nested 
 * Context Language - NCL Eclipse.
 * Direitos Autorais Reservados (c) 2007-2010 UFMA/LAWS (LaboratÃ³rio de Sistemas 
 * AvanÃ§ados da Web)
 *
 * Este programa Ã© software livre; vocÃª pode redistribuÃ­-lo e/ou modificÃ¡-lo sob
 * os termos da LicenÃ§a PÃºblica Geral GNU versÃ£o 2 conforme publicada pela Free 
 * Software Foundation.
 *
 * Este programa Ã© distribuÃ­do na expectativa de que seja Ãºtil, porÃ©m, SEM 
 * NENHUMA GARANTIA; nem mesmo a garantia implÃ­cita de COMERCIABILIDADE OU
 * ADEQUAÃ‡ÃƒO A UMA FINALIDADE ESPECÃ�FICA. Consulte a LicenÃ§a PÃºblica Geral do
 * GNU versÃ£o 2 para mais detalhes. VocÃª deve ter recebido uma cÃ³pia da LicenÃ§a
 * PÃºblica Geral do GNU versÃ£o 2 junto com este programa; se nÃ£o, escreva para a
 * Free Software Foundation, Inc., no endereÃ§o 59 Temple Street, Suite 330,
 * Boston, MA 02111-1307 USA.
 *
 * Para maiores informaÃ§Ãµes:
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.Workbench;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;
import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;
import br.ufma.deinf.laws.ncleclipse.launch.util.GingaVMRemoteUtility;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceInitializer;

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

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();

		if (editor.isDirty()) {

			boolean save = MessageDialog.openQuestion(Workbench.getInstance()
					.getActiveWorkbenchWindow().getShell(),
					"NCL Eclipse Informação", "Deseja salvar as "
							+ "alterações feitas no arquivo " + file.getName()
							+ "?");

			if (save) {
				editor.doSave(null);
			}
		}

		Thread runThread = new Thread() {
			public void run() {
				// Getting default values
				String hostName = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_SSH_RUN_IP);

				String userName = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_SSH_RUN_USER);

				String userPassword = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_SSH_RUN_PASSW);

				String remoteLauncher = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_SSH_RUN_SCRIPT);

				String remoteWorkspace = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_SSH_RUN_WORKSPACE);

				boolean enableRemoteSettings = NCLEditorPlugin
						.getDefault()
						.getPreferenceStore()
						.getBoolean(
								PreferenceConstants.P_ENABLE_REMOTE_SETTINGS);

				String remoteSettingsIni = NCLEditorPlugin.getDefault()
						.getPreferenceStore()
						.getString(PreferenceConstants.P_REMOTE_SETTINGS_PATH);

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
				ConsolePlugin.getDefault().getConsoleManager()
						.addConsoles(consoles);

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
						remoteLauncher, remoteWorkspace, remoteSettingsIni);

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

				// Synchronizing clocks
				consoleStream.println("Synchronizing clocks...");

				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss -z");
				String date = dateFormat.format(new Date());

				try {
					remoteUtility.exec("date --set=\"" + date + "\"");
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

				if (enableRemoteSettings) {
					consoleStream.println("Replacing remote settings...");
					String tmp = "::\t\t= 0\n";
					tmp += "||\t\t= 0\n";

					String[][] settings = PreferenceInitializer
							.parseString(NCLEditorPlugin
									.getDefault()
									.getPreferenceStore()
									.getString(
											PreferenceConstants.P_REMOTE_SETTINGS_VARIABLES));

					for (int i = 0; i < settings.length; i++)
						tmp += settings[i][0] + " = " + settings[i][1] + "\n";

					try {
						remoteUtility.exec("echo \"" + tmp + "\" >"
								+ remoteSettingsIni);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
