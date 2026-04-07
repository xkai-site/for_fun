import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:18080';

const bizFailure = new Counter('biz_failure');

export const options = {
  scenarios: {
    read_api: {
      executor: 'ramping-vus',
      exec: 'readApi',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 20 },
        { duration: '40s', target: 20 },
        { duration: '20s', target: 0 }
      ],
      gracefulRampDown: '5s'
    },
    create_order_success: {
      executor: 'ramping-vus',
      exec: 'createOrderSuccess',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 8 },
        { duration: '40s', target: 8 },
        { duration: '20s', target: 0 }
      ],
      gracefulRampDown: '5s'
    },
    create_order_rollback: {
      executor: 'ramping-vus',
      exec: 'createOrderRollback',
      startVUs: 1,
      stages: [
        { duration: '20s', target: 5 },
        { duration: '40s', target: 5 },
        { duration: '20s', target: 0 }
      ],
      gracefulRampDown: '5s'
    }
  },
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<1200'],
    checks: ['rate>0.95']
  }
};

function parseJson(response) {
  try {
    return response.json();
  } catch (e) {
    return null;
  }
}

function headers() {
  return {
    headers: {
      'Content-Type': 'application/json',
      'X-Request-Id': `k6-${__VU}-${__ITER}-${Date.now()}`
    }
  };
}

export function readApi() {
  const userRes = http.get(`${BASE_URL}/api/users/1`);
  const productRes = http.get(`${BASE_URL}/api/products/1`);

  const userBody = parseJson(userRes);
  const productBody = parseJson(productRes);

  const ok = check(userRes, {
    'user http status == 200': (r) => r.status === 200,
    'user biz code == 0': () => userBody && userBody.code === 0
  }) && check(productRes, {
    'product http status == 200': (r) => r.status === 200,
    'product biz code == 0': () => productBody && productBody.code === 0
  });

  if (!ok) {
    bizFailure.add(1);
  }

  sleep(0.2);
}

export function createOrderSuccess() {
  const payload = JSON.stringify({
    userId: 1,
    productId: 1,
    count: 1,
    forceFail: false
  });

  const createRes = http.post(`${BASE_URL}/api/orders`, payload, headers());
  const createBody = parseJson(createRes);

  const created = check(createRes, {
    'create success http status == 200': (r) => r.status === 200,
    'create success biz code == 0': () => createBody && createBody.code === 0,
    'create success has order id': () => createBody && createBody.data && createBody.data.id
  });

  if (!created) {
    bizFailure.add(1);
    sleep(0.2);
    return;
  }

  const orderId = createBody.data.id;
  const queryRes = http.get(`${BASE_URL}/api/orders/${orderId}`);
  const queryBody = parseJson(queryRes);

  const queried = check(queryRes, {
    'query order http status == 200': (r) => r.status === 200,
    'query order biz code == 0': () => queryBody && queryBody.code === 0,
    'query order id matches': () => queryBody && queryBody.data && queryBody.data.id === orderId
  });

  if (!queried) {
    bizFailure.add(1);
  }

  sleep(0.2);
}

export function createOrderRollback() {
  const payload = JSON.stringify({
    userId: 1,
    productId: 1,
    count: 1,
    forceFail: true
  });

  const res = http.post(`${BASE_URL}/api/orders`, payload, headers());
  const body = parseJson(res);

  // In this project, GlobalExceptionHandler returns ApiResponse.fail(...), usually with HTTP 200.
  const rollbackOk = check(res, {
    'rollback response http status in [200,500]': (r) => r.status === 200 || r.status === 500,
    'rollback biz code != 0': () => body && body.code !== 0
  });

  if (!rollbackOk) {
    bizFailure.add(1);
  }

  sleep(0.2);
}
