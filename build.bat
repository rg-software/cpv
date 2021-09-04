javac -classpath .;jgraph.jar;src -d classes src/cpv/Application.java
copy jgraph.jar cpv.jar
jar ufm cpv.jar src/meta-inf/manifest.mf -C classes cpv
