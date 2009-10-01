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

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.laws.ncleclipse.NCLEditor;
import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.util.DocumentUtil;

/**
 * This class is responsible for open a hyperlink.
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLEclipseHyperlink implements IHyperlink {
	int start;
	int length;

	private String text;
	private IRegion region;

	private ITextViewer textViewer;
	private NCLElement nclElement;

	public NCLEclipseHyperlink(ITextViewer textViewer, IRegion region,
			String text) {
		this.region = region;
		this.text = text;
		this.textViewer = textViewer;
		this.nclElement = null;
	}
	
	public NCLEclipseHyperlink(ITextViewer textViewer, IRegion region,
			String text, NCLElement nclElement) {
		this.region = region;
		this.text = text;
		this.textViewer = textViewer;
		this.nclElement = nclElement;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public void open() {
		if (text != null) {
			try {
				int indexOfPound = text.indexOf("#");
				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				IWorkbenchPage page = win.getActivePage();
				NCLEditor editor = ((NCLMultiPageEditor) page.getActiveEditor())
						.getNCLEditor();
				if (indexOfPound == -1) { // not alias. So, set focus to
					// elementId
					editor.setFocusToElementId(text);

				}
				// the hiperlink text has alias so open the editor
				else {
					String nclText = editor.getInputDocument().get();
					NCLContentHandler nclContentHandler = new NCLContentHandler();
					NCLDocument nclDocument = new NCLDocument();
					File currentFile = editor.getCurrentFile();
					if (currentFile == null)
						return;
					nclDocument.setParentURI(currentFile.getParentFile()
							.toURI());
					nclContentHandler.setNclDocument(nclDocument);
					NCLParser parser = new NCLParser();
					parser.setContentHandler(nclContentHandler);
					parser.doParse(nclText);
					NCLElement el = nclDocument.getElementByAlias(text
							.substring(0, indexOfPound));
					String file = el.getAttributeValue("documentURI");
					if (file != null) {
						String fileAbsolutePath = DocumentUtil
								.getAbsoluteFileName(editor.getCurrentFile()
										.getAbsolutePath(), file);
						// open as an external file
						OpenEditorAction openEditorAction = new OpenEditorAction();
						openEditorAction.setExternalFile(fileAbsolutePath, text
								.substring(indexOfPound + 1, text.length()));
						openEditorAction.run();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getTypeLabel() {
		
		return null;
	}

	public String getHyperlinkText() {
		return "<" + nclElement.getTagName() + " id=\"" + nclElement.getAttributeValue("id") + "\"  ...\\>";
	}

}
