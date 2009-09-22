package br.ufma.deinf.laws.ncleclipse.hover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension2;
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

public class NCLTextHoverExtension2 extends DefaultTextHover implements ITextHoverExtension2{
	NCLSourceDocument doc = null;
	Object result = null;
	File currentFile = null;
	
	
	public NCLTextHoverExtension2 (ISourceViewer sourceViewer) {
		super(sourceViewer);
	}
	
	public Vector<RegionValues> getRegionChildrenTree (int offset){
		Vector<RegionValues>  tree= new Vector<RegionValues>();
		System.out.println (doc.getCurrentTagname(offset));
		try {
			offset = doc.getLineInformationOfOffset(offset).getOffset();
			offset += doc.getLineInformationOfOffset(offset).getLength() + 1;
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!doc.getCurrentTagname(offset).equals("regionBase")){
			System.out.println (doc.getCurrentTagname(offset));
			RegionValues values = new RegionValues();
			if(doc.getAttributeValueFromCurrentTagName(offset, "top")!=null){
				values.setTop(doc.getAttributeValueFromCurrentTagName(offset, "top"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "left")!=null){
				values.setLeft(doc.getAttributeValueFromCurrentTagName(offset, "left"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "width")!=null){
				values.setWidth(doc.getAttributeValueFromCurrentTagName(offset, "width"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "height")!=null){
				values.setHeight(doc.getAttributeValueFromCurrentTagName(offset, "height"));
			}
			tree.add(values);
			
			
 			offset++;
		}

		return tree;
	}
	
	public Vector<RegionValues> getRegionFatherTree(int offset){
		Vector<RegionValues>  tree= new Vector<RegionValues>();
	
		
		while(doc.getCurrentTagname(offset).equals("region") == true){
			RegionValues values = new RegionValues();
			if(doc.getAttributeValueFromCurrentTagName(offset, "top")!=null){
				values.setTop(doc.getAttributeValueFromCurrentTagName(offset, "top"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "left")!=null){
				values.setLeft(doc.getAttributeValueFromCurrentTagName(offset, "left"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "width")!=null){
				values.setWidth(doc.getAttributeValueFromCurrentTagName(offset, "width"));
			}
			if(doc.getAttributeValueFromCurrentTagName(offset, "height")!=null){
				values.setHeight(doc.getAttributeValueFromCurrentTagName(offset, "height"));
			}
			tree.add(values);
			
			
 			offset = doc.getFatherPartitionOffset(offset);
		}

		return tree;
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
			
			Vector<String> image = new Vector<String>();
			Vector<String> text = new Vector<String> ();
			image.add("bmp");
			image.add("png");
			image.add("gif");
			image.add("jpg");
			
			text.add("html");
			text.add("htm");
			text.add("css");
			text.add("xml");
			text.add("txt");
			
			if (!doc.getCurrentAttribute(offset).equals ("descriptor") && doc.getCurrentTagname(offset).equals("media")){
				String mime = doc.getAttributeValueFromCurrentTagName(offset, "src");
				
				
				int index = mime.indexOf(".");
				String sbstr = mime.substring(index+1);
				
				
				if (text.contains(sbstr)){
			
					result = "";
					String nomeArquivo = doc.getAttributeValueFromCurrentTagName(offset, "src");
					File arquivo = new File (nomeArquivo);
					//Caso o caminho do arquivo seja um caminho completo
					if (arquivo.isFile()){
			        	FileReader in = null;
			        	try {
							in = new FileReader (arquivo);	
							BufferedReader leitor = new BufferedReader (in);
							String tmp, aux = "";
							while((tmp = leitor.readLine()) != null)
								aux += tmp + "\n";
							result = aux;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
					else {
						//Caso o caminho do arquivo seja um caminho relativo	
				    	nomeArquivo = ResourcesPlugin.getWorkspace().getRoot().getLocation() 
								+ currentFile.getParent() + File.separatorChar + doc.getAttributeValueFromCurrentTagName(offset, "src");
						arquivo = new File (nomeArquivo);
				        if (arquivo.isFile()){
				        	FileReader in = null;
				        	try {
								in = new FileReader (arquivo);	
								BufferedReader leitor = new BufferedReader (in);
								String tmp, aux = "";
								while((tmp = leitor.readLine()) != null)
									aux += tmp + "\n";
								result = aux;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        }
					}
				
				}
				else if (image.contains(sbstr)){
					String nomeArquivo = doc.getAttributeValueFromCurrentTagName(offset, "src");
					ImageTest img = null;
					if (new File (nomeArquivo).isFile())
						img = new ImageTest(nomeArquivo);
					else{
						nomeArquivo = ResourcesPlugin.getWorkspace().getRoot().getLocation() 
							+ currentFile.getParent() + File.separatorChar + doc.getAttributeValueFromCurrentTagName(offset, "src");
						if (new File (nomeArquivo).isFile())
							img = new ImageTest(nomeArquivo);
					}
					result = img;
				}
			}
			else if (doc.getCurrentAttribute(offset).equals ("id") && doc.getCurrentTagname(offset).equals ("region")){
				RegionTest t = new RegionTest (getRegionFatherTree(offset));
				result = t;
			}
			else if (doc.getCurrentTagname(offset).equals ("regionBase")){
				
				getRegionChildrenTree(offset);
				
			}else if(doc.getCurrentTagname(offset).equals("descriptor") && doc.getCurrentAttribute(offset).equals("region")){
			
				
				String teste = doc.getAttributeValueFromCurrentTagName(offset,"region");
				
				while(((doc.getCurrentTagname(offset).equals("region")== false) ||
						(doc.getAttributeValueFromCurrentTagName(offset,"id").equals(teste)==false))&&
						(offset>0)){
					offset--;
					
				}
				RegionTest t = new RegionTest (getRegionFatherTree(offset));
				result = t;
			}
			else result = "";
			
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
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		// TODO Auto-generated method stub
		Object aux = result;
		result = null;
		return aux;
	}
}