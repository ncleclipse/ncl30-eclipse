package br.ufma.deinf.laws.ncleclipse.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "conn". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class ConnectorBaseNewWizard extends NCLNewWizard {

	/**
	 * Constructor for ConnectorBaseNewWizard.
	 */
	public ConnectorBaseNewWizard() {
		super();
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */

	protected InputStream openContentStream(String fileId) {
		String contents =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n" +
			"<!-- Generated	by NCL Eclipse -->" + "\n" +			
			"<ncl id=\""+ fileId +"\" xmlns=\"http://www.ncl.org.br/NCL3.0/EDTVProfile\">" + "\n" +
			"\t<head>" + "\n" + "\n" +
			"\t</head>" + "\n" +
			"</ncl>";
		return new ByteArrayInputStream(contents.getBytes());
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ConnectorBaseNewWizardPage(selection);
		addPage(page);
	}

}