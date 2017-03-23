@echo off
if %1==env goto env
if %1==demo goto demo
if %1==jar goto jar
if %1==test goto test
if %1==docs goto docs

:env
echo Building the Environment
javac -g -d bin src/uk/ac/nott/cs/g53dia/multilibrary/*.java 
copy src\uk\ac\nott\cs\g53dia\multilibrary\images\* bin\uk\ac\nott\cs\g53dia\multilibrary\images
exit /b

:demo
echo Building the demo tanker
javac -d bin -g -classpath bin src/uk/ac/nott/cs/g53dia/multidemo/*.java 
exit /b

:jar
echo Building the jar file
jar cmvf src/manifest-addition G53DIA.jar -C bin uk/ac/nott/cs/g53dia/multilibrary bin/uk/ac/nott/cs/g53dia/multidemo/*.class
exit /b

:test
echo Running the demo test
java -jar G53DIA.jar k.ac.nott.cs.g53dia.multidemo.DemoSimulator
exit /b

:docs
echo Making documentation
javadoc src/uk/ac/nott/cs/g53dia/multilibrary/*.java  src/uk/ac/nott/cs/g53dia/multidemo/*.java -d doc -private
