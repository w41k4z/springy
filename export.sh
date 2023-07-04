#!/bin/bash

# compiling source code
javac -parameters -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d ./bin --enable-preview --release 20 $(find src -name '*.java')

# adding framework librairies as dependencies
    # r-w-x_file
    mkdir exportedRwx
    cd ./exportedRwx
    jar xvf ../lib/r-w-x_file.jar
    cp -r ./fileActivity ../bin
    cd ../
    rm -r ./exportedRwx

    # gson
    mkdir exportedGson
    cd ./exportedGson
    jar xvf ../lib/gson-2.8.2.jar
    cp -r ./com ../bin
    cd ../
    rm -r ./exportedGson

# exporting the framework to a jar file
cd ./bin
jar cvf ../springy-1.0.0.jar *

# removing framework librairies dependencies
rm -r ./fileActivity

cd ../

# documentation
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src etu2011.framework.annotations etu2011.framework.config etu2011.framework.enumerations etu2011.framework.exceptions etu2011.framework.handler etu2011.framework.renderer etu2011.framework.servlet etu2011.framework.utils
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src 