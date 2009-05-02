package br.ufma.deinf.laws.ncleclipse.hyperlinks;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.internal.Workbench;

public class NCLEclipseHyperlink implements IHyperlink {

	private String location;
	private IRegion region;
	
	public NCLEclipseHyperlink(IRegion region, String text) {
	    this.region= region;
	    this.location = text;
	}
	
	public IRegion getHyperlinkRegion() {
	    return region;
	}
	
	public void open() {
	    if(location!=null)
	    {
	        TextEditor editor = (TextEditor) Workbench.getInstance().getActiveWorkbenchWindow().getActivePage();
	        editor.selectAndReveal(1,0);
	        editor.setFocus();
	    }
	}
	public String getTypeLabel() {
	    return null;
	}
	public String getHyperlinkText() {
	    return null;
	}


}
