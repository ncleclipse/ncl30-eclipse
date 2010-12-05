/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.launch.util;

import java.io.IOException;

import org.eclipse.ui.console.MessageConsoleStream;

public class GingaVMRemoteUtility extends RemoteUtility {

	private String remoteLauncher;
	
	private String remoteWorkspace;
	
	public GingaVMRemoteUtility(
			String hostName, 
			String userName,
			String userPassword,
			MessageConsoleStream consoleStream) {
		
		super(hostName, userName, userPassword, consoleStream);
	}
	
	public GingaVMRemoteUtility(
			String hostName, 
			String userName,
			String userPassword,
			MessageConsoleStream consoleStream,
			String remoteLauncher,
			String remoteWorkspace) {
		
		super(hostName, userName, userPassword, consoleStream);
		
		setRemoteLauncher(remoteLauncher);
		setRemoteWorkspace(remoteWorkspace);
	}
	
	public void commit(String workspaceProject) 
		throws IOException {

		commit(workspaceProject, remoteWorkspace);
	}

	public void play(String workspaceProjectFile) 
		throws IOException {
		exec("export LD_LIBRARY_PATH=/usr/local/lib/lua/5.1/socket:" +
				"/usr/local/lib/ginga:" +
				"/usr/local/lib/ginga/adapters:" +
				"/usr/local/lib/ginga/cm:" +
				"/usr/local/lib/ginga/converters:" +
				"/usr/local/lib/ginga/ic:" +
				"/usr/local/lib/ginga/iocontents:" +
				"/usr/local/lib/ginga/players:" +
				"/usr/local/lib/ginga/dp:" +
				"/usr/local/lib/ginga/epgfactory:" +
				"$LD_LIBRARY_PATH ;" +
				remoteLauncher +
				" " +
				format(workspaceProjectFile));
	}

	public String getRemoteLauncher() {
		return remoteLauncher;
	}

	public void setRemoteLauncher(String remoteLauncher) {
		this.remoteLauncher = remoteLauncher;
	}

	public String getRemoteWorkspace() {
		return remoteWorkspace;
	}

	public void setRemoteWorkspace(String remoteWorkspace) {
		this.remoteWorkspace = remoteWorkspace;
	}
}
