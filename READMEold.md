# ATC

*Air Traffic Control; An API scoped access management solution for organizations that don't enjoy vendor lock-in.*

# Features

### Service access control

Control what applications can and can't talk to your microservices.

### Configuration Hot Reloads

ATC will automatically detect `configuration.yml` file changes and apply them, no restarts required!

### Fork requests

Call an endpoint on a service with the `X-ATC-ForkMode: 1` header and receive a "Forked Request ID" in response. Poll the endpoint again with `X-ATC-ForkId: yourForkIdHere`, receiving `HTTP 503 Service Temporarily Unavailable` if the response is still not available, or receiving the proper response from your service.

### Tracing

Automated tracing with the `X-ATC-Trace` header.

# How it works

ATC in a nutshell is an API gateway that handles directing requests to your internal services. When writing an
application consuming internal APIs, your application will actually be talking to a ATC instance. ATC checks your application's `X-ATC-Access` header, compares it with issued access tokens, then compares it with allowed scopes and routes your request accordingly (or responds with an error).

# How to use it

1. Point your custom SDKs/client libraries to your ATC instance
2. Modify `/etc/hosts`/DNS Records to point to ATC
Providing a slightly better developer experience, this option is preferred. For example, if a developer points their code to `https://auth.api.smokelabs.com/v1/beginSession`, the hosts file / DNS server will actually forward their request to `https://atc.api.smokelabs.com`.
