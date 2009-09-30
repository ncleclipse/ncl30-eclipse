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
package br.ufma.deinf.laws.ncleclipse;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.layout.NCLLayoutEditor;
import br.ufma.deinf.laws.ncleclipse.layout.NCLLayoutEditorActionBarContributor;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;

public class NCLMultiPageEditor extends MultiPageEditorPart implements IResourceChangeListener{
	/** The text editor used in page 0. */
	private NCLEditor editor = null;
	private NCLLayoutEditor layoutEditor = null;
	
	/**
	 * Creates a multi-page editor example.
	 */
	public NCLMultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createNCLEditorPage() {
		try {
			editor = new NCLEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "NCL File");
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which allows you to change the font used in page 2.
	 */
	void createLayoutViewPage() {
		try {
			layoutEditor = new NCLLayoutEditor();
			int index = addPage(layoutEditor, getEditorInput());
			layoutEditor.setNclSourceDocument((NCLSourceDocument)editor.getInputDocument());
			setPageText(index, "Layout");
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested layout editor",
				null,
				e.getStatus());
		}
	}
	
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createNCLEditorPage();
		if(NCLEditorPlugin.getDefault().getPreferenceStore().
				getBoolean(PreferenceConstants.P_NCL_LAYOUT_EDITOR_ACTIVATE) 
			)
			createLayoutViewPage();
		updateTitle();
	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		IEditorPart editor = getEditor(0);
		editor.doSave(monitor);
		//Ajeitar
		if(layoutEditor != null){
			editor = getEditor(1);
			editor.doSave(monitor);
		}
		updateTitle();
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
		updateTitle();
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		//if (!(editorInput instanceof IFileEditorInput) && !(editorInput instanceof IStorageEditorInput))
			//throw new PartInitException("Invalid Input: Must be IFileEditorInput");	
		super.init(site, editorInput);
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of page 1 when the it is activated.
	 */
	NCLActionContributor nclActionContributor = null;
	NCLLayoutEditorActionBarContributor nclLayoutActionBarContributor = null;
	boolean layoutActive = false;
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			layoutEditor.refreshGraphicalViewer();
			layoutActive = true;
		}
		else if(newPageIndex == 0 && layoutEditor != null && layoutActive){
			layoutEditor.refreshNCLSourceDocument();
			layoutActive = false;
		}

		NCLMultiPageActionBarContributor ac = new NCLMultiPageActionBarContributor();		
		ac.setActiveEditor(getEditor(newPageIndex));
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}

	public NCLEditor getNCLEditor(){
		return editor;
	}
	
	public IEditorPart getActivePageAsEditor(){
		return getEditor(getActivePage());
	}
	
	void updateTitle() {
		  IEditorInput input = getNCLEditor().getEditorInput();
		  setPartName(editor.getInput().getName());
		  setTitleToolTip(input.getToolTipText());
	}

}
