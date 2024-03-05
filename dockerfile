FROM debian:bookworm

COPY build/native/nativeCompile/atc /usr/local/bin/atc

ENTRYPOINT ["atc"]