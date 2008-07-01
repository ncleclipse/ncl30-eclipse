package javax.tv.graphics;

import java.awt.Container;

import javax.tv.xlet.XletContext;

public class TVContainer {
	public static Container getRootContainer(XletContext context){
		if(context == null)
			throw new NullPointerException();
		return (java.awt.Container)context.getXletProperty("javax.tv.xlet.container");
	}
}
