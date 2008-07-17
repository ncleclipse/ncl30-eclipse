package br.ufma.deinf.laws.ncleclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Document;
import org.xml.sax.helpers.LocatorImpl;

import br.ufma.deinf.gia.labmint.composer.NCLValidator;
import br.ufma.deinf.gia.labmint.document.NclValidatorDocument;
import br.ufma.deinf.gia.labmint.main.NclParseErrorHandler;
import br.ufma.deinf.gia.labmint.message.Message;
import br.ufma.deinf.gia.labmint.message.MessageList;
import br.ufma.deinf.laws.ncleclipse.marker.MarkingErrorHandler;
import br.ufma.deinf.laws.ncleclipse.outline.EditorContentOutlinePage;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;
import br.ufma.deinf.laws.ncleclipse.util.NCLDocumentProvider;
import br.ufma.deinf.laws.ncleclipse.xml.XMLParser;

public class NCLEditor extends TextEditor {
	public static String CONTENT_ASSIST_ACTION = "br.ufma.deinf.laws.ncleclipse.actions.CONTENT_ASSIST"; 
	public static String FORMAT_ACTION = "br.ufma.deinf.laws.ncleclipse.actions.format";

	private IEditorInput input;
	private EditorContentOutlinePage outlinePage;
	
	private ColorManager colorManager;

	public NCLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new NCLConfiguration(colorManager, this));
		setDocumentProvider(new NCLDocumentProvider());
	}
	
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	 
	protected void createActions(){
		super.createActions();
		IAction action = new ContentAssistAction(NCLEditorMessages.getResourceBundle(), "ContentAssistProposal.", this); //$NON-NLS-1$
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);		
		setAction(CONTENT_ASSIST_ACTION , action); //$NON-NLS-1$
		markAsStateDependentAction(CONTENT_ASSIST_ACTION , true); //$NON-NLS-1$
		
		action = new TextOperationAction(NCLEditorMessages.getResourceBundle(), "ContentFormat.", this, ISourceViewer.FORMAT);
        action.setActionDefinitionId(FORMAT_ACTION);
        action.setAccelerator(SWT.CTRL | SWT.SHIFT | 'f');
        setAction("ContentFormat", action);
		
	}
	
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, "ContentAssistProposal"); //$NON-NLS-1$
	}
	
	protected void doSetInput(IEditorInput newInput) throws CoreException
	{
		super.doSetInput(newInput);
		this.input = newInput;

		if (outlinePage != null)
			outlinePage.setInput(input); // update content outline
		
		validateAndMark();
	}

	protected void editorSaved()
	{
		super.editorSaved();


		if (outlinePage != null)
			outlinePage.update();	//update content outline
	
		//we validate and mark document here
		validateAndMark();

	}
	protected void validateAndMark(){
		try
		{
			IDocument document = getInputDocument();
			String text = document.get();
			MarkingErrorHandler markingErrorHandler = new MarkingErrorHandler(getInputFile(), document);
			markingErrorHandler.setDocumentLocator(new LocatorImpl());
			markingErrorHandler.removeExistingMarkers();
			
			//Validação Xerces
			XMLParser parser = new XMLParser();
			parser.setErrorHandler(markingErrorHandler);
			parser.doParse(text);
			
			//Validação ncl30-validator
			IFile file = getInputFile();
			File docFile = new File(file.getLocationURI());
			Document doc = null;
			MessageList.clear();
			MessageList.setLanguage(MessageList.PORTUGUESE);
	        try {
	        		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        		DocumentBuilder db = dbf.newDocumentBuilder();
	        		NclParseErrorHandler p = new NclParseErrorHandler();
	        		p.setFile(docFile.getAbsolutePath());
	        		db.setErrorHandler(p);
	        		doc = db.parse(docFile);
	        		
	        		Vector <NclValidatorDocument> documents = new Vector<NclValidatorDocument>();
	        		//NclDocumentManager.resetDocumentManager();
	        		NclValidatorDocument nclValidatorDocument = new NclValidatorDocument(doc);
	        		documents.add(nclValidatorDocument);
	        		       		
	        		NCLValidator.validate(documents);
	       		        		
	        } 
	        catch (Exception e) {
	        	//TODO Alguma coisa
	        	e.printStackTrace();
	        	MessageList.addError(docFile.getAbsolutePath(), "Erro sint�tico no XML ("+e.getMessage()+")", null, MessageList.PORTUGUESE);
	        }
	        
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
					MarkerUtilities.setMessage(map, warnings.get(i).getDescription());
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
					MarkerUtilities.setMessage(map, erros.get(i).getDescription());
					MarkerUtilities.createMarker(file, map, IMarker.PROBLEM);
					
				}
				catch (CoreException ee)
				{
					ee.printStackTrace();
				}
			}			
			
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
	}
	
	protected IDocument getInputDocument()
	{
		IDocument document = getDocumentProvider().getDocument(input);
		return document;
	}

	public IFile getInputFile()
	{
		IFileEditorInput ife = (IFileEditorInput) input;
		IFile file = ife.getFile();
		return file;
	}
	
	public IEditorInput getInput()
	{
		return input;
	}
	public Object getAdapter(Class required)
	{

		if (IContentOutlinePage.class.equals(required))
		{
			if (outlinePage == null)
			{
				outlinePage = new EditorContentOutlinePage(this);
				if (getEditorInput() != null)
					outlinePage.setInput(getEditorInput());
			}
			return outlinePage;
		}

		return super.getAdapter(required);
		
	}
	/** FOLDING **/
		private ProjectionSupport projectionSupport; 	//Used to folding
		
		public void createPartControl(Composite parent)
	    {
	        super.createPartControl(parent);
	        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
	        
	        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
			projectionSupport.install();
			
			//turn projection mode on
			viewer.doOperation(ProjectionViewer.TOGGLE);
			
			annotationModel = viewer.getProjectionAnnotationModel();
			
	    }
		private Annotation[] oldAnnotations;
		private ProjectionAnnotationModel annotationModel;
		
		public void updateFoldingStructure(ArrayList positions)
		{
			Annotation[] annotations = new Annotation[positions.size()];
			
			//this will hold the new annotations along
			//with their corresponding positions
			HashMap newAnnotations = new HashMap();
			HashMap<Position, Boolean> oldPosisitions = new HashMap<Position, Boolean>();
			if(oldAnnotations != null){
				for(int i = 0; i < oldAnnotations.length; i++){
					ProjectionAnnotation tmp = (ProjectionAnnotation) oldAnnotations[i];
					if(tmp.isCollapsed()){
						oldPosisitions.put(annotationModel.getPosition(tmp), true);					
					}
				}
			}
			
			for(int i =0; i<positions.size(); i++)
			{
				ProjectionAnnotation annotation = new ProjectionAnnotation();
				if(oldPosisitions.containsKey(positions.get(i)) && oldPosisitions.get(positions.get(i)).booleanValue()) //if was collapsed
					annotation.markCollapsed();
				newAnnotations.put(annotation,positions.get(i));
				annotations[i]=annotation;
			}
			
			//Melhorar isto aqui Remover apenas as necess�rias e n�o todas as antigas
			annotationModel.modifyAnnotations(oldAnnotations,newAnnotations, null);
			
			oldAnnotations=annotations;
		}
		
	    /* (non-Javadoc)
	     * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
	     */
	    protected ISourceViewer createSourceViewer(Composite parent,
	            IVerticalRuler ruler, int styles)
	    {
	        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

	    	// ensure decoration support has been created and configured.
	    	getSourceViewerDecorationSupport(viewer);
	    	
	    	return viewer;
	    }
	 /** END FOLDING **/	    
}
