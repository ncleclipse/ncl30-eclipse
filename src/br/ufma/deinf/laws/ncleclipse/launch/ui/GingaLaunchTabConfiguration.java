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
package br.ufma.deinf.laws.ncleclipse.launch.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

//FIXME: Fazer igual ao GingaVM
//TODO: Busca do project ver new wizard

public class GingaLaunchTabConfiguration extends AbstractLaunchConfigurationTab {
	protected Label fProjectLabel;
	protected Text fProjectText;
	protected Label fNCLFileLabel;
	protected Text fNCLFileText;
	protected Label fNCLLauncherPathLabel;
	protected Text fNCLLauncherPathText;

	public static String DEFAULT_PROJECT = "";
	public static String DEFAULT_NCL_FILE = "";
	public static String DEFAULT_NCL_LAUNCHER_PATH = "/misc/launcher.sh";

	protected GridLayout topLayout = null;
	protected Composite composite = null;

	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		composite = new Composite(parent, SWT.NONE);
		topLayout = new GridLayout();
		topLayout.numColumns = 2;
		composite.setLayout(topLayout);
		setControl(composite);

		GridData gd;
		gd = new GridData(GridData.FILL_HORIZONTAL);
/*
		//Project
		fProjectLabel = new Label(composite, SWT.NONE);
		fProjectLabel.setText("Project:");
		fProjectText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fProjectText.setLayoutData(gd);

		//NCL File
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fNCLFileLabel = new Label(composite, SWT.NONE);
		fNCLFileLabel.setText("NCL File:");
		fNCLFileText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fNCLFileText.setLayoutData(gd);
*/
		//Launcher Path
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fNCLLauncherPathLabel = new Label(composite, SWT.NONE);
		fNCLLauncherPathLabel.setText("Remote laucher:");
		fNCLLauncherPathText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fNCLLauncherPathText.setLayoutData(gd);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Main";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		try {
		/*	fProjectText.setText(configuration.getAttribute("project",
					DEFAULT_PROJECT));
			fNCLFileText.setText(configuration.getAttribute("nclFile",
					DEFAULT_NCL_FILE));
			*/fNCLLauncherPathText.setText(
					configuration.getAttribute(
							"remoteLauncher", 
							DEFAULT_NCL_LAUNCHER_PATH));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
//		configuration.setAttribute("project", fProjectText.getText());
//		configuration.setAttribute("nclFile", fNCLFileText.getText());
		configuration.setAttribute("remoteLauncher", fNCLLauncherPathText
				.getText());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#isValid(ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		//TODO: validate 
		return true;
	}

}
