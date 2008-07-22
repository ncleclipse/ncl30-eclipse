package br.ufma.deinf.laws.ncleclipse;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class NCLEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "br.ufma.deinf.laws.ncleclipse.editors.NCLEditor";

	// The shared instance
	private static NCLEditorPlugin plugin;
	
	/**
	 * The constructor
	 */
	public NCLEditorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
		public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static NCLEditorPlugin getDefault() {
		return plugin;
	}
	

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static final String LINK_ICON = "br.ufma.deinf.laws.ncleclipse.image.link_icon";
	
	protected void initializeImageRegistry(ImageRegistry registry) {
        Bundle bundle = Platform.getBundle(PLUGIN_ID);
        IPath path = new Path("icons/sample.gif");
        URL url = Platform.find(bundle, path);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        registry.put(LINK_ICON, desc);
    }

}
