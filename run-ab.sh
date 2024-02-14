#!/bin/bash

REQUESTS=8192
THREADS=(1 2 4 8 16 64 128)
ITERATIONS=5

for ths in "${THREADS[@]}"; do   # For each number of threads
  for path in "$@"; do           # For each path in line argument
    for ((n=0;n<$ITERATIONS;n++)); do
      result=$(ab -q -n $REQUESTS -c $ths http://localhost:8080/$path | grep "Time taken for tests")
      echo "$path:$ths:$result"
    done
  done
done
