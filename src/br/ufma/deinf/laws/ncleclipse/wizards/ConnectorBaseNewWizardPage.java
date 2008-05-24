package br.ufma.deinf.laws.ncleclipse.wizards;

import org.eclipse.jface.viewers.ISelection;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (conn).
 */

public class ConnectorBaseNewWizardPage extends NCLNewWizardPage {
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ConnectorBaseNewWizardPage(ISelection selection) {
		super(selection);
		setExtension(".ncl");
		setTitle("Connector Base New Wizard.");
		setDescription("This wizard creates a new file with *.conn extension that can be opened by the ncl-eclipse editor.");
		this.selection = selection;
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	protected void initialize() {
		super.initialize();
		fileId.setText("new_connectorBase");
	}
}
