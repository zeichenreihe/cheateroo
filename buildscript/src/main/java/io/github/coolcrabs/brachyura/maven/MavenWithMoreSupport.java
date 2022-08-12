package io.github.coolcrabs.brachyura.maven;

import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MavenWithMoreSupport {
	private MavenWithMoreSupport() {
	}

	public static JavaJarDependency getLiteModDep(String mavenRepo, MavenId dep) {
		try {
			Class<Maven> clazz = Maven.class;

			// private static Dependency getMavenDep(String mavenRepo, MavenId dep, String extension, boolean isJavaJar, boolean allowDownload)
			Method method = clazz.getDeclaredMethod("getMavenDep", String.class, MavenId.class, String.class, boolean.class, boolean.class);

			// allow access, because method is private
			method.setAccessible(true);

			// pass null because it's a static method
			return (JavaJarDependency) method.invoke(null, mavenRepo, dep, ".litemod", true, true);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static JavaJarDependency getAnyDep(String mavenRepo, MavenId dep, String fileExtension) {
		try {
			Class<Maven> clazz = Maven.class;

			// private static Dependency getMavenDep(String mavenRepo, MavenId dep, String extension, boolean isJavaJar, boolean allowDownload)
			Method method = clazz.getDeclaredMethod("getMavenDep", String.class, MavenId.class, String.class, boolean.class, boolean.class);

			// allow access, because method is private
			method.setAccessible(true);

			// pass null because it's a static method
			return (JavaJarDependency) method.invoke(null, mavenRepo, dep, fileExtension, true, true);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
