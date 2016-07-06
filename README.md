# valdisxp1/sshd-socat

Based opon robbertkl/ssh

[![](https://badge.imagelayers.io/valdisxp1/sshd-socat:latest.svg)](https://imagelayers.io/?images=valdisxp1/sshd-socat:latest)

Docker container running OpenSSH server:

* Exposes port 22
* Fills `~/.ssh/authorized_keys` from an environment variable or from specified URL.
* Add socat for use with forwarding the `/var/run/docker.sock`. See https://github.com/RickyCook/ssh-forward-unix-socket

## Usage

Run like this:

```bash
docker run -d -e AUTHORIZED_KEYS="..." -p 2222:22 valdisxp1/sshd-socat
```

or you can using an url:

```bash
docker run -d -e AUTHORIZED_KEYS_URL="https://..." -p 2222:22 valdisxp1/sshd-socat
```

You can then SSH with user root.

### Updating the keys in an existing container

When `AUTHORIZED_KEYS_URL` is populated the keys will be re-downloaded:

1. on restart
2. by running `docker exec <container_name> /update_keys.sh`

If the website is down or the new keys are invalid, they will be ignored and the old keys used instead.

This doesn't do much when there is only a static key specified with `AUTHORIZED_KEYS`.

## Environment variables

* `AUTHORIZED_KEYS` (comma-separated list of SSH keys)
* `AUTHORIZED_KEYS_URL` url for downloading the SSH keys. I strongly recomend using **HTTPS** here.

## Authors

* Robbert Klarenbeek, <robbertkl@renbeek.nl> (original robbertkl/ssh docker container)
* Valdis Adamsons, <valdisxp1@gmail.com>

## License

This repo is published under the [MIT License](http://www.opensource.org/licenses/mit-license.php).
