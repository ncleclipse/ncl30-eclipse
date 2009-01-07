package br.ufma.deinf.laws.ncleclipse.launch.ui;


import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GingaEmulatorLaunchTabConfiguration extends AbstractLaunchConfigurationTab {
	Label fProjectLabel;
	Text fProjectText;
	Label fNCLFileLabel;
	Text fNCLFileText;
	Label fNCLLauncherPathLabel;
	Text fNCLLauncherPathText;
	
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
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Main";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		
	}
	
}
