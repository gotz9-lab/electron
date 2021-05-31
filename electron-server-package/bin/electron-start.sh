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

[ -f "$ELECTRON_HOME/conf/default.properties" ] && ELECTRON_CONFIG_FILE="$ELECTRON_HOME/conf/default.properties"
[ -f "$ELECTRON_HOME/conf/config.properties" ] && ELECTRON_CONFIG_FILE="$ELECTRON_HOME/conf/config.properties"

if [ -z $ELECTRON_CONFIG_FILE ] || [ ! -f $ELECTRON_CONFIG_FILE ]; then
    echo "config file $ELECTRON_CONFIG_FILE is not exists!"
    exit
fi

ELECTRON_CONFIG_PROPS=

cat $ELECTRON_CONFIG_FILE | awk '{ if (match($0, /^[^#].*$/)) print "-D"$0}' | xargs

echo $ELECTRON_CONFIG_PROPS

exec java \
        -classpath "$ELECTRON_SERVER_LIB" \
        $ELECTRON_CONFIG_PROPS \
        com.github.gotz9.electron.server.ApplicationLauncher "$@"