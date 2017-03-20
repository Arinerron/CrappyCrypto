#!/bin/sh

set -e

javac Main.java
java Main $@
