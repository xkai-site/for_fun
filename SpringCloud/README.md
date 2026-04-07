# springcloud-lab (JDK 17)

A small but complete Spring Cloud Alibaba practice project.

## Stack

- JDK 17
- Spring Boot 3.2.x
- Spring Cloud 2023.0.x
- Spring Cloud Alibaba 2023.0.1.0
- Nacos (registry + config)
- Gateway
- OpenFeign
- Sentinel
- Seata (AT)
- Zipkin

## Modules

- `common`: shared DTO, request/response, api wrapper
- `service-gateway`: unified entry and routing
- `service-user`: user query service
- `service-product`: product query/deduct service
- `service-account`: account deduct service
- `service-order`: orchestration service, global transaction entry

## Quick Start

1. Start infra:
   ```bash
   docker compose up -d
   ```
   - MySQL host port is `13306` (container internal port is `3306`).
2. Start services one by one:
   - `service-user`
   - `service-product`
   - `service-account`
   - `service-order`
   - `service-gateway`
3. Verify in browser:
   - Nacos: http://localhost:8848/nacos
   - Sentinel: http://localhost:8858
   - Zipkin: http://localhost:9411

## Demo API

- Query user: `GET http://localhost:18080/api/users/1`
- Query product: `GET http://localhost:18080/api/products/1`
- Create order: `POST http://localhost:18080/api/orders`

Example body:
```json
{
  "userId": 1,
  "productId": 1,
  "count": 1,
  "forceFail": false
}
```

Rollback test:
```json
{
  "userId": 1,
  "productId": 1,
  "count": 1,
  "forceFail": true
}
```

## Learning Resources

- SCA version mapping: https://sca.aliyun.com/docs/2023/overview/version-explain/
- Spring Boot system requirements: https://docs.spring.io/spring-boot/system-requirements.html
- Spring Cloud supported versions: https://github.com/spring-cloud/spring-cloud-release/wiki/Supported-Versions
- OpenFeign docs: https://docs.spring.io/spring-cloud-openfeign/reference/
- Gateway docs: https://docs.spring.io/spring-cloud-gateway/reference/4.1/
- Sentinel docs: https://sentinelguard.io/en-us/docs/introduction.html
- Seata quick start: https://sca.aliyun.com/docs/2023/user-guide/seata/quick-start/
- Spring Boot 3 migration guide: https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide

## Notes

- This repo is for learning and local verification.
- Keep the scenario intentionally small and focused on service governance.
