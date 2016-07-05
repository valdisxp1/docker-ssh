#!/bin/sh

if [ -z "${AUTHORIZED_KEYS}" ]
then
	>&2 echo "ERROR: make sure \$AUTHORIZED_KEYS contains at least 1 key"
	exit 1
fi

echo "${AUTHORIZED_KEYS}" | tr , "\n" > /root/.ssh/authorized_keys
chmod 600 /root/.ssh/authorized_keys
