#!/bin/bash

java -cp amt-application/target/amt-application-1.0-SNAPSHOT-shaded.jar:./third-party-libs/* ch.heigvd.amt.framework.engine.Server

