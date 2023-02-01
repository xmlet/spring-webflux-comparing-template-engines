#!/bin/bash
#set -x
ADRESS=$1
TESTS=(jsp velocity freemarker thymeleaf mustache jade pebble handlebars scalate httl chunk htmlFlow trimou rocker ickenham rythm groovy liqp)

for ip in "${TESTS[@]}"; do
  result=`ab -q -n 25000 -c 25 http://$ADRESS:8080/$ip | grep "Time taken for tests"`
  echo $ip $result
done
