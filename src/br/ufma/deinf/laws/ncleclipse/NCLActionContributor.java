/******************************************************************************
Este arquivo eh parte da implementacao do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratorio de Sistemas Avancados da Web) 

Este programa eh software livre; voce pode redistribui-lo e/ou modifica-lo sob 
os termos da Licenca Publica Geral GNU vers√£o 2 conforme publicada pela Free 
Software Foundation.

Este programa eh distribui≠do na expectativa de que seja util, porem, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE OU 
ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licenca Publica Geral do 
GNU versao 2 para mais detalhes. 

Voce deve ter recebido uma copia da Licenca Publica Geral do GNU versao 2 junto 
com este programa; se nao, escreva para a Free Software Foundation, Inc., no 
endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informacoes:
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

package br.ufma.deinf.laws.ncleclipse;


import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.*;

/**
 * Contributes interesting NCL actions to the desktop's Edit menu and the toolbar.
 */
public class NCLActionContributor extends TextEditorActionContributor {

	protected RetargetTextEditorAction fContentAssistProposal;
	protected RetargetTextEditorAction fContentFormat;
	//protected RetargetTextEditorAction fContentAssistTip;
	//protected TextEditorAction fTogglePresentation;

	/**
	 * Default constructor.
	 */
	public NCLActionContributor() {
		super();
		fContentAssistProposal= new RetargetTextEditorAction(NCLEditorMessages.getResourceBundle(), "ContentAssistProposal."); //$NON-NLS-1$
		fContentAssistProposal.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		fContentFormat = new RetargetTextEditorAction(NCLEditorMessages.getResourceBundle(), "ContentFormat.");
		fContentFormat.setActionDefinitionId(NCLEditor.FORMAT_ACTION);
		//fContentAssistTip= new RetargetTextEditorAction(NCLEditorMessages.getResourceBundle(), "ContentAssistTip."); //$NON-NLS-1$
		//fContentAssistTip.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
		//fTogglePresentation= new PresentationAction();
	}
	
	/*
	 * @see IEditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		super.init(bars);
		
		IMenuManager menuManager= bars.getMenuManager();
		IMenuManager editMenu= menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
			editMenu.add(fContentAssistProposal);
			editMenu.add(fContentFormat);
		}	
		
		IToolBarManager toolBarManager= bars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new Separator());
			//toolBarManager.add(fTogglePresentation);
		}
		bars.getToolBarManager().remove(ITextEditorActionConstants.DELETE);
	}
	
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor= null;
		if (part instanceof ITextEditor)
			editor= (ITextEditor) part;

		fContentAssistProposal.setAction(getAction(editor, "ContentAssistProposal")); //$NON-NLS-1$
		fContentFormat.setAction(getAction(editor, "ContentFormat"));
		//fContentAssistTip.setAction(getAction(editor, "ContentAssistTip")); //$NON-NLS-1$

		//fTogglePresentation.setEditor(editor);
		//fTogglePresentation.update();
	}
	
	/*
	 * @see IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		setActiveEditor(null);
		super.dispose();
	}
}
