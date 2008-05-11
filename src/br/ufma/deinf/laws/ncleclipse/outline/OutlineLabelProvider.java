/*
 * Created on Oct 7, 2004
 */
package br.ufma.deinf.laws.ncleclipse.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import br.ufma.deinf.laws.ncleclipse.xml.XMLElement;


/**O
 * @author Phil Zoio
 */
public class OutlineLabelProvider implements ILabelProvider
{

	public OutlineLabelProvider()
	{
		super();
	}

	public Image getImage(Object element)
	{
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