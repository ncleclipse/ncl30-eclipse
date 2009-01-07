/******************************************************************************
Este arquivo é parte da implementação do ambiente de autoria em Nested Context
Language - NCL Eclipse.

Direitos Autorais Reservados (c) 2007-2008 UFMA/LAWS (Laboratório de Sistemas Avançados da Web) 

Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob 
os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
Software Foundation.

Este programa é distribuído na expectativa de que seja útil, porém, SEM 
NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU 
ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do 
GNU versão 2 para mais detalhes. 

Você deve ter recebido uma cópia da Licença Pública Geral do GNU versão 2 junto 
com este programa; se não, escreva para a Free Software Foundation, Inc., no 
endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informações:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

******************************************************************************
This file is part of the authoring environment in Nested Context Language -
NCL Eclipse.

Copyright: 2007-2008 UFMA/LAWS (Laboratory of Advanced Web Systems), All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncleclipse@laws.deinf.ufma.br
http://www.laws.deinf.ufma.br/ncleclipse
http://www.laws.deinf.ufma.br

*******************************************************************************/
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


//TODO: Habilitar o botão apply (ainda não sei como)
public class GingaVMLaunchTabConfiguration extends AbstractLaunchConfigurationTab{
	private Label fProjectLabel;
	private Text fProjectText;
	private Label fNCLFileLabel;
	private Text fNCLFileText;
	private Label fNCLLauncherPathLabel;
	private Text fNCLLauncherPathText;
	private Label fRemoteAppDirPathLabel;
	private Text fRemoteAppDirPathText;
	private Label fHostLabel;
	private Text fHostText;
	private Label fUserNameLabel;
	private Text fUserNameText;
	private Label fUserPasswordLabel;
	private Text fUserPasswordText;
	
	
	public static String DEFAULT_PROJECT = "";
	public static String DEFAULT_NCL_FILE = "";
	public static String DEFAULT_NCL_LAUNCHER_PATH = "/misc/gingaNcl";
	public static String DEFAULT_REMOTE_APP_DIR_PATH = "/misc/ncl30/";
	public static String DEFAULT_HOST = "192.168.117.1";
	public static String DEFAULT_USER_NAME = "root";
	public static String DEFAULT_USER_PASSWORD = "telemidia";
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		comp.setLayout(topLayout);
		setControl(comp);
		
		GridData gd;
		gd = new GridData(GridData.FILL_HORIZONTAL);

		//Project
			fProjectLabel = new Label(comp, SWT.NONE);
			fProjectLabel.setText("Project:");
			fProjectText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fProjectText.setLayoutData(gd);
		
		
		//NCL File
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fNCLFileLabel = new Label(comp, SWT.NONE);
			fNCLFileLabel.setText("NCL File:");
			fNCLFileText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fNCLFileText.setLayoutData(gd);
			
		//Launcher Path
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fNCLLauncherPathLabel = new Label(comp, SWT.NONE);
			fNCLLauncherPathLabel.setText("NCL Launcher Path:");
			fNCLLauncherPathText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fNCLLauncherPathText.setLayoutData(gd);
			
		//Remote App
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fRemoteAppDirPathLabel = new Label(comp, SWT.NONE);
			fRemoteAppDirPathLabel.setText("Remote apps directory path:");
			fRemoteAppDirPathText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fRemoteAppDirPathText.setLayoutData(gd);

		//Host
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fHostLabel = new Label(comp, SWT.NONE);
			fHostLabel.setText("Host:");
			fHostText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fHostText.setLayoutData(gd);

		//User name
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fUserNameLabel = new Label(comp, SWT.NONE);
			fUserNameLabel.setText("User name:");
			fUserNameText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fUserNameText.setLayoutData(gd);
		
		//User password
			gd = new GridData(GridData.FILL_HORIZONTAL);
			
			fUserPasswordLabel = new Label(comp, SWT.NONE);
			fUserPasswordLabel.setText("User password:");
			fUserPasswordText = new Text(comp, SWT.SINGLE | SWT.BORDER);
			fUserPasswordText.setLayoutData(gd);
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			fProjectText.setText(configuration.getAttribute("project", DEFAULT_PROJECT));
			fNCLFileText.setText(configuration.getAttribute("nclFile", DEFAULT_NCL_FILE));
			fNCLLauncherPathText.setText(configuration.getAttribute("nclLauncherPath", DEFAULT_NCL_LAUNCHER_PATH));
			fRemoteAppDirPathText.setText(configuration.getAttribute("remoteAppDirPath", DEFAULT_REMOTE_APP_DIR_PATH));
			fHostText.setText(configuration.getAttribute("host", DEFAULT_HOST));
			fUserNameText.setText(configuration.getAttribute("userName", DEFAULT_USER_NAME));
			fUserPasswordText.setText(configuration.getAttribute("userPassword", DEFAULT_USER_PASSWORD));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute("project", fProjectText.getText());
		configuration.setAttribute("nclFile", fNCLFileText.getText());
		configuration.setAttribute("nclLauncherPath", fNCLLauncherPathText.getText());
		configuration.setAttribute("remoteAppDirPath", fRemoteAppDirPathText.getText());
		configuration.setAttribute("host", fHostText.getText());
		configuration.setAttribute("userName", fUserNameText.getText());
		configuration.setAttribute("userPassword", fUserPasswordText.getText());
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
