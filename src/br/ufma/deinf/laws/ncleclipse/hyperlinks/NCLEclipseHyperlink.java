package br.ufma.deinf.laws.ncleclipse.hyperlinks;

import java.io.File;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;

public class NCLEclipseHyperlink implements IHyperlink {
	int start;
	int length;

	private String text;
	private IRegion region;
	private File currentFile;

	ITextViewer textViewer;
	ITextEditor textEditor;

	public NCLEclipseHyperlink(ITextViewer textViewer, ITextEditor textEditor, IRegion region, String text) {
		this.region = region;
		this.text = text;

		this.textViewer = textViewer;
		this.textEditor = textEditor;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public void open() {
		if (text != null) {
			System.out.println("Moving...");	
			
			try {
				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				IWorkbenchPage page = win.getActivePage();
				IEditorPart editor = page.getActiveEditor();
				
				if (editor.getEditorInput() instanceof IFileEditorInput) {
					currentFile = ((IFileEditorInput) editor.getEditorInput())
							.getFile().getFullPath().toFile();
				} else {
					currentFile = new File(((IURIEditorInput) editor
							.getEditorInput()).getURI());
				}
				
				String nclText = textViewer.getDocument().get();
				NCLContentHandler nclContentHandler = new NCLContentHandler();
				NCLDocument nclDocument = new NCLDocument();
				nclDocument.setParentURI(currentFile.getParentFile().toURI());
				nclContentHandler.setNclDocument(nclDocument);
				NCLParser parser = new NCLParser();
				parser.setContentHandler(nclContentHandler);		
				parser.doParse(nclText);
				
				NCLElement el = nclDocument.getElementById(text);
				
				int line = el.getLineNumber();
				int lineOffset = textViewer.getDocument().getLineOffset(line);
				int lineLength = textViewer.getDocument().getLineLength(line);
				
				//Move cursor to new position
				textEditor.resetHighlightRange();
				textEditor.setHighlightRange(lineOffset, lineLength, true);
				textEditor.setFocus();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getTypeLabel() {
		return null;
	}

	public String getHyperlinkText() {
		return null;
	}

}
