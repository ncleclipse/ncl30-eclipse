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
