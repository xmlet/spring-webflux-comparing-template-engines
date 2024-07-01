#!/bin/bash
#
# Start spring webflux server and redirect output to spring-webflux.log.
# Make interleaved timeout of 1 millis between Flux elements to
# promote context switch to a different scheduler.
#
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DbenchTimeout=1" > spring-webflux.log &
PID_WEBFLUX=$!
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
ROUTES=(thymeleaf/sync rocker/sync kotlinx/sync htmlFlow/sync thymeleaf kotlinx htmlFlow htmlFlow/suspending)
#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  for ((n=0;n<3;n++)); do
    ab -n 1000 -c 32 http://localhost:8080/$path
  done
done
#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}"

# Gracefully terminate the Spring Boot application
kill $PID_WEBFLUX

# Wait for the process to exit
wait $PID_WEBFLUX
