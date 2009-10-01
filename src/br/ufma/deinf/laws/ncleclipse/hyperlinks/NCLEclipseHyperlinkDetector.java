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
import java.util.Collection;
import java.util.Vector;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncleclipse.NCLEditor;
import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLEclipseHyperlinkDetector implements IHyperlinkDetector {
	ITextViewer textViewer = null;

	public NCLEclipseHyperlinkDetector(ITextViewer textViewer) {
		this.textViewer = textViewer;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {

		// get the region offset
		int offset = region.getOffset();
		NCLSourceDocument doc = (NCLSourceDocument) textViewer.getDocument();
		ITypedRegion typedRegion;
		boolean tmp = false;
		if (!tmp) { // test
			try {
				typedRegion = doc.getPartition(region.getOffset());

				// Return null if partition is different to XML_START_TAG
				if (typedRegion.getType() != XMLPartitionScanner.XML_START_TAG)
					return null;

				// get the current tagname
				String tagname = doc.getCurrentTagname(offset);
				int startRegionOffset = typedRegion.getOffset();

				// calculate just if the offset is a attribute value
				if (doc.isAttributeValue(offset)) {
					String currentAttr = doc.getCurrentAttribute(offset);
					String attrValue = doc.getAttributeValueFromCurrentTagName(
							offset, currentAttr);
					int startAttributeValue = doc
							.getStartAttributeValueOffset(offset) + 1;
					if (startAttributeValue != -1 && !attrValue.equals("")) {
						NCLStructure nclStructure = NCLStructure.getInstance();
						Collection nclReference = nclStructure.getNCLReference(
								tagname, currentAttr);
						if (nclReference == null || nclReference.size() == 0) {
							Collection c = nclStructure.getNCLReverseReference(
									tagname, currentAttr);

							IWorkbench wb = PlatformUI.getWorkbench();
							IWorkbenchWindow win = wb
									.getActiveWorkbenchWindow();
							IWorkbenchPage page = win.getActivePage();
							NCLEditor editor = ((NCLMultiPageEditor) page
									.getActiveEditor()).getNCLEditor();
							String nclText = editor.getInputDocument().get();
							NCLContentHandler nclContentHandler = new NCLContentHandler();
							NCLDocument nclDocument = new NCLDocument();
							File currentFile = editor.getCurrentFile();
							nclDocument.setParentURI(currentFile
									.getParentFile().toURI());
							nclContentHandler.setNclDocument(nclDocument);
							NCLParser parser = new NCLParser();
							parser.setContentHandler(nclContentHandler);
							parser.doParse(nclText);

							Vector<NCLElement> v = nclDocument.getElements(
									tagname, attrValue);
							IHyperlink[] values = new NCLEclipseHyperlink[v
									.size()];

							if (c != null && c.size() != 0 && v != null) {
								IRegion region1 = new Region(
										startAttributeValue, attrValue.length());

								for (int i = 0; i < v.size(); i++)
									values[i] = new NCLEclipseHyperlink(
											textViewer, region1, v.get(i)
													.getAttributeValue("id"), v
													.get(i));

								return values;
							}
						} else {
							IRegion region1 = new Region(startAttributeValue,
									attrValue.length());

							return new IHyperlink[] { new NCLEclipseHyperlink(
									textViewer, region1, attrValue) };
						}
					}
				}

			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			return null;
		}
		IRegion lineInfo;
		String line;
		try {
			lineInfo = doc.getLineInformationOfOffset(offset);
			line = doc.get(lineInfo.getOffset(), lineInfo.getLength());
		} catch (BadLocationException ex) {
			return null;
		}
		int begin = line.indexOf("<");
		int end = line.indexOf(">");
		if (end < 0 || begin < 0 || end == begin + 1)
			return null;
		String text = line.substring(begin + 1, end);
		IRegion region1 = new Region(lineInfo.getOffset() + begin + 1, text
				.length() - 1);

		// Return new Hiperlink
		return new IHyperlink[] { new NCLEclipseHyperlink(textViewer, region1,
				text) };
	}

}
