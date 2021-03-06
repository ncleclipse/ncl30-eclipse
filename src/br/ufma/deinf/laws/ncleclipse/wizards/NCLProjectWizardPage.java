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
package br.ufma.deinf.laws.ncleclipse.wizards;

import java.net.URI;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WorkingSetGroup;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea;
import org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter;

import br.ufma.deinf.laws.ncleclipse.NCLEditorMessages;

/**
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
public class NCLProjectWizardPage extends WizardPage {

    // initial value stores
    private String initialProjectFieldValue;
    
    

    // widgets
    private Text projectNameField;
    private Text nameMainNclNameField;
    private Text idMainNclNameField;
    private Button importConnBaseCheckBox;
    private Button createMediaDirCheckBox;
    private Button createMainNclCheckBox;
    private Label fileIdLabel;
    private Label fileNameLabel;
    protected String extension;

    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
        	setLocationForSelection();
            boolean valid = validatePage();
            setPageComplete(valid);
                
        }
    };

	private ProjectContentsLocationArea locationArea;

	private WorkingSetGroup workingSetGroup;

    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;

    /**
     * Creates a new project creation wizard page.
     *
     * @param pageName the name of this page
     */
    public NCLProjectWizardPage (String pageName) {
    	super(pageName);
	    setPageComplete(false);
	    setExtension(".ncl");
    }

    /**
	 * Creates a new project creation wizard page.
	 * 
	 * @param pageName
	 * @param selection
	 * @param workingSetTypes
	 * 
	 * @deprecated default placement of the working set group has been removed.
	 *             If you wish to use the working set block please call
	 *             {@link #createWorkingSetGroup(Composite, IStructuredSelection, String[])}
	 *             in your overridden {@link #createControl(Composite)}
	 *             implementation.
	 * @since 3.4
	 */
	public NCLProjectWizardPage (String pageName,
			IStructuredSelection selection, String[] workingSetTypes) {
		this(pageName);
	}

	/** (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
    

        initializeDialogUnits(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite,
                IIDEHelpContextIds.NEW_PROJECT_WIZARD_PAGE);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createProjectNameGroup(composite);
        locationArea = new ProjectContentsLocationArea(getErrorReporter(), composite);
        if(initialProjectFieldValue != null) {
			locationArea.updateProjectName(initialProjectFieldValue);
		}
        
        createOptions(composite);
        createMainFileOptions(composite);
        createMainNclCheckBox.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event e) 
            {
                Button button = (Button)(e.widget);
				
                if (button.getSelection()){
                	fileIdLabel.setEnabled(true);
                	idMainNclNameField.setEnabled(true);
                	fileNameLabel.setEnabled(true);
                	nameMainNclNameField.setEnabled(true);
                }
                else {
                	fileIdLabel.setEnabled(false);
                	idMainNclNameField.setEnabled(false);
                	idMainNclNameField.setText("");
                	fileNameLabel.setEnabled(false);
                	nameMainNclNameField.setEnabled(false);
                	nameMainNclNameField.setText("");
                	
                }
            }
        });
        
		// Scale the button based on the rest of the dialog
		setButtonLayoutData(locationArea.getBrowseButton());
		
        setPageComplete(validatePage());
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
        Dialog.applyDialogFont(composite);
    }
    
    public boolean isImportConnectorBase (){
    	return importConnBaseCheckBox.getSelection();
    }
    
    public boolean mustCreateMediaDir(){
    	return createMediaDirCheckBox.getSelection();
    }
    
    public boolean mustCreateMainNcl(){
    	return createMainNclCheckBox.getSelection();
    }
    
    public String getFileName(){
    	return nameMainNclNameField.getText();
    }
    
    public String getFileId() {
		return idMainNclNameField.getText();
	}
    
    public void setExtension(String ext) {
		this.extension = ext;
	}
    public String getExtension() {
		return extension;
	}
    
    /**
	 * @param parent
	 */
	private void createOptions(Composite parent) {
        Composite checkBoxGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        checkBoxGroup.setLayout(layout);
        checkBoxGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        importConnBaseCheckBox = new Button (checkBoxGroup, SWT.CHECK | SWT.RIGHT);
        importConnBaseCheckBox.setSelection(false);
        importConnBaseCheckBox.setText(NCLEditorMessages.getInstance().getString(
							"NCLProjectWizard.ImportConnectorBase"));
        importConnBaseCheckBox.setFont(parent.getFont());
        
        createMediaDirCheckBox = new Button (checkBoxGroup, SWT.CHECK | SWT.RIGHT);
        createMediaDirCheckBox.setSelection(false);
        createMediaDirCheckBox.setText(NCLEditorMessages.getInstance().getString(
							"NCLProjectWizard.CreateMediaDir"));
        createMediaDirCheckBox.setFont(parent.getFont());
        
        createMainNclCheckBox = new Button (checkBoxGroup, SWT.CHECK | SWT.RIGHT);
        createMainNclCheckBox.setSelection(false);
        createMainNclCheckBox.setText(NCLEditorMessages.getInstance().getString(
							"NCLProjectWizard.CreateMainNcl"));
        createMainNclCheckBox.setFont(parent.getFont());
            
	}
	
	public void createMainFileOptions(Composite parent){
		 Composite projectGroup = new Composite(parent, SWT.NONE);
	        GridLayout layout1 = new GridLayout();
	        layout1.numColumns = 2;
	        projectGroup.setLayout(layout1);
	        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	        
	        // main NCL ID label
	        fileIdLabel = new Label(projectGroup, SWT.NONE);
	        fileIdLabel.setText("&Id:");
	        fileIdLabel.setFont(parent.getFont());
	        
	        idMainNclNameField = new Text(projectGroup, SWT.BORDER);
	        GridData data = new GridData(GridData.FILL_HORIZONTAL);
	        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
	        idMainNclNameField.setLayoutData(data);
	        idMainNclNameField.setFont(parent.getFont());
	        idMainNclNameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					fileIdChanged();
				}
			});
	        
	        // main NCL name label 
	        fileNameLabel = new Label(projectGroup, SWT.NONE);
	        fileNameLabel.setText(IDEWorkbenchMessages.WizardNewFileCreationPage_fileLabel);
	        fileNameLabel.setFont(parent.getFont());

	        // new file name entry field
	        nameMainNclNameField = new Text(projectGroup, SWT.BORDER);
	        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
	        nameMainNclNameField.setLayoutData(data);
	        nameMainNclNameField.setFont(parent.getFont());
	        nameMainNclNameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					fileNameChanged();
				}
			});
	        
	        idMainNclNameField.setEnabled(false);
	        nameMainNclNameField.setEnabled(false);
	        fileIdLabel.setEnabled(false);
	        fileNameLabel.setEnabled(false);
	}
	
	protected void fileNameChanged() {
		String fileName = getFileName();
		if (fileName.length() == 0 && mustCreateMainNcl()) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		//System.out.println(fileName);
		if (!fileName.endsWith(".ncl") && mustCreateMainNcl()) {
			updateStatus("File extension must be \"" + getExtension() + "\"");
			return;
		}
		updateStatus(null);
	}
	
	protected void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	protected void fileIdChanged() {
		String fileId = getFileId();
		nameMainNclNameField.setText(fileId + getExtension());
		if (fileId.length() == 0 && mustCreateMainNcl()) {
			updateStatus("File id must be specified.");
			return;
		}
		updateStatus(null);
	}
	/**
	 * Create a working set group for this page. This method can only be called
	 * once.
	 * 
	 * @param composite
	 *            the composite in which to create the group
	 * @param selection
	 *            the current workbench selection
	 * @param supportedWorkingSetTypes
	 *            an array of working set type IDs that will restrict what types
	 *            of working sets can be chosen in this group
	 * @return the created group. If this method has been called previously the
	 *         original group will be returned.
	 * @since 3.4
	 */
	public WorkingSetGroup createWorkingSetGroup(Composite composite,
			IStructuredSelection selection, String[] supportedWorkingSetTypes) {
		if (workingSetGroup != null)
			return workingSetGroup;
		workingSetGroup = new WorkingSetGroup(composite, selection,
				supportedWorkingSetTypes);
		return workingSetGroup;
	}
    
    /**
	 * Get an error reporter for the receiver.
	 * @return IErrorMessageReporter
	 */
	private IErrorMessageReporter getErrorReporter() {
		return new IErrorMessageReporter(){
			/* (non-Javadoc)
			 * @see org.eclipse.ui.internal.ide.dialogs.ProjectContentsLocationArea.IErrorMessageReporter#reportError(java.lang.String)
			 */
	
			public void reportError(String errorMessage, boolean infoOnly) {
				if (infoOnly) {
					setMessage(errorMessage, IStatus.INFO);
					setErrorMessage(null);
				}
				else
					setErrorMessage(errorMessage);
				boolean valid = errorMessage == null;
				if(valid) {
					valid = validatePage();
				}
				
				setPageComplete(valid);
			}
		};
	}

    /**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectNameGroup(Composite parent) {
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setText(IDEWorkbenchMessages.WizardNewProjectCreationPage_nameLabel);
        projectLabel.setFont(parent.getFont());

        // new project name entry field
        projectNameField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(data);
        projectNameField.setFont(parent.getFont());
        
        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null) {
			projectNameField.setText(initialProjectFieldValue);
		}
        projectNameField.addListener(SWT.Modify, nameModifyListener);
    }

    /**
     * Returns the current project location path as entered by 
     * the user, or its anticipated initial value.
     * Note that if the default has been returned the path
     * in a project description used to create a project
     * should not be set.
     *
     * @return the project location path or its anticipated initial value.
     */
    public IPath getLocationPath() {
        return new Path(locationArea.getProjectLocation());
    }
    
    /**
    /**
     * Returns the current project location URI as entered by 
     * the user, or <code>null</code> if a valid project location
     * has not been entered.
     *
     * @return the project location URI, or <code>null</code>
     * @since 3.2
     */
    public URI getLocationURI() {
    	return locationArea.getProjectLocationURI();
    }
    
    
    /**
	 * Creates a project resource handle for the current project name field
	 * value. The project handle is created relative to the workspace root.
	 * <p>
	 * This method does not create the project resource; this is the
	 * responsibility of <code>IProject::create</code> invoked by the new
	 * project resource wizard.
	 * </p>
	 * 
	 * @return the new project resource handle
	 */
    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(
                getProjectName());
    }

    /**
     * Returns the current project name as entered by the user, or its anticipated
     * initial value.
     *
     * @return the project name, its anticipated initial value, or <code>null</code>
     *   if no project name is known
     */
    public String getProjectName() {
        if (projectNameField == null) {
			return initialProjectFieldValue;
		}

        return getProjectNameFieldValue();
    }

    /**
     * Returns the value of the project name field
     * with leading and trailing spaces removed.
     * 
     * @return the project name in the field
     */
    private String getProjectNameFieldValue() {
        if (projectNameField == null) {
			return ""; //$NON-NLS-1$
		}

        return projectNameField.getText().trim();
    }

    /**
     * Sets the initial project name that this page will use when
     * created. The name is ignored if the createControl(Composite)
     * method has already been called. Leading and trailing spaces
     * in the name are ignored.
     * Providing the name of an existing project will not necessarily 
     * cause the wizard to warn the user.  Callers of this method 
     * should first check if the project name passed already exists 
     * in the workspace.
     * 
     * @param name initial project name for this page
     * 
     * @see IWorkspace#validateName(String, int)
     * 
     */
    public void setInitialProjectName(String name) {
        if (name == null) {
			initialProjectFieldValue = null;
		} else {
            initialProjectFieldValue = name.trim();
            if(locationArea != null) {
				locationArea.updateProjectName(name.trim());
			}
        }
    }

    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    void setLocationForSelection() {
    	locationArea.updateProjectName(getProjectNameFieldValue());
    }

  
    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

        String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage(null);
            setMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectNameEmpty);
            return false;
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents,
                IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }

        IProject handle = getProjectHandle();
        if (handle.exists()) {
            setErrorMessage(IDEWorkbenchMessages.WizardNewProjectCreationPage_projectExistsMessage);
            return false;
        }
                
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				getProjectNameFieldValue());
		locationArea.setExistingProject(project);
		
		String validLocationMessage = locationArea.checkValidLocation();
		if (validLocationMessage != null) { // there is no destination location given
			setErrorMessage(validLocationMessage);
			return false;
		}

        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    /*
     * see @DialogPage.setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
			projectNameField.setFocus();
		}
    }

    /**
     * Returns the useDefaults.
     * @return boolean
     */
    public boolean useDefaults() {
        return locationArea.isDefault();
    }

    /**
	 * Return the selected working sets, if any. If this page is not configured
	 * to interact with working sets this will be an empty array.
	 * 
	 * @return the selected working sets
	 * @since 3.4
	 */
	public IWorkingSet[] getSelectedWorkingSets() {
		return workingSetGroup == null ? new IWorkingSet[0] : workingSetGroup
				.getSelectedWorkingSets();
	}
}
