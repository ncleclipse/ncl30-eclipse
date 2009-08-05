package br.ufma.deinf.laws.ncleclipse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.IPreferenceConstants;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;

public class Language
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public Language() {
		super(GRID);
		setPreferenceStore(NCLEditorPlugin.getDefault().getPreferenceStore());
		setDescription("Preferences related to NCL Eclipse Language");
	}
	
	public void createFieldEditors() {
		addField(
				new RadioGroupFieldEditor(
						PreferenceConstants.P_LANGUAGE,
						"Error Language",
						1,
						new String[][] {
							{"Portuguese", 
							 "messagesPt"
							},
							{"English", 
							 "messagesEn"
							}
						},
						getFieldEditorParent(),
						true)
		);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}