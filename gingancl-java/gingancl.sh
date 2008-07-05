#!/bin/sh

WORKINGDIR=${0%/*}

DYLD_LIBRARY_PATH=$WORKINGDIR/lib
export DYLD_LIBRARY_PATH


JAVA_CMD="$JAVA_HOME/bin/java"
if [ -z "$JAVA_HOME" ]; then
    JAVA_CMD="java"
fi

LIB_PATH="./lib"
CORE_LIBS_PATH="$LIB_PATH/core"
EXT_LIBS_PATH="$LIB_PATH/ext"

echo "Ginga-NCL Emulator v1.1.1"
echo "Copyright PUC-Rio (Laboratorio TeleMidia), 1989-2007"

$JAVA_CMD -cp .\
:$CORE_LIBS_PATH/nclInterface.jar\
:$CORE_LIBS_PATH/nclConverter.jar\
:$CORE_LIBS_PATH/nclImplementation.jar\
:$CORE_LIBS_PATH/gingancl-gui.jar\
:$CORE_LIBS_PATH/gingancl-Interfaces.jar\
:$CORE_LIBS_PATH/gingacc-io-impl.jar\
:$CORE_LIBS_PATH/gingacc-io-interface.jar\
:$CORE_LIBS_PATH/gingacc-player-impl.jar\
:$CORE_LIBS_PATH/gingacc-player-interface.jar\
:$CORE_LIBS_PATH/gingancl-adapters-Impl.jar\
:$CORE_LIBS_PATH/gem.jar\
:$CORE_LIBS_PATH/gingancl-Impl.jar\
:$EXT_LIBS_PATH/jmf.jar\
:$EXT_LIBS_PATH/fobs4jmf.jar\
:$EXT_LIBS_PATH/luajava-1.1.jar \
-Djava.library.path=$EXT_LIBS_PATH \
-Xmx512m \
br.pucrio.telemidia.ginga.ncl.gui.GingaPlayerWindow $1
