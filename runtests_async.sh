#!/bin/bash
#set -x
ADRESS=$1
TESTS=(thymeleaf kotlinx htmlFlow trimou liqp)

for ip in "${TESTS[@]}"; do
  result=`ab -q -n 1000 -c 10 http://$ADRESS:8080/async/$ip | grep "Time taken for tests"`
  echo $ip $result
done
