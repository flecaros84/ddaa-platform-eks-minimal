#!/usr/bin/env bash
set -euo pipefail

# Pruebas básicas de la versión simplificada local.
# Ejecutar después de: docker compose up --build

echo "Probando health del backend..."
curl -s http://localhost:8082/actuator/health && echo

echo "Probando listado DDAA..."
curl -s http://localhost:8082/api/ddaa && echo

echo "Probando opciones del formulario..."
curl -s http://localhost:8082/api/ddaa/form-options && echo

echo "Pruebas básicas finalizadas. Frontend: http://localhost:3000"
