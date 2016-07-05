FROM alpine:latest
MAINTAINER Robbert Klarenbeek <robbertkl@renbeek.nl>

RUN apk add --no-cache openssh socat

RUN >/etc/motd
COPY init.sh /
COPY update_keys.sh /

EXPOSE 22

CMD [ "/init.sh" ]
