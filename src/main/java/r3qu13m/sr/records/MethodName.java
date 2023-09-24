package r3qu13m.sr.records;

import java.util.Optional;

public record MethodName(String searge, String name, int side, String desc) {
	public static Optional<MethodName> parse(final String[] line) {
		if (line.length == 4 && !line[2].equals("side")) {
			return Optional.of(new MethodName(line[0], line[1], Integer.parseInt(line[2]), line[3]));
		}
		return Optional.empty();
	}
}
