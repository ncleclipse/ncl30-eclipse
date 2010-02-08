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
package br.ufma.deinf.laws.ncleditor.editor.contentassist;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.eclipse.jface.text.contentassist.CompletionProposal;

import br.ufma.deinf.laws.util.UrlUtils;

/**
 * @author Roberto Azevedo
 * 
 * @since October 15 2008
 * 
 *        This class implements a mechanism to suggest URI of files based on
 *        pathRoot and a qualifier that is a substring of the final URI that the
 *        users want write.
 */

public class URIProposer {
	String rootPath;
	URI uriRoot;
	String root;
	boolean isRelative = false;

	public URIProposer(String rootPath) throws URISyntaxException {
		this.rootPath = UrlUtils.encodeURL(rootPath);
		uriRoot = new URI(this.rootPath);
		System.out.println(this.rootPath);
		root = rootPath;
	}

	private File computeParentFile(String qualifier) throws URISyntaxException {
		isRelative = !qualifier.startsWith("file://"); // true if qualifier is
		// relative
		File file; // file Path

		if (isRelative) {
			if (qualifier.contains("/")) {
				System.out.println(uriRoot.getPath() + "/"
						+ qualifier.substring(0, qualifier.lastIndexOf("/")));
				file = new File(uriRoot.getPath() + "/"
						+ qualifier.substring(0, qualifier.lastIndexOf("/"))); // is
				// relative
			} else
				file = new File(uriRoot.getPath());
		} else {
			String parentPath = qualifier;
			if (qualifier.contains("/")) {
				parentPath = parentPath.substring(0, parentPath
						.lastIndexOf("/"))
						+ "/";
			}
			System.out.print(parentPath);
			uriRoot = new URI(UrlUtils.encodeURL(parentPath));
			file = new File(uriRoot); // not relative
		}
		return file;
	}

	public Vector<String> getSrcSuggest(String qualifier) {
		File parent;
		String aux = qualifier;

		String temp[] = qualifier.split("\\" + "/");
		if (temp.length > 1)
			qualifier = temp[temp.length - 1];
		if (aux.endsWith("" + "/"))
			qualifier = "";

		String path = "";
		path = aux.substring(0, aux.length()
					- qualifier.length());
		parent = new File(path);
		if ((!(parent.isDirectory() || parent.isFile())) || aux.startsWith(".."))
			parent = new File(root + "/" + path);
			if (!parent.isDirectory()) {
				parent = new File(path);
			}
		if (!(parent.isFile() || parent.isDirectory()))
			return null;
		String list[] = parent.list();
		Vector<String> completions = new Vector<String>();
		if (list == null) return completions;
		for (int i = 0; i < list.length; i++) {
			if (new File(parent.getAbsolutePath() + "/"
					+ list[i]).isDirectory())
				list[i] += "/";
			if (list[i].startsWith(qualifier)) {
				completions.add(path + list[i]);

			}
		}
		return completions;
	}

	private Vector<String> getResource(String qualifier, boolean isFile) {
		Vector<String> resources = new Vector<String>();
		File f;
		String strSuggest;
		try {
			f = computeParentFile(qualifier);
			System.out.println("Absolute Path -> " + f.getAbsolutePath());
			String[] children = f.list();
			if (children == null)
				return resources;
			System.out.println(children.length);
			for (int i = 0; i < children.length; i++) {
				File suggest = new File(f.getAbsoluteFile() + "/" + children[i]);
				if ((suggest.isDirectory() && !isFile)
						|| (suggest.isFile() && isFile)) {
					if (!isRelative) {
						strSuggest = "file://" + uriRoot.getPath()
								+ children[i];
						if (!isFile)
							strSuggest += "/";
						resources.add(strSuggest);
					} else {
						if (qualifier.contains("/"))
							strSuggest = qualifier.substring(0, qualifier
									.lastIndexOf("/"))
									+ "/" + children[i];
						else
							strSuggest = children[i];
						if (!isFile)
							strSuggest += "/";
						resources.add(strSuggest);
					}
					System.out.println(strSuggest);
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resources;
	}

	/**
	 * 
	 * @param qualifier
	 * @return A vector of directories computed
	 */
	public Vector<String> getDirectories(String qualifier) {
		System.out.println("Computing Directories");
		return getResource(qualifier, false);
	}

	/**
	 * 
	 * @param qualifier
	 * @return A Vector of files computed
	 */
	public Vector<String> getFiles(String qualifier) {
		System.out.println("Computing Files");
		return getResource(qualifier, true);
	}

	/**
	 * just for tests
	 * 
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException {
		URIProposer fs = new URIProposer("file:///C:/");
		Vector<String> v = fs
				.getDirectories("file:///C:/Users/usuario/Program Files (x86)/");
		System.out.println("## DIRECTORIES ##");
		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.get(i));
		}

		System.out.println("## FILES ##");
		v = fs.getFiles("file:///Arquivos de Programas/");
		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.get(i));
		}
	}

}
