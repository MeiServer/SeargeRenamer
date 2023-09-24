package r3qu13m.sr.records;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public record Config(Path targetDir, Set<MethodName> methods, Set<FieldName> fields, Set<ParamName> params) {

	public static final boolean DEBUG = true;

	public boolean isDebug() {
		return Config.DEBUG;
	}

	public Optional<String> lookupMethod(final String id) {
		return this.methods.parallelStream().filter(x -> x.searge().equals(id)).findFirst().map(x -> x.name());
	}

	public Optional<String> lookupField(final String id) {
		return this.fields.parallelStream().filter(x -> x.searge().equals(id)).findFirst().map(x -> x.name());
	}

	public Optional<String> lookupParam(final String id) {
		return this.params.parallelStream().filter(x -> x.param().equals(id)).findFirst().map(x -> x.name());
	}

	private static <T extends Record> Set<T> readCsv(final Path file, final Function<String[], Optional<T>> parseFunc)
			throws IOException {
		return Files.readAllLines(file).parallelStream().map(x -> x.split(",", 4)).map(parseFunc)
				.filter(x -> x.isPresent()).collect(HashSet::new, (x, y) -> x.add(y.get()), (x, y) -> x.addAll(y));
	}

	public static Optional<Config> load(final String targetDir, final String configDir) {
		try {
			final var methods = Config.readCsv(Paths.get(configDir, "methods.csv"), MethodName::parse);
			final var fields = Config.readCsv(Paths.get(configDir, "fields.csv"), FieldName::parse);
			final var params = Config.readCsv(Paths.get(configDir, "params.csv"), ParamName::parse);
			return Optional.of(new Config(Paths.get(targetDir), methods, fields, params));
		} catch (final IOException e) {
			if (Config.DEBUG) {
				e.printStackTrace();
			}
			return Optional.empty();
		}
	}
}
