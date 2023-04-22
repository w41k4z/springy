# compiling source code
javac -parameters -classpath ./lib/servlet-api.jar:./lib/r-w-x_file.jar:./lib/orm.jar -d ./bin/framework --enable-preview --release 20 $(find  ./framework/src/ -name '*.java')

# adding framework librairies as dependencies
    # orm
    mkdir exportedOrm
    cd ./exportedOrm
    jar xvf ../lib/orm.jar
    cp -r ./orm ../bin/framework/
    cd ../
    rm -r ./exportedOrm

    # r-w-x_file
    mkdir exportedRwx
    cd ./exportedRwx
    jar xvf ../lib/r-w-x_file.jar
    cp -r ./fileActivity ../bin/framework/
    cd ../
    rm -r ./exportedRwx

# exporting the framework to a jar file
cd ./bin/framework
jar cvf ../../springy-1.0.0.jar *

# creating the directory structure for the project test to deploy
cd ../../
mkdir ./temp ./temp/views ./temp/modelControllers ./temp/WEB-INF ./temp/WEB-INF/classes ./temp/WEB-INF/lib

# copying jar file to the project library and the web.xml file
cp ./springy-1.0.0.jar ./temp/WEB-INF/lib 
cp ./test-framework/web.xml ./temp/WEB-INF/
cp ./test-framework/views/* ./temp/views/
cp ./test-framework/modelControllers/* ./temp/modelControllers/

# compiling models and other user necessity to the project classes directory
javac -parameters -classpath ./springy-1.0.0.jar -d ./temp/WEB-INF/classes --enable-preview --release 20 $(find ./test-framework/ -name '*.java')
cd ./temp

# exporting the temp directory to a war file and move it to tomcat webapps folder
jar cvf $CATALINA_HOME/webapps/test-springy.war *
cd ../

# removing temp directory
rm -r ./temp