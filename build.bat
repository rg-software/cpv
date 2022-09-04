@echo off
javac -classpath .;lib/flatlaf-2.4.jar;lib/jgraph-5.13.0.0.jar;src -d build src/cpv/Application.java





rem copy jgraph.jar cpv.jar
rem jar ufm cpv.jar src/meta-inf/manifest.mf -C classes cpv

pushd build
jar -xf ../lib/flatlaf-2.4.jar
jar -xf ../lib/jgraph-5.13.0.0.jar
jar --create --file cpv.jar --main-class cpv.Application .
popd