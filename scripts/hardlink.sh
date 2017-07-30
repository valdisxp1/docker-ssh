#!/bin/bash

#deduplice onto scripts and sshd_config in amd64
rm amd64/include-docker-client/{init.sh,sshd_config,update_keys.sh}
ln amd64/init.sh amd64/include-docker-client/init.sh
ln amd64/sshd_config amd64/include-docker-client/sshd_config
ln amd64/update_keys.sh amd64/include-docker-client/update_keys.sh

rm armv7/{init.sh,sshd_config,update_keys.sh}
ln amd64/init.sh armv7/init.sh
ln amd64/sshd_config armv7/sshd_config
ln amd64/update_keys.sh armv7/update_keys.sh

rm armv7/include-docker-client/{init.sh,sshd_config,update_keys.sh}
ln amd64/init.sh armv7/include-docker-client/init.sh
ln amd64/sshd_config armv7/include-docker-client/sshd_config
ln amd64/update_keys.sh armv7/include-docker-client/update_keys.sh
