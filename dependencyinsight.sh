#!/bin/bash
#How you find clashes in dependicies
VAR=$1
echo $VAR
echo $1
gradle -q app:dependencies |  grep --color -E '^|${TERM}'
