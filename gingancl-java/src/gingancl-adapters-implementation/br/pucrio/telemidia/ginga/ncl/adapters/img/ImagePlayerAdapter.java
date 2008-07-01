package br.pucrio.telemidia.ginga.ncl.adapters.img;

import java.io.File;

import br.pucrio.telemidia.ginga.core.player.image.ImagePlayer;
import br.pucrio.telemidia.ginga.ncl.adapters.DefaultFormatterPlayerAdapter;

public class ImagePlayerAdapter extends DefaultFormatterPlayerAdapter {

	@Override
	protected void createPlayer() {
		File contentFile = new File(this.getMRL().getFile());
		if (contentFile.exists()){
			player = new ImagePlayer(this.getMRL());
		}else{
			System.err.println("[ERR] " + this.getClass().getCanonicalName() +": Could not find IMAGE!");
			player = null;
		}
		super.createPlayer();
	}

}
