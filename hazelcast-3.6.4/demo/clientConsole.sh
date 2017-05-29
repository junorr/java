#!/bin/sh

java -server -Djava.net.preferIPv4Stack=true -cp ../lib/hazelcast-all-3.6.4.jar com.hazelcast.client.console.ClientConsoleApp
