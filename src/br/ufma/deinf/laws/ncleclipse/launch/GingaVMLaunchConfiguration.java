package br.ufma.deinf.laws.ncleclipse.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

public class GingaVMLaunchConfiguration extends LaunchConfigurationDelegate{
	private String project;
	private String nclFile;
	private String nclLauncherPath;
	private String remoteAppDirPath;	
	private String host;
	private String userName;
	private String userPassword;
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getNclFile() {
		return nclFile;
	}

	public void setNclFile(String nclFile) {
		this.nclFile = nclFile;
	}

	public String getNclLauncherPath() {
		return nclLauncherPath;
	}

	public void setNclLauncherPath(String nclLauncherPath) {
		this.nclLauncherPath = nclLauncherPath;
	}

	public String getRemoteAppDirPath() {
		return remoteAppDirPath;
	}

	public void setRemoteAppDirPath(String remoteAppDirPath) {
		this.remoteAppDirPath = remoteAppDirPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		System.out.println("Ginga VM Launch Configuration");
	}

}
