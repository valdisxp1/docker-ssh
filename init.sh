#!/bin/sh

if ! ls /etc/ssh/ssh_host_* 1> /dev/null 2>&1
then
	ssh-keygen -A
fi

mkdir -p /root/.ssh
chmod 700 /root/.ssh

./update_keys.sh

exec /usr/sbin/sshd -D
