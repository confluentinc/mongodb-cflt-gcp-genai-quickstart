ARG IMAGE_ARCH
FROM ubuntu:22.04-${IMAGE_ARCH}

# install terraform
RUN dnf install -y dnf-plugins-core && \
    dnf config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo && \
    dnf -y install terraform && \
    dnf clean all

# install confluent cli
RUN dnf config-manager --add-repo https://packages.confluent.io/confluent-cli/rpm/confluent-cli.repo && \
    dnf -y install confluent-cli && \
    dnf clean all

WORKDIR /app

# make the directories for frontend and infrastructure
RUN mkdir -p infrastructure

WORKDIR /app/infrastructure

ENTRYPOINT ["terraform"]