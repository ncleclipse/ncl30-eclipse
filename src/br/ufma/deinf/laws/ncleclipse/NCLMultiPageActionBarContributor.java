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

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorPart;

import br.ufma.deinf.laws.ncleclipse.layout.NCLLayoutEditor;
import br.ufma.deinf.laws.ncleclipse.layout.NCLLayoutEditorActionBarContributor;

/**
 * Mix of the TextEditorActionContributor and ActionBarContributor from GEF
 * This class must be changed soon
 * 
 * @author roberto
 *
 */
public class NCLMultiPageActionBarContributor 
extends MultiPageEditorActionBarContributor{
	NCLActionContributor nclActionContributor = null;
	NCLLayoutEditorActionBarContributor nclLayoutActionBarContributor = null;
	IEditorPart activeNestedEditor = null;

	public NCLMultiPageActionBarContributor() {
		super();
		nclActionContributor = new NCLActionContributor();
		nclLayoutActionBarContributor = new NCLLayoutEditorActionBarContributor();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		//super.init(bars, page);
		nclActionContributor.init(bars);
		nclLayoutActionBarContributor.init(bars, page);
    }
	
	public void init(IActionBars bars){
		//super.init(bars);
		//if(activeNestedEditor instanceof NCLLayoutEditor)
			
		//else if(activeNestedEditor instanceof NCLEditor)
		nclActionContributor.init(bars);
		nclLayoutActionBarContributor.init(bars);
	}
	
	public void setActivePage(IEditorPart activeEditor) {
		// TODO Auto-generated method stub
		/*IActionBars actionBars = getActionBars();
		if(actionBars == null) return;
		if(activeEditor instanceof NCLEditor){
			try{
				nclLayoutActionBarContributor.dispose();
			}catch(Exception e){}
			nclActionContributor.setActiveEditor(activeEditor);
			nclActionContributor.init(actionBars);
			getActionBars().updateActionBars();
		}
		else if(activeEditor instanceof NCLLayoutEditor){
			try{
				nclActionContributor.dispose();
			}catch(Exception e){}
			nclLayoutActionBarContributor.init(actionBars);
			nclLayoutActionBarContributor.setActiveEditor(activeEditor);
			getActionBars().updateActionBars();
		}*/
	}

	public void setActiveEditor(IEditorPart part) {
	    if (part instanceof MultiPageEditorPart) {
	        part = ((NCLMultiPageEditor) part).getActivePageAsEditor();
	        activeNestedEditor = part;
	        if(part instanceof NCLLayoutEditor){
	        	nclLayoutActionBarContributor.setActiveEditor(part);
	        }
	        else if(part instanceof NCLEditor){
	        	nclActionContributor.setActiveEditor(part);
	        }
	    }
	    super.setActiveEditor(part);
	}
	/**
	 * Contributes to the given menu.
	 * <p>
	 * The <code>EditorActionBarContributor</code> implementation of this method
	 * does nothing. Subclasses may reimplement to add to the menu portion of this
	 * contribution.
	 * </p>
	 *
	 * @param menuManager the manager that controls the menu
	 *
	public void contributeToMenu(IMenuManager menuManager) {
		/*IEditorPart activeEditor = getActiveNestedEditor(); 
		if( activeEditor instanceof NCLEditor)
			nclActionContributor.contributeToMenu(menuManager);
		else if (activeEditor instanceof NCLLayoutEditor){
			nclLayoutActionBarContributor.contributeToMenu(menuManager);
		}
		else 
		super.contributeToMenu(menuManager); 
	}
	 */
	public IEditorPart getActiveNestedEditor() {
		return activeNestedEditor;
	}

	public void setActiveNestedEditor(IEditorPart activeNestedEditor) {
		this.activeNestedEditor = activeNestedEditor;
	}

	/**
	 * Contributes to the given status line.
	 * <p>
	 * The <code>EditorActionBarContributor</code> implementation of this method
	 * does nothing. Subclasses may reimplement to add to the status line portion of
	 * this contribution.
	 * </p>
	 *
	 * @param statusLineManager the manager of the status line
	 *
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		/*IEditorPart activeEditor = getActiveNestedEditor();
		if( activeEditor instanceof NCLEditor)
			nclActionContributor.contributeToStatusLine(statusLineManager);
		else if (activeEditor instanceof NCLLayoutEditor){
			nclLayoutActionBarContributor.contributeToStatusLine(statusLineManager);
		}
		else 
		super.contributeToStatusLine(statusLineManager);
	}

	/**
	 * Contributes to the given tool bar.
	 * <p>
	 * The <code>EditorActionBarContributor</code> implementation of this method
	 * does nothing. Subclasses may reimplement to add to the tool bar portion of
	 * this contribution.
	 * </p>
	 *
	 * @param toolBarManager the manager that controls the workbench tool bar
	 *
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		/*
		IEditorPart activeEditor = getActiveNestedEditor(); 
		if( activeEditor instanceof NCLEditor)
			nclActionContributor.contributeToToolBar(toolBarManager);
		else if (activeEditor instanceof NCLLayoutEditor){
			nclLayoutActionBarContributor.contributeToToolBar(toolBarManager);
		}
		else
		super.contributeToToolBar(toolBarManager);
	}

	/**
	 * Contributes to the given cool bar.
	 * <p>
	 * The <code>EditorActionBarContributor</code> implementation of this method
	 * does nothing. Subclasses may reimplement to add to the cool bar portion of
	 * this contribution. There can only be contributions from a cool bar or a tool bar.
	 * </p>
	 *
	 * @param coolBarManager the manager that controls the workbench cool bar.
	 * 
	 * @since 3.0
	 *
	public void contributeToCoolBar(ICoolBarManager coolBarManager) {
		/*
		IEditorPart activeEditor = getActiveNestedEditor();
		if( activeEditor instanceof NCLEditor)
			nclActionContributor.contributeToCoolBar(coolBarManager);
		else if (activeEditor instanceof NCLLayoutEditor){
			nclLayoutActionBarContributor.contributeToCoolBar(coolBarManager);
		}
		else 
		super.contributeToCoolBar(coolBarManager);
	}

	/**
	 * The <code>EditorActionBarContributor</code> implementation of this 
	 * <code>IEditorActionBarContributor</code> method does nothing,
	 * subclasses may override.
	 */
	public void dispose() {
		nclActionContributor.dispose();
		nclLayoutActionBarContributor.dispose();
		super.dispose();
		//setActiveEditor(null);
//		/getActionBars().clearGlobalActionHandlers();
	}

	/**
	 * The <code>EditorActionBarContributor</code> implementation of this 
	 * <code>IEditorActionBarContributor</code> method remembers the page
	 * then forwards the call to <code>init(IActionBars)</code> for
	 * backward compatibility
	 */
	/*public void init(IActionBars bars, IWorkbenchPage page) {
		/*IEditorPart activeEditor = getActiveNestedEditor();
	    if( activeEditor instanceof NCLEditor)
	    	nclActionContributor.init(bars, page);
	    else if (activeEditor instanceof NCLLayoutEditor)
	    	nclLayoutActionBarContributor.init(bars, page);
	    else
		super.init(bars, page); 
	}*

	/**
	 * This method calls:
	 * <ul>
	 *  <li><code>contributeToMenu</code> with <code>bars</code>' menu manager</li>
	 *  <li><code>contributeToToolBar</code> with <code>bars</code>' tool bar
	 *    manager</li>
	 *  <li><code>contributeToCoolBar</code> with <code>bars</code>' cool bar
	 *    manager if <code>IActionBars</code> is of extended type <code>IActionBars2</code> </li>
	 *  <li><code>contributeToStatusLine</code> with <code>bars</code>' status line
	 *    manager</li>
	 * </ul>
	 * The given action bars are also remembered and made accessible via 
	 * <code>getActionBars</code>.
	 * 
	 * @param bars the action bars
	 *
	public void init(IActionBars bars) {
		/* IEditorPart activeEditor = getActiveNestedEditor();
	    if( activeEditor instanceof NCLEditor)
	    	nclActionContributor.init(bars);
	    else if (activeEditor instanceof NCLLayoutEditor)
	    	nclLayoutActionBarContributor.init(bars);
	    else*
		super.init(bars);
	}
	*/
	/** The global actions to be connected with editor actions *
	private final static String[] ACTIONS= {
		ITextEditorActionConstants.UNDO,
		ITextEditorActionConstants.REDO,
		ITextEditorActionConstants.CUT,
		ITextEditorActionConstants.COPY,
		ITextEditorActionConstants.PASTE,
		ITextEditorActionConstants.DELETE,
		ITextEditorActionConstants.SELECT_ALL,
		ITextEditorActionConstants.FIND,
		ITextEditorActionConstants.PRINT,
		ITextEditorActionConstants.PROPERTIES,
		ITextEditorActionConstants.REVERT
	};
	
	/** Related to TextEditor **
		/**
		 * Status field definition used on TextEditor
		 *
		private static class StatusFieldDef {
			private String category;
			private String actionId;
			private boolean visible;
			private int widthInChars;
	
			private StatusFieldDef(String category, String actionId, boolean visible, int widthInChars) {
				Assert.isNotNull(category);
				this.category= category;
				this.actionId= actionId;
				this.visible= visible;
				this.widthInChars= widthInChars;
			}
		}

	/** Related to Layout Page **
	
		private List globalActionKeys = new ArrayList();
		private List retargetActions = new ArrayList();
		private ActionRegistry registry = new ActionRegistry();
		private IToolBarManager toolBarManager = null;
	
	/**
	* Initialization
	*
	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}
	
	/**
	* Builds the actions.
	*
	* @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	*
	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}
	
	/**
	* Adds the retarded actions.
	*
	* @param action
	* The action to add
	*
	
	protected void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}
	/**
	* Adds global action key.
	*
	* @param key
	* The key to add
	*
	
	protected void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}
	/**
	* Adds to action registry an action.
	*
	* @param action
	* The action to add
	*
	
	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}
	/**
	* Gets the registry.
	*
	* @return ActionRegistry
	* The registry
	*
	
	protected ActionRegistry getActionRegistry() {
		return registry;
	}
	/**
	* Declares the global action keys.
	*
	* @see
	org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	*
	
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());
		addGlobalActionKey(ActionFactory.DELETE.getId());
	}

	/**
	 * 
	 * @param id
	 * @return
	 *

	 protected IAction getAction(String id) {
		return getActionRegistry().getAction(id);
	}
	/**
	* Adds the undo and redo items to the toolbar.
	*
	* @param tbm The IToolBarManager
	* @see
	org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
	*
	 
	public void contributeToToolBar(IToolBarManager tbm) {
	 	tbm.add(getAction(ActionFactory.UNDO.getId()));
		tbm.add(getAction(ActionFactory.REDO.getId()));
		tbm.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager = tbm;
	}
	/**
	 * Remove action from toolbar when layout editor lost the focus
	 *
	public void removeFromToolBar(){
		toolBarManager.remove(ActionFactory.UNDO.getId());
		toolBarManager.remove(ActionFactory.REDO.getId());
		toolBarManager.remove(ActionFactory.DELETE.getId());
	}
	/**
	* Sets the page to active status.
	*
	* @param activeEditor
	* The active editor
	*
/**	public void setActivePage(IEditorPart activeEditor) {
		ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
		if(registry == null) return;
		IActionBars bars = getActionBars();
		if(activeEditor instanceof NCLLayoutEditor){
			for (int i = 0; i < globalActionKeys.size(); i++){
				String id = (String) globalActionKeys.get(i);
				bars.setGlobalActionHandler(id, registry.getAction(id));
			}
			getActionBars().updateActionBars();
		}
		else {
			bars.clearGlobalActionHandlers();
			getActionBars().updateActionBars();
			dispose();
		}
	}
**/	
/**	public void dispose(){
		removeFromToolBar();
		super.dispose();
	} **/
}