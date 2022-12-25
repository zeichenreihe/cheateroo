import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.fabric.*;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.ide.IdeModule;
import io.github.coolcrabs.brachyura.mappings.Namespaces;
import io.github.coolcrabs.brachyura.mappings.tinyremapper.MetaInfFixer;
import io.github.coolcrabs.brachyura.mappings.tinyremapper.RemapperProcessor;
import io.github.coolcrabs.brachyura.mappings.tinyremapper.TrWrapper;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.processing.*;
import io.github.coolcrabs.brachyura.project.Task;
import io.github.coolcrabs.brachyura.util.GsonUtil;
import io.github.coolcrabs.brachyura.util.MessageDigestUtil;
import io.github.coolcrabs.brachyura.util.OsUtil;
import io.github.coolcrabs.brachyura.util.Util;
import net.fabricmc.mappingio.tree.MappingTree;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Buildscript extends SimpleFabricProject {
	@Override
	protected FabricContext createContext() {
		return new CustomFabricContext();
	}

	@Override
	protected FabricModule createModule() {
		return new CustomModule(context.get());
	}

	@Override
	public VersionMeta createMcVersion() {
		return Minecraft.getVersion(Versions.MINECRAFT);
	}

	@Override
	public MappingTree createMappings() {
		return Versions.LEGACY_YARN.ofMaven(Yarn::ofMaven).tree;
	}

	@Override
	public FabricLoader getLoader() {
		return Versions.FABRIC_LOADER.ofMaven(FabricLoader::new);
	}

	@Override
	public void getModDependencies(ModDependencyCollector d) {
		for (Dependency dependency: Dependency.values()) {
			d.add(
					dependency.getJavaJarDependency(),
					FabricContext.ModDependencyFlag.COMPILE,
					FabricContext.ModDependencyFlag.RUNTIME
			);
		}
	}

	@Override
	public BrachyuraDecompiler decompiler() {
		// Uses QuiltFlower instead of CFR
		return new FernflowerDecompiler(
				Versions.QUILT_FLOWER.ofMaven(Maven::getMavenJarDep)
		);
	}

	public class CustomFabricContext extends SimpleFabricContext {
		@Override
		protected MappingTree createIntermediary() {
			// use legacy fabric intermediary
			return Versions.LEGACY_INTERMEDIARY.ofMaven(Intermediary::ofMaven).tree;
		}

		/*@Override
		public ProcessorChain modRemapChainOverrideOnlyIfYouOverrideRemappedModsRootPathAndLogicVersion(TrWrapper trw, List<Path> cp, Map<ProcessingSource, MavenId> c) {
			//return new CustomProcessorChain(
			return new ProcessorChain(
					new RemapperProcessor(trw, cp),
					new MetaInfFixer(trw),
					FabricContext.JijRemover.INSTANCE,
					new AccessWidenerRemapper(mappings.get(), mappings.get().getNamespaceId(Namespaces.NAMED), AccessWidenerRemapper.FabricAwCollector.INSTANCE),
					new FabricContext.FmjGenerator(c)
					//new CustomFmjGenerator(c)
			);
		}*/
	}
/*
	public static class CustomFmjGenerator extends FabricContext.FmjGenerator {
		public Map<ProcessingSource, MavenId> mapRef;
		public CustomFmjGenerator(Map<ProcessingSource, MavenId> map) {
			super(map);
			this.mapRef = map;
		}

		@Override
		public void process(Collection<ProcessingEntry> inputs, ProcessingSink sink) throws IOException {
			HashSet<ProcessingSource> fmj = new HashSet<>();
			for (ProcessingEntry e : inputs) {
				if ("fabric.mod.json".equals(e.id.path)) {
					fmj.add(e.id.source);
				}
				sink.sink(e.in, e.id);
			}
			for (Map.Entry<ProcessingSource, MavenId> e : mapRef.entrySet()) {
				if ((!fmj.contains(e.getKey())) && (!e.getValue().groupId.startsWith("fi.dy.masa."))) {
					Logger.info("Generating fmj for {}", e.getValue());
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					String modId = (e.getValue().groupId + "_" + e.getValue().artifactId).replace('.', '_').toLowerCase(Locale.ENGLISH);
					if (modId.length() > 64) {
						MessageDigest md = MessageDigestUtil.messageDigest(MessageDigestUtil.SHA256);
						MessageDigestUtil.update(md, modId);
						modId = modId.substring(0, 50) + MessageDigestUtil.toHexHash(md.digest()).substring(0, 14);
					}
					final JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("schemaVersion", 1);
					jsonObject.addProperty("id", modId);
					jsonObject.addProperty("version", e.getValue().version);
					jsonObject.addProperty("name", e.getValue().artifactId);
					JsonObject custom = new JsonObject();
					custom.addProperty("fabric-loom:generated", true);
					jsonObject.add("custom", custom);
					sink.sink(() -> GsonUtil.toIs(jsonObject, gson), new ProcessingId("fabric.mod.json", e.getKey()));
				}
			}
		}
	}*/

	/*@Override
	public IdeModule[] getIdeModules() {
		IdeModule module = super.module.get().ideModule();
		List<JavaJarDependency> deps = module.dependencies.get();

		for(Dependency dependency : Dependency.values()){
			deps.add(dependency.getJavaJarDependency());
		}

		IdeModule.IdeModuleBuilder builder = new IdeModule.IdeModuleBuilder();
		builder.dependencies(deps);
		builder.dependencyModules(module.dependencyModules);
		builder.javaVersion(module.javaVersion);
		builder.name(module.name);
		builder.resourcePaths(module.resourcePaths);
		builder.root(module.root);

		List<IdeModule.RunConfigBuilder> runConfigs = new ArrayList<>(module.runConfigs.size());
		for(IdeModule.RunConfig config : module.runConfigs){
			IdeModule.RunConfigBuilder runConfigBuilder = new IdeModule.RunConfigBuilder();

			runConfigBuilder.name(config.name);
			runConfigBuilder.mainClass(config.mainClass);
			runConfigBuilder.cwd(config.cwd);
			runConfigBuilder.vmArgs(config.vmArgs);
			runConfigBuilder.args(config.args);
			runConfigBuilder.classpath(config.classpath);
			runConfigBuilder.additionalModulesClasspath(config.additionalModulesClasspath);
			runConfigBuilder.resourcePaths(config.resourcePaths);

			runConfigs.add(runConfigBuilder);
		}
		builder.runConfigs(runConfigs);

		builder.sourcePaths(module.sourcePaths);
		builder.testResourcePaths(module.testResourcePaths);

		for(Path path : module.testSourcePaths)
			builder = builder.testSourcePaths(path);

		IdeModule replacement = builder.build();

		return new IdeModule[]{replacement};
	}*/


	public class CustomModule extends SimpleFabricProject.SimpleFabricModule {
		public CustomModule(FabricContext context) {
			super(context);
		}

		@Override
		public List<String> ideVmArgs(boolean client) {
			try {
				ArrayList<String> r = new ArrayList<>();
				r.add("-Dfabric.development=true");
				r.add("-Dfabric.remapClasspathFile=" + context.runtimeRemapClasspath.get());
				r.add("-Dlitefabric.dump.classes=true");
				r.add("-Dlitefabric.dump.resources=true");
				r.add("-Dlog4j.configurationFile=" + writeLog4jXml());
				r.add("-Dlog4j2.formatMsgNoLookups=true");
				r.add("-Dfabric.log.disableAnsi=false");

				// TODO: figure out how to generate this
				r.add("-javaagent:/home/zeichenreihe/.brachyura/cache/maven/CD2BC0B41A25DBA43374D732CE21FBA15B511735AE1464E9AB19B28312C55B17/net/fabricmc/sponge-mixin/0.11.2+mixin.0.8.5/sponge-mixin-0.11.2+mixin.0.8.5.jar");
				if (client) {
					String natives = context.extractedNatives.get().stream().map(Path::toString).collect(Collectors.joining(File.pathSeparator));
					r.add("-Djava.library.path=" + natives);
					r.add("-Dtorg.lwjgl.librarypath=" + natives);
					if (OsUtil.OS == OsUtil.Os.OSX) {
						r.add("-XstartOnFirstThread");
					}
				}
				return r;
			} catch (Exception e) {
				throw Util.sneak(e);
			}
		}
	}

	@Override
	public void getTasks(Consumer<Task> p){
		super.getTasks(p);
		p.accept(Task.of("buildRun", this::buildRun));
	}

	public void buildRun(){
		build();
		runTask("runMinecraftClient");
	}

	public static final SimpleDateFormat DEV_VERSION_FORMAT = new SimpleDateFormat(".yyyyMMdd.HHmmss");
	private Date buildDate = null; // to save the build date for the version

	@Override
	public String getVersion(){
		String version = super.getVersion();

		// append the build date when it's the dev version
		if(version.endsWith("-dev")){
			Objects.requireNonNull(buildDate, "build date not set");
			//version += DEV_VERSION_FORMAT.format(buildDate);
		}

		return version;
	}

	static {
		DEV_VERSION_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}


	@Override
	public JavaJarDependency build() {
		// we want to know from when the build is
		this.buildDate = new Date();

		return super.build();
	}
/*
	@Override
	public void getPublishTasks(Consumer<Task> p) {
		//SimpleJavaProject.createPublishTasks(p, this::build);
		JavaJarDependency build = this.build();

		String fixedVersion = build.mavenId.version;
		Date buildDate = new Date();
		if (build.mavenId.version.endsWith("-dev")) {
			SimpleDateFormat df = new SimpleDateFormat(".yyyyMMdd.HHmmss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			fixedVersion += df.format(buildDate);

		JavaJarDependency fixedBuild = new JavaJarDependency(
				build.jar,
				build.sourcesJar,
				new MavenId(
						build.mavenId.groupId,
						build.mavenId.artifactId,
						fixedVersion,
						build.mavenId.classifier
				)
		);

		p.accept(Task.of("publishToMavenLocal", () -> MavenPublishing.publish(MavenPublishing.AuthenticatedMaven.ofMavenLocal(), fixedBuild)));
	}*/
}
