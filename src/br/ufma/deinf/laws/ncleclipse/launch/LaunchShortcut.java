package br.ufma.deinf.laws.ncleclipse.launch;

import java.io.IOException;

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
		System.out.println(Platform.getOS());
		if(Platform.getOS().equals("win32")){ //Windows
			String cmd [] = new String[1];
			gingaNcl = platformPath+"plugins/ncl_eclipse_1.0.0/gingancl-java";
			cmd[0] = "\""+gingaNcl+"/gingancl.bat\" "
				+"\""+file+"\"";
			System.out.println(cmd[0]);
			try {
				Process p = DebugPlugin.exec(cmd, new java.io.File(gingaNcl));
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else { //Linux
			String cmd [] = new String[2];
			gingaNcl = "/"+platformPath+"plugins/ncl_eclipse_1.0.0/gingancl-java/";
			cmd[0] = gingaNcl+"gingancl.sh";
			cmd[1] = file;
			System.out.println(cmd[0]);
			try {
				Process p = DebugPlugin.exec(cmd, new java.io.File(gingaNcl));
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
