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


import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * A toolbar action which toggles the presentation model of the
 * connected text editor. The editor shows either the highlight range
 * only or always the whole document.
 */
public class PresentationAction extends TextEditorAction {

	/**
	 * Constructs and updates the action.
	 */
	public PresentationAction() {
		super(NCLEditorMessages.getResourceBundle(), "TogglePresentation.", null); //$NON-NLS-1$
		update();
	}
	
	/* (non-Javadoc)
	 * Method declared on IAction
	 */
	public void run() {

		ITextEditor editor= getTextEditor();

		editor.resetHighlightRange();
		boolean show= editor.showsHighlightRangeOnly();
		setChecked(!show);
		editor.showHighlightRangeOnly(!show);
	}
	
	/* (non-Javadoc)
	 * Method declared on TextEditorAction
	 */
	public void update() {
		setChecked(getTextEditor() != null && getTextEditor().showsHighlightRangeOnly());
		setEnabled(true);
	}
}
