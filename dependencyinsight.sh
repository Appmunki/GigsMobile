#!/bin/bash
#How you find clashes in dependicies
TERM=$1
echo $TERM
gradle -q app:dependencies |  grep --color -E '^|${TERM}'
