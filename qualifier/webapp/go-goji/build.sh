#!/bin/sh

# GOOS=linux GOARCH=amd64 gb build -F
gb build -F

rm -rf bin/src
mkdir -p bin/src/github.com/isucon/isucon4/public
mkdir -p bin/src/github.com/isucon/isucon4/templates
cp -ipR src/github.com/isucon/isucon4/public/* bin/src/github.com/isucon/isucon4/public
cp -ipR src/github.com/isucon/isucon4/templates/* bin/src/github.com/isucon/isucon4/templates
