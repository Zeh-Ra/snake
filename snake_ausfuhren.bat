
cd bin
del /S *.class

cd ..\src
javac.exe Snakegame.java
javac.exe GameFrame.java
javac.exe GamePanel.java


cd ..\bin
java.exe SnakeGame 
pause