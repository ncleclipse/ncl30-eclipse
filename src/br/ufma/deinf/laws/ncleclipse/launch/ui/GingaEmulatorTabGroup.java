package br.ufma.deinf.laws.ncleclipse.launch.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class GingaEmulatorTabGroup extends AbstractLaunchConfigurationTabGroup  {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new GingaEmulatorLaunchTabConfiguration(),
		};
		setTabs(tabs);
		
	}

}
