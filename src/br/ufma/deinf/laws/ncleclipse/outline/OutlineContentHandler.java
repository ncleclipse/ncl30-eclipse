/*******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * 
 * Copyright: 2007-2009 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * For further information contact:
 * 		ncleclipse@laws.deinf.ufma.br
 * 		http://www.laws.deinf.ufma.br/ncleclipse
 * 		http://www.laws.deinf.ufma.br
 ********************************************************************************/
package br.ufma.deinf.laws.ncleclipse.outline;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.ufma.deinf.laws.ncleclipse.xml.XMLAttribute;
import br.ufma.deinf.laws.ncleclipse.xml.XMLElement;
import br.ufma.deinf.laws.ncleclipse.xml.XMLTree;
/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class OutlineContentHandler extends DefaultHandler implements ContentHandler
{

    private XMLTree dtdTree;

    private XMLElement dtdElement;

    private Locator locator;

    private IDocument document;

    private String positionCategory;

    public OutlineContentHandler()
    {
        super();
    }

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    public void startElement(String namespace, String localname, String qName, Attributes attributes)
            throws SAXException
    {

        int lineNumber = locator.getLineNumber() - 1;
        XMLElement element = new XMLElement(localname);

        int startPosition = getOffsetFromLine(lineNumber);
        Position position = new Position(startPosition);

        addPosition(position);
        element.setPosition(position);

        if (dtdTree == null)
        {
            this.dtdTree = new XMLTree();
            this.dtdTree.setRootElement(element);
        }

        if (attributes != null)
        {
            int attributeLength = attributes.getLength();
            for (int i = 0; i < attributeLength; i++)
            {
                String value = attributes.getValue(i);
                String localName = attributes.getLocalName(i);

                element.addChildAttribute(new XMLAttribute(localName, value));
            }
        }

        if (dtdElement != null)
            dtdElement.addChildElement(element);

        dtdElement = element;

    }

    public void endElement(String namespace, String localname, String qName) throws SAXException
    {

        int lineNumber = locator.getLineNumber();
        int endPosition = getOffsetFromLine(lineNumber);

        if (dtdElement != null)
        {

            Position position = dtdElement.getPosition();
            int length = endPosition - position.getOffset();
            position.setLength(length);

            dtdElement = dtdElement.getParent();

        }
    }

    private void addPosition(Position position)
    {
        try
        {
            document.addPosition(positionCategory, position);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
        catch (BadPositionCategoryException e)
        {
            e.printStackTrace();
        }
    }

    public void endDocument() throws SAXException
    {
        super.endDocument();
    }

    private int getOffsetFromLine(int lineNumber)
    {
        int offset = 0;
        try
        {
            //System.out.print("Line " + lineNumber);
            offset = document.getLineOffset(lineNumber);
            //System.out.println(", offset: " + offset);
        }
        catch (BadLocationException e)
        {
            try
            {
                offset = document.getLineOffset(lineNumber - 1);
            }
            catch (BadLocationException e1)
            {
            }
        }
        return offset;
    }

    public XMLElement getRootElement()
    {
        return dtdTree.getRootElement();
    }

    public void setDocument(IDocument document)
    {
        this.document = document;
    }

    public void setPositionCategory(String positionCategory)
    {
        this.positionCategory = positionCategory;
    }

}