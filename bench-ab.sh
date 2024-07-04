#!/bin/bash
#
# Start spring webflux server and redirect output to spring-webflux.log.
# Make interleaved timeout of 1 millis between Flux elements to
# promote context switch to a different scheduler.
#
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DbenchTimeout=1" > spring-webflux.log &
PID_WEBFLUX=$!
echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_WEBFLUX"
#
# Wait until appear the message 'Netty started on port 8080' in spring-webflux.log
#
sleep 1
while ! grep -m1 'Netty started on port 8080' < spring-webflux.log; do
    sleep 1
done
#
# Define routes for benchmark
#
ROUTES=(
  thymeleaf
  thymeleaf/sync
  kotlinx/sync
  htmlFlow
  htmlFlow/suspending
  htmlFlow/sync
  rocker/sync
  jstachio/sync
  pebble/sync
  freemarker/sync
  trimou/sync
  velocity/sync
)
#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  ab -n 1000 -c 32 http://localhost:8080/$path
done
#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}" > ab.log


# Gracefully terminate the Spring Boot application when running on local machine.
# It will send a SIGTERM corresponding to Exit code 143.
if [ "$GH" != "true" ]; then
  kill $PID_WEBFLUX

  # Wait for the process to exit
  wait $PID_WEBFLUX
fi
