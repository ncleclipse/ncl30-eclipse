package br.ufma.deinf.laws.ncleclipse.launch;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class LaunchShortcut implements ILaunchShortcut {
	private String platformPath = Platform.getInstallLocation().getURL().getPath().substring(1);
	@Override
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		String file = ((File)((TreeSelection)selection).getFirstElement()).getLocation().toString();
		run(file);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub
		String file = ((IFileEditorInput) editor.getEditorInput()).getFile().getLocation().toString();
		run(file);
	}
	
	public void run(String file){
		String gingaNcl = platformPath+"/plugins/ncl_eclipse_1.0.0/gingancl-java";
		String cmd [] = new String[1];
		cmd[0] = "\""+gingaNcl+"/gingancl.bat\" "
			+"\""+file+"\"";
		try {
			Process p = DebugPlugin.exec(cmd, new java.io.File(gingaNcl));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
