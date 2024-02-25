# Request Director
*An access management solution for internal APIs*
With an emphasis on safety, reliability and ease of use, we will get these requests where they need to go!

# How it works
Request Director in a nutshell is a middleman HTTPS server that handles directing requests to services internally. When writing an
application consuming internal APIs, your application will actually be talking to a Request Director instance. Request Director checks your application's `X-RD-Access` header, comparing it with issued access tokens, and routes your request accordingly.

# How to use it
1. Modify `/etc/hosts`/DNS Records to point to Request Director
Providing a slightly better developer experience, this option is preferred. For example, if a developer points their code to `https://auth.api.smokelabs.com/v1/beginSession`, the hosts file / DNS server will actually forward their request to `https://requestdirector.api.smokelabs.com`.

