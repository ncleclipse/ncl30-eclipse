/******************************************************************************
Este arquivo é parte da implementação do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratório de Sistemas Avançados da Web) 

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

******************************************************************************
This file is part of the authoring environment in Nested Context Language -
NCL Eclipse.

Copyright: 2007-2008 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

*******************************************************************************/

package br.ufma.deinf.laws.ncleclipse.outline;

import java.util.List;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.xml.sax.helpers.LocatorImpl;

import br.ufma.deinf.laws.ncleclipse.xml.XMLElement;
import br.ufma.deinf.laws.ncleclipse.xml.XMLParser;


public class OutlineContentProvider implements ITreeContentProvider
{

	private XMLElement root = null;
	private IEditorInput input;
	private IDocumentProvider documentProvider;

	protected final static String TAG_POSITIONS = "__tag_positions";
	protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(TAG_POSITIONS);

	public OutlineContentProvider(IDocumentProvider provider)
	{
		super();
		this.documentProvider = provider;
	}

	public Object[] getChildren(Object parentElement)
	{
		if (parentElement == input)
		{
			if (root == null)
				return new Object[0];
			List childrenDTDElements = root.getChildrenDTDElements();
			if (childrenDTDElements != null)
				return childrenDTDElements.toArray();
		}
		else
		{
			XMLElement parent = (XMLElement)parentElement;
			List childrenDTDElements = parent.getChildrenDTDElements();
			if (childrenDTDElements != null)
				return childrenDTDElements.toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element)
	{
		if (element instanceof XMLElement)
			return ((XMLElement)element).getParent();
		return null;
	}

	public boolean hasChildren(Object element)
	{
		if (element == input) return true;
		else
		{
			return ((XMLElement)element).getChildrenDTDElements().size() > 0;
		}
	}

	public Object[] getElements(Object inputElement)
	{
		if (root == null)
			return new Object[0];
		List childrenDTDElements = root.getChildrenDTDElements();
		if (childrenDTDElements != null)
			return childrenDTDElements.toArray();
		return new Object[0];
	}

	public void dispose()
	{
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{

		if (oldInput != null)
		{
			IDocument document = documentProvider.getDocument(oldInput);
			if (document != null)
			{
				try
				{
					document.removePositionCategory(TAG_POSITIONS);
				}
				catch (BadPositionCategoryException x)
				{
				}
				document.removePositionUpdater(positionUpdater);
			}
		}
		
		input = (IEditorInput) newInput;

		if (newInput != null)
		{
			IDocument document = documentProvider.getDocument(newInput);
			if (document != null)
			{
				document.addPositionCategory(TAG_POSITIONS);
				document.addPositionUpdater(positionUpdater);

				XMLElement rootElement = parseRootElement(document);
				if (rootElement != null)
				{
					root = rootElement;
				}
			}
		}
	}

	private XMLElement parseRootElement(IDocument document)
	{
		String text = document.get();
		XMLElement tagPositions = parseRootElements(text, document);
		return tagPositions;
	}

	private XMLElement parseRootElements(String text, IDocument document)
	{
		try
		{
			XMLParser xmlParser = new XMLParser();
			OutlineContentHandler contentHandler = new OutlineContentHandler();
			contentHandler.setDocument(document);
			contentHandler.setPositionCategory(TAG_POSITIONS);
			contentHandler.setDocumentLocator(new LocatorImpl());
			xmlParser.setContentHandler(contentHandler);
			xmlParser.doParse(text);
			XMLElement root = contentHandler.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			return null;
		}
	}

}