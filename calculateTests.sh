#!/bin/bash

file=$1


while read line
do
if [ ! -f "executions/${line}_tests.txt" ];
then
printf "executions/${line}_tests.txt not found !!!\n\n"
else
totalTests="$(cat "executions/${line}_tests.txt" | awk -F'Tests run: ' '{print $2}' | awk -F',' '{print $1}' | awk '{sum+=$1} END {print sum}')"
failed="$(cat "executions/${line}_tests.txt" | awk -F'Failures: ' '{print $2}' | awk -F',' '{print $1}' | awk '{sum+=$1} END {print sum}')"
error="$(cat "executions/${line}_tests.txt" | awk -F'Errors: ' '{print $2}' | awk -F',' '{print $1}' | awk '{sum+=$1} END {print sum}')"
skipped="$(cat "executions/${line}_tests.txt" | awk -F'Skipped: ' '{print $2}' | awk -F',' '{print $1}' | awk '{sum+=$1} END {print sum}')"
passed=`expr "$totalTests" - "$failed" - "$error" - "$skipped"`
echo "**************Results of $line ****************"
echo "total=$totalTests"
echo "passed=$passed"
echo "failed=$failed"
echo "error=$error"
echo "skipped=$skipped"
printf "***********************************************\n\n"
fi
done < "${file}"
