package br.ufma.deinf.laws.ncleclipse.hover;

import javax.swing.JComponent;

public class PreViewImage extends JComponent {

	private String path;

	public PreViewImage(String filename) {
		this.path = filename;

	}

	public String toString() {

		return this.path;
	}
}