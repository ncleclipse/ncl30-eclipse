package br.ufma.deinf.laws.ncleclipse.launch;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class LaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		// TODO Auto-generated method stub
		String file = selection.toString();
		System.out.println(file);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO Auto-generated method stub
		String file = ((IFileEditorInput) editor.getEditorInput()).getFile().getLocation().toString();
		run(file);
	}
	
	public void run(String file){
		String gingaNcl = "D:/workspace/ncl-eclipse/gingancl-java";
		String cmd [] = new String[1];
		cmd[0] = "\""+gingaNcl+"/gingancl.bat\" "
			+"\""+file+"\"";
		try {
			Process p = DebugPlugin.exec(cmd, new File(gingaNcl));
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
