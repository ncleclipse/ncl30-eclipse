package br.ufma.deinf.laws.ncleclipse.hover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.deinf.ufma.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
 
public class NCLTextHover extends DefaultTextHover{

	NCLSourceDocument doc = null;
	String result = "";
	File currentFile = null;
	
	public NCLTextHover(ISourceViewer sourceViewer) {
		super(sourceViewer);
	}


	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		TypedRegion typedRegion;
		
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
		
		
		try {
			doc = (NCLSourceDocument) textViewer.getDocument();
			typedRegion = (TypedRegion) doc.getPartition(offset);

			if (typedRegion.getType() != XMLPartitionScanner.XML_START_TAG)
				return null;
			if (doc.getCurrentAttribute(offset).equals ("src") && doc.getCurrentTagname(offset).equals("media")){
				if (doc.getAttributeValueFromCurrentTagName(offset, "type").equals("text/plain")){
					System.out.println("sim");
					//Descobrir o pq não consigo pegar o diretório do arquivo
			    	String nomeArquivo =  "/media/Dados/runtime-New_configuration" + currentFile.getParent() + "/" + doc.getAttributeValueFromCurrentTagName(offset, "src");
			    	File arquivo = new File (nomeArquivo);
			        if (arquivo.isFile()){
			        	System.out.println (currentFile.getParentFile().toString());
			        	FileReader in = null;
			        	try {
							in = new FileReader (arquivo);	
							BufferedReader leitor = new BufferedReader (in);
							String tmp;
							while((tmp = leitor.readLine()) != null)
								result += tmp + "\n";
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        } 
				}
			}
			else result = "false";
			
			Point selection = textViewer.getSelectedRange();
				if (selection.x <= offset && offset < selection.x + selection.y)
					return new Region(selection.x, selection.y);
			return new Region(offset, 0);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		// TODO Auto-generated method stub
		return result;
	}

	

}