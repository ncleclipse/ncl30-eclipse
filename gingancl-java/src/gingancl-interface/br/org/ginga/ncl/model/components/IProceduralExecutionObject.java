package br.org.ginga.ncl.model.components;

import br.org.ginga.ncl.model.event.IFormatterEvent;

public interface IProceduralExecutionObject extends IExecutionObject{
	void setCurrentEvent(IFormatterEvent event);
}
