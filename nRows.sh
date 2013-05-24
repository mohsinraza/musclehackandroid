#!/bin/bash
find . -type f -name "*.java" -exec cat {} \; | wc
find . -type f -name "*.xml" -exec cat {} \; | wc
