del /a /f /s /q bin\*.class >nul

javac -d bin -classpath src src/fripbird.java

java -classpath bin fripbird
