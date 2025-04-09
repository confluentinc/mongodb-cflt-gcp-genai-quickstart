ARG IMAGE_ARCH
FROM --platform=${IMAGE_ARCH} fedora:latest

RUN dnf -y update && \
    dnf -y install dnf-plugins-core curl gnupg unzip jq && \
    \
    # Add HashiCorp repo and install the latest Terraform
    curl -fsSL https://rpm.releases.hashicorp.com/fedora/hashicorp.repo -o /etc/yum.repos.d/hashicorp.repo && \
    dnf -y install terraform && \
    \
    # Add Confluent repo and install the latest Confluent CLI
    curl -sL --http1.1 https://packages.confluent.io/rpm/7.6/archive.key | gpg --dearmor -o /etc/pki/rpm-gpg/RPM-GPG-KEY-confluent && \
    curl -fsSL https://packages.confluent.io/rpm/7.6/confluent.repo -o /etc/yum.repos.d/confluent.repo && \
    dnf -y install confluent-cli && \
    \
    # Clean up
    dnf clean all

WORKDIR /app

RUN mkdir -p infrastructure

WORKDIR /app/infrastructure

ENTRYPOINT ["terraform"]
