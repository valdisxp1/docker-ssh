#!/bin/sh

if ! ls /etc/ssh/ssh_host_* 1> /dev/null 2>&1
then
	ssh-keygen -A
fi

mkdir -p /root/.ssh
chmod 700 /root/.ssh

./update_keys.sh

if ! ls /root/.ssh/authorized_keys 1> /dev/null 2>&1
then
	>&2 echo "ERROR: authorized_keys file missing in /root/.ssh/"
        >&2 echo "       make sure \$AUTHORIZED_KEYS contains at least 1 key"
	exit 255
fi

exec /usr/sbin/sshd -D
