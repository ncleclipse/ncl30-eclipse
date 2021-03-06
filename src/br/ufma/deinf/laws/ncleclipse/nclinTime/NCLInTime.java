///*******************************************************************************
// * This file is part of the NCL authoring environment - NCL Eclipse.
// *
// * Copyright (C) 2007-2012, LAWS/UFMA.
// *
// * This program is free software; you can redistribute it and/or modify it under
// * the terms of the GNU General Public License version 2 as published by
// * the Free Software Foundation.
// *
// * This program is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
// * more details. You should have received a copy of the GNU General Public 
// * License version 2 along with this program; if not, write to the Free 
// * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
// * 02110-1301, USA.
// *
// * For further information contact:
// * - ncleclipse@laws.deinf.ufma.br
// * - http://www.laws.deinf.ufma.br/ncleclipse
// * - http://www.laws.deinf.ufma.br
// *
// ******************************************************************************/
//package br.ufma.deinf.laws.ncleclipse.nclinTime;
//
//import java.io.IOException;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.TransformerException;
//
//import org.eclipse.jface.action.IAction;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.ui.IEditorActionDelegate;
//import org.eclipse.ui.IEditorPart;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.console.ConsolePlugin;
//import org.eclipse.ui.console.IConsole;
//import org.eclipse.ui.console.IConsoleConstants;
//import org.eclipse.ui.console.IConsoleManager;
//import org.eclipse.ui.console.IConsoleView;
//import org.eclipse.ui.console.MessageConsole;
//import org.eclipse.ui.console.MessageConsoleStream;
//import org.xml.sax.SAXException;
//
//import br.ufma.deinf.laws.ncleclipse.NCLEditor;
//import br.ufma.deinf.laws.ncleclipse.NCLMultiPageEditor;
//import br.ufma.deinf.laws.nclinTime.DOMParser;
//
///**
// * @author Rodrigo Costa <rodrim.c@laws.deinf.ufma.br>
// * 
// */
//public class NCLInTime implements IEditorActionDelegate {
//	private NCLEditor editor = null;
//	private NCLEditor editorPrev = null;
//	private String doc1 = "";
//	private String doc2 = "";
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface
//	 * .action.IAction, org.eclipse.ui.IEditorPart)
//	 */
//	@Override
//	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
//		if (editor != null)
//			editorPrev = editor;
//
//		if (targetEditor == null)
//			return;
//
//		editor = ((NCLMultiPageEditor) targetEditor.getEditorSite().getPage()
//				.getActiveEditor()).getNCLEditor();
//
//		if (editorPrev == null)
//			editorPrev = editor;
//
//		if (editorPrev != editor)
//			action.setChecked(false);
//
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
//	 */
//	@Override
//	public void run(IAction action) {
//
//		String text = "";
//
//		text = editor.getInputDocument().get();
//
//		if (action.isChecked()) {
//			doc1 = text;
//		} else {
//			doc2 = text;
//			// Chama a funcao do Rodrigo
//			try {
//				DOMParser execute = new DOMParser();
//				String comando = execute.exe(doc2, doc1);
//				MessageConsole myConsole = findConsole("");
//				IWorkbenchPage page = editor.getEditorSite().getPage();
//				String id = IConsoleConstants.ID_CONSOLE_VIEW;
//				IConsoleView view = (IConsoleView) page.showView(id);
//				view.display(myConsole);
//				myConsole.clearConsole();
//				MessageConsoleStream out = myConsole.newMessageStream();
//
//				out.println(comando);
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParserConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SAXException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (TransformerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	private MessageConsole findConsole(String name) {
//		ConsolePlugin plugin = ConsolePlugin.getDefault();
//		IConsoleManager conMan = plugin.getConsoleManager();
//		IConsole[] existing = conMan.getConsoles();
//		for (int i = 0; i < existing.length; i++)
//			if (name.equals(existing[i].getName()))
//				return (MessageConsole) existing[i];
//		// no console found, so create a new one
//		MessageConsole myConsole = new MessageConsole(name, null);
//		conMan.addConsoles(new IConsole[] { myConsole });
//		return myConsole;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
//	 * .IAction, org.eclipse.jface.viewers.ISelection)
//	 */
//	@Override
//	public void selectionChanged(IAction action, ISelection selection) {
//		// System.out.println ("selectionChanged");
//	}
//
//}
