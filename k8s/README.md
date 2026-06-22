# Kubernetes / EKS

Esta carpeta queda reservada para la siguiente fase del trabajo.

La conversión local → EKS debería partir desde estos componentes:

1. `namespace.yaml`
2. `secret-sqlserver.yaml`
3. `configmap-ddaa.yaml`
4. `sqlserver-deployment.yaml` o `sqlserver-statefulset.yaml`
5. `ddaa-service-deployment.yaml`
6. `frontend-deployment.yaml`
7. `frontend-service.yaml` con `LoadBalancer` o `Ingress` con ALB
8. `hpa-ddaa-service.yaml`
9. `hpa-frontend.yaml`

En la versión EKS, el frontend debe mantener el proxy `/api` hacia el Service interno `ddaa-service`.
