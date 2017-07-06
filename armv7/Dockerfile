FROM container4armhf/armhf-alpine:latest
MAINTAINER Valdis Adamsons <valdisxp1@gmail.com>

RUN apk add --no-cache openssh socat openssl

RUN >/etc/motd
COPY init.sh /
COPY update_keys.sh /

COPY sshd_config /etc/ssh/

EXPOSE 22

CMD [ "/init.sh" ]
