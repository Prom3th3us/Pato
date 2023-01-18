resolvers in ThisBuild += "lightbend-commercial-mvn" at
  "https://repo.lightbend.com/pass/GHyq8a2Qcn0ObLVlDvc0CeGPbFjL6KJupNZpGXFpUGHWBHj_/commercial-releases"
resolvers in ThisBuild += Resolver.url("lightbend-commercial-ivy",
  url("https://repo.lightbend.com/pass/GHyq8a2Qcn0ObLVlDvc0CeGPbFjL6KJupNZpGXFpUGHWBHj_/commercial-releases"))(Resolver.ivyStylePatterns)
