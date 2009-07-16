package br.ufma.deinf.laws.ncleclipse.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;

public class NCLTextDocumentProvider extends TextFileDocumentProvider {
	/**
	 * Creates a new properties file document provider and sets up the parent
	 * chain.
	 */
	public NCLTextDocumentProvider() {
		IDocumentProvider provider = new TextFileDocumentProvider(new NCLDocumentProvider());
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
