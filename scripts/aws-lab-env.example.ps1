# Credenciales temporales del AWS Academy Learner Lab.
# IMPORTANTE: este archivo es local y NO debe subirse al repositorio.

$env:AWS_ACCESS_KEY_ID="PEGA_AQUI_TU_ACCESS_KEY"
$env:AWS_SECRET_ACCESS_KEY="PEGA_AQUI_TU_SECRET_KEY"
$env:AWS_SESSION_TOKEN="PEGA_AQUI_TU_SESSION_TOKEN"
$env:AWS_DEFAULT_REGION="us-east-1"

# Permite usar eksctl aunque todavía no esté fijo en el PATH permanente.
$env:Path += ";C:\tools\eksctl"

Write-Host "Credenciales temporales AWS Academy cargadas para esta sesión." -ForegroundColor Green
Write-Host "Región activa: $env:AWS_DEFAULT_REGION"