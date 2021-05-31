PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

ELECTRON_HOME=`cd "$PRGDIR/.." && pwd`

ELECTRON_SERVER_LIB_HOME="$ELECTRON_HOME/lib"

ELECTRON_SERVER_LIB=

for file in `ls -a "$ELECTRON_HOME/lib" | grep '.*\.jar$'`
do
    if test -f "$ELECTRON_SERVER_LIB_HOME/$file"
    then
      ELECTRON_SERVER_LIB="$ELECTRON_SERVER_LIB:$ELECTRON_SERVER_LIB_HOME/$file"
    fi
done

exec java \
        -classpath "$ELECTRON_SERVER_LIB" \
        com.github.gotz9.electron.server.ApplicationLauncher "$@"