package br.ufma.deinf.laws.ncleclipse.hover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
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

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;

public class NCLTextHoverExtension extends DefaultTextHover implements
		ITextHoverExtension2, ITextHover {

	NCLSourceDocument doc = null;
	Object result = null;
	File currentFile = null;
	boolean hasfather = true;

	public NCLTextHoverExtension(ISourceViewer sourceViewer) {
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

	private void hoverAudioVideo(String nameFile, String type, String path) {
		File arquivo = new File(nameFile);
		if (arquivo.isFile()) {
			result = new PreViewMedia(nameFile, type);

		} else {
			nameFile = path + "/" + nameFile;
			arquivo = new File(nameFile);
			if (arquivo.isFile())

				result = new PreViewMedia(nameFile, type);

		}
	}

	private void hoverText(String nameFile, String sbstr, String path) {
		result = "";

		if (sbstr.equals("html") || sbstr.equals("xml") || sbstr.equals("htm")) {

			File arquivo = new File(nameFile);

			if (arquivo.isFile()) {
				FileReader in = null;
				try {
					in = new FileReader(arquivo);
					BufferedReader leitor = new BufferedReader(in);
					String tmp, aux = "";
					while ((tmp = leitor.readLine()) != null)
						aux += tmp + "\n";
					PreViewXML pre = new PreViewXML(300, 300, aux, nameFile);
					result = pre;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				nameFile = path + "/" + currentFile.getParent() + "/"
						+ nameFile;
				arquivo = new File(nameFile);
				if (arquivo.isFile()) {
					FileReader in = null;
					try {
						in = new FileReader(arquivo);
						BufferedReader leitor = new BufferedReader(in);
						String tmp, aux = "";
						while ((tmp = leitor.readLine()) != null)
							aux += tmp + "\n";

						PreViewXML pre = new PreViewXML(300, 300, aux, nameFile);
						result = pre;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else if (sbstr.equals("txt") || sbstr.equals("css")) {
			File file = new File(nameFile);
			// Caso o caminho do arquivo seja um caminho
			// completo
			if (file.isFile()) {
				FileReader in = null;
				try {
					in = new FileReader(file);
					BufferedReader leitor = new BufferedReader(in);
					String tmp, aux = "";
					while ((tmp = leitor.readLine()) != null)
						aux += tmp + "\n";
					aux = aux.substring(0, aux.length() - 1);
					result = aux;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// Caso o caminho do arquivo seja um caminho
				// relativo
				nameFile = path + currentFile.getParent() + "/" + nameFile;
				file = new File(nameFile);
				if (file.isFile()) {
					FileReader in = null;
					try {
						in = new FileReader(file);
						BufferedReader leitor = new BufferedReader(in);
						String tmp, aux = "";
						while ((tmp = leitor.readLine()) != null)
							aux += tmp + "\n";
						aux = aux.substring(0, aux.length() - 1);
						result = aux;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void hoverImage(String nameFile, String path) {
		PreViewImage img = null;
		if (new File(nameFile).isFile())
			img = new PreViewImage(nameFile);
		else {
			nameFile = path + "/" + nameFile;
			if (new File(nameFile).isFile())
				img = new PreViewImage(nameFile);
		}
		result = img;
	}

	private void hoverCausalConnector(int offset) {
		Vector<Integer> offsets = doc.getChildrenOffsets(offset);
		PreViewConnector connectorRoleValues = new PreViewConnector();
		if (connectorRoleValues != null) {
			for (int i : offsets) {
				String tag = doc.getCurrentTagname(i);
				if (tag.equals("simpleCondition")) {
					Attributes att = new Attributes();
					String role = doc.getAttributeValueFromCurrentTagName(i,
							"role");
					att.setAttribute("role", role);
					String max = doc.getAttributeValueFromCurrentTagName(i,
							"max");
					if (max == null)
						max = "1";
					att.setAttribute("max", max);

					String min = doc.getAttributeValueFromCurrentTagName(i,
							"min");
					if (min == null)
						min = "1";
					att.setAttribute("min", min);

					String qualifier = doc.getAttributeValueFromCurrentTagName(
							i, "qualifier");
					if (qualifier != null)
						att.setAttribute("qualifier", qualifier);
					connectorRoleValues.setConditionRole(att);
				} else if (tag.equals("simpleAction")) {
					Attributes att = new Attributes();
					String role = doc.getAttributeValueFromCurrentTagName(i,
							"role");
					att.setAttribute("role", role);
					String max = doc.getAttributeValueFromCurrentTagName(i,
							"max");
					if (max == null)
						max = "1";
					att.setAttribute("max", max);

					String min = doc.getAttributeValueFromCurrentTagName(i,
							"min");
					if (min == null)
						min = "1";
					att.setAttribute("min", min);

					String qualifier = doc.getAttributeValueFromCurrentTagName(
							i, "qualifier");
					if (qualifier != null)
						att.setAttribute("qualifier", qualifier);

					connectorRoleValues.setActionRole(att);
				} else if (tag.equals("compoundCondition"))
					connectorRoleValues
							.setCompoundCondition(doc
									.getAttributeValueFromCurrentTagName(i,
											"operator"));
				else if (tag.equals("compoundAction"))
					connectorRoleValues
							.setCompoundAction(doc
									.getAttributeValueFromCurrentTagName(i,
											"operator"));
				else if (tag.equals("attributeAssessment")) {
					Attributes att = new Attributes();
					att.setAttribute("role", "test");
					connectorRoleValues.setConditionRole(att);
				}
			}
			result = connectorRoleValues;
		}
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		int returnOffset = offset;
		TypedRegion typedRegion;

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);
		String path = null;
		
		try {
			path = file.getProject().getLocation().toString();
			String values [] = path.split("/");
			path = "";
			for (int i=0; i < values.length - 1; i++) path += values[i] + "/";
		} catch(Exception e){
			return null;
		}

		if (editor.getEditorInput() instanceof IFileEditorInput) {
			currentFile = ((IFileEditorInput) editor.getEditorInput())
					.getFile().getFullPath().toFile();
		} else {
			currentFile = new File(((IURIEditorInput) editor.getEditorInput())
					.getURI());
		}
		
		
		path += currentFile.getParent().substring(1);
		
		
		if (NCLEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.P_PREVIEW)) {

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

				String refer = "";

				refer = doc.getAttributeValueFromCurrentTagName(offset,
						"component");
				if (refer != null && !refer.equals("")) {
					offset = doc.getNextTagPartition(
							doc.getElementOffset(refer)).getOffset();
				}

				refer = doc
						.getAttributeValueFromCurrentTagName(offset, "refer");

				
				Vector<String> refersPath = new Vector<String>();
	
				while (refer != null && !refer.equals("")) {
					String currentId = doc.getAttributeValueFromCurrentTagName(offset,
							"id");
					if(refersPath.contains(refer))
						break;
					refersPath.add(currentId);
					
					offset = doc.getNextTagPartition(
							doc.getElementOffset(refer)).getOffset();

					refer = doc.getAttributeValueFromCurrentTagName(offset,
							"refer");
				}

				String CurrentAttribute = doc.getCurrentAttribute(offset);
				String CurrentTagname = doc.getCurrentTagname(offset);

				if ((!CurrentAttribute.equals("descriptor") && CurrentTagname
						.equals("media"))
						|| (CurrentTagname.equals("descriptor") && (CurrentAttribute
								.equals("focusSelSrc") || CurrentAttribute
								.equals("focusSrc")))) {

					String mime = doc.getAttributeValueFromCurrentTagName(
							offset, "src");

					if (mime == null) { // significa que a tag em questão é
						// focusSelSrc ou focusSrc
						mime = doc.getAttributeValueFromCurrentTagName(offset,
								"focusSelSrc");
						if (mime == null)
							mime = doc.getAttributeValueFromCurrentTagName(
									offset, "focusSrc");

						if (mime == null)
							return new Region(offset, 0);

					}

					String temp = mime;

					temp = temp.toLowerCase();
					if (temp.length() > 7) {
						if (temp.substring(0, 7).equals("http://")) {
							result = new PreViewXML(300, 300, "", mime);
						}
					}

					String values[] = temp.split("\\.");
					String sbstr = "";
					if (values.length > 1)
						sbstr = values[values.length - 1];

					if (audio.contains(sbstr) || video.contains(sbstr)) {

						hoverAudioVideo(mime, sbstr, path);

					} else if (text.contains(sbstr)) {

						hoverText(mime, sbstr, path);

					} else if (image.contains(sbstr)) {

						hoverImage(mime, path);

					}

				} else if (doc.getCurrentAttribute(offset).equals("id")
						&& doc.getCurrentTagname(offset).equals("region")) {

					PreViewRegion region = new PreViewRegion(
							getRegionFatherTree(offset));
					result = region;

				} else if (doc.getCurrentTagname(offset).equals("descriptor")
						&& doc.getCurrentAttribute(offset).equals("region")) {

					String regionId = doc.getAttributeValueFromCurrentTagName(
							offset, "region");

					String aliasRegion = "";
					int index = regionId.indexOf("#");
					if (index != -1)
						if (index == regionId.lastIndexOf("#")) {
							aliasRegion = regionId.substring(0, index);
							if (aliasRegion.equals("")) return null;
							
							regionId = regionId.substring(index + 1);
							offset = doc.getOffsetByValue("alias", aliasRegion);
							String documentURI = doc
									.getAttributeValueFromCurrentTagName(
											offset, "documentURI");
							if (documentURI != null && !documentURI.equals("")) {
								try {
									File importedFile;
									importedFile = new File(documentURI);
									BufferedReader reader = null;
									if (!importedFile.isFile()) {
										importedFile = new File(path + "/"
												+ documentURI);
									}
									if (importedFile != null
											&& importedFile.isFile()) {

										reader = new BufferedReader(
												new FileReader(importedFile));
									}
									String newNCL = "";
									while (reader.ready())
										newNCL += reader.readLine() + "\n";
									
									doc = new NCLSourceDocument(newNCL);
									offset = doc.getElementOffset(regionId);
									
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						else return null;

					int newOffset = offset;
					try{
						newOffset = doc.getNextTagPartition(
							doc.getElementOffset(regionId)).getOffset();
					}catch (BadLocationException e) {
						return null;
					}

					PreViewRegion region = new PreViewRegion(
							getRegionFatherTree(newOffset));

					result = region;
				} else if (doc.getCurrentTagname(offset).equals(
						"causalConnector")) {

					hoverCausalConnector(offset);

				} else
					result = "";

				Point selection = textViewer.getSelectedRange();
				if (selection.x <= offset && offset < selection.x + selection.y)
					return new Region(selection.x, selection.y);
				return new Region(returnOffset, 0);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
