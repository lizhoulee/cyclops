package cyclops.streams.push.reactivestreamspath;

import cyclops.Reducers;
import cyclops.stream.Streamable;
import org.junit.Test;

import static cyclops.stream.Spouts.of;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReactiveScanningRSTest {
	@Test
	public void testScanLeftStringConcat() {
		assertThat(of("a", "b", "c").scanLeft("", String::concat).to(Streamable::fromStream).toList(), is(asList("", "a", "ab", "abc")));
	}

	@Test
	public void testScanLeftSum() {
		assertThat(of("a", "ab", "abc").map(str -> str.length()).scanLeft(0, (u, t) -> u + t).to(Streamable::fromStream).toList(), is(asList(0, 1, 3, 6)));
	}

	@Test
	public void testScanLeftStringConcatMonoid() {
		assertThat(of("a", "b", "c").scanLeft(Reducers.toString("")).to(Streamable::fromStream).toList(), is(asList("", "a", "ab", "abc")));
	}

	@Test
	public void testScanLeftSumMonoid() {
		assertThat(of("a", "ab", "abc").map(str -> str.length()).scanLeft(Reducers.toTotalInt()).to(Streamable::fromStream).toList(), is(asList(0, 1, 3, 6)));
	}

	@Test
	public void testScanRightStringConcat() {
		assertThat(of("a", "b", "c").scanRight("", String::concat).to(Streamable::fromStream).toList(), is(asList("", "c", "bc", "abc")));
	}

	@Test
	public void testScanRightSum() {
		assertThat(of("a", "ab", "abc").map(str -> str.length()).scanRight(0, (t, u) -> u + t).to(Streamable::fromStream).toList(), is(asList(0, 3, 5, 6)));

	}
	@Test
	public void testScanRightStringConcatMonoid() {
		assertThat(of("a", "b", "c").scanRight(Reducers.toString("")).to(Streamable::fromStream).toList(), is(asList("", "c", "bc", "abc")));
	}

	@Test
	public void testScanRightSumMonoid() {
		assertThat(of("a", "ab", "abc").map(str -> str.length()).scanRight(Reducers.toTotalInt()).to(Streamable::fromStream).toList(), is(asList(0, 3, 5, 6)));

	}
}
