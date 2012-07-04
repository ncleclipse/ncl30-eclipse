/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import br.ufma.deinf.laws.ncleclipse.launch.util.GingaVMRemoteUtility;

public class GingaVMLaunchConfiguration extends LaunchConfigurationDelegate {
	
	public void launch(
			ILaunchConfiguration configuration, 
			String mode,
			ILaunch launch, 
			IProgressMonitor monitor
			) throws CoreException {
			
		// Getting default values
		String hostName = configuration.getAttribute(
				"hostName", "192.168.64.129");
		
		String userName = configuration.getAttribute(
				"userName", "root");
		
		String userPassword = configuration.getAttribute(
				"hostPassword", "telemidia");
		
		String remoteLauncher = configuration.getAttribute(
				"remoteLauncher", "/misc/launcher.sh");
		
		String remoteWorkspace = configuration.getAttribute(
				"remoteWorkspace", "/misc/ncl30");
		
		// Getting workspace path
		String workspace = ResourcesPlugin
			.getWorkspace()
			.getRoot()
			.getLocation()
			.toString();
		
		// Getting active project and active file
		IFile activeFile = null;
		IProject activeProject = null;
		
		IWorkbenchWindow[] windows = 
			PlatformUI.getWorkbench().getWorkbenchWindows();
		
		IWorkbenchWindow window = windows[0];

		if ( window != null ) {
			IWorkbenchPage page = window.getActivePage();
		   
			if ( page != null ) {  
				IEditorPart editor = page.getActiveEditor();
		      
				if ( editor != null ) {
					IEditorInput input = editor.getEditorInput();
					
					if ( input instanceof IFileEditorInput ) {
						IFileEditorInput fileInput = (IFileEditorInput) input;
					  	
						activeFile = fileInput.getFile();
						activeProject = fileInput.getFile().getProject();   
					}
				}
			}		
		
		}
		
		// Creating console
		
		MessageConsole console = 
			new MessageConsole(
					"Ginga-NCL VM Player - "+userName+"@"+hostName,null);
		
		console.activate();
		console.clearConsole();
		
		ConsolePlugin
			.getDefault()
			.getConsoleManager()
			.addConsoles((IConsole[]) new IConsole[] { (IConsole) console });
		
		MessageConsoleStream consoleStream = console.newMessageStream();
		
		//  Validating values
		consoleStream.println("Validating values...");
		
		if (activeFile != null && activeProject != null){
			consoleStream.println("Done!");
		}else{
			consoleStream.println("Fail!");
			return;
		}
	
		// Setting values
		GingaVMRemoteUtility remoteUtility = new GingaVMRemoteUtility(
				hostName,
				userName,
				userPassword,
				consoleStream,
				remoteLauncher,
				remoteWorkspace,
				"");
		
		remoteUtility.setVerboseMode(true);
		
		String workspaceProject = activeProject.getFullPath().toString();		
		String workspaceProjectFile = activeFile.getFullPath().toString();
		
		// Connecting to server
		consoleStream.println(
				"Connecting to server...");
		try {
			remoteUtility.connect();
			consoleStream.println("Done!");
		} catch (IOException e) {
			consoleStream.println("Fail!");
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
			return;
		}
		
		// Copying files to server
		consoleStream.println(
				"Copying files to server...");
		try {
			remoteUtility.commit(workspace+workspaceProject);
			consoleStream.println("Done!");
		} catch (IOException e) {
			consoleStream.println("Fail!");
			return;
		}
		
		// Play application
		consoleStream.println(
				"Playing application on server...");
		try {
			remoteUtility.play(remoteWorkspace+workspaceProjectFile);
			consoleStream.println("Done!");
		} catch (IOException e) {
			consoleStream.println("Fail!");
			return;
		}
		
	}	
}
