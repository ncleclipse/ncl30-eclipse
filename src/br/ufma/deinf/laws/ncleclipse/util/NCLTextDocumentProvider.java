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
package br.ufma.deinf.laws.ncleclipse.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 * 
 */
public class NCLTextDocumentProvider extends TextFileDocumentProvider {
	/**
	 * Creates a new properties file document provider and sets up the parent
	 * chain.
	 */
	public NCLTextDocumentProvider() {
		IDocumentProvider provider = new TextFileDocumentProvider(
				new NCLDocumentProvider());
		setParentDocumentProvider(provider);
	}

	protected FileInfo createFileInfo(Object element) throws CoreException {
		FileInfo info = super.createFileInfo(element);
		if (info == null) {
			info = createEmptyFileInfo();
		}
		System.out.println(info.fTextFileBuffer.getDocument().getClass());
		IDocument document = info.fTextFileBuffer.getDocument();
		if (document != null) {
			IDocumentPartitioner partitioner = new XMLPartitioner(
					new XMLPartitionScanner(), new String[] {
							XMLPartitionScanner.XML_START_TAG,
							XMLPartitionScanner.XML_PI,
							XMLPartitionScanner.XML_DOCTYPE,
							XMLPartitionScanner.XML_END_TAG,
							XMLPartitionScanner.XML_TEXT,
							XMLPartitionScanner.XML_CDATA,
							XMLPartitionScanner.XML_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return info;
	}
}
