#!/bin/bash
./gradlew clean check test

if [ "$(uname)" == "Darwin" ]; then
    # Do something under Mac OS X platform        
    open app/build/test-report/debug/index.html &                                                       
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Do something under Linux platform
    xdg-open app/build/test-report/debug/index.html &                                                     
fi

