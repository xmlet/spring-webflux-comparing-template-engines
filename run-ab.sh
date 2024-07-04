#!/bin/bash

REQUESTS=256
THREADS=(1 2 4 8 16 64 128)
ITERATIONS=3

for ths in "${THREADS[@]}"; do   # For each number of threads
  for path in "$@"; do           # For each path in line argument
    for ((n=0;n<$ITERATIONS;n++)); do
      total_requests=$((REQUESTS * ths))
      result=$(ab -q -s 240 -n $total_requests -c $ths http://localhost:8080/$path | grep "Time taken for tests")
      echo ":::::::::::::::::::::::::::::::     $path:$ths:$total_requests:$result"
    done
  done
done
