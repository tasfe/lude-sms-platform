#应用程序配置
HOME=/home/hsweb/lude/sms
PROGRAM=com/lude/sms/interfaces/Container
PROPERTIES=config/mt_send

JDKHOME=/home/hsweb/jdk1.6.0_02/

for JAR in $HOME/lib/*.jar
do
	CLASSPATH="$CLASSPATH":"$JAR"
done

$JDKHOME/bin/java $PROGRAM $PROPERTIES nohup &