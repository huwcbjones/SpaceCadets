# Java Chat Client/Server
## Server
* Runs in console
* Prints a log to stderr/stdout
* JAR Download [chatd.jar](https://code.huwcbjones.co.uk/SpaceCadets/Challenge6/chatd.jar)/[chatd.jar](http://code.huwcbjones.co.uk/SpaceCadets/Challenge6/chatd.jar)
* Run `java -jar chatd.jar`
* Help `java -jar chatd.jar -h`
* Specify port number (default is 60666) `java -jar chatd.jar -p [PORT]`
* Command API/Interface for expansion, just takes long to write the commands

## Client
* Runs in console or GUI (highly suggest GUI because CBA to work on console... it was fine until GUI called)
* Prints a log to stderr/stdout. Print streams are redirected to GUI for assistance
* JAR Download [chat.jar](https://code.huwcbjones.co.uk/SpaceCadets/Challenge6/chat.jar)/[chat.jar](http://code.huwcbjones.co.uk/SpaceCadets/Challenge6/chat.jar)
* Run `java -jar chat.jar [SERVER HOSTNAME/ADDRESS]`
* Help `java -jar chat.jar -h`
* Run GUI mode `java -jar chatd.jar -g`
* Specify nickname `java -jar chatd.jar -n [NICKNAME]`
* Specify port number (default is 60666) `java -jar chatd.jar -p [PORT]`
