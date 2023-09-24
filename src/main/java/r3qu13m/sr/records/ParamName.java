package r3qu13m.sr.records;

import java.util.Optional;

public record ParamName(String param, String name, int side) {
	public static Optional<ParamName> parse(final String[] line) {
		if (line.length == 3 && !line[2].equals("side")) {
			return Optional.of(new ParamName(line[0], line[1], Integer.parseInt(line[2])));
		}
		return Optional.empty();
	}
}
