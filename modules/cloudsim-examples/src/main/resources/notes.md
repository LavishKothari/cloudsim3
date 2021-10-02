
### To decide:
- when to fail a VM (time)?
  - let's say that the VM fails at T10
- vary parameters
  - What parameters to vary
    - RAM
    - CPU
    - Storage
    - BW
  - How to vary these parameters?
    - increase the value of all these parameters
  - When to vary these parameters
    - T0,   T1,   T2,   T3,   T4,   T5,   T6,   T7,   T8, T9
    - llll, lllm, lmll, mmll, mmml, hhhl, hhhh, hhhh, hhhh  

- Notify at T6, that the host is about tho fail

### STEPS:

1. startSimulation
2. vary parameters (simultaneously keep on monitoring the hosts and calculate the risk)
3. Inject failure


### Parameters

- RAM
- CPU
- Storage
- BW


initial host parameters
```java
int mips = 3720;
int ram = 2048; // host memory (MB)
long storage = 1000000; // host storage
int bw = 10000;
```

initial VM parameters

```java
//VM Parameters
int mips = 360;
int ram = 500; //vm memory (MB)
long size = 1000; //image size (MB)
long bw = 300;
```





Map<String, Object> data = new HashMap<>();
data.put("host", vm.getHost());
data.put("newHostParameters", HostFluctuatingParameters.PARAMETER_MAP.get(currentTime));