#!/usr/bin/env bash

DOCKER_LOCAL_REPOSITORY="alixey/micronaut-example"
DOCKER_REMOTE_REPOSITORY="alixey/micronaut-example"

DOCKER_IMAGE_VERSION="$(git rev-parse --short=8 HEAD)"

#docker build \
#  -t ${DOCKER_LOCAL_REPOSITORY}:"${DOCKER_IMAGE_VERSION}" \
#  -t ${DOCKER_LOCAL_REPOSITORY}:latest \
#  .

docker buildx build \
    --push \
    --platform linux/arm64,linux/amd64 \
    --tag "${DOCKER_REMOTE_REPOSITORY}":"${DOCKER_IMAGE_VERSION}" \
    --tag "${DOCKER_REMOTE_REPOSITORY}":latest \
    .