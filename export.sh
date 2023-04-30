# compiling source code
javac -parameters -classpath ./lib/servlet-api.jar:./lib/r-w-x_file.jar:./lib/orm.jar -d ./bin --enable-preview --release 20 $(find  src -name '*.java')

# adding framework librairies as dependencies
    # r-w-x_file
    mkdir exportedRwx
    cd ./exportedRwx
    jar xvf ../lib/r-w-x_file.jar
    cp -r ./fileActivity ../bin
    cd ../
    rm -r ./exportedRwx

# exporting the framework to a jar file
cd ./bin
jar cvf ../../springy-1.0.0.jar *