import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))
    addonMavenDependency("com.rappytv.globaltags:GlobalTagsJava:1.1.4")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}