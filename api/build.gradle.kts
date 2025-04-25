import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    labyApi("api")
    addonMavenDependency("com.rappytv.globaltags:GlobalTagsJava:1.2.7-SNAPSHOT")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.INTERFACE
}