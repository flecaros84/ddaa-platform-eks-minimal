# Pruebas básicas de la versión simplificada local.
# Ejecutar después de: docker compose up --build

$ErrorActionPreference = "Stop"

Write-Host "Probando health del backend..."
Invoke-RestMethod http://localhost:8082/actuator/health

Write-Host "Probando listado DDAA..."
Invoke-RestMethod http://localhost:8082/api/ddaa | Select-Object -First 3

Write-Host "Probando opciones del formulario..."
Invoke-RestMethod http://localhost:8082/api/ddaa/form-options

Write-Host "Pruebas básicas finalizadas. Frontend: http://localhost:3000"
