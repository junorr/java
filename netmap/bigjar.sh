#!/bin/bash
cd dist
thejar=$(ls *.jar)

mkdir bigjar
cd bigjar

for i in $(ls ../lib/*.jar); do
  7z -y x "../$i"
done
7z -y x "../$thejar"

rm "../$thejar"
7z a $thejar *
mv $thejar ../
cd ..
rm -rf bigjar
