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
		
		//  Validating values
		System.out.println("[#] Validating values...");
		
		if (activeFile != null && activeProject != null){
			System.out.println("[#] Done!");
		}else{
			System.out.println("[#] Fail!");
			return;
		}
		
		System.out.println();
		
		String workspaceProject = activeProject.getFullPath().toString();
		
		String workspaceProjectFile = activeFile.getFullPath().toString();
		
		// Setting values
		GingaVMRemoteUtility remoteUtility = new GingaVMRemoteUtility(
				hostName,
				userName,
				userPassword,
				remoteLauncher,
				remoteWorkspace);
		
		remoteUtility.setVerboseMode(true);
		
		// Connecting to server
		System.out.println(
				"[#] Connecting to server" +
				" " +
				"(" +
				userName +
				"@" +
				hostName +
				")" +
				"...");
		try {
			remoteUtility.connect();
			System.out.println("[#] Done!");
		} catch (IOException e) {
			System.out.println("[#] Fail!");
			return;
		}
		
		System.out.println();
		
		// Commit to server
		System.out.println(
				"[#] Committing files to server...");
		try {
			remoteUtility.commit(workspace+workspaceProject);
			System.out.println("[#] Done!");
		} catch (IOException e) {
			System.out.println("[#] Fail!");
			return;
		}
		
		System.out.println();
		
		// Play application
		System.out.println(
				"[#] Playing application on server...");
		try {
			remoteUtility.play(remoteWorkspace+workspaceProjectFile);
			System.out.println("[#] Done!");
		} catch (IOException e) {
			System.out.println("[#] Fail!");
			return;
		}
		
		System.out.println();
	}	
}
