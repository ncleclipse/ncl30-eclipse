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
package br.ufma.deinf.laws.ncleclipse.launch.util;

import java.io.IOException;

import org.eclipse.ui.console.MessageConsoleStream;

public class GingaVMRemoteUtility extends RemoteUtility {

	private String remoteLauncher;
	private String remoteWorkspace;
	private String remoteSettingsIni;
	
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
			String remoteWorkspace,
			String remoSettingsIni) {
		
		super(hostName, userName, userPassword, consoleStream);
		
		setRemoteLauncher(remoteLauncher);
		setRemoteWorkspace(remoteWorkspace);
		setRemoteSettingsIni(remoSettingsIni);
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
	
	/**
	 * @return the remoteSettingsIni
	 */
	public String getRemoteSettingsIni() {
		return remoteSettingsIni;
	}

	/**
	 * @param remoteSettingsIni the remoteSettingsIni to set
	 */
	public void setRemoteSettingsIni(String remoteSettingsIni) {
		this.remoteSettingsIni = remoteSettingsIni;
	}
}
