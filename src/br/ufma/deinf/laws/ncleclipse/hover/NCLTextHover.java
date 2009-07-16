package br.ufma.deinf.laws.ncleclipse.hover;

import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;

public class NCLTextHover extends DefaultTextHover {
	public NCLTextHover(ISourceViewer sourceViewer) {
		super(sourceViewer);
	}

	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (hoverRegion != null) {
			return "Eu aqui!";
		}
		return "NULL";
	}

	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		Point selection = textViewer.getSelectedRange();
		if (selection.x <= offset && offset < selection.x + selection.y)
			return new Region(selection.x, selection.y);
		return new Region(offset, 0);
	}
}
