/*******************************************************************************
 * Este arquivo é parte da implementação do ambiente de autoria em Nested 
 * Context Language - NCL Eclipse.
 * Direitos Autorais Reservados (c) 2007-2010 UFMA/LAWS (Laboratório de Sistemas 
 * Avançados da Web)
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob
 * os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
 * Software Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM 
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU
 * ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do
 * GNU versão 2 para mais detalhes. Você deve ter recebido uma cópia da Licença
 * Pública Geral do GNU versão 2 junto com este programa; se não, escreva para a
 * Free Software Foundation, Inc., no endereço 59 Temple Street, Suite 330,
 * Boston, MA 02111-1307 USA.
 *
 * Para maiores informações:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 *******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * Copyright: 2007-2010 UFMA/LAWS (Laboratory of Advanced Web Systems), All
 * Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITypedRegion;
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
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.GotoLastEditPositionAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.w3c.dom.Document;
import org.xml.sax.helpers.LocatorImpl;

import br.ufma.deinf.gia.labmint.composer.NCLValidator;
import br.ufma.deinf.gia.labmint.document.NclValidatorDocument;
import br.ufma.deinf.gia.labmint.main.NclParseErrorHandler;
import br.ufma.deinf.gia.labmint.message.MessageList;
import br.ufma.deinf.gia.labmint.xml.XMLParserExtend;
import br.ufma.deinf.laws.ncl.help.NCLHelper;
import br.ufma.deinf.laws.ncleclipse.document.NCLSourceDocument;
import br.ufma.deinf.laws.ncleclipse.marker.MarkingErrorHandler;
import br.ufma.deinf.laws.ncleclipse.navigation.NCLNavigationHistory;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLContentHandler;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLDocument;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLElement;
import br.ufma.deinf.laws.ncleclipse.ncl.NCLParser;
import br.ufma.deinf.laws.ncleclipse.outline.EditorContentOutlinePage;
import br.ufma.deinf.laws.ncleclipse.preferences.PreferenceConstants;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;
import br.ufma.deinf.laws.ncleclipse.util.NCLDocumentProvider;
import br.ufma.deinf.laws.ncleclipse.util.NCLTextDocumentProvider;
import br.ufma.deinf.laws.ncleclipse.xml.XMLParser;

public class NCLEditor extends TextEditor implements IDocumentListener {
	public static String CONTENT_ASSIST_ACTION = "br.ufma.deinf.laws.ncleclipse.actions.CONTENT_ASSIST";
	public static String FORMAT_ACTION = "br.ufma.deinf.laws.ncleclipse.actions.format";
	public static String GO_TO_LAST_EDIT_POSITION = "br.ufma.deinf.laws.ncleclipse.actions.GotoLastEditPosition";

	private IEditorInput input;
	private EditorContentOutlinePage outlinePage;

	private ColorManager colorManager;

	public NCLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new NCLConfiguration(colorManager, this));
		loadHelp();
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	protected void createActions() {
		super.createActions();
		IAction action = new ContentAssistAction(NCLEditorMessages
				.getInstance().getResourceBundle(),
				"ContentAssistProposal.", this); //$NON-NLS-1$
		action
				.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction(CONTENT_ASSIST_ACTION, action); //$NON-NLS-1$
		markAsStateDependentAction(CONTENT_ASSIST_ACTION, true); //$NON-NLS-1$

		action = new TextOperationAction(NCLEditorMessages.getInstance()
				.getResourceBundle(), "ContentFormat.", this,
				ISourceViewer.FORMAT);
		action.setActionDefinitionId(FORMAT_ACTION);
		action.setAccelerator(SWT.CTRL | SWT.SHIFT | 'f');
		setAction("ContentFormat", action);

		action = new GotoLastEditPositionAction();
		action.setAccelerator(SWT.CTRL | 'Q');
		markAsStateDependentAction(
				ITextEditorActionDefinitionIds.GOTO_LAST_EDIT_POSITION, true); //$NON-NLS-1$
		action.setEnabled(true);

	}

	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, "ContentAssistProposal"); //$NON-NLS-1$
	}

	protected void doSetInput(IEditorInput newInput) throws CoreException {
		setDocumentProvider(createDocumentProvider(newInput));
		super.doSetInput(newInput);
		this.input = newInput;

		if (outlinePage != null)
			outlinePage.setInput(input); // update content outline

		validateAndMark();
		getInputDocument().addDocumentListener(this);
	}

	private IDocumentProvider createDocumentProvider(IEditorInput input2) {
		// System.out.println(input2.getClass().toString());
		if (input2 instanceof IFileEditorInput) {
			return new NCLDocumentProvider();
		} else if (input2 instanceof IURIEditorInput) {
			NCLTextDocumentProvider docProvider = new NCLTextDocumentProvider();
			return docProvider;
		} else {
			return new TextFileDocumentProvider();
		}
	}

	protected void editorSaved() {
		super.editorSaved();

		if (outlinePage != null)
			outlinePage.update(); // update content outline

		// we validate and mark document here
		validateAndMark();

	}

	protected void validateAndMark() {
		try {
			IDocument document = getInputDocument();
			String text = document.get();
			IFile file = getInputFile();
			if (file != null) {
				MarkingErrorHandler markingErrorHandler = new MarkingErrorHandler(
						getInputFile(), document);
				markingErrorHandler.setDocumentLocator(new LocatorImpl());
				markingErrorHandler.removeExistingMarkers();

				// Validacao Xerces
				XMLParser parser = new XMLParser();
				parser.setErrorHandler(markingErrorHandler);
				parser.doParse(text);

				// Validacao ncl30-validator
				File docFile = getCurrentFile();
				Document doc = null;
				MessageList.clear();

				// getting the description error file
				// NCLEclipseValidatorErrorMessages prop = new
				// NCLEclipseValidatorErrorMessages();
				// MessageHandler.setPropertyMessage(prop);

				// MessageList.setLanguage(MessageList.PORTUGUESE);
				try {
					XMLParserExtend parserExtend = new XMLParserExtend();

					NclParseErrorHandler p = new NclParseErrorHandler();
					p.setFile(docFile.getAbsolutePath());
					parserExtend.setErrorHandler(p);
					parserExtend.parseString(text);

					doc = parserExtend.getDocument();
					doc.setDocumentURI(docFile.toURI().toString());

					Vector<NclValidatorDocument> documents = new Vector<NclValidatorDocument>();
					// NclDocumentManager.resetDocumentManager();
					NclValidatorDocument nclValidatorDocument = new NclValidatorDocument(
							doc);
					documents.add(nclValidatorDocument);

					NCLValidator.validate(documents);

				} catch (Exception e) {
					// TODO Alguma coisa
					e.printStackTrace();
					Vector<String> args = new Vector<String>();
					args.add(e.getMessage());
					MessageList.addError(docFile.getAbsolutePath(), 1002, null,
							args);
				}
				markingErrorHandler.MarkNCLValidatorErrorsAndWarnings();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public IDocument getInputDocument() {
		IDocument document = getDocumentProvider().getDocument(input);
		return document;
	}

	public IFile getInputFile() {
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			return file;
		}
		return null;
	}

	public IEditorInput getInput() {
		return input;
	}

	public Object getAdapter(Class required) {

		if (IContentOutlinePage.class.equals(required)) {
			if (outlinePage == null) {
				outlinePage = new EditorContentOutlinePage(this);
				if (getEditorInput() != null)
					outlinePage.setInput(getEditorInput());
			}
			return outlinePage;
		}

		return super.getAdapter(required);

	}

	/** FOLDING **/
	private ProjectionSupport projectionSupport; // Used to folding

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		projectionSupport = new ProjectionSupport(viewer,
				getAnnotationAccess(), getSharedColors());
		projectionSupport.install();

		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

		annotationModel = viewer.getProjectionAnnotationModel();

	}

	private Annotation[] oldAnnotations;
	private ProjectionAnnotationModel annotationModel;

	public void updateFoldingStructure(ArrayList positions) {
		Annotation[] annotations = new Annotation[positions.size()];

		// this will hold the new annotations along
		// with their corresponding positions
		HashMap newAnnotations = new HashMap();
		HashMap<Position, Boolean> oldPosisitions = new HashMap<Position, Boolean>();
		if (oldAnnotations != null) {
			for (int i = 0; i < oldAnnotations.length; i++) {
				ProjectionAnnotation tmp = (ProjectionAnnotation) oldAnnotations[i];
				if (tmp.isCollapsed()) {
					oldPosisitions.put(annotationModel.getPosition(tmp), true);
				}
			}
		}

		for (int i = 0; i < positions.size(); i++) {
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			if (oldPosisitions.containsKey(positions.get(i))
					&& oldPosisitions.get(positions.get(i)).booleanValue()) // if
				// was
				// collapsed
				annotation.markCollapsed();
			newAnnotations.put(annotation, positions.get(i));
			annotations[i] = annotation;
		}

		// Melhorar isto aqui Remover apenas as necessarias e nao todas as
		// antigas
		annotationModel.modifyAnnotations(oldAnnotations, newAnnotations, null);

		oldAnnotations = annotations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse
	 * .swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler,
	 * int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	/** END FOLDING **/

	/**
	 * Responsavel por carregar as informacoes que serao mostradas no help
	 * contextual
	 */
	protected void loadHelp() {
		try {
			NCLHelper nclHelper = NCLHelper.getNCLHelper();
			nclHelper.setHelpFileName(NCLEditorPlugin.getResourcesLocation()
					.getPath()
					+ "help.txt");
			nclHelper.buildHelp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#handleCursorPositionChanged
	 * ()
	 */
	@Override
	public void handleCursorPositionChanged() {
		// TODO Auto-generated method stub
		NCLNavigationHistory.movedcursor(getCurrentFile().getAbsolutePath(),
				getCursorPosition());
		super.handleCursorPositionChanged();
	}

	public void setFocus(int lineOffset, int lineLength) {
		try {
			resetHighlightRange();
			setHighlightRange(getSourceViewer().getDocument().getLineOffset(
					--lineOffset), lineLength, true);
			setFocus();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFocus2(int lineOffset) {
		resetHighlightRange();
		setHighlightRange(lineOffset, 0, true);
		setFocus();
	}

	public void setFocusToElementId(String elementId) {
		File currentFile = getCurrentFile();

		ISourceViewer viewer = this.getSourceViewer();
		int previousOffset = viewer.getSelectedRange().x;

		String nclText = viewer.getDocument().get();
		NCLContentHandler nclContentHandler = new NCLContentHandler();
		NCLDocument nclDocument = new NCLDocument();
		nclDocument.setParentURI(currentFile.getParentFile().toURI());
		nclContentHandler.setNclDocument(nclDocument);
		NCLParser parser = new NCLParser();
		parser.setContentHandler(nclContentHandler);
		parser.doParse(nclText);

		NCLElement el = nclDocument.getElementById(elementId);
		int line = el.getLineNumber();
		int lineOffset, lineLength;

		try {
			lineOffset = viewer.getDocument().getLineOffset(line);
			lineLength = viewer.getDocument().getLineLength(line);

			// Move cursor to new position
			resetHighlightRange();
			setHighlightRange(lineOffset, lineLength, true);
			setFocus();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param nclElement
	 */
	public void setFocusToElement(NCLElement nclElement) {
		int line = nclElement.getLineNumber();
		int lineOffset, lineLength;
		ISourceViewer viewer = getSourceViewer();
		try {
			lineOffset = viewer.getDocument().getLineOffset(line);
			lineLength = viewer.getDocument().getLineLength(line);

			// Move cursor to new position
			resetHighlightRange();
			setHighlightRange(lineOffset, lineLength, true);
			setFocus();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public File getCurrentFile() {
		File currentFile = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			currentFile = new File(((IFileEditorInput) getEditorInput())
					.getFile().getLocationURI());
		} else {
			currentFile = new File(((IURIEditorInput) this.getEditorInput())
					.getURI());
		}
		return currentFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org
	 * .eclipse.jface.text.DocumentEvent)
	 */
	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	/**
	 * This job will be running in background and will update the markers.
	 */
	private Job updateMarkers = new Job("NCL Eclipse Update Markers") {
		protected IStatus run(IProgressMonitor monitor) {
			validateAndMark();
			return Status.OK_STATUS;
		}
	};

	/**
	 * This job will be running in background and will update the outlineView.
	 */
	private Job updateOutlineView = new Job("NCL Eclipse Update OutlineView") {
		protected IStatus run(IProgressMonitor monitor) {
			if (outlinePage != null)
				outlinePage.update(); // update content outline
			return Status.OK_STATUS;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.
	 * jface.text.DocumentEvent)
	 */
	@Override
	public void documentChanged(DocumentEvent event) {
		// UpdateMarkers
		if (NCLEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				PreferenceConstants.P_VALIDATION)) {
			updateMarkers.cancel();
			updateMarkers.setPriority(Job.SHORT);
			updateMarkers.schedule();
		}
		// TODO: Update Outline View
		// updateOutlineView.cancel();
		// updateOutlineView.setPriority(Job.SHORT);
		// updateOutlineView.schedule();

		if (event.fText.equals("/")) {
			final NCLSourceDocument doc = (NCLSourceDocument) event
					.getDocument();
			try {
				// We are CLOSING a START_TAG in a single line
				if (doc.getChar(event.fOffset + 1) == '>') {
					ITypedRegion region = doc.getPartition(event.fOffset);

					// if we are in a START_TAG, see if we can delete some
					// END_TAG
					if (region.getType().equals(
							XMLPartitionScanner.XML_START_TAG)) {
						String tagname = doc.getCurrentTagname(event.fOffset);
						final int fOffset = region.getOffset()
								+ region.getLength();
						int nextPartitionOffSet = fOffset + 1;

						int cont = 1;
						int offset = event.fOffset;

						/* How many fathers with my tagname ?? */
						String fatherTagName = doc.getFatherTagName(offset);
						while (fatherTagName.equals(tagname)) {
							cont++;
							offset = doc.getFatherPartitionOffset(offset);
							fatherTagName = doc.getFatherTagName(offset);
						}

						char ch = doc.getChar(nextPartitionOffSet);
						while (Character.isWhitespace(ch)) {
							ch = doc.getChar(nextPartitionOffSet++);
						}
						region = doc.getPartition(nextPartitionOffSet);
						offset = nextPartitionOffSet;

						do {
							if (cont == 0)
								break;
							offset = region.getOffset() + region.getLength();

							if (region.getType().equals(
									XMLPartitionScanner.XML_END_TAG)) {
								if (doc
										.getCurrentEndTagName(
												region.getOffset()).equals(
												fatherTagName))
									break;

								if (doc
										.getCurrentEndTagName(
												region.getOffset()).equals(
												tagname))
									--cont;
							}

							if (region.getType().equals(
									XMLPartitionScanner.XML_START_TAG))
								if (doc.getCurrentTagname(region.getOffset())
										.equals(tagname))
									++cont;

							// Melhor andar de partição em partição, mais
							// eficiente
							// do que andar em todos os caracteres
							region = doc.getNextPartition(region);

							/*ch = doc.getChar(offset);
							while (Character.isWhitespace(ch)) {
							    ch = doc.getChar(offset++); 
							}
							region = doc.getPartition(offset);*/

						} while (true);

						region = doc.getPartition(nextPartitionOffSet);

						doc.acceptPostNotificationReplaces();

						if (region.getType().equals(
								XMLPartitionScanner.XML_END_TAG)
								&& cont == 0) {

							String endTagName = doc
									.getCurrentEndTagName(nextPartitionOffSet);

							if (tagname.equals(endTagName)) {

								final int lastOffset = (region.getOffset() + region
										.getLength())
										- fOffset;

								doc.registerPostNotificationReplace(null,
										new IDocumentExtension.IReplace() {
											@Override
											public void perform(
													IDocument document,
													IDocumentListener owner) {
												try {
													doc.replace(fOffset,
															lastOffset, "");
												} catch (BadLocationException e) {
													e.printStackTrace();
												}
											}
										});
							}
						}
					}
				}
			} catch (BadLocationException e) {
				return;
			}
		}
	}

}
