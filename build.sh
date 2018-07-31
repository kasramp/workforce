#!/usr/bin/env bash

mvn clean package
docker build -t workforce:v0.1 -t workforce:latest .