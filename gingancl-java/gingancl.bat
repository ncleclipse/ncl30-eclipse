@echo off
set CMPDEPDIR=./lib
set CMPDEPLIBDIR=%CMPDEPDIR%/ext
set CMPDEPCOREDIR=%CMPDEPDIR%/core

echo Ginga-NCL Emulator v1.1.1
echo Copyright PUC-Rio (Laboratorio TeleMidia), 1989-2007

java -cp .;"%CMPDEPLIBDIR%/fobs4jmf.jar";"%CMPDEPLIBDIR%/jmf.jar";"%CMPDEPCOREDIR%/gem.jar";"%CMPDEPCOREDIR%/gingacc-io-impl.jar";"%CMPDEPCOREDIR%/gingacc-io-interface.jar";"%CMPDEPCOREDIR%/gingacc-player-impl.jar";"%CMPDEPCOREDIR%/gingacc-player-interface.jar";"%CMPDEPCOREDIR%/gingancl-adapters-Impl.jar";"%CMPDEPCOREDIR%/gingancl-gui.jar";"%CMPDEPCOREDIR%/gingancl-Impl.jar";"%CMPDEPCOREDIR%/gingancl-Interfaces.jar";"%CMPDEPCOREDIR%/ncl-domImpl.jar";"%CMPDEPCOREDIR%/nclConverter.jar";"%CMPDEPCOREDIR%/nclImplementation.jar";"%CMPDEPCOREDIR%/nclInterface.jar";"%CMPDEPLIBDIR%/cobra.jar";"%CMPDEPLIBDIR%/js.jar";"%CMPDEPLIBDIR%/luajava-1.1.jar"; -Djava.library.path="%CMPDEPLIBDIR%" br.pucrio.telemidia.ginga.ncl.gui.GingaPlayerWindow %1