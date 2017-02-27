#!/bin/sh

/usr/bin/java -Xmx256m -jar target/similarity-sorter-1.0.0-jar-with-dependencies.jar 2>&1 | /usr/bin/tee -a log/similarity-sorter.log

