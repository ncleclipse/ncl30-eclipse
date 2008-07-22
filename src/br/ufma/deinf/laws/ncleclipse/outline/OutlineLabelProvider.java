/*
 * Created on Oct 7, 2004
 */
package br.ufma.deinf.laws.ncleclipse.outline;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.internal.Workbench;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;
import br.ufma.deinf.laws.ncleclipse.xml.XMLElement;


/**O
 * @author Phil Zoio
 */
public class OutlineLabelProvider implements ILabelProvider
{
	private Image linkImage;
	public OutlineLabelProvider()
	{
		super();
		/*System.out.println(NCLEditorPlugin.getDefault().getImageRegistry());
		linkImage = NCLEditorPlugin.getDefault().getImageRegistry().get(NCLEditorPlugin.LINK_ICON);*/
		//TODO: Imagem nos elementos (Não estou conseguindo fazer)
	}

	public Image getImage(Object element)
	{
		/*if (element instanceof XMLElement)
		{
			XMLElement dtdElement = (XMLElement) element;
			if(dtdElement.getName().equals("link")){
				return linkImage;
			}
		}*/
		return null;
	}

	public String getText(Object element)
	{
		if (element instanceof XMLElement)
		{
			XMLElement dtdElement = (XMLElement) element;
			String textToShow = dtdElement.getName();
			System.out.println("text to show = " + textToShow);
			String idAttributte = dtdElement.getAttributeValue("id");
			if(idAttributte == null || idAttributte.equals("")){
				String nameAttribute = dtdElement.getAttributeValue("name");
				if (nameAttribute != null && !nameAttribute.equals(""))
					textToShow += " (" + nameAttribute+")";
			}
			else
				textToShow += " ("+idAttributte+")";
			
			return textToShow;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener)
	{
	}

	public void dispose()
	{
	}

	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	public void removeListener(ILabelProviderListener listener)
	{
	}

}