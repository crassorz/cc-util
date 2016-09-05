package cc.gu.util.gather;

import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.Occluder;

public interface Gatherer <T> {
	T get() throws Throwable;
	T get(Occluder occluder) throws OccludedException, Throwable;
}