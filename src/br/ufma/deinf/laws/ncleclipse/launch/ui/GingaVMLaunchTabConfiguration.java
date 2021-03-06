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

/**
 * 
 * @author Roberto Azevedo <roberto@laws.deinf.ufma.br>
 *
 */
//FIXME: Habilitar o Botão Apply buscar os projetos e arquivos com extensão NCL 
public class GingaVMLaunchTabConfiguration extends GingaLaunchTabConfiguration {
	protected Label fRemoteAppDirPathLabel;
	protected Text fRemoteAppDirPathText;
	protected Label fHostLabel;
	protected Text fHostText;
	protected Label fUserNameLabel;
	protected Text fUserNameText;
	protected Label fUserPasswordLabel;
	protected Text fUserPasswordText;

	public static String DEFAULT_REMOTE_APP_DIR_PATH = "/misc/ncl30";
	public static String DEFAULT_HOST = "192.168.64.129";
	public static String DEFAULT_USER_NAME = "root";
	public static String DEFAULT_USER_PASSWORD = "telemidia";

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		GridData gd;
		gd = new GridData(GridData.FILL_HORIZONTAL);

		//Remote App
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fRemoteAppDirPathLabel = new Label(composite, SWT.NONE);
		fRemoteAppDirPathLabel.setText("Remote workspace:");
		fRemoteAppDirPathText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fRemoteAppDirPathText.setLayoutData(gd);

		//Host
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fHostLabel = new Label(composite, SWT.NONE);
		fHostLabel.setText("Host:");
		fHostText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fHostText.setLayoutData(gd);

		//User name
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fUserNameLabel = new Label(composite, SWT.NONE);
		fUserNameLabel.setText("User name:");
		fUserNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fUserNameText.setLayoutData(gd);

		//User password
		gd = new GridData(GridData.FILL_HORIZONTAL);

		fUserPasswordLabel = new Label(composite, SWT.NONE);
		fUserPasswordLabel.setText("User password:");
		fUserPasswordText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fUserPasswordText.setLayoutData(gd);
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			super.initializeFrom(configuration);
			
			fRemoteAppDirPathText.setText(
					configuration.getAttribute(
							"remoteWorkspace", 
							DEFAULT_REMOTE_APP_DIR_PATH));
			
			fHostText.setText(
					configuration.getAttribute(
							"hostName", 
							DEFAULT_HOST));
			
			fUserNameText.setText(
					configuration.getAttribute(
							"userName",
							DEFAULT_USER_NAME));
			
			fUserPasswordText.setText(
					configuration.getAttribute(
							"userPassword", 
							DEFAULT_USER_PASSWORD));
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		configuration.setAttribute(
				"remoteWorkspace", 
				fRemoteAppDirPathText.getText());
		
		configuration.setAttribute(
				"hostName", 
				fHostText.getText());
		
		configuration.setAttribute(
				"userName", 
				fUserNameText.getText());
		
		configuration.setAttribute(
				"userPassword", 
				fUserPasswordText.getText());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
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
