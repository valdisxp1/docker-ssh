FROM schmich/armv7hf-alpine-qemu:latest

RUN ["cross-build-start"]

MAINTAINER Valdis Adamsons <valdisxp1@gmail.com>

RUN apk add --no-cache openssh socat openssl

RUN >/etc/motd

RUN ["cross-build-end"]

COPY init.sh /
COPY update_keys.sh /

COPY sshd_config /etc/ssh/

EXPOSE 22

CMD [ "/init.sh" ]
