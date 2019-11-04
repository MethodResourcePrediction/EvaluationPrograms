#!/usr/bin/env bash

PROJECT="sleep"

cd EvaluationPrograms/

filename="start_parameters.txt"
while read -r line; do
    parameters=($line)
    methodname=${parameters[0]}
    
    echo $methodname

    cd ../Instrumenter/
    java -jar ../Instrumenter.jar --inputJar="../EvaluationPrograms.jar" --methodSignature="LSleep;.${methodname}([Ljava/lang/String;)V" > /dev/null

    cd generated/
    java -noverify -jar instrumented.jar $line
    error_code=$?
    echo "Error code: ${error_code}"

#    if [$error_code -eq 0]
#    then
      mkdir -p "/home/ralf/EclipseProjects/workspace/TestCases/${PROJECT}/${methodname}/"
      cp -v report.xml "/home/ralf/EclipseProjects/workspace/TestCases/${PROJECT}/${methodname}/"
#    fi
    cd ../../EvaluationPrograms/
#    break
done < "$filename"

