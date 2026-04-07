# 2-Week Learning Sprint (JDK 17)

## Week 1: Governance and Service Calls

- Day 1: start infra, verify Nacos registration for all services
- Day 2: verify Gateway routing and API entry unification
- Day 3: walk through OpenFeign contracts and fallback behavior
- Day 4: set Sentinel rules (flow + degrade), test blocking/fallback
- Day 5: use Nacos config for one dynamic property and verify refresh
- Day 6: read trace data in Zipkin, map one full order chain
- Day 7: write a short recap: what each component solves

## Week 2: Distributed Transaction and Hardening

- Day 8: understand Seata roles (TC/TM/RM) and AT flow
- Day 9: run order create success path, verify DB states
- Day 10: run `forceFail=true` rollback scenario, verify consistency
- Day 11: improve error handling and logs
- Day 12: add postman/curl validation script for key APIs
- Day 13: perform failure drills (slowdown, service down)
- Day 14: final review and architecture explanation rehearsal

## Study References

- https://sca.aliyun.com/docs/2023/overview/version-explain/
- https://docs.spring.io/spring-boot/system-requirements.html
- https://github.com/spring-cloud/spring-cloud-release/wiki/Supported-Versions
- https://docs.spring.io/spring-cloud-openfeign/reference/
- https://docs.spring.io/spring-cloud-gateway/reference/4.1/
- https://sentinelguard.io/en-us/docs/introduction.html
- https://sca.aliyun.com/docs/2023/user-guide/seata/quick-start/
- https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide
