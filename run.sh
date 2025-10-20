#!/bin/bash

echo "============================================"
echo "           AI Resume Builder                "
echo "============================================"
echo ""

echo "Cleaning up previous compilation..."
rm -rf bin
mkdir -p bin

echo " Compiling all Java files..."
javac -d bin -cp ".:lib/*" src/auth/*.java src/details/*.java src/ai/*.java src/pdf/*.java src/utils/*.java src/main/*.java

if [ $? -eq 0 ]; then
    echo ""
    echo "Compilation successful! ✓"
    echo ""
    echo " Running......."
    echo "============================================"
    java -cp "bin:lib/*" main.Main
else
    echo ""
    echo "Compilation failed!"
fi

echo ""
read -p "Press enter to continue..."