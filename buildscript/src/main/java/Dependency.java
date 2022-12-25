import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.maven.MavenWithMoreSupport;

public enum Dependency {
	/*MALILIB(
			"https://masa.dy.fi/maven",
			"fi.dy.masa.malilib",
			"malilib-liteloader-1.12.2",
			"0.52.0",
			Type.LITELOADER_MOD
	),*/
	MALILIB(
			Maven.MAVEN_LOCAL,
			"fi.dy.masa.malilib",
			"malilib-fabricloader-1.12.2",
			//"0.52.0-named",
			"0.54.0",
			Type.FABRIC_MOD
	),
	MODMENU(
			Maven.MAVEN_LOCAL,
			"de.skyrising",
			"modmenu",
			"1.16.9+1.12.2.76ef206", //f6c5516
			Type.FABRIC_MOD
	),
	LITEFABRIC(
			Maven.MAVEN_LOCAL,
			"litefabric",
			"litefabric",
			//"0.0.0-dev.20220903.124411",
			//"0.0.0-dev.20221007.221856",
			//"0.0.0-dev.20221010.191048",
			//"0.0.0-dev.20221207.200444",
			"0.0.0-dev.20221220.021722",
			Type.FABRIC_MOD
	);

	public final String mavenRepo;
	public final String groupId;
	public final String artifactId;
	public final String version;
	public final Type type;

	Dependency(String mavenRepo, String groupId, String artifactId, String version, Type type){
		this.mavenRepo = mavenRepo;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.type = type;
	}

	public MavenId getMavenId(){
		return new MavenId(groupId, artifactId, version);
	}

	public JavaJarDependency getJavaJarDependency(){
		MavenId mavenId = getMavenId();
		String fileExtension = type.fileExtension;

		return MavenWithMoreSupport.getAnyDep(mavenRepo, mavenId, fileExtension);
	}

	enum Type {
		FABRIC_MOD(".jar"),
		LITELOADER_MOD(".litemod");

		public final String fileExtension;
		Type(String fileExtension){
			this.fileExtension = fileExtension;
		}
	}
}
