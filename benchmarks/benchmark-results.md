# Distributed Rate Limiter Benchmark Results

## Test Configuration

- Virtual Users: 50
- Duration: 30s
- Distributed Instances: 3
- Shared Redis Coordination
- NGINX Load Balancer

---

## Fixed Window

- Max Allowed Rate: 4.55
- Max Blocked Rate: 260

Observation:
Aggressive burst rejection due to static window boundaries.

---

## Sliding Window

- Max Allowed Rate: 28.2
- Max Blocked Rate: 236

Observation:
Smoother traffic distribution and improved fairness.

---

## Token Bucket

- Max Allowed Rate: 36.4
- Max Blocked Rate: 226

Observation:
Best burst tolerance and highest throughput efficiency.
