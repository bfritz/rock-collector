#!/bin/bash

set -e

DELAY=$1
SN=$2
SC=0

function send {
    echo $1
}


while read line; do
    if [[ $line == GET* ]]; then
        L=`echo $line | sed "s/\(.*\)SN=[0-9]\+\&SC=[0-9]\+\(&V=.*\)/\1SN=$SN\&SC=$SC\2/"`
        send "$L"
        SC=$((SC + 10))
    else
        send "$line"
    fi

    [[ $line == Host* ]] && sleep $DELAY
done
