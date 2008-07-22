package br.ufma.deinf.laws.ncleclipse.marker;


import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.xml.sax.SAXParseException;

import br.ufma.deinf.gia.labmint.composer.NCLValidator;
import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.laws.ncleclipse.xml.XMLValidationError;
import br.ufma.deinf.laws.ncleclipse.xml.XMLValidationErrorHandler;


public class MarkingErrorHandler extends XMLValidationErrorHandler
{

	public static final String ERROR_MARKER_ID = "editorarticle.dtderror";

	private IFile file;
	private IDocument document;

	public MarkingErrorHandler(IFile file, IDocument document)
	{
		super();
		this.file = file;
		this.document = document;
	}

	public void removeExistingMarkers()
	{
		try
		{
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO);
		}
		catch (CoreException e1)
		{
			e1.printStackTrace();
		}
	}

	protected XMLValidationError nextError(SAXParseException e, boolean isFatal)
	{

		XMLValidationError validationError = super.nextError(e, isFatal);

		Map map = new HashMap();
		int lineNumber = e.getLineNumber();
		int columnNumber = e.getColumnNumber();
		MarkerUtilities.setLineNumber(map, lineNumber);
		MarkerUtilities.setMessage(map, "Erro sint�tico no XML ('"+ e.getMessage()+"').");
		map.put(IMarker.LOCATION, file.getFullPath().toString());

		Integer charStart = getCharStart(lineNumber, columnNumber);
		if (charStart != null)
			map.put(IMarker.CHAR_START, charStart);

		Integer charEnd = getCharEnd(lineNumber, columnNumber);
		if (charEnd != null)
			map.put(IMarker.CHAR_END, charEnd);

		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));

		try
		{
			MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
		}
		catch (CoreException ee)
		{
			ee.printStackTrace();
		}

		return validationError;

	}

	private Integer getCharEnd(int lineNumber, int columnNumber)
	{
		try
		{
			return new Integer(document.getLineOffset(lineNumber - 1) + columnNumber);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private Integer getCharStart(int lineNumber, int columnNumber)
	{
		try
		{
			int lineStartChar = document.getLineOffset(lineNumber - 1);
			Integer charEnd = getCharEnd(lineNumber, columnNumber);
			if (charEnd != null)
			{
				ITypedRegion typedRegion = document.getPartition(charEnd.intValue()-2);
				int partitionStartChar = typedRegion.getOffset();
				return new Integer(partitionStartChar);
			}
			else
				return new Integer(lineStartChar);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void MarkNCLValidatorErrorsAndWarnings(){
        //TODO: Falta pegar a posição do erro e/ou warning!
		Vector <Message> warnings = NCLValidator.getWarnings();
		Vector <Message> erros = NCLValidator.getErrors();	
		//Imprime os warning
		Map map = new HashMap();
		map.put(IMarker.LOCATION, file.getFullPath().toString());
		
		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_WARNING));
		for(int i = 0; i < warnings.size(); i++){
			try
			{
				int lineNumber = (new Integer((String)warnings.get(i).getElement().getUserData("startLine"))).intValue();
				int columnNumber = (new Integer((String)warnings.get(i).getElement().getUserData("startColumn"))).intValue();
				Integer charStart = getCharStart(lineNumber, columnNumber);
				if (charStart != null)
					map.put(IMarker.CHAR_START, charStart);

				Integer charEnd = getCharEnd(lineNumber, columnNumber);
				if (charEnd != null)
					map.put(IMarker.CHAR_END, charEnd);

				MarkerUtilities.setMessage(map, warnings.get(i).getDescription());
				MarkerUtilities.setLineNumber(map, new Integer((String)warnings.get(i).getElement().getUserData("startLine")));
				MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
				
			}
			catch (CoreException ee)
			{
				ee.printStackTrace();
			}
		}
		//Imprime os erros
		map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
		for(int i = 0; i < erros.size(); i++){
			try
			{
				int lineNumber = (new Integer((String)erros.get(i).getElement().getUserData("startLine"))).intValue();
				int columnNumber = (new Integer((String)erros.get(i).getElement().getUserData("startColumn"))).intValue();
				Integer charStart = getCharStart(lineNumber, columnNumber);
				if (charStart != null)
					map.put(IMarker.CHAR_START, charStart);

				Integer charEnd = getCharEnd(lineNumber, columnNumber);
				if (charEnd != null)
					map.put(IMarker.CHAR_END, charEnd);

				MarkerUtilities.setMessage(map, erros.get(i).getDescription());
				MarkerUtilities.setLineNumber(map, new Integer((String)erros.get(i).getElement().getUserData("startLine")));
				
				MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
			}
			catch (CoreException ee)
			{
				ee.printStackTrace();
			}
		}
	}
}
