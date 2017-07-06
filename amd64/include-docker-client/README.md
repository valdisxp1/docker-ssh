# valdisxp1/sshd-socat-docker

Based opon robbertkl/ssh

[![nodesource/node](http://dockeri.co/image/valdisxp1/sshd-socat-docker)](https://registry.hub.docker.com/u/valdisxp1/sshd-socat-docker/)

Docker container running OpenSSH server:

* Exposes port 22
* Fills `~/.ssh/authorized_keys` from an environment variable or from specified URL.
* Socat for use with forwarding the `/var/run/docker.sock`. See https://github.com/RickyCook/ssh-forward-unix-socket

## Usage

Run like this:

```bash
docker run -d -e AUTHORIZED_KEYS="..." -p 2222:22 valdisxp1/sshd-socat-docker
```

or you can using an url:

```bash
docker run -d -e AUTHORIZED_KEYS_URL="https://..." -p 2222:22 valdisxp1/sshd-socat-docker
```

You can then SSH with user root.

### Updating the keys in an existing container

When `AUTHORIZED_KEYS_URL` is populated the keys will be re-downloaded:

1. on restart
2. by running `docker exec <container_name> /update_keys.sh`

If the website is down or the new keys are invalid, they will be ignored and the old keys used instead.

This doesn't do much when there is only a static key specified with `AUTHORIZED_KEYS`.

### Connecting to the docker remotely via SSH by forwarding the docker socket

This assumes docker is already working on the server and docker client is installed on the client.

1. Run the `valdisxp1/sshd-socat-docker` container on the server. Mount the docker socket as a volume, i.e., `-v /var/run/docker.sock:/var/run/docker.sock`. Specify the keys as usual.
2. Dowload https://github.com/RickyCook/ssh-forward-unix-socket on the client
3. Specify your ssh connection details and run this in a seperate shell (must have this open):
  ```bash
  sudo ./forward_socket --local_path /var/run/docker2.sock --local_user $(id -un) "ssh -i <path-to-key> root@<server-host> -p 2222" /var/run/docker.sock
  ``` 
  Local path is `/var/run/docker2.sock` to avoid possible conflicts with local docker.

4. You can now connect to the remote docker by adding `-H unix:///var/run/docker2.sock` to your command.

  Example:
  ```bash
    docker -H unix:///var/run/docker2.sock ps
  ```
  
  Alternatively you can use the enviroment variable.
  ```bash
    export DOCKER_HOST="unix:///var/run/docker2.sock"
    docker ps
  ```

## Environment variables

* `AUTHORIZED_KEYS` (comma-separated list of SSH keys)
* `AUTHORIZED_KEYS_URL` url for downloading the SSH keys. I strongly recomend using **HTTPS** here.

## Authors

* Robbert Klarenbeek, <robbertkl@renbeek.nl> (original robbertkl/ssh docker container)
* Valdis Adamsons, <valdisxp1@gmail.com>

## License

This repo is published under the [MIT License](http://www.opensource.org/licenses/mit-license.php).
