FROM openjdk:11 as stage0
LABEL snp-multi-stage="intermediate"
LABEL snp-multi-stage-id="d2bc892d-64ad-4b47-bb7e-dad7c2cd4af1"
WORKDIR /opt/docker
COPY pcs/target/docker/stage/opt/docker .
USER root
RUN ["chmod", "-R", "u=rX,g=rX", "/opt/docker"]
RUN ["chmod", "u+x,g+x", "/opt/docker/bin/pcs"]
# Set the same time Zone from Host
RUN ln -sf /usr/share/zoneinfo/Etc/GMT+3  /etc/localtime
RUN echo "America/Argentina/Cordoba" > /etc/timezone

FROM openjdk:8
USER root
RUN id -u sergiomaurenzi 1>/dev/null 2>&1 || (( getent group 0 1>/dev/null 2>&1 || ( type groupadd 1>/dev/null 2>&1 && groupadd -g 0 root || addgroup -g 0 -S root )) && ( type useradd 1>/dev/null 2>&1 && useradd --system --create-home --uid 1001 --gid 0 demiourgos728 || adduser -S -u 1001 -G root demiourgos728 ))
WORKDIR /opt/docker

# Set the same time Zone from Host
RUN ln -sf /usr/share/zoneinfo/Etc/GMT+3  /etc/localtime
RUN echo "America/Argentina/Cordoba" > /etc/timezone

COPY --from=stage0 --chown=sergiomaurenzi:root /opt/docker /opt/docker
EXPOSE 2551 2552 2553 8081 8083 8084 8558 9095 5266
USER 1001:0
ENTRYPOINT ["/opt/docker/bin/pcs"]
CMD []
