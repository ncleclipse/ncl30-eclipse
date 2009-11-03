package br.ufma.deinf.laws.ncleclipse.hover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;

public class NCLTextHoverExtension2 extends DefaultTextHover implements
		ITextHoverExtension2 {

	NCLSourceDocument doc = null;
	Object result = null;
	File currentFile = null;
	boolean hasfather = true;

	public NCLTextHoverExtension2(ISourceViewer sourceViewer) {
		super(sourceViewer);
	}

	public Vector<RegionValues> getRegionFatherTree(int offset) {
		Vector<RegionValues> tree = new Vector<RegionValues>();
		Vector<Integer> offsets = new Vector<Integer>();



		while (doc.getCurrentTagname(offset).equals("region") == true) {
			offsets.add(offset);
			offset = doc.getFatherPartitionOffset(offset);
		}

		for (int i = offsets.size() - 1; i >= 0; i--) {
			RegionValues values = new RegionValues();
			if (doc.getAttributeValueFromCurrentTagName(offsets.get(i), "top") != null) {
				values.setTop(doc.getAttributeValueFromCurrentTagName(offsets
						.get(i), "top"));
			}
			if (doc.getAttributeValueFromCurrentTagName(offsets.get(i), "left") != null) {
				values.setLeft(doc.getAttributeValueFromCurrentTagName(offsets
						.get(i), "left"));
			}
			if (doc
					.getAttributeValueFromCurrentTagName(offsets.get(i),
							"width") != null) {
				values.setWidth(doc.getAttributeValueFromCurrentTagName(offsets
						.get(i), "width"));
			}
			if (doc.getAttributeValueFromCurrentTagName(offsets.get(i),
					"height") != null) {
				values.setHeight(doc.getAttributeValueFromCurrentTagName(
						offsets.get(i), "height"));
			}
			if (doc.getAttributeValueFromCurrentTagName(offsets.get(i),
					"bottom") != null) {
				values.setBottom(doc.getAttributeValueFromCurrentTagName(
						offsets.get(i), "bottom"));
			}
			if (doc
					.getAttributeValueFromCurrentTagName(offsets.get(i),
							"right") != null) {
				values.setRigth(doc.getAttributeValueFromCurrentTagName(offsets
						.get(i), "right"));
			}

			tree.add(values);

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
			currentFile = new File(((IURIEditorInput) editor.getEditorInput())
					.getURI());
		}

		try {
			doc = (NCLSourceDocument) textViewer.getDocument();
			typedRegion = (TypedRegion) doc.getPartition(offset);

			if (typedRegion.getType() != XMLPartitionScanner.XML_START_TAG)
				return null;

			Vector<String> image = new Vector<String>();
			Vector<String> text = new Vector<String>();
			Vector<String> audio = new Vector<String>();
			Vector<String> video = new Vector<String>();
			
			image.add("bmp");
			image.add("png");
			image.add("gif");
			image.add("jpg");

			text.add("html");
			text.add("htm");
			text.add("css");
			text.add("xml");
			text.add("txt");

			audio.add("wav");
			audio.add("mp3");
			audio.add("mp2");
			audio.add("mp4");
			audio.add("mpg4");

			video.add("mpeg");
			video.add("mpg");
			
			String CurrentAttribute = doc.getCurrentAttribute(offset);
			String CurrentTagname = doc.getCurrentTagname(offset);
			
			
			if ((!CurrentAttribute.equals("descriptor") && CurrentTagname.equals("media") ) || 
					( CurrentTagname.equals("descriptor") && ( CurrentAttribute.equals("focusSelSrc") || 
									CurrentAttribute.equals("focusSrc") ) ) ) {
				String mime = doc.getAttributeValueFromCurrentTagName(offset,
						"src");
				if (mime == null){
					mime = doc.getAttributeValueFromCurrentTagName(offset, "focusSelSrc");
					if (mime == null)
						mime = doc.getAttributeValueFromCurrentTagName(offset, "focusSrc");
				}
				
			
				System.out.println(mime.substring(0, 4));
				
				//if()
				if(mime.substring(0, 4).equals("http")){
					URL url = new URL(mime);
					if(url!=null){
						System.out.println(url.getProtocol());
						result= new PreHtml(300,300," ",url.toString());
					}
				}
				
				String values[] = mime.split("\\.");
				String sbstr = "";
				if (values.length > 1)
					sbstr = values[values.length - 1];
				
				if (audio.contains(sbstr) || video.contains(sbstr)) {

					String nomeArquivo = mime;

					File arquivo = new File(nomeArquivo);
					if (arquivo.isFile()) {
						result = new MediaTest(nomeArquivo, sbstr);

					} else {
						nomeArquivo = ResourcesPlugin.getWorkspace().getRoot()
								.getLocation()
								+ currentFile.getParent()
								+ File.separatorChar
								+ mime;
						arquivo = new File(nomeArquivo);
						if (arquivo.isFile())

							result = new MediaTest(nomeArquivo, sbstr);

					}

				}
				if (text.contains(sbstr)) {
					result = "";
					String nomeArquivo = mime;
					
					
					if (sbstr.equals("html") || sbstr.equals("xml")
							|| sbstr.equals("htm")) {
						
						
						File arquivo = new File(nomeArquivo);

						if (arquivo.isFile()) {
							FileReader in = null;
							try {
								in = new FileReader(arquivo);
								BufferedReader leitor = new BufferedReader(in);
								String tmp, aux = "";
								while ((tmp = leitor.readLine()) != null)
									aux += tmp + "\n";
								PreHtml pre = new PreHtml(300, 300, aux,
										nomeArquivo);
								result = pre;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							nomeArquivo = ResourcesPlugin.getWorkspace()
									.getRoot().getLocation()
									+ currentFile.getParent()
									+ File.separatorChar
									+ mime;
							arquivo = new File(nomeArquivo);
							if (arquivo.isFile()) {
								FileReader in = null;
								try {
									in = new FileReader(arquivo);
									BufferedReader leitor = new BufferedReader(
											in);
									String tmp, aux = "";
									while ((tmp = leitor.readLine()) != null)
										aux += tmp + "\n";

									PreHtml pre = new PreHtml(300, 300, aux,
											nomeArquivo);
									result = pre;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} else if (sbstr.equals("txt") || sbstr.equals("css")){
						File arquivo = new File(nomeArquivo);
						// Caso o caminho do arquivo seja um caminho
						// completo
						if (arquivo.isFile()) {
							FileReader in = null;
							try {
								in = new FileReader(arquivo);
								BufferedReader leitor = new BufferedReader(in);
								String tmp, aux = "";
								while ((tmp = leitor.readLine()) != null)
									aux += tmp + "\n";
								result = aux;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							// Caso o caminho do arquivo seja um caminho
							// relativo
							nomeArquivo = ResourcesPlugin.getWorkspace()
									.getRoot().getLocation()
									+ currentFile.getParent()
									+ File.separatorChar
									+ mime;
							arquivo = new File(nomeArquivo);
							if (arquivo.isFile()) {
								FileReader in = null;
								try {
									in = new FileReader(arquivo);
									BufferedReader leitor = new BufferedReader(
											in);
									String tmp, aux = "";
									while ((tmp = leitor.readLine()) != null)
										aux += tmp + "\n";
									result = aux;
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				} else if (image.contains(sbstr)) {
					String nomeArquivo = mime;
					ImageTest img = null;
					if (new File(nomeArquivo).isFile())
						img = new ImageTest(nomeArquivo);
					else {
						nomeArquivo = ResourcesPlugin.getWorkspace().getRoot()
								.getLocation()
								+ currentFile.getParent()
								+ File.separatorChar
								+ mime;
						if (new File(nomeArquivo).isFile())
							img = new ImageTest(nomeArquivo);
					}
					// Image image = new
					// Image(NCLConfiguration.getInformationControlCreator().getShell(),
					// nomeArquivo);
					//result = img;
					result=img;
				}
			} else if (doc.getCurrentAttribute(offset).equals("id")
					&& doc.getCurrentTagname(offset).equals("region")) {
				RegionTest t = new RegionTest(getRegionFatherTree(offset));
				result = t;
			} else if (doc.getCurrentTagname(offset).equals("descriptor") &&
					doc.getCurrentAttribute(offset).equals("region")) {

				String teste = doc.getAttributeValueFromCurrentTagName(offset,
						"region");
				int aux=offset;
				while (((doc.getCurrentTagname(aux).equals("region") == false) || (doc
						.getAttributeValueFromCurrentTagName(aux, "id")
						.equals(teste) == false))
						&& (aux > 0)) {
					aux--;

				}
				RegionTest t = new RegionTest(getRegionFatherTree(aux));
			
				result = t;
			} 
			else
				result = "";

			Point selection = textViewer.getSelectedRange();
			if (selection.x <= offset && offset < selection.x + selection.y)
				return new Region(selection.x, selection.y);
			return new Region(offset, 0);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("pau");
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