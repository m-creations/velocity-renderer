Velocity Renderer
=================

## How to use
After checkout and installing it with maven:
```
mvn clean install
```  
You can run it with following command inside the root of checked out folder of project:
```
$JAVA_HOME/bin/java \
-Dfile.encoding=UTF-8 \
-jar ~/.m2/repository/com/mcreations/renderer/velocity-renderer/1.0-SNAPSHOT/velocity-renderer-1.0-SNAPSHOT-jar-with-dependencies.jar \
-s ./src/test/resources -f .*.vm -d /tmp/
```
After that you can see the rendred file here /tmp/sample.test:
```
cat /tmp/sample.test
``` 
This file is the rendered version of src/test/resources/sample.test.vm which it is in the source of project:
```
cat src/test/resources/sample.test.vm
```