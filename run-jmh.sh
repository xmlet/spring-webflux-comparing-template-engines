#!/bin/bash

for threads in 8 2 4 8; do
  java -jar target/template-engines.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t $threads -rff results.csv -rf csv
done