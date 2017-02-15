Velocity Renderer
=================

## How to use
After checkout and install with maven:
```
mvn clean instal
```  
You can run it with following command inside the checkedout project:
```
$JAVA_HOME/bin/java \
-Dfile.encoding=UTF-8 \
-jar ~/.m2/repository/com/mcreations/renderer/velocity-renderer/1.0-SNAPSHOT/velocity-renderer-1.0-SNAPSHOT-jar-with-dependencies.jar \
-s ./src -f .*.vm -d /tmp/
```
After that you can see rendred file in here /tmp/sample.test:
```
cat /tmp/sample.test
``` 
This file is the rendered version of src/test/resources/sample.test.vm in the source of project.
```
cat src/test/resources/sample.test.vm
```