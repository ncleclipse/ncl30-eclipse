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

package br.ufma.deinf.laws.ncleclipse.launch;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

/**
 * TODO: All
 * Initializes and activates the launcher for the GrGen.NET application.
 */
public class GingaNCLLaunch {
	

	   private static GingaNCLLaunch instance;

	   /** Public ID of GrGen.NET application name. */
	   public static final String GingaNCLLaunch_ID = "br.ufma.deinf.laws.ncleclipse.gingancllaunch";
	   /** Launch configuration. */
	   private ILaunchConfiguration config;
	   /** The launch configuration's working copy. */
	   private ILaunchConfigurationWorkingCopy wc;

	   private GingaNCLLaunch() {
	      try {
	         initGingaNCLLaunch();
	      } catch (CoreException e) {
	         e.printStackTrace();
	      }
	   }

	   /**
	    * Returns the singleton instance of GrgenLauncher.
	    *
	    * @return the single instance of GrgenLauncher
	    */
	   public static GingaNCLLaunch getDefault() {
	      if (instance == null) {
	         instance = new GingaNCLLaunch();
	      }
	      return instance;
	   }

	   /**
	    * Initialize the launcher for the GrGen.NET application. Initializes the default
	    * debugger plug-in and its launch manager. A launch configuration of type 'Program'
	    * will be added to the launchers shortcuts.
	    *
	    * @throws CoreException
	    */
	   private void initGingaNCLLaunch() throws CoreException {
	      String grgenPath = getGingaNCLPath(System.getenv("PATH"));

	      // Get the default launch manager
	      DebugPlugin debug = DebugPlugin.getDefault();
	      ILaunchManager lm = debug.getLaunchManager();
	      // Set launch configuration type to 'Program'
	      ILaunchConfigurationType type = lm
	            .getLaunchConfigurationType("org.eclipse.ui.externaltools.ProgramLaunchConfigurationType");
	      wc = type.newInstance(null, GingaNCLLaunch_ID);
	      // Set necessary attributes for the launch configuration
	      wc.setAttribute("org.eclipse.debug.core.appendEnvironmentVariables",
	            true);
	      wc.setAttribute("org.eclipse.ui.externaltools.ATTR_LOCATION", grgenPath
	            + File.separator + "GrShell.exe");
	   }

	   /**
	    * FOR INTERNAL USE ONLY.
	    *
	    * Resolves the path of the GrGen.NET application, which is set in the
	    * 'PATH' environment variable.
	    *
	    * @param getenv
	    *            the environment variable
	    * @return the path where the GrGen.NET application is located
	    */
	   private String getGingaNCLPath(String getenv) {
	      // Split the environment up to an array
	      String[] getenvs = getenv.split(";");

	      String env = null;
	      for (int i = 0; i < getenvs.length; i++) {
	         if (getenvs[i].contains("GrGen")) {
	            env = getenvs[i];
	            break;
	         }
	      }
	      // Return GrGen.NET's application path
	      return env;
	   }

	   /**
	    * Launch the GrGen.NET application project in the specified working directory.
	    *
	    * @param dir
	    *            the application's working directory
	    * @throws CoreException
	    */
	   public void launchGrgenInWorkingDirectory(String dir)
	         throws NullPointerException, CoreException {
	      if (dir != null && dir.length() > 0) {
	         wc.setAttribute(
	               "org.eclipse.ui.externaltools.ATTR_WORKING_DIRECTORY",
	               "${workspace_loc}\\" + dir);
	         config = wc.doSave();
	         config.launch(ILaunchManager.RUN_MODE, null);
	      } else
	         throw new NullPointerException("Working directory is unspecified.");
	   }

	}
