enablePlugins(AkkaserverlessPlugin, JavaAppPackaging, DockerPlugin)
dockerBaseImage    := "docker.io/library/adoptopenjdk:11-jre-hotspot"
dockerUsername     := sys.props.get("docker.username")
dockerRepository   := sys.props.get("docker.registry")
dockerUpdateLatest := true
