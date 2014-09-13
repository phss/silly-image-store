FROM dockerfile/java:oracle-java8

ADD target/silly-image-store-0.1.0-SNAPSHOT-standalone.jar /opt/app/silly-image-store.jar

EXPOSE 3000
CMD java -Dbase.store.dir=/opt/images -jar /opt/app/silly-image-store.jar

