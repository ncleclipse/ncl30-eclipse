/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
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

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.URLHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import br.ufma.deinf.laws.ncleclipse.format.NCLDocumentFormattingStrategy;
import br.ufma.deinf.laws.ncleclipse.format.XMLAutoIdentStrategy;
import br.ufma.deinf.laws.ncleclipse.hover.NCLInformationControl;
import br.ufma.deinf.laws.ncleclipse.hover.NCLTextHoverExtension;
import br.ufma.deinf.laws.ncleclipse.hyperlinks.NCLEclipseHyperlinkDetector;
import br.ufma.deinf.laws.ncleclipse.scanners.CDataScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLPartitionScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTagScanner;
import br.ufma.deinf.laws.ncleclipse.scanners.XMLTextScanner;
import br.ufma.deinf.laws.ncleclipse.util.ColorManager;
import br.ufma.deinf.laws.ncleclipse.util.IXMLColorConstants;
import br.ufma.deinf.laws.ncleclipse.util.NCLDoubleClickStrategy;
import br.ufma.deinf.laws.ncleditor.editor.contentassist.NCLCompletionProposal;

public class NCLConfiguration extends TextSourceViewerConfiguration {
	private NCLDoubleClickStrategy doubleClickStrategy;
	private XMLTagScanner tagScanner;
	private XMLScanner scanner;
	private CDataScanner cdataScanner;
	private XMLTextScanner textScanner;
	private ColorManager colorManager;
	private NCLEditor editor;

	public NCLConfiguration(ColorManager colorManager, NCLEditor editor) {
		this.colorManager = colorManager;
		this.editor = editor;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				XMLPartitionScanner.XML_COMMENT, XMLPartitionScanner.XML_PI,
				XMLPartitionScanner.XML_DOCTYPE,
				XMLPartitionScanner.XML_START_TAG,
				XMLPartitionScanner.XML_END_TAG, XMLPartitionScanner.XML_TEXT, };
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new NCLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected XMLScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new XMLScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IXMLColorConstants.DEFAULT))));
		}
		return scanner;
	}

	protected XMLTextScanner getXMLTextScanner() {
		if (textScanner == null) {
			textScanner = new XMLTextScanner(colorManager);
			textScanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IXMLColorConstants.DEFAULT))));
		}
		return textScanner;
	}

	protected CDataScanner getCDataScanner() {
		if (cdataScanner == null) {
			cdataScanner = new CDataScanner(colorManager);
			cdataScanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IXMLColorConstants.CDATA_TEXT))));
		}
		return cdataScanner;
	}

	protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(IXMLColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_START_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_START_TAG);

		dr = new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_END_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_END_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_DOCTYPE);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_DOCTYPE);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_PI);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_PI);

		dr = new DefaultDamagerRepairer(getXMLTextScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_TEXT);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TEXT);

		dr = new DefaultDamagerRepairer(getCDataScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_CDATA);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_CDATA);

		TextAttribute textAttribute = new TextAttribute(colorManager
				.getColor(IXMLColorConstants.XML_COMMENT));
		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				textAttribute);
		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

		return reconciler;
	}

	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		NCLReconcilingStrategy strategy = new NCLReconcilingStrategy();
		strategy.setEditor(editor);

		MonoReconciler reconciler = new MonoReconciler(strategy, false);

		return reconciler;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor nclCompletionProcessor = new NCLCompletionProposal();
		assistant
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		// set nclCompletionProposal to XML_START_TAG
		assistant.setContentAssistProcessor(nclCompletionProcessor,
				XMLPartitionScanner.XML_START_TAG);
		assistant.setContentAssistProcessor(nclCompletionProcessor,
				XMLPartitionScanner.XML_END_TAG);
		// set nclCompletionProposal to DEFAULT_CONTENT
		assistant.setContentAssistProcessor(nclCompletionProcessor,
				IDocument.DEFAULT_CONTENT_TYPE);

		assistant
				.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant
				.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		// Enable AutoActivation
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);

		Color bgColor = colorManager.getColor(new RGB(255, 255, 255));
		assistant.setProposalSelectorBackground(bgColor);
		return assistant;
	}

	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		MultiPassContentFormatter formatter = new MultiPassContentFormatter(
				getConfiguredDocumentPartitioning(sourceViewer),
				IDocument.DEFAULT_CONTENT_TYPE);

		formatter.setMasterStrategy(new NCLDocumentFormattingStrategy());
		return formatter;
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		IAutoEditStrategy strategy = (IDocument.DEFAULT_CONTENT_TYPE
				.equals(contentType) ? new XMLAutoIdentStrategy()
				: new DefaultIndentLineAutoEditStrategy());
		return new IAutoEditStrategy[] { strategy };
	}

	/**
	 * Add NCLEclipse Hyperlink Detector to Source Viewer Configuration
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return new IHyperlinkDetector[] {
				new NCLEclipseHyperlinkDetector(sourceViewer),
				new URLHyperlinkDetector() };
	}

	/*
	 * (non-Javadoc) Method declared on SourceViewerConfiguration
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {

		return new NCLTextHoverExtension(sourceViewer);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getInformationControlCreator(org.eclipse.jface.text.source.ISourceViewer)
	 */
	/**
	 * TODO: This function must be reimplemented to return a
	 * NCLHoverInformationControl
	 */

	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {

		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				// return new DefaultInformationControl(parent); return new
				return new NCLInformationControl(parent, false);
			}
		};

	}

}
