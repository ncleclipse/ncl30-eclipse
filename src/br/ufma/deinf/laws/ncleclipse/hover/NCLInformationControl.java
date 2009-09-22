package br.ufma.deinf.laws.ncleclipse.hover;

import org.eclipse.jface.text.IInformationControlExtension2;

public class NCLInformationControl implements IInformationControlExtension2{

	@Override
	public void setInput(Object input) {
		if (input instanceof String){
			((String) input).length();
		}
	}
}
