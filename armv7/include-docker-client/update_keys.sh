#!/bin/sh

if [ -n "${AUTHORIZED_KEYS_URL}" ]
then
	AUTHORIZED_KEYS="`wget -qO- $AUTHORIZED_KEYS_URL`"
fi

echo "${AUTHORIZED_KEYS}" | tr , "\n" > /root/.ssh/authorized_keys.new

# check if the keys are valid
echo "New key file signatures:"
ssh-keygen -lf /root/.ssh/authorized_keys.new
if [ $? -ne 0 ]
then
	>&2 echo "ERROR: the new authorized_keys file are not valid, SSH keys will not be changed"
else

# authorized_keys file is valid, replace the old one
mv /root/.ssh/authorized_keys /root/.ssh/authorized_keys.old 2>/dev/null
mv /root/.ssh/authorized_keys.new /root/.ssh/authorized_keys
chmod 600 /root/.ssh/authorized_keys

fi

echo "Authorized keys:"
cat /root/.ssh/authorized_keys
echo "------------"
