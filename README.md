Velocity Renderer
=================

## Introduction

We needed a simple tool which would allow us to replace values of
environment variables in files and would work as a Java library and as
a command line tool.

[Apache Velocity](http://velocity.apache.org/) is a mature and
reliable Java template engine which we chose as the basis for this
small utility.

## How to use

After checkout, build it with maven:

```
mvn clean install
```  

You can run it with following command inside the project root folder:

```
java -jar target/velocity-renderer-standalone.jar -s ./src/test/resources -f .*.vm -d ./target
```

After that you can see the rendered file at `target/sample.test`:

```
cat target/sample.test
``` 

To see the template, from which `sample.test` was generated:

```
cat src/test/resources/sample.test.vm
```
