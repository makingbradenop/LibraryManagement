#!/bin/bash
# ──────────────────────────────────────────
#  Library Management System - Build Script
# ──────────────────────────────────────────

set -e
DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$DIR/src"
LIB="$DIR/lib"
OUT="$DIR/out"

mkdir -p "$LIB" "$OUT"

# Download SQLite JDBC if not present
SQLITE_JAR="$LIB/sqlite-jdbc.jar"
if [ ! -f "$SQLITE_JAR" ]; then
    echo "Downloading SQLite JDBC driver..."
    curl -L -o "$SQLITE_JAR" \
        "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar"
    echo "✅ SQLite JDBC downloaded."
fi

# Compile
echo "Compiling..."
javac -cp "$SQLITE_JAR" -d "$OUT" "$SRC"/*.java
echo "✅ Compilation successful."

# Run
echo ""
echo "Starting Library Management System..."
echo ""
java -cp "$OUT:$SQLITE_JAR" LibraryApp
