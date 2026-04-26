#!/bin/bash
# Script para compilar e executar o Sistema de Eventos da Cidade
# Requer: Java 17+ instalado

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC="$SCRIPT_DIR/src"
OUT="$SCRIPT_DIR/out"

echo "======================================="
echo "  Sistema de Eventos da Cidade"
echo "  Compilando..."
echo "======================================="

mkdir -p "$OUT"
javac -d "$OUT" -sourcepath "$SRC" $(find "$SRC" -name "*.java") 2>&1

if [ $? -ne 0 ]; then
    echo "ERRO: Falha na compilação."
    exit 1
fi

echo "Compilado com sucesso! Iniciando..."
echo ""
cd "$SCRIPT_DIR"
java -cp "$OUT" Main
