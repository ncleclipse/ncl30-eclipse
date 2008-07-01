package br.org.ginga.ncl.adapters.procedural;

import br.org.ginga.ncl.adapters.IFormatterPlayerAdapter;
import br.org.ginga.ncl.model.event.IFormatterEvent;

public interface IProceduralPlayerAdapter extends IFormatterPlayerAdapter{
	void setCurrentEvent(IFormatterEvent event);
}
