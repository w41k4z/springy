REM compiling source code
dir /b /s .\framework\src\*.java > sources.txt
javac -classpath .\lib\servlet-api.jar;.\lib\r-w-x_file.jar -d .\bin\framework --enable-preview --release 20 @sources.txt
del sources.txt
cd ".\bin\framework"

REM exporting the framework to a jar file
jar cvf "..\..\springy-1.0.0.jar" *
cd "..\.."

REM creating the directory structure for the project test to deploy
mkdir ".\temp" ".\temp\views" ".\temp\models" ".\temp\WEB-INF" ".\temp\WEB-INF\classes" ".\temp\WEB-INF\lib"

REM copying jar file to the project library and the web.xml file
copy ".\springy-1.0.0.jar" ".\temp\WEB-INF\lib" 
copy ".\lib\*.jar" ".\temp\WEB-INF\lib"
copy ".\test-framework\web.xml" ".\temp\WEB-INF\"
xcopy ".\test-framework\views" ".\temp\views" /s /e
xcopy ".\test-framework\models" ".\temp\models" /s /e

REM compiling models and other user necessity to the project classes directory
dir /b /s .\test-framework\*.java > sources.txt
javac -classpath ".\lib\servlet-api.jar;.\springy-1.0.0.jar" -d ".\temp\WEB-INF\classes" --enable-preview --release 20 @sources.txt
del sources.txt
cd ".\temp"

REM exporting the temp directory to a war file and move it to tomcat webapps folder
jar cvf "%CATALINA_HOME%\webapps\test-springy.war" *
cd ..

REM removing temp directory
rmdir /s /q ".\temp"
