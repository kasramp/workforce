#!/usr/bin/env bash

docker run --name workforce-v0.1 -d -v /tmp/workforce/logs:/tmp/workforce/logs -p 8080:8080 workforce

