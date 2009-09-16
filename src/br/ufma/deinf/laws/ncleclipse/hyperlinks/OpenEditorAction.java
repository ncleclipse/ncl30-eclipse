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
package br.ufma.deinf.laws.ncleclipse.hyperlinks;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

/**
 * This class implements an action responsible to open an editor for a 
 * file (external on external).
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class OpenEditorAction extends Action {
	private IFile fFile;
	private IWorkbenchPage fPage;
	private String fExternalFile;

	public void setExternalFile(String externalFile) {
		fExternalFile = externalFile;
	}
	
	public void setInternalFile(String externalFile) {
		fExternalFile = externalFile;
	}

	public void run() {
		// open external file
		if (fExternalFile != null && !fExternalFile.equals("")) {
			File fileToOpen = new File(fExternalFile);
			if (fileToOpen.exists() && fileToOpen.isFile()) {
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(
						fileToOpen.toURI());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();

				try {
					IDE.openEditorOnFileStore(page, fileStore);
				} catch (PartInitException e1) {
					// Put your exception handler here if you wish to
				}
			} else {
				// Do something if the file does not exist
			}
		} 
		//Open internal file
		else {
			// get the Active Workbench Page
			fPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage();
			IEditorDescriptor desc = PlatformUI.getWorkbench()
					.getEditorRegistry().getDefaultEditor(fFile.getName());
			try {
				fPage.openEditor(new FileEditorInput(fFile), desc.getId());
			} catch (PartInitException e) {

			}
		}
	}
}
