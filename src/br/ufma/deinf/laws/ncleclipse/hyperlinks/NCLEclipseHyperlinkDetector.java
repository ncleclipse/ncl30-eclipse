package br.ufma.deinf.laws.ncleclipse.hyperlinks;

import java.util.Collection;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.texteditor.ITextEditor;

import br.ufma.deinf.laws.ncl.NCLStructure;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
import br.ufma.deinf.laws.ncleditor.editor.contentassist.NCLSourceDocument;

public class NCLEclipseHyperlinkDetector implements IHyperlinkDetector {
	ITextViewer textViewer = null;
	ITextEditor textEditor = null;

	public NCLEclipseHyperlinkDetector(ITextViewer textViewer, ITextEditor textEditor) {
		this.textViewer = textViewer;
		this.textEditor = textEditor;
	}

	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {

		int offset = region.getOffset();
		NCLSourceDocument doc = (NCLSourceDocument) textViewer.getDocument();
		ITypedRegion typedRegion;
		boolean tmp = false;
		if (!tmp) {
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

						if (nclReference != null && nclReference.size() != 0) {
							IRegion region1 = new Region(startAttributeValue,
									attrValue.length());

							return new IHyperlink[] { new NCLEclipseHyperlink(
									textViewer, textEditor, region1, attrValue) };
						}

					}
				}

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
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
		return new IHyperlink[] { new NCLEclipseHyperlink(textViewer, this.textEditor, region1,
				text) };
	}

}
