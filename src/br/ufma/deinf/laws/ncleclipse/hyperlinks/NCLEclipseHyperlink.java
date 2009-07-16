package br.ufma.deinf.laws.ncleclipse.hyperlinks;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

public class NCLEclipseHyperlink implements IHyperlink {
	int start;
	int length;
	
	private String location;
	private IRegion region;
	
	ITextEditor editor;
	
	public NCLEclipseHyperlink(ITextEditor editor, IRegion region, String text) {
	    this.region= region;
	    this.location = text;
	    
	    this.editor = editor;
	}
	
	public IRegion getHyperlinkRegion() {
	    return region;
	}
	
	public void open() {
	    if(location != null)
	    {
	        this.editor.setHighlightRange(0, 10, true);
	      //  this.editor.setFocus();
	    }
	}
	public String getTypeLabel() {
	    return null;
	}
	public String getHyperlinkText() {
	    return null;
	}


}
